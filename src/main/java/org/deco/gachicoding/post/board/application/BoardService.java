package org.deco.gachicoding.post.board.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.post.board.application.dto.BoardDtoAssembler;
import org.deco.gachicoding.post.board.application.dto.request.*;
import org.deco.gachicoding.post.board.application.dto.response.BoardResponseDto;
import org.deco.gachicoding.post.board.domain.Board;
import org.deco.gachicoding.post.board.domain.repository.BoardRepository;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.tag.application.TagService;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.exception.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.deco.gachicoding.exception.StatusEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final TagService tagService;

    String BOARD = "BOARD";

    @Transactional
    public Long registerBoard(BoardSaveRequestDto dto) throws Exception {
        Board board = boardRepository.save(createBoard(dto));

//        Long boardIdx = board.getBoardIdx();
//        String boardContent = board.getBoardContents().getBoardContents();

//        if (dto.getTags() != null)
//            tagService.registerBoardTag(boardIdx, dto.getTags(), BOARD);

//        try {
//            board.updateContent(fileService.extractImgSrc(boardIdx, boardContent, BOARD));
//        } catch (Exception e) {
//            log.error("Failed To Extract {} File", "Board Content");
//            e.printStackTrace();
//            removeBoard(boardIdx);
//            tagService.removeBoardTags(boardIdx, BOARD);
//            // throw해줘야 Advice에서 예외를 감지 함
//            throw e;
//        }

        return board.getBoardIdx();
    }

    private Board createBoard(BoardSaveRequestDto dto) {
        User user = findAuthor(dto.getUserEmail());

        return BoardDtoAssembler.board(user, dto);
    }

    @Transactional
    public List<BoardResponseDto> getBoardList(BoardListRequestDto dto) {
//        Page<BoardResponseDto> boardList =
//                boardRepository.findAllBoardByKeyword(keyword, pageable).map(entity -> new BoardPostResponseDto(entity));

//        boardList.forEach(
//                boardResponseDto ->
//                        tagService.getTags(boardResponseDto.getBoardIdx(), BOARD, boardResponseDto)
//        );

        return BoardDtoAssembler.boardResponseDtos(boardRepository.findAllBoardByKeyword(dto.getKeyword(), dto.getPageable()));
    }

    @Transactional
    public BoardResponseDto getBoardDetail(BoardDetailRequestDto dto) {
//        Board board = boardRepository.findById(boardIdx)
//                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));

//        fileService.getFiles(boardIdx, boardCategory, boardDetail);
//        tagService.getTags(boardIdx, BOARD, boardDetail);

        return BoardDtoAssembler.boardResponseDto(findEnableBoard(dto.getBoardIdx()));
    }

    @Transactional
    public BoardResponseDto modifyBoard(BoardUpdateRequestDto dto) {
        Board board = findEnableBoard(dto.getBoardIdx());

        User user = findAuthor(dto.getUserEmail());

        board.hasSameAuthor(user);

        board.update(dto.getBoardTitle(), dto.getBoardContents());

        return BoardDtoAssembler.boardResponseDto(board);
    }

    @Transactional
    public void disableBoard(BoardBasicRequestDto dto) {
        Board board = findBoard(dto.getBoardIdx());

        User user = findAuthor(dto.getUserEmail());

        board.hasSameAuthor(user);

        board.disableBoard();
    }

    @Transactional
    public void enableBoard(BoardBasicRequestDto dto) {
        Board board = findBoard(dto.getBoardIdx());

        User user = findAuthor(dto.getUserEmail());

        board.hasSameAuthor(user);

        board.enableBoard();
    }

    @Transactional
    public void removeBoard(BoardBasicRequestDto dto) {
        Board board = findBoard(dto.getBoardIdx());

        User user = findAuthor(dto.getUserEmail());

        board.hasSameAuthor(user);

        boardRepository.delete(board);
    }

    private Board findEnableBoard(Long boardIdx) {
        return boardRepository.findEnableBoardByIdx(boardIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));
    }

    private Board findBoard(Long boardIdx) {
        return boardRepository.findBoardByIdx(boardIdx)
                .orElseThrow(() -> new ApplicationException(DATA_NOT_EXIST));
    }

    private User findAuthor(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
    }
}
