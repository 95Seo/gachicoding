package org.deco.gachicoding.post.answer.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.answer.domain.repository.AnswerRepository;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.repository.QuestionRepository;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.post.answer.dto.response.AnswerResponseDto;
import org.deco.gachicoding.post.answer.dto.request.AnswerSaveRequestDto;
import org.deco.gachicoding.post.answer.dto.request.AnswerSelectRequestDto;
import org.deco.gachicoding.post.answer.dto.request.AnswerUpdateRequestDto;
import org.deco.gachicoding.exception.ApplicationException;
import org.deco.gachicoding.exception.ResponseState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.deco.gachicoding.exception.StatusEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Transactional
    public Long registerAnswer(AnswerSaveRequestDto dto) {
        User writer = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Question question = questionRepository.findById(dto.getQueIdx())
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        // findById() -> 실제로 데이터베이스에 도달하고 실제 오브젝트 맵핑을 데이터베이스의 행에 리턴한다. 데이터베이스에 레코드가없는 경우 널을 리턴하는 것은 EAGER로드 한것이다.
        // getOne ()은 내부적으로 EntityManager.getReference () 메소드를 호출한다. 데이터베이스에 충돌하지 않는 Lazy 조작이다. 요청된 엔티티가 db에 없으면 EntityNotFoundException을 발생시킨다.
//        entity.setQuestion(questionRepository.getOne(dto.getQueIdx()));

        Answer answer = answerRepository.save(dto.toEntity(writer, question));

        Long ansIdx = answer.getAnsIdx();
        String ansContent = answer.getAnsContents();

        try {
            answer.update(fileService.extractImgSrc(ansIdx, ansContent, "answer"));
            log.info("Success Upload Question Idx : {}", ansIdx);
        } catch (Exception e) {
            log.error("Failed To Extract {} File", "Answer Content");
            e.printStackTrace();
            removeAnswer(ansIdx);
        }

        return ansIdx;
    }

    @Transactional(readOnly = true)
    public Page<AnswerResponseDto> getAnswerList(String keyword, Pageable pageable) {
        Page<Answer> answers = answerRepository.findByAnsContentsContainingIgnoreCaseAndAnsActivatedTrueOrderByAnsIdxDesc(keyword, pageable);
        Page<AnswerResponseDto> answersList = answers.map(
                result -> new AnswerResponseDto(result)
        );
        return answersList;
    }

    @Transactional(readOnly = true)
    public AnswerResponseDto getAnswerDetail(Long ansIdx) {
        Answer answer = answerRepository.findById(ansIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        AnswerResponseDto answerDetail = AnswerResponseDto.builder()
                .answer(answer)
                .build();
        return answerDetail;
    }

    @Transactional
    public AnswerResponseDto modifyAnswer(AnswerUpdateRequestDto dto) throws RuntimeException {
        Answer answer = answerRepository.findById(dto.getAnsIdx())
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        User user = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        if (!isSameWriter(answer, user)) {
            throw new ApplicationException(INVALID_AUTH_USER);
        }

        answer = answer.update(dto.getAnsContent());

        AnswerResponseDto answerDetail = AnswerResponseDto.builder()
                .answer(answer)
                .build();

        return answerDetail;
    }

    // 질문 작성자 확인 로직 추가
    @Transactional
    public ResponseEntity<ResponseState> selectAnswer(AnswerSelectRequestDto dto) {
        Answer answer = answerRepository.findById(dto.getAnsIdx())
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        Question question = answer.getQuestion();

        User user = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        // 좀 헷갈리지만 같을때 true가 나오기 때문에 !를 붙여야함
        if(!selectAuthCheck(question, user))
            return ResponseState.toResponseEntity(INVALID_AUTH_USER);;

        if(!question.getQueSolve()) {
            answer.toSelect();
            question.toSolve();
            return ResponseState.toResponseEntity(SELECT_SUCCESS);
        } else {
            return ResponseState.toResponseEntity(ALREADY_SOLVE);
        }
    }

    @Transactional
    public ResponseEntity<ResponseState> disableAnswer(Long ansIdx) {
        Answer answer = answerRepository.findById(ansIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        answer.disableAnswer();
        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<ResponseState> enableAnswer(Long ansIdx) {
        Answer answer = answerRepository.findById(ansIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        answer.enableAnswer();
        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<ResponseState> removeAnswer(Long ansIdx) {
        Answer answer = answerRepository.findById(ansIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        answerRepository.delete(answer);
        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
    }

    private Boolean isSameWriter(Answer answer, User user) {
        String writerEmail = answer.getWriter().getUserEmail();
        String userEmail = user.getUserEmail();

        return (writerEmail.equals(userEmail)) ? true : false;
    }

    // answer의 작성자가 아니라 question의 작성자가 맞는지 검사해야한다.
    // 하지만 위의 메서드와 하는 일은 같으니 통합시킬 수 없을까?
    // 뒤는 부탁할게 인환몬!
    private Boolean selectAuthCheck(Question question, User user) {
        String writerEmail = question.getWriter().getUserEmail();
        String userEmail = user.getUserEmail();

        return (writerEmail.equals(userEmail)) ? true : false;
    }
}
