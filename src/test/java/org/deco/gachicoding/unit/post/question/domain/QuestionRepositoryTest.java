package org.deco.gachicoding.unit.post.question.domain;

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
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    User testUser;

    String queTitle = "테스트 질문 제목 (고양이)";
    String queContent = "테스트 질문 내용";
    String queError = "테스트 질문 에러 로그";
    String queCategory = "자바";

    @BeforeEach
    private void before() {
        User user = User.builder()
                .userEmail("test111@test.com")
                .userPassword("test1234")
                .userName("테스트")
                .userNick("testMachine")
                .build();

        testUser = userRepository.save(user);
    }

    private Question createQuestionMock() {
        Question question = Question.builder()
                .queTitle(queTitle)
                .queContents(queContent)
                .queError(queError)
                .queCategory(queCategory)
                .writer(testUser)
                .build();

        return questionRepository.save(question);
    }

    @Test
    public void 인덱스로_질문_조회() {
        Question testQuestion = createQuestionMock();

        Long questionIdx = testQuestion.getQueIdx();

        Optional<Question> question = questionRepository.findById(questionIdx);
        assertTrue(question.isPresent());
        assertEquals(queTitle, question.get().getQueTitle());
        assertEquals(queContent, question.get().getQueContents());
    }

    @Test
    public void 질문_목록_조회() {
        for(int i = 0; i < 10; i++) {
            createQuestionMock();
        }

        String findKeyword = "";

        Page<Question> questions = questionRepository.findByQueContentsContainingIgnoreCaseAndQueActivatedTrueOrQueTitleContainingIgnoreCaseAndQueActivatedTrueOrderByQueIdxDesc(findKeyword, findKeyword, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "queIdx")));

        // NumberOfElements 요청 페이지에서 조회 된 데이터의 갯수
        assertEquals(10, questions.getTotalElements());
    }

    @Test
    public void 인덱스로_질문_삭제() {
        Question testQuestion = createQuestionMock();

        Long questionIdx = testQuestion.getQueIdx();

        Optional<Question> question = questionRepository.findById(questionIdx);

        assertTrue(question.isPresent());

        questionRepository.deleteById(questionIdx);

        question = questionRepository.findById(questionIdx);

        assertTrue(question.isEmpty());
    }

    @Test
    public void 검색어로_질문_검색_리스트() {
        Question testQuestion = createQuestionMock();

        Long questionIdx = testQuestion.getQueIdx();

        Optional<Question> question = questionRepository.findById(questionIdx);

        assertTrue(question.isPresent());

        String findKeyword = "고양이";

        Page<Question> search_question = questionRepository.findByQueContentsContainingIgnoreCaseAndQueActivatedTrueOrQueTitleContainingIgnoreCaseAndQueActivatedTrueOrderByQueIdxDesc(findKeyword, findKeyword, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "queIdx")));

        for (Question que : search_question) {
            assertEquals(que.getQueTitle(),queTitle);
            assertEquals(que.getQueContents(),queContent);
            assertEquals(que.getQueError(),queError);
            assertEquals(que.getQueCategory(),queCategory);
        }
    }
}
