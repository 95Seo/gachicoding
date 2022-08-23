package org.deco.gachicoding.post.question.domain.repository;

import org.deco.gachicoding.post.question.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByQueIdxAndQueActivatedTrue(Long queIdx);

    // Containing이 없다면 해당 키워드와 일치하는 결과만 찾고, 이 키워드가 있는 경우는 포함하는 결과를 검색 즉, SQL문의 like %xx% 와 비슷함
    // IgnoreCase 키워드는 대소문자 구별을 하지 않는다는 의미, 없다면 대소문자 구별
    Page<Question> findByQueContentsContainingIgnoreCaseAndQueActivatedTrueOrQueTitleContainingIgnoreCaseAndQueActivatedTrueOrderByQueIdxDesc(String queContent, String queTitle, Pageable pageable);

}
