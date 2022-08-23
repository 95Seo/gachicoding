package org.deco.gachicoding.post.question.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.repository.QuestionRepository;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.tag.application.TagService;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.post.question.dto.response.QuestionDetailPostResponseDto;
import org.deco.gachicoding.post.question.dto.response.QuestionListResponseDto;
import org.deco.gachicoding.post.question.dto.request.QuestionSaveRequestDto;
import org.deco.gachicoding.post.question.dto.request.QuestionUpdateRequestDto;
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
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final TagService tagService;
    private final String BOARD_TYPE = "QUESTION";

    @Transactional
    public Long registerQuestion(QuestionSaveRequestDto dto) throws Exception {
        User writer = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Question question = questionRepository.save(dto.toEntity(writer));

        Long queIdx = question.getQueIdx();
        String queContent = question.getQueContents();
        String queError = question.getQueError();

        if (dto.getTags() != null)
            tagService.registerBoardTag(queIdx, dto.getTags(), BOARD_TYPE);

        try {
            question.updateContent(fileService.extractImgSrc(queIdx, queContent, BOARD_TYPE));
            question.updateError(fileService.extractImgSrc(queIdx, queError, BOARD_TYPE));
            log.info("Success Upload Question Idx : {}", queIdx);
        } catch (Exception e) {
            log.error("Failed To Extract {} File", "Question Content");
            e.printStackTrace();
            removeQuestion(queIdx);
            tagService.removeBoardTags(queIdx, BOARD_TYPE);
            throw e;
        }

        return queIdx;
    }

    // 리팩토링 - 검색 조건에 error도 추가
    @Transactional(readOnly = true)
    public Page<QuestionListResponseDto> getQuestionList(String keyword, Pageable pageable) {
        Page<Question> questions = questionRepository.findByQueContentsContainingIgnoreCaseAndQueActivatedTrueOrQueTitleContainingIgnoreCaseAndQueActivatedTrueOrderByQueIdxDesc(keyword, keyword, pageable);

        Page<QuestionListResponseDto> questionList = questions.map(
                result -> new QuestionListResponseDto(result)
        );

        questionList.forEach(
                questionListResponseDto ->
                        tagService.getTags(questionListResponseDto.getQueIdx(), BOARD_TYPE, questionListResponseDto)
        );

        return questionList;
    }


    @Transactional(readOnly = true)
    public QuestionDetailPostResponseDto getQuestionDetail(Long queIdx) {
        Question question = questionRepository.findByQueIdxAndQueActivatedTrue(queIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        QuestionDetailPostResponseDto questionDetail = QuestionDetailPostResponseDto.builder()
                .question(question)
                .build();

//        fileService.getFiles(queIdx, BOARD_TYPE, questionDetail);
        tagService.getTags(queIdx, BOARD_TYPE, questionDetail);

        return questionDetail;
    }

    @Transactional
    public QuestionDetailPostResponseDto modifyQuestion(QuestionUpdateRequestDto dto) throws RuntimeException {
        Question question = questionRepository.findById(dto.getQueIdx())
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        // 작성자와 수정 시도하는 유저가 같은지 판별
        // 아마 제공되는 인증 로직이 있지 않을까 싶음.
        User user = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        if (!isSameWriter(question, user)) {
            throw new ApplicationException(INVALID_AUTH_USER);
        }

        // null 문제 해결 못함
        question = question.update(dto);

        QuestionDetailPostResponseDto questionDetail = QuestionDetailPostResponseDto.builder()
                .question(question)
                .build();

        return questionDetail;
    }

    @Transactional
    public ResponseEntity<ResponseState> disableQuestion(Long queIdx) {
        Question question = questionRepository.findByQueIdxAndQueActivatedTrue(queIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        question.isDisable();
        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<ResponseState> enableQuestion(Long queIdx) {
        Question question = questionRepository.findById(queIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        question.isEnable();
        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<ResponseState> removeQuestion(Long queIdx) {
        Question question = questionRepository.findById(queIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

        questionRepository.delete(question);
        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
    }

    private Boolean isSameWriter(Question question, User user) {
        String writerEmail = question.getWriter().getUserEmail();
        String userEmail = user.getUserEmail();

        return (writerEmail.equals(userEmail)) ? true : false;
    }
}
