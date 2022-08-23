package org.deco.gachicoding.comment.presentation;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.comment.dto.response.CommentResponseDto;
import org.deco.gachicoding.comment.dto.request.CommentSaveRequestDto;
import org.deco.gachicoding.comment.dto.request.CommentUpdateRequestDto;
import org.deco.gachicoding.exception.ResponseState;
import org.deco.gachicoding.comment.application.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "댓글 정보 처리 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestCommentComtroller {
    private final CommentService commentService;

    @ApiOperation(value = "댓글 작성")
    @ApiResponses(
            @ApiResponse(code = 200, message = "등록된 댓글 번호 반환")
    )
    @PostMapping("/comment")
    public Long registerComment(@ApiParam(value = "댓글 요청 body 정보") @RequestBody CommentSaveRequestDto dto) {
        log.info("{} Register Controller", "Comment");

        return commentService.registerComment(dto);
    }

    @ApiOperation(value = "댓글 목록")
    @ApiResponses(
            @ApiResponse(code = 200, message = "댓글 목록 반환")
    )
    @GetMapping("/comment/list")
    public Page<CommentResponseDto> getCommentList(@ApiParam(value = "글 카테고리") @RequestParam(value = "articleCategory", defaultValue = "") String articleCategory,
                                                   @ApiParam(value = "글 번호") @RequestParam(value = "articleIdx", defaultValue = "") Long articleIdx,
                                                   @ApiIgnore @PageableDefault(size = 10) Pageable pageable) {

        return commentService.getCommentList(articleCategory, articleIdx, pageable);
    }

    @ApiOperation(value = "댓글 수정")
    @ApiResponses(
            @ApiResponse(code = 200, message = "수정 성공")
    )
    @PutMapping("/comment/modify")
    public void modifyComment(@ApiParam(value = "댓글 수정 요청 body 정보") @RequestBody CommentUpdateRequestDto dto) {
        commentService.modifyComment(dto);
    }

    @ApiOperation(value = "댓글 비활성화", notes = "사용자 입장에서 댓글 데이터를 삭제")
    @ApiResponses(
            @ApiResponse(code = 200, message = "비활성화 성공")
    )
    @PutMapping("/comment/disable/{commentIdx}")
    public ResponseEntity<ResponseState> disableComment(@ApiParam(value = "댓글 번호") @PathVariable Long commentIdx) {
        return commentService.disableComment(commentIdx);
    }

    @ApiOperation(value = "댓글 활성화", notes = "사용자 입장에서 삭제된 댓글 데이터 복구")
    @ApiResponses(
            @ApiResponse(code = 200, message = "활성화 성공")
    )
    @PutMapping("/comment/enable/{commentIdx}")
    public ResponseEntity<ResponseState> enableComment(@ApiParam(value = "댓글 번호") @PathVariable Long commentIdx) {
        return commentService.enableComment(commentIdx);
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글 데이터를 DB에서 완전히 삭제")
    @ApiResponses(
            @ApiResponse(code = 200, message = "삭제 성공")
    )
    @DeleteMapping("/comment/{commentIdx}")
    public ResponseEntity<ResponseState> removeComment(@ApiParam(value = "댓글 번호") @PathVariable Long commentIdx) {
        return commentService.removeComment(commentIdx);
    }
}
