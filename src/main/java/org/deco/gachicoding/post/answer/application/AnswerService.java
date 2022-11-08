package org.deco.gachicoding.post.answer.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.post.answer.AnswerAlreadySelectedException;
import org.deco.gachicoding.exception.post.answer.AnswerInactiveException;
import org.deco.gachicoding.exception.post.answer.AnswerNotFoundException;
import org.deco.gachicoding.exception.post.question.QuestionAlreadySolvedException;
import org.deco.gachicoding.exception.post.question.QuestionInactiveException;
import org.deco.gachicoding.exception.post.question.QuestionNotFoundException;
import org.deco.gachicoding.exception.user.UserNotFoundException;
import org.deco.gachicoding.post.answer.application.dto.request.AnswerSelectRequestDto;
import org.deco.gachicoding.post.answer.application.dto.request.AnswerUpdateRequestDto;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.answer.domain.repository.AnswerRepository;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.repository.QuestionRepository;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.post.answer.application.dto.request.AnswerSaveRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Transactional(rollbackFor = Exception.class)
    public Long registerAnswer(AnswerSaveRequestDto dto) {

        Answer answer = answerRepository.save(createAnswer(dto));

        Long ansIdx = answer.getAnsIdx();
        String ansContent = answer.getAnsContents();

        answer.update(
                fileService.extractPathAndS3Upload(ansIdx, "ANSWER", ansContent)
        );

        return answer.getQueIdx();
    }

    private Answer createAnswer(AnswerSaveRequestDto dto) {
        User user = findAuthor(dto.getUserEmail());
        Question question = findQuestion(dto.getQueIdx());

        return dto.toEntity(user, question);
    }

//    @Transactional(readOnly = true)
//    public Page<AnswerResponseDto> getAnswerList(String keyword, Pageable pageable) {
//        Page<Answer> answers = answerRepository.findByAnsContentsContainingIgnoreCaseAndAnsActivatedTrueOrderByAnsIdxDesc(keyword, pageable);
//        Page<AnswerResponseDto> answersList = answers.map(
//                result -> new AnswerResponseDto(result)
//        );
//        return answersList;
//    }
//
//    @Transactional(readOnly = true)
//    public AnswerResponseDto getAnswerDetail(Long ansIdx) {
//        Answer answer = answerRepository.findById(ansIdx)
//                .orElseThrow(AnswerNotFoundException::new);
//
//        AnswerResponseDto answerDetail = AnswerResponseDto.builder()
//                .answer(answer)
//                .build();
//        return answerDetail;
//    }

    @Transactional
    public Long modifyAnswer(AnswerUpdateRequestDto dto) {
        String updateContents = fileService.compareFilePathAndOptimization(
                dto.getAnsIdx(),
                "ANSWER",
                dto.getAnsContents()
        );

        Answer answer = findAnswer(dto.getAnsIdx());

        if (!answer.getAnsLocked())
            throw new AnswerInactiveException();

        User user = findAuthor(dto.getUserEmail());

        answer.hasSameAuthor(user);

        answer.update(updateContents);

        return answer.getQueIdx();
    }

    // 이부분 다시 한번 봐 주셈 (2022.11.09)
    // 예외 처리가 너무 많은가?
    @Transactional
    public Long selectAnswer(AnswerSelectRequestDto dto) {
        Answer answer = findAnswer(dto.getAnsIdx());

        if (!answer.getAnsLocked())
            throw new AnswerInactiveException();

        Question question = answer.getQuestion();

        if (!question.getQueLocked())
            throw new QuestionInactiveException();

        User requester = findAuthor(dto.getUserEmail());

        // 답변을 채택하는 사람은 질문을 작성한 작성자이기 때문에
        // 요청을 보낸 요청자와 질문의 작성자가 같아야 채택 가능
        // hasSameAuthor -> unAuthorizedCheck 같은 걸로 바꾸는게 나은 듯
        question.hasSameAuthor(requester);

        // 채택 검사
        // 이미 해결 된 질문은 채택을 진행 할 수 없다.
        if(question.getQueSolved())
            throw new QuestionAlreadySolvedException();

        // 이미 채택 된 답변을 다시 채택 할 수 없다.
        if (answer.getAnsSelected())
            throw new AnswerAlreadySelectedException();

        answer.toSelect();
        question.toSolve();

        return question.getQueIdx();
    }

//    @Transactional
//    public void disableAnswer(Long ansIdx) {
//        Answer answer = answerRepository.findById(ansIdx)
//                .orElseThrow(AnswerNotFoundException::new);
//
//        answer.disableAnswer();
////        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
//    }
//
//    @Transactional
//    public void enableAnswer(Long ansIdx) {
//        Answer answer = answerRepository.findById(ansIdx)
//                .orElseThrow(AnswerNotFoundException::new);
//
//        answer.enableAnswer();
////        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
//    }
//
//    @Transactional
//    public void removeAnswer(Long ansIdx) {
//        Answer answer = answerRepository.findById(ansIdx)
//                .orElseThrow(AnswerNotFoundException::new);
//
//        answerRepository.delete(answer);
////        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
//    }
//
//    private Boolean isSameWriter(Answer answer, User user) {
//        String writerEmail = answer.getAnswerer().getUserEmail();
//        String userEmail = user.getUserEmail();
//
//        return (writerEmail.equals(userEmail)) ? true : false;
//    }

    // answer의 작성자가 아니라 question의 작성자가 맞는지 검사해야한다.
    // 하지만 위의 메서드와 하는 일은 같으니 통합시킬 수 없을까?
    private Boolean selectAuthCheck(Question question, User user) {
        String writerEmail = question.getQuestioner().getUserEmail();
        String userEmail = user.getUserEmail();

        return (writerEmail.equals(userEmail)) ? true : false;
    }

    private Question findQuestion(Long queIdx) {
        return questionRepository.findQuestionByIdx(queIdx)
                .orElseThrow(QuestionNotFoundException::new);
    }

    private Answer findAnswer(Long ansIdx) {
        return answerRepository.findAnswerByIdx(ansIdx)
                .orElseThrow(AnswerNotFoundException::new);
    }

    private User findAuthor(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);
    }
}
