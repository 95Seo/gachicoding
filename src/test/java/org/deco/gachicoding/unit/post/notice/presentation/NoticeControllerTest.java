package org.deco.gachicoding.unit.post.notice.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.deco.gachicoding.common.factory.post.notice.NoticeFactory;
import org.deco.gachicoding.common.factory.user.UserFactory;
import org.deco.gachicoding.config.SecurityConfig;
import org.deco.gachicoding.exception.ApplicationException;
import org.deco.gachicoding.post.notice.application.NoticeService;
import org.deco.gachicoding.post.notice.application.dto.request.*;
import org.deco.gachicoding.post.notice.application.dto.response.NoticeResponseDto;
import org.deco.gachicoding.post.notice.domain.Notice;
import org.deco.gachicoding.post.notice.presentation.RestNoticeController;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeSaveRequest;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeUpdateRequest;
import org.deco.gachicoding.post.notice.presentation.dto.response.NoticeResponse;
import org.deco.gachicoding.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.deco.gachicoding.exception.StatusEnum.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** 이슈 정리
 * <br> 1. SecurityConfig 빈 생성 불가 문제
 * <br> 해결 : excludeFilters 를 통해 객체 주입 제외
 * <br> 참고 : https://velog.io/@cieroyou/WebMvcTest%EC%99%80-Spring-Security-%ED%95%A8%EA%BB%98-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
 *
 * <br><br> 2. jpaAuditing 빈 생성 불가 문제
 * <br> 문제 : SpringBootApplication 클래스에 EnableJpaAuditing 어노테이션을 함께 둔 것이 문제,
 * <br> EnableJpaAuditing이 SpringBootApplication에 위치하므로써 모든 테스트가 jpa 관련 빈을 필요로 하게 되었음
 * <br> 해결1 : 따로 config 클래스를 생성해 필요한 곳에만 주입하는 방법
 * <br> 참고 : https://stackoverflow.com/questions/41250177/getting-at-least-one-jpa-metamodel-must-be-present-with-webmvctest, https://xlffm3.github.io/spring%20&%20spring%20boot/JPAError/
 * <br> 해결 2 : 테스트마다 MockBean으로 jpa 의존성을 주입하는 방법
 * <br> 참고 : https://stackoverflow.com/questions/41250177/getting-at-least-one-jpa-metamodel-must-be-present-with-webmvctest,
 *
 * <br><br> 3. csrf 토큰 널로 에러 - 403
 * <br> 해결 : spring-security-test 의존성 추가 후 SecurityMockMvcRequestPostProcessors.csrf()를 통해 요청에 csrf 토큰 주입
 * <br> 참고 : https://velog.io/@cieroyou/WebMvcTest%EC%99%80-Spring-Security-%ED%95%A8%EA%BB%98-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
 * <br><br> 4. 시큐리티 권한 에러 - 401
 * <br> 해결 : WithMockUser 어노테이션으로 유저 권한 부여
 * <br> 참고 : https://velog.io/@cieroyou/WebMvcTest%EC%99%80-Spring-Security-%ED%95%A8%EA%BB%98-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
 * <br><br> 5. 잘못된 요청 에러 - 400
 * <br> 해결 : param이 아닌 content에 objectMapper로 dto 클래스를 파라미터로 주입, objectMapper에 대한 추가적인 이해 필요
 * <br> 참고 : 없음
**/

// 특정 컨트롤러 클래스만 지정하여 스캔
@WebMvcTest(controllers = RestNoticeController.class,
            excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
            })
