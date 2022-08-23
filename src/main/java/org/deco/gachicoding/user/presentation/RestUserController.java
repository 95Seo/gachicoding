package org.deco.gachicoding.user.presentation;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.auth.Auth;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.dto.request.UserSaveRequestDto;
import org.deco.gachicoding.user.dto.request.UserUpdateRequestDto;
import org.deco.gachicoding.user.application.UserAuthenticationService;
import org.deco.gachicoding.service.SocialService;
import org.deco.gachicoding.user.application.UserService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Api(tags = "사용자 정보 처리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestUserController {

    private final UserService userService;
    private final SocialService socialService;
    private final UserAuthenticationService userAuthenticationService;


    @ApiOperation(value = "이메일 중복 체크", notes = "이메일의 중복을 체크 수행")
    @ApiImplicitParam(name = "email", value = "중복체크 이메일", required = true)
    @ApiResponses(
            @ApiResponse(code = 200, message = "이메일이 중복일 경우 false, 아닐 경우 true 반환")
    )
    @GetMapping("/user/regist/check-email")
    public boolean checkEmail(@ApiParam(name = "email") @RequestParam("email") String email) {
        return !userService.isDuplicatedEmail(email);
    }

    /**
     * 이메일 인증을 위한 토큰 발행
     *
     * @return UUID 토큰
     */
    @ApiOperation(value = "이메일 인증 토큰 발행", notes = "이메일 인증을 위한 토큰 생성 후 메일 발송")
    @ApiResponses(
            @ApiResponse(code = 200, message = "인증 메일이 발송되었습니다.")
    )
    @GetMapping("/user/auth-token")
    public UUID sendEmailToken(@ApiParam(value = "인증을 진행할 이메일") @RequestParam String email) {
        return userAuthenticationService.sendEmailConfirmationToken(email);
    }

    /**
     * @param authToken
     * @return userAuth
     */
    @ApiOperation(value = "이메일 인증", notes = "UUID 토큰을 통한 이메일 인증")
    @ApiResponses(
            @ApiResponse(code = 200, message = "이메일 인증이 완료되었습니다.")
    )
    @Transactional // 서비스 계층으로 빼야함
    @GetMapping("/user/authentication-email")
    public boolean authenticateEmail(@ApiParam(value = "유저 이메일로 발송된 인증 토큰") @RequestParam UUID authToken) {
        Auth auth = userAuthenticationService.checkToken(authToken);
        Optional<User> user = userService.getUserByUserEmail(auth.getAuthEmail());

        auth.useToken();
        user.get().emailAuthenticated();

        return user.get().isUserEnabled();
    }


    @ApiOperation(value = "회원가입", notes = "회원가입 수행")
    @ApiResponses(
            @ApiResponse(code = 200, message = "회원가입 완료")
    )
    @PostMapping("/user/create")
    public Long registerUser(@ApiParam(name = "요청 DTO", value = "회원가입을 위한 요청 body 정보") @Valid @RequestBody UserSaveRequestDto dto) {
        return userService.createUser(dto);
    }

    @ApiOperation(value = "유저 정보 업데이트", notes = "userIdx, UserUpdateRequestDto 를 받아서 유저 업데이트 수행")
    @ApiResponses(
            @ApiResponse(code = 200, message = "사용자 수정 완료")
    )
    @PutMapping("/user/{userIdx}")
    public Long updateUser(@ApiParam(value = "수정할 유저의 번호") @PathVariable Long userIdx,
                           @ApiParam(value = "사용자 정보 수정을 위한 요청 body 정보") @RequestBody UserUpdateRequestDto dto) {
        return userService.updateUser(userIdx, dto);
    }

    @ApiOperation(value = "유저 삭제", notes = "userIdx 값을 받아 유저 삭제 수행, ")
    @ApiResponses(
            @ApiResponse(code = 200, message = "사용자 정보 삭제 완료")
    )
    @DeleteMapping("/user/{userIdx}")
    public Long deleteUser(@ApiParam(value = "삭제할 사용자의 번호") @PathVariable Long userIdx) {
        return userService.deleteUser(userIdx);
    }
}