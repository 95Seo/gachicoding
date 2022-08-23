package org.deco.gachicoding.unit.post.question.application;

import org.deco.gachicoding.post.question.dto.response.QuestionDetailPostResponseDto;
import org.deco.gachicoding.post.question.dto.response.QuestionListResponseDto;
import org.deco.gachicoding.post.question.dto.request.QuestionSaveRequestDto;
import org.deco.gachicoding.post.question.dto.request.QuestionUpdateRequestDto;
import org.deco.gachicoding.post.question.application.QuestionService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class QuestionServiceTest {
    @Autowired
    QuestionService questionService;

    Long queIdx;

    String queTitle = "질문 테스트 제목 고양이 병아리(테스트)";
    String queContent = "질문 테스트 내용 강아지 병아리(테스트)";
    String queError = "질문 테스트 에러 소스";
    String queCategory = "자바";
    String userEmail = "1234@1234.com";

    @BeforeEach
    void before() {
        try {
            QuestionSaveRequestDto dto = QuestionSaveRequestDto.builder()
                    .userEmail(userEmail)
                    .queTitle(queTitle)
                    .queContent(queContent)
                    .queError(queError)
                    .queCategory(queCategory)
                    .build();

            queIdx = questionService.registerQuestion(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void after() {
        if (queIdx != null) {
            questionService.removeQuestion(this.queIdx);
        }
        queIdx = null;
    }

    @Test
    @DisplayName("질문_작성_테스트")
    void Question_Integration_Testcase_1() {
        QuestionDetailPostResponseDto responseDto = questionService.getQuestionDetail(queIdx);

        assertEquals(queTitle, responseDto.getQueTitle());
        assertEquals(queContent, responseDto.getQueContent());
        assertEquals(queError, responseDto.getQueError());
        assertEquals(queCategory, responseDto.getQueCategory());
    }

    @Test
    @DisplayName("질문_리스트_조회")
    public void Question_Integration_Testcase_2() {
        String keyword = "병아리(테스트)";

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "queIdx"));
        Page<QuestionListResponseDto> questionList = questionService.getQuestionList(keyword, pageable);

        assertEquals(questionList.getTotalElements(), 1);
    }

    @Test
    @DisplayName("인덱스로_질문_수정")
    public void Question_Integration_Testcase_3() {
        String updateTitle = "질문 테스트 제목 고양이 병아리(수정 테스트)";
        String updateContent = "질문 테스트 내용 고양이 병아리(수정 테스트)";
        String updateError = "질문 테스트 에러 소스(수정)";
        String updateCategory = "파이썬";

        QuestionUpdateRequestDto updateQuestion = QuestionUpdateRequestDto.builder()
                .queIdx(queIdx)
                .queTitle(updateTitle)
                .queContent(updateContent)
                .queError(updateError)
                .queCategory(updateCategory)
                .build();

//        questionService.modifyQuestion(updateQuestion);

        QuestionDetailPostResponseDto responseDto = questionService.getQuestionDetail(queIdx);

        assertNotEquals(queTitle, responseDto.getQueTitle());
        assertEquals(updateTitle, responseDto.getQueTitle());

        assertNotEquals(queContent, responseDto.getQueContent());
        assertEquals(updateContent, responseDto.getQueContent());

        assertNotEquals(queError, responseDto.getQueError());
        assertEquals(updateError, responseDto.getQueError());

        assertNotEquals(queCategory, responseDto.getQueCategory());
        assertEquals(updateCategory, responseDto.getQueCategory());
    }

    @Test
    @DisplayName("인덱스로_질문_비활성화")
    public void Question_Integration_Testcase_4() {
        questionService.disableQuestion(queIdx);

        assertThrows(IllegalArgumentException.class, () -> questionService.getQuestionDetail(queIdx));
    }

    @Test
    @DisplayName("인덱스로_질문_활성화")
    public void Question_Integration_Testcase_5() {
        questionService.enableQuestion(queIdx);

        QuestionDetailPostResponseDto responseDto = questionService.getQuestionDetail(queIdx);

        assertEquals(responseDto.getQueActivated(), true);
    }

    @Test
    @DisplayName("질문_삭제")
    public void Question_Integration_Testcase_6() {
        questionService.removeQuestion(queIdx);
        assertThrows(IllegalArgumentException.class, () -> questionService.getQuestionDetail(queIdx));
        queIdx = null;
    }
}
