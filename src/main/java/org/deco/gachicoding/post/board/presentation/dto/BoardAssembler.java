package org.deco.gachicoding.post.board.presentation.dto;

import org.deco.gachicoding.post.board.application.dto.request.*;
import org.deco.gachicoding.post.board.application.dto.response.BoardResponseDto;
import org.deco.gachicoding.post.board.presentation.dto.request.BoardSaveRequest;
import org.deco.gachicoding.post.board.presentation.dto.request.BoardUpdateRequest;
import org.deco.gachicoding.post.board.presentation.dto.response.BoardResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BoardAssembler {

    private BoardAssembler() {}

    public static BoardBasicRequestDto boardBasicRequestDto(Long boardIdx, String userEmail) {
        return BoardBasicRequestDto.builder()
                .boardIdx(boardIdx)
                .userEmail(userEmail)
                .build();
    }

    public static BoardSaveRequestDto boardSaveRequestDto(BoardSaveRequest request) {
        return BoardSaveRequestDto.builder()
                .userEmail(request.getUserEmail())
                .boardTitle(request.getBoardTitle())
                .boardContents(request.getBoardContents())
                .boardCategory(request.getBoardCategory())
                .boardViews(request.getBoardViews())
                .build();
    }

    public static BoardListRequestDto boardListRequestDto(String keyword, Pageable pageable) {
        return BoardListRequestDto.builder()
                .keyword(keyword)
                .pageable(pageable)
                .build();
    }

    public static BoardDetailRequestDto boardDetailRequestDto(Long boardIdx) {
        return BoardDetailRequestDto.builder()
                .boardIdx(boardIdx)
                .build();
    }

    public static BoardResponse boardResponse(BoardResponseDto dto) {
        return BoardResponse.builder()
                .boardIdx(dto.getBoardIdx())
                .authorEmail(dto.getAuthorEmail())
                .authorNick(dto.getAuthorNick())
                .boardTitle(dto.getBoardTitle())
                .boardContents(dto.getBoardContents())
                .boardViews(dto.getBoardViews())
                .createAt(dto.getCreateAt())
                .updateAt(dto.getUpdateAt())
                .build();
    }

    public static BoardUpdateRequestDto boardUpdateRequestDto(Long boardIdx, BoardUpdateRequest request) {
        return BoardUpdateRequestDto.builder()
                .boardIdx(boardIdx)
                .userEmail(request.getUserEmail())
                .boardTitle(request.getBoardTitle())
                .boardContents(request.getBoardContents())
                .build();
    }

    public static List<BoardResponse> boardResponses(List<BoardResponseDto> boardResponseDtos) {
        return boardResponseDtos.stream()
                .map(boardResponse())
                .collect(Collectors.toList());
    }

    private static Function<BoardResponseDto, BoardResponse> boardResponse() {
        return boardResponseDto -> BoardResponse.builder()
                .boardIdx(boardResponseDto.getBoardIdx())
                .authorEmail(boardResponseDto.getAuthorEmail())
                .authorNick(boardResponseDto.getAuthorNick())
                .boardTitle(boardResponseDto.getBoardTitle())
                .boardContents(boardResponseDto.getBoardContents())
                .boardViews(boardResponseDto.getBoardViews())
                .createAt(boardResponseDto.getCreateAt())
                .updateAt(boardResponseDto.getUpdateAt())
                .build();
    }

}
