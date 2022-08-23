package org.deco.gachicoding.post.answer.presentation;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.post.answer.dto.response.AnswerResponseDto;
import org.deco.gachicoding.post.answer.dto.request.AnswerSaveRequestDto;
import org.deco.gachicoding.post.answer.dto.request.AnswerSelectRequestDto;
import org.deco.gachicoding.post.answer.dto.request.AnswerUpdateRequestDto;
import org.deco.gachicoding.exception.ResponseState;
import org.deco.gachicoding.post.answer.application.AnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "가치답변 정보 처리 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestAnswerController {
    private final AnswerService answerService;

    @ApiOperation(value = "답변 등록", notes = "답변 등록 수행")
    @ApiResponses(
            @ApiResponse(code = 200, message = "등록된 답변 번호 반환")
    )
    @PostMapping("/answer")
    public Long registerAnswer(@ApiParam(value = "질문 요청 body 정보") @RequestBody AnswerSaveRequestDto dto) {
        return answerService.registerAnswer(dto);
    }

//    @ApiOperation(value = "답변 리스트")
//    @GetMapping("/answer/list")
//    public Page<AnswerResponseDto> getAnswerList(@RequestParam(value = "keyword", defaultValue = "") String keyword, @PageableDefault(size = 10) Pageable pageable) {
//        return answerService.getAnswerList(keyword, pageable);
//    }

    @ApiOperation(value = "답변 디테일")
    @ApiResponses(
            @ApiResponse(code = 200, message = "답변 상세 정보 봔한")
    )
    @GetMapping("/answer/{ansIdx}")
    public AnswerResponseDto getAnswerDetail(@ApiParam(value = "답변 번호") @PathVariable Long ansIdx) {
        return answerService.getAnswerDetail(ansIdx);
    }

    @ApiOperation(value = "답변 수정")
    @ApiResponses(
            @ApiResponse(code = 200, message = "수정 후 답변 상세 정보 봔한")
    )
    @PutMapping("/answer/modify")
    public AnswerResponseDto modifyAnswer(@ApiParam(value = "답변 수정 요청 body 정보") @RequestBody AnswerUpdateRequestDto dto) {
        return answerService.modifyAnswer(dto);
    }

    @ApiOperation(value = "답변 채택")
    @ApiResponses({
            @ApiResponse(code = 200, message = "채택 성공"),
            @ApiResponse(code = 409, message = "해결이 완료된 질문입니다"),
            @ApiResponse(code = 500, message = "권한이 없는 유저입니다")
    })
    @PutMapping("/answer/select")
    public ResponseEntity<ResponseState> selectAnswer(@ApiParam(value = "답변 채택 요청 body 정보") @RequestBody AnswerSelectRequestDto dto) {
        return answerService.selectAnswer(dto);
    }

    @ApiOperation(value = "답변 비활성화")
    @ApiResponses(
            @ApiResponse(code = 200, message = "비활성화 성공")
    )
    @PutMapping("/answer/disable/{ansIdx}")
    public ResponseEntity<ResponseState> disableAnswer(@ApiParam(value = "답변 번호") @PathVariable Long ansIdx) {
        return answerService.disableAnswer(ansIdx);
    }

    @ApiOperation(value = "답변 활성화")
    @ApiResponses(
            @ApiResponse(code = 200, message = "활성화 성공")
    )
    @PutMapping("/answer/enable/{ansIdx}")
    public ResponseEntity<ResponseState> enableAnswer(@ApiParam(value = "답변 번호") @PathVariable Long ansIdx) {
        return answerService.enableAnswer(ansIdx);
    }

    @ApiOperation(value = "답변 삭제")
    @ApiResponses(
            @ApiResponse(code = 200, message = "삭제 성공")
    )
    @DeleteMapping("/answer/{ansIdx}")
    public ResponseEntity<ResponseState> removeAnswer(@ApiParam(value = "답변 번호") @PathVariable Long ansIdx) {
        return answerService.removeAnswer(ansIdx);
    }
}
