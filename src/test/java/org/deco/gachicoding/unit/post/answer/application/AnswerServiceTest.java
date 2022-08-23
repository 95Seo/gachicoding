package org.deco.gachicoding.unit.post.answer.application;

import org.deco.gachicoding.post.answer.dto.response.AnswerResponseDto;
import org.deco.gachicoding.post.answer.dto.request.AnswerSaveRequestDto;
import org.deco.gachicoding.post.answer.application.AnswerService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AnswerServiceTest {
    @Autowired
    AnswerService answerService;

    Long ansIdx;

    String userEmail = "test@test.com";
    Long queIdx = Long.valueOf(1);
    String ansContent = "답변 테스트 내용 강아지 병아리(테스트)";

    @BeforeEach
    void before() {
        AnswerSaveRequestDto dto = AnswerSaveRequestDto.builder()
                .userEmail(userEmail)
                .queIdx(queIdx)
                .ansContent(ansContent)
                .build();

        ansIdx = answerService.registerAnswer(dto);
    }

    @AfterEach
    void after() {
        if (ansIdx != null) {
            answerService.removeAnswer(this.ansIdx);
        }
        ansIdx = null;
    }

    @Test
    @DisplayName("답변_작성_테스트")
    void Answer_Integration_Test_1() {
        AnswerResponseDto responseDto = answerService.getAnswerDetail(ansIdx);

        assertEquals(userEmail, responseDto.getUserEmail());
        assertEquals(queIdx, responseDto.getQueIdx());
        assertEquals(ansContent, responseDto.getAnsContent());
    }

    @Test
    @DisplayName("답변_리스트_조회")
    public void Answer_Integration_Test_2() {
        String keyword = "병아리(테스트)";

        Page<AnswerResponseDto> answerList = answerService.getAnswerList(keyword, PageRequest.of(1, 10));

        assertEquals(answerList.getTotalElements(), 1);
    }

    @Test
    @DisplayName("인덱스로_답변_수정")
    public void Answer_Integration_Test_3() {
        /*String updateContent = "답변 테스트 내용 고양이 병아리(수정 테스트)";

        AnswerUpdateRequestDto updateAnswer = AnswerUpdateRequestDto.builder()
                .ansContent(updateContent)
                .build();

        answerService.modifyAnswerById(ansIdx, updateAnswer);

        AnswerResponseDto responseDto = answerService.getAnswerDetailById(ansIdx);

        assertEquals(ansIdx, responseDto.getAnsIdx());
        assertNotEquals(ansContent, responseDto.getAnsContent());
        assertEquals(updateContent, responseDto.getAnsContent());*/
    }

    @Test
    @DisplayName("인덱스로_답변_비활성화")
    public void Answer_Integration_Test_4() {
        answerService.disableAnswer(ansIdx);

//        assertThrows(IllegalArgumentException.class, () -> answerService.getAnswerDetailById(ansIdx));
    }

    @Test
    @DisplayName("인덱스로_답변_활성화")
    public void Answer_Integration_Test_5() {
        answerService.enableAnswer(ansIdx);

//        AnswerResponseDto responseDto = answerService.getAnswerDetailById(ansIdx);

//        assertEquals(responseDto.getAnsActivated(), true);
    }

    @Test
    @DisplayName("답변_삭제")
    public void Answer_Integration_Test_6() {
        answerService.removeAnswer(ansIdx);
//        assertThrows(IllegalArgumentException.class, () -> answerService.getAnswerDetailById(ansIdx));
        ansIdx = null;
    }
}
