package org.deco.gachicoding.user.presentation;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.user.application.UserService;
import org.deco.gachicoding.user.dto.request.LoginRequestDto;
import org.deco.gachicoding.user.dto.response.UserResponseDto;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Api(tags = "사용자 로그인 처리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestLoginController {

    private final UserService userService;

    @ApiOperation(value = "로그인", notes = "로그인 수행")
    @ApiResponses(
            @ApiResponse(code = 200, message = "로그인 성공")
    )
    @PostMapping("/user/login")
    public UserResponseDto login(@ApiParam(value = "로그인을 위한 요청 body 정보") @RequestBody LoginRequestDto dto,
                                 @ApiIgnore HttpSession httpSession) throws Exception {

//        UserResponseDto userResponseDto = userService.login(dto, httpSession);
//        return userResponseDto;
        return null;
    }

    /**
     * @param httpSession
     * @return UserResponseDto
     * @link Spring Security 를 통한 세션 관리 로직으로 수정해야 함.
     */
    @ApiOperation(value = "유저정보 받기", notes = "세션을 통해 유저정보 전달, 실제로는 HttpSession 클래스를 파라미터로 받음")
    @ApiResponses(
            @ApiResponse(code = 200, message = "세션 정보 가져오기 성공")
    )
    @GetMapping("/user/info")
    public UserResponseDto getUserInfo(@ApiIgnore HttpSession httpSession) {
        UserResponseDto userInfo = (UserResponseDto) httpSession.getAttribute("user");
        return userInfo;
    }

    /**
     * 혹시 세션이 존재하지 않을 경우에 로그아웃 요청이 들어오면, 새롭게 세션을 생성하지 않도록 함.
     * Spring Security 를 통한 세션 관리 로직으로 수정 필요.
     *
     * @return void
     */
    @ApiOperation(value = "로그아웃", notes = "세션 객체 무효화, 실제로는 HttpServletRequest 클래스 정보를 파라미터로 받음")
    @ApiResponses(
            @ApiResponse(code = 200, message = "로그아웃 성공")
    )
    @GetMapping("/user/logout")
    public void logout(@ApiIgnore HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        httpSession.invalidate();
    }
}
