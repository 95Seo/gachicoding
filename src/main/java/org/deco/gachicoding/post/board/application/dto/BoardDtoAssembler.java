package org.deco.gachicoding.post.board.application.dto;

import org.deco.gachicoding.post.board.application.dto.request.BoardSaveRequestDto;
import org.deco.gachicoding.post.board.application.dto.response.BoardResponseDto;
import org.deco.gachicoding.post.board.domain.Board;
import org.deco.gachicoding.user.domain.User;

import java.util.List;

import static java.util.stream.Collectors.*;

public class BoardDtoAssembler {

    private BoardDtoAssembler() {}

    public static Board board(User user, BoardSaveRequestDto dto) {
        return Board.builder()
                .author(user)
                .boardTitle(dto.getBoardTitle())
                .boardContents(dto.getBoardContents())
                .boardCategory(dto.getBoardCategory())
                .build();
    }

    public static BoardResponseDto boardResponseDto(Board board) {
        return BoardResponseDto.builder()
                .boardIdx(board.getBoardIdx())
                .authorEmail(board.getAuthorEmail())
                .authorNick(board.getAuthorNick())
                .boardTitle(board.getBoardTitle())
                .boardContents(board.getBoardContents())
                .boardCategory(board.getBoardCategory())
                .boardViews(board.getBoardViews())
                .createAt(board.getCreatedAt())
                .updateAt(board.getUpdatedAt())
                .build();
    }

    public static List<BoardResponseDto> boardResponseDtos(List<Board> boards) {
        return boards.stream()
                .map(board -> convertForm(board))
                .collect(toList());
    }

    private static BoardResponseDto convertForm(Board board) {
        return BoardResponseDto.builder()
                .boardIdx(board.getBoardIdx())
                .authorEmail(board.getAuthorEmail())
                .authorNick(board.getAuthorNick())
                .boardTitle(board.getBoardTitle())
                .boardContents(board.getBoardContents())
                .boardCategory(board.getBoardCategory())
                .boardViews(board.getBoardViews())
                .createAt(board.getCreatedAt())
                .updateAt(board.getUpdatedAt())
                .build();
    }
}
