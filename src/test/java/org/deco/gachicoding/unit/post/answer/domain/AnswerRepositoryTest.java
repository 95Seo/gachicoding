package org.deco.gachicoding.unit.post.answer.domain;

import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.answer.domain.repository.AnswerRepository;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.repository.QuestionRepository;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    User testUser;

    Question testQuestion;

    String ansContent = "테스트 답변 내용 (고양이)";

    @BeforeEach
    private void before() {

        User user = User.builder()
                .userEmail("test111@test.com")
                .userPassword("test1234")
                .userName("테스트")
                .userNick("testMachine")
                .build();

        testUser = userRepository.save(user);

        Question question = Question.builder()
                .queTitle("테스트 질문 제목")
                .queContents("테스트 질문 내용")
                .queError("테스트 질문 에러 로그")
                .queCategory("자바")
                .writer(testUser)
                .build();

        testQuestion = questionRepository.save(question);
    }

    private Answer createAnswerMock() {
        Answer answer = Answer.builder()
                .writer(testUser)
                .question(testQuestion)
                .ansContents(ansContent)
                .build();

        return answerRepository.save(answer);
    }

    @Test
    public void 인덱스로_답변_조회() {
        Answer testAnswer = createAnswerMock();
        Long answerIdx = testAnswer.getAnsIdx();

        Optional<Answer> answer = answerRepository.findById(answerIdx);

        assertEquals(ansContent, answer.get().getAnsContents());
    }

    @Test
    public void 답변_목록_조회() {
        for(int i = 0; i < 10; i++) {
            createAnswerMock();
        }

        String findKeyword = "";

        Page<Answer> answers = answerRepository.findByAnsContentsContainingIgnoreCaseAndAnsActivatedTrueOrderByAnsIdxDesc(findKeyword, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "ansIdx")));

        // NumberOfElements 요청 페이지에서 조회 된 데이터의 갯수
        assertEquals(10, answers.getTotalElements());
    }

    @Test
    public void 인덱스로_답변_삭제() {
        Answer testAnswer = createAnswerMock();

        Long answerIdx = testAnswer.getAnsIdx();

        Optional<Answer> answer = answerRepository.findById(answerIdx);

        assertTrue(answer.isPresent());

        answerRepository.deleteById(answerIdx);

        answer = answerRepository.findById(answerIdx);

        assertTrue(answer.isEmpty());
    }

    @Test
    public void 검색어로_답변_검색_리스트() {
        Answer testAnswer = createAnswerMock();

        Long answerIdx = testAnswer.getAnsIdx();

        Optional<Answer> answer = answerRepository.findById(answerIdx);

        assertTrue(answer.isPresent());

        String findKeyword = "고양이";

        Page<Answer> search_answer = answerRepository.findByAnsContentsContainingIgnoreCaseAndAnsActivatedTrueOrderByAnsIdxDesc(findKeyword, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "ansIdx")));

        for (Answer ans : search_answer) {
            assertEquals(ans.getAnsContents(), ansContent);
        }
    }
}
