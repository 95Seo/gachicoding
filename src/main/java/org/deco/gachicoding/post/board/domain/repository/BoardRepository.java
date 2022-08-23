package org.deco.gachicoding.post.board.domain.repository;

import org.deco.gachicoding.post.board.domain.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
//    Page<Board> findByBoardCategoryAndBoardContentContainingIgnoreCaseAndBoardActivatedTrueOrBoardCategoryAndBoardTitleContainingIgnoreCaseAndBoardActivatedTrue(String boardType1, String boardContent,String boardType2,  String boardTitle, Pageable pageable);
//    Page<Board> findByBoardContentsContainingIgnoreCaseAndBoardActivatedTrueOrBoardTitleContainingIgnoreCaseAndBoardActivatedTrue(String boardContent, String boardTitle, Pageable pageable);

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.author WHERE b.boardIdx = :boardIdx")
    Optional<Board> findBoardByIdx(@Param("boardIdx") Long boardIdx);

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.author WHERE b.boardLocked = true AND b.boardIdx = :boardIdx")
    Optional<Board> findEnableBoardByIdx(@Param("boardIdx") Long boardIdx);

    @Query("SELECT DISTINCT b FROM Board b LEFT JOIN FETCH b.author WHERE b.boardLocked = true AND (b.boardTitle.boardTitle LIKE %:keyword% OR b.boardContents.boardContents LIKE %:keyword%) ")
    List<Board> findAllBoardByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
