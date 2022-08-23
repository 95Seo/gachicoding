package org.deco.gachicoding.post.notice.presentation;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.ResponseState;
import org.deco.gachicoding.post.notice.application.dto.request.*;
import org.deco.gachicoding.post.notice.application.NoticeService;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeResponseDto;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeUpdateResponseDto;
import org.deco.gachicoding.post.notice.presentation.dto.NoticeAssembler;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeSaveRequest;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeUpdateRequest;
import org.deco.gachicoding.post.notice.presentation.dto.response.NoticeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.util.List;

import static org.deco.gachicoding.exception.StatusEnum.*;

@Api(tags = "공지사항 정보 처리 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestNoticeController {

    private static final String REDIRECT_URL = "/api/notice/%d";

    private final NoticeService noticeService;

    @ApiOperation(value = "공지사항 등록", notes = "공지사항 작성 요청 DTO를 받아 공지사항 등록 수행")
    @ApiResponses(
            @ApiResponse(code = 200, message = "등록된 공지사항 번호 반환")
    )
    @PostMapping("/notice")
    public ResponseEntity<Void> registerNotice(@ApiParam(value = "공지사항 요청 body 정보") @RequestBody NoticeSaveRequest request) throws Exception {
        log.info("{} Register Controller", "Notice");

        Long notIdx = noticeService.registerNotice(NoticeAssembler.noticeSaveRequestDto(request));
        String redirectUrl = String.format(REDIRECT_URL, notIdx);

        // 이 부분 더 알아보자 리다이렉트 부분
        return ResponseEntity.created(URI.create(redirectUrl)).build();
    }

    @ApiOperation(value = "공지사항 리스트 보기", notes = "공지사항 목록을 응답")
    @ApiResponses(
            @ApiResponse(code = 200, message = "공지사항 목록 반환")
    )
    @GetMapping("/notice/list")
    public ResponseEntity<List<NoticeResponse>> getNoticeList(@ApiParam(value = "keyword") @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                              @ApiIgnore @PageableDefault(size = 10) Pageable pageable) {
        NoticeListRequestDto dto = NoticeAssembler.noticeListRequestDto(keyword, pageable);

        List<NoticeResponseDto> noticeResponseDtos = noticeService.getNoticeList(dto);

        List<NoticeResponse> noticeResponses = NoticeAssembler.noticeResponses(noticeResponseDtos);

        return ResponseEntity.ok(noticeResponses);
    }

    @ApiOperation(value = "공지사항 상세 보기", notes = "상세한 공지사항 데이터 응답")
    @ApiResponses(
            @ApiResponse(code = 200, message = "공지사항 상세 정보 반환")
    )
    @GetMapping("/notice/{notIdx}")
    public ResponseEntity<NoticeResponse> getNoticeDetail(@ApiParam(value = "공지사항 번호") @PathVariable Long notIdx) {

        NoticeDetailRequestDto dto = NoticeAssembler.noticeDetailDto(notIdx);

        NoticeResponse noticeResponse = NoticeAssembler.noticeResponse(noticeService.getNoticeDetail(dto));

        return ResponseEntity.ok(noticeResponse);
    }

    @ApiOperation(value = "공지사항 수정")
    @ApiResponses(
            @ApiResponse(code = 200, message = "수정 후 공지사항 상세 정보 반환")
    )
    @PutMapping("/notice/modify/{notIdx}")
    public ResponseEntity<NoticeUpdateResponseDto> modifyNotice(@ApiParam(value = "공지사항 번호") @PathVariable Long notIdx, @ApiParam(value = "공지사항 수정 요청 body 정보") @RequestBody NoticeUpdateRequest request) {

        NoticeUpdateRequestDto dto = NoticeAssembler.noticeUpdateRequestDto(notIdx, request);

        noticeService.modifyNotice(dto);

        String redirectUrl = String.format(REDIRECT_URL, notIdx);

        return ResponseEntity.created(URI.create(redirectUrl)).build();
    }

    @ApiOperation(value = "공지사항 비활성화")
    @ApiResponses(
            @ApiResponse(code = 200, message = "비활성화 성공")
    )
    @PutMapping("/notice/disable/{notIdx}")
    public ResponseEntity<ResponseState> disableNotice(@ApiParam(value = "공지사항 번호") @PathVariable Long notIdx, @ApiParam(value = "userEmail") @RequestParam(value = "userEmail", defaultValue = "") String userEmail) {
        System.out.println(userEmail);
        NoticeBasicRequestDto dto = NoticeAssembler.noticeBasicRequestDto(notIdx, userEmail);

        noticeService.disableNotice(dto);

        // 활성 비활성 삭제도 리다이렉트..?
        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
    }

    @ApiOperation(value = "공지사항 활성화")
    @ApiResponses(
            @ApiResponse(code = 200, message = "활성화 성공")
    )
    @PutMapping("/notice/enable/{notIdx}")
    public ResponseEntity<ResponseState> enableNotice(@ApiParam(value = "공지사항 번호") @PathVariable Long notIdx, @ApiParam(value = "userEmail") @RequestParam(value = "userEmail", defaultValue = "") String userEmail) {

        NoticeBasicRequestDto dto = NoticeAssembler.noticeBasicRequestDto(notIdx, userEmail);

        noticeService.enableNotice(dto);

        // 활성 비활성 삭제도 리다이렉트..?
        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
    }

    @ApiOperation(value = "공지사항 삭제", notes = "공지사항 번호를 받아 공지사항 삭제 수행")
    @ApiResponses(
            @ApiResponse(code = 200, message = "삭제 성공")
    )
    @DeleteMapping("/notice/remove/{notIdx}")
    public ResponseEntity<ResponseState> removeNotice(@ApiParam(value = "공지사항 번호") @PathVariable Long notIdx, @ApiParam(value = "userEmail") @RequestParam(value = "userEmail", defaultValue = "") String userEmail) {

        NoticeBasicRequestDto dto = NoticeAssembler.noticeBasicRequestDto(notIdx, userEmail);

        noticeService.removeNotie(dto);

        // 활성 비활성 삭제도 리다이렉트..?
        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
    }

}