@MockBean(JpaMetamodelMappingContext.class)     // jpaAuditingHandler
@WithMockUser
public class NoticeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoticeService noticeService;

    @Test
    @DisplayName("사용자는 공지사항을 작성할 수 있다.")
    void write_writeNoticeWithUser_Success() throws Exception {
        // given
        String userEmail = "gachicoding@test.com";
        String notTitle = "테스트 공지사항 제목 수정 전";
        String notContents = "테스트 공지사항 내용 수정 전";

        NoticeSaveRequest request = NoticeFactory.mockNoticeSaveRequest(userEmail, notTitle, notContents);

        given(noticeService.registerNotice(any(NoticeSaveRequestDto.class)))
                .willReturn(1L);

        // when
        ResultActions perform = mockMvc.perform(post("/api/notice")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isCreated());

        // userRepository의 findByUserEmail이 1번 실행되었는지 검사한다.
        verify(noticeService, times(1))
                .registerNotice(any(NoticeSaveRequestDto.class));
    }

    // 인가 로직 개발 완료 후 추가 개발
//    @Test
//    @DisplayName("사용자가 아니면 공지사항을 작성할 수 없다.")
//    void write_writeNoticeWithGuest_Exception() throws Exception {
//        // given
//        String notTitle = "테스트 공지사항 제목 수정 전";
//        String notContents = "테스트 공지사항 내용 수정 전";
//
//        NoticeSaveRequest request = NoticeFactory.mockNoticeSaveRequest(null, notTitle, notContents);
//
//        given(noticeService.registerNotice(any(NoticeSaveRequestDto.class)))
//                .willThrow(ApplicationException.class);
//
//        // when
//        ResultActions perform = mockMvc.perform(post("/api/notice")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request))
//                .with(SecurityMockMvcRequestPostProcessors.csrf()));
//
//        // then
//        perform.andExpect(status().isCreated());
//
//        // userRepository의 findByUserEmail이 1번 실행되었는지 검사한다.
//        verify(noticeService, times(1))
//                .registerNotice(any(NoticeSaveRequestDto.class));
//    }



    @Test
    @DisplayName("활성화 된 공지사항이 존재하는 경우 공지사항의 목록을 가져온다.")
    public void read_readAllEnableList_Success() throws Exception {
        // given
        User user = UserFactory.user();

        NoticeResponseDto noticeResponseDto1 = NoticeFactory.mockNoticeResponseDto(NoticeFactory.mockNotice(1L, user, true));
        NoticeResponseDto noticeResponseDto2 = NoticeFactory.mockNoticeResponseDto(NoticeFactory.mockNotice(2L, user, true));
        NoticeResponseDto noticeResponseDto3 = NoticeFactory.mockNoticeResponseDto(NoticeFactory.mockNotice(3L, user, true));

        List<NoticeResponseDto> noticeResponseDtos = List.of(
                noticeResponseDto1,
                noticeResponseDto2,
                noticeResponseDto3
        );

        List<NoticeResponse> noticeResponses = List.of(
                NoticeFactory.mockNoticeResponse(noticeResponseDto1),
                NoticeFactory.mockNoticeResponse(noticeResponseDto2),
                NoticeFactory.mockNoticeResponse(noticeResponseDto3)
        );


        given(noticeService.getNoticeList(any(NoticeListRequestDto.class)))
                .willReturn(noticeResponseDtos);

        // when
        ResultActions perform = mockMvc.perform(get("/api/notice/list")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(noticeResponses)));

        verify(noticeService, times(1))
                .getNoticeList(any(NoticeListRequestDto.class));
    }

    @Test
    @DisplayName("활성화 된 공지사항이 존재하지 않는 경우 빈배열을 가져온다.")
    public void read_readNotExistList_Success() throws Exception {
        // given
        List<NoticeResponseDto> noticeResponseDtos = new ArrayList<>();

        given(noticeService.getNoticeList(any(NoticeListRequestDto.class)))
                .willReturn(noticeResponseDtos);

        // when
        ResultActions perform = mockMvc.perform(get("/api/notice/list")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(noticeResponseDtos)));

        verify(noticeService, times(1))
                .getNoticeList(any(NoticeListRequestDto.class));
    }

    @Test
    @DisplayName("활성화 된 공지사항이 존재하는 경우 공지사항 내용을 가져온다.")
    public void read_readEnableDetail_Success() throws Exception {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        NoticeResponseDto noticeResponseDto = NoticeFactory.mockNoticeResponseDto(NoticeFactory.mockNotice(notIdx, user, true));

        given(noticeService.getNoticeDetail(any(NoticeDetailRequestDto.class)))
                .willReturn(noticeResponseDto);

        NoticeResponse noticeResponse = NoticeFactory.mockNoticeResponse(noticeResponseDto);

        // when
        ResultActions perform = mockMvc.perform(get("/api/notice/{notIdx}", notIdx)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(noticeResponse)));

        verify(noticeService, times(1))
                .getNoticeDetail(any(NoticeDetailRequestDto.class));
    }

    @Test
    @DisplayName("존재하지 않는 공지사항에 접근할 경우 예외가 발생한다.")
    public void read_readNotExistDetail_Exception() throws Exception {
        // given
        Long notIdx = 1L;

        given(noticeService.getNoticeDetail(any(NoticeDetailRequestDto.class)))
                .willThrow(new ApplicationException(NOTICE_NOT_FOUND));

        // when
        ResultActions perform = mockMvc.perform(get("/api/notice/{notIdx}", notIdx)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("해당 공지사항이 존재하지 않습니다."));

        verify(noticeService, times(1))
                .getNoticeDetail(any(NoticeDetailRequestDto.class));
    }

    @Test
    @DisplayName("비 활성화 된 공지사항에 접근할 경우 예외가 발생한다.")
    public void read_readDisableDetail_Exception() throws Exception {
        // given
        Long notIdx = 1L;

        given(noticeService.getNoticeDetail(any(NoticeDetailRequestDto.class)))
                .willThrow(new ApplicationException(INACTIVE_NOTICE));

        // when
        ResultActions perform = mockMvc.perform(get("/api/notice/{notIdx}", notIdx)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("비활성 처리 된 공지사항입니다."));

        verify(noticeService, times(1))
                .getNoticeDetail(any(NoticeDetailRequestDto.class));
    }

    @Test
    @DisplayName("공지사항의 작성자는 공지사항을 수정할 수 있다.")
    public void modify_modifyNotice_Success() throws Exception {
        // given
        User user = UserFactory.user();

        Long notIdx = 1L;
        String notTitle = "Test Notice Modified Title";
        String notContents = "Test Notice Modified Contents";

        NoticeUpdateRequest request = NoticeFactory.mockNoticeUpdateRequest(user.getUserEmail(), notIdx, notTitle, notContents);

        NoticeResponseDto noticeResponseDto = NoticeFactory.mockNoticeResponseDto(NoticeFactory.mockNotice(notIdx, user, notTitle, notContents, true));

        given(noticeService.modifyNotice(any(NoticeUpdateRequestDto.class)))
                .willReturn(noticeResponseDto);

        NoticeResponse noticeResponse = NoticeFactory.mockNoticeResponse(noticeResponseDto);

        // when
        ResultActions perform = mockMvc.perform(put("/api/notice/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(noticeResponse)));

        verify(noticeService, times(1))
                .modifyNotice(any(NoticeUpdateRequestDto.class));
    }

    @Test
    @DisplayName("존재하지 않는 공지사항에 수정 요청할 경우 예외가 발생한다.")
    public void modify_modifyNotExistNotice_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long notIdx = 1L;
        String notTitle = "Test Notice Modified Title";
        String notContents = "Test Notice Modified Contents";

        NoticeUpdateRequest request = NoticeFactory.mockNoticeUpdateRequest(user.getUserEmail(), notIdx, notTitle, notContents);

        given(noticeService.modifyNotice(any(NoticeUpdateRequestDto.class)))
                .willThrow(new ApplicationException(NOTICE_NOT_FOUND));

        // when
        ResultActions perform = mockMvc.perform(put("/api/notice/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("해당 공지사항이 존재하지 않습니다."));

        verify(noticeService, times(1))
                .modifyNotice(any(NoticeUpdateRequestDto.class));
    }

    @Test
    @DisplayName("비 활성화 된 공지사항을 수정할 경우 예외가 발생한다.")
    public void modify_modifyDisableNotice_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long notIdx = 1L;
        String notTitle = "Test Notice Modified Title";
        String notContents = "Test Notice Modified Contents";

        NoticeUpdateRequest request = NoticeFactory.mockNoticeUpdateRequest(user.getUserEmail(), notIdx, notTitle, notContents);

        given(noticeService.modifyNotice(any(NoticeUpdateRequestDto.class)))
                .willThrow(new ApplicationException(INACTIVE_NOTICE));

        // when
        ResultActions perform = mockMvc.perform(put("/api/notice/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("비활성 처리 된 공지사항입니다."));

        verify(noticeService, times(1))
                .modifyNotice(any(NoticeUpdateRequestDto.class));
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 공지사항 수정 요청할 경우 예외가 발생한다.")
    public void modify_modifyNotExistUser_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long notIdx = 1L;
        String notTitle = "Test Notice Modified Title";
        String notContents = "Test Notice Modified Contents";

        NoticeUpdateRequest request = NoticeFactory.mockNoticeUpdateRequest(user.getUserEmail(), notIdx, notTitle, notContents);

        given(noticeService.modifyNotice(any(NoticeUpdateRequestDto.class)))
                .willThrow(new ApplicationException(USER_NOT_FOUND));

        // when
        ResultActions perform = mockMvc.perform(put("/api/notice/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("해당 유저 정보를 찾을 수 없습니다."));

        verify(noticeService, times(1))
                .modifyNotice(any(NoticeUpdateRequestDto.class));
    }

    @Test
    @DisplayName("공지사항 수정 시 요청자와 작정자가 다를 경우 예외가 발생한다.")
    public void modify_modifyDifferentAuthor_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long notIdx = 1L;
        String notTitle = "Test Notice Modified Title";
        String notContents = "Test Notice Modified Contents";

        NoticeUpdateRequest request = NoticeFactory.mockNoticeUpdateRequest(user.getUserEmail(), notIdx, notTitle, notContents);

        given(noticeService.modifyNotice(any(NoticeUpdateRequestDto.class)))
                .willThrow(new ApplicationException(INVALID_AUTH_USER));

        // when
        ResultActions perform = mockMvc.perform(put("/api/notice/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("권한이 없는 유저입니다."));

        verify(noticeService, times(1))
                .modifyNotice(any(NoticeUpdateRequestDto.class));
    }

    @Test
    @DisplayName("공지사항 수정 시 제목이 널이면 예외가 발생한다.")
    public void modify_modifyNotExistTitle_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long notIdx = 1L;
        String notContents = "Test Notice Modified Contents";

        NoticeUpdateRequest request = NoticeFactory.mockNoticeUpdateRequest(user.getUserEmail(), notIdx, null, notContents);

        given(noticeService.modifyNotice(any(NoticeUpdateRequestDto.class)))
                .willThrow(new ApplicationException(NULL_TITLE));

        // when
        ResultActions perform = mockMvc.perform(put("/api/notice/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("제목이 널일 수 없습니다."));

        verify(noticeService, times(1))
                .modifyNotice(any(NoticeUpdateRequestDto.class));
    }

    @Test
    @DisplayName("공지사항 수정 시 제목이 공백이면 예외가 발생한다.")
    public void modify_modifyEmptyTitle_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long notIdx = 1L;
        String notContents = "Test Notice Modified Contents";

        NoticeUpdateRequest request = NoticeFactory.mockNoticeUpdateRequest(user.getUserEmail(), notIdx, null, notContents);

        given(noticeService.modifyNotice(any(NoticeUpdateRequestDto.class)))
                .willThrow(new ApplicationException(EMPTY_TITLE));

        // when
        ResultActions perform = mockMvc.perform(put("/api/notice/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("제목이 공백일 수 없습니다."));

        verify(noticeService, times(1))
                .modifyNotice(any(NoticeUpdateRequestDto.class));
    }

    @Test
    @DisplayName("공지사항 수정 시 내용이 널이면 예외가 발생한다.")
    public void modify_modifyNotExistContents_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long notIdx = 1L;
        String notTitle = "Test Notice Modified Title";

        NoticeUpdateRequest request = NoticeFactory.mockNoticeUpdateRequest(user.getUserEmail(), notIdx, notTitle, null);

        given(noticeService.modifyNotice(any(NoticeUpdateRequestDto.class)))
                .willThrow(new ApplicationException(NULL_CONTENTS));

        // when
        ResultActions perform = mockMvc.perform(put("/api/notice/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("내용이 널일 수 없습니다."));

        verify(noticeService, times(1))
                .modifyNotice(any(NoticeUpdateRequestDto.class));
    }

    @Test
    @DisplayName("공지사항 수정 시 내용이 공백이면 예외가 발생한다.")
    public void modify_modifyEmptyContents_Exception() throws Exception {
        // given
        User user = UserFactory.user();

        Long notIdx = 1L;
        String notTitle = "Test Notice Modified Title";

        NoticeUpdateRequest request = NoticeFactory.mockNoticeUpdateRequest(user.getUserEmail(), notIdx, notTitle, null);

        given(noticeService.modifyNotice(any(NoticeUpdateRequestDto.class)))
                .willThrow(new ApplicationException(EMPTY_CONTENTS));

        // when
        ResultActions perform = mockMvc.perform(put("/api/notice/modify")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("내용이 공백일 수 없습니다."));

        verify(noticeService, times(1))
                .modifyNotice(any(NoticeUpdateRequestDto.class));
    }

    @Test
    @DisplayName("공지사항의 작성자는 공지사항을 비활성화할 수 있다.")
    public void disable_disableAuthorMe_Success() throws Exception {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        willDoNothing()
                .given(noticeService)
                .disableNotice(any(NoticeBasicRequestDto.class));

        // when
        ResultActions perform = mockMvc.perform(put("/api/notice/disable/{notIdx}", notIdx)
                .param("userEmail", user.getUserEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("NOTICE_DISABLE_SUCCESS"));

        verify(noticeService, times(1))
                .disableNotice(any(NoticeBasicRequestDto.class));
    }

    @Test
    @DisplayName("공지사항의 작성자는 공지사항을 활성화할 수 있다.")
    public void enable_enableAuthorMe_Success() throws Exception {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        willDoNothing()
                .given(noticeService)
                .enableNotice(any(NoticeBasicRequestDto.class));

        // when
        ResultActions perform = mockMvc.perform(put("/api/notice/enable/{notIdx}", notIdx)
                .param("userEmail", user.getUserEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("NOTICE_ENABLE_SUCCESS"));

        verify(noticeService, times(1))
                .enableNotice(any(NoticeBasicRequestDto.class));
    }

    @Test
    @DisplayName("사용자는 공지사항을 삭제한다.")
    public void delete_deleteAuthorMe_Success() throws Exception {
        // given
        Long notIdx = 1L;
        User user = UserFactory.user();

        willDoNothing()
                .given(noticeService)
                .removeNotice(any(NoticeBasicRequestDto.class));

        // when
        ResultActions perform = mockMvc.perform(delete("/api/notice/remove/{notIdx}", notIdx)
                .param("userEmail", user.getUserEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("code").value("NOTICE_REMOVE_SUCCESS"));

        verify(noticeService, times(1))
                .removeNotice(any(NoticeBasicRequestDto.class));
    }

}