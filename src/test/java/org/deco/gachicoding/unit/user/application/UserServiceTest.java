package org.deco.gachicoding.unit.user.application;

import org.deco.gachicoding.user.dto.request.UserSaveRequestDto;
import org.deco.gachicoding.user.application.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

// 서비스 테스트에서 비즈니스 로직에서 발생할 수 있는 예외 상황의 테스트를 진행한다
@ExtendWith(SpringExtension.class)
@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {

    @Autowired
    UserService userService;


    @Test
    @DisplayName("UserService - 회원가입 테스트")
    void JoinUser() {
        // Given
        /*
        UserSaveRequestDto userSaveRequestDto = new UserSaveRequestDto();
        userSaveRequestDto.setEmail("ay9564@naver.com");
        userSaveRequestDto.setName("서영준");
        userSaveRequestDto.setPassword("ay789456");

        // When
        Long idx = userService.registerUser(userSaveRequestDto);
        UserResponseDto user = userService.getUser(idx);

        // Then
        assertEquals(user.getEmail(), userSaveRequestDto.getEmail());
        */
    }

    @Test
    @DisplayName("UserService - 중복 아이디 회원가입 테스트")
    void duplicationEmailJoin() {
        // Given
        UserSaveRequestDto userSaveRequestDto1 = new UserSaveRequestDto();
//        userSaveRequestDto1.setEmail("ay9564@naver.com");
//        userSaveRequestDto1.setName("서영준");
//        userSaveRequestDto1.setPassword("ay789456");

        // When
//        Long exception_code = userService.registerUser(userSaveRequestDto1);

        // exception_code(난중에 다시 정하자) : -100, message : "중복된 아이디 입니다."
        // Then
//        assertEquals(exception_code, -100);
    }

    @Test
    @DisplayName("UserService - 이메일 형식이 아닌 아이디 회원가입 테스트")
    void notEmailFormatIdJoinUser() {
        // Given
        UserSaveRequestDto userSaveRequestDto1 = new UserSaveRequestDto();
//        userSaveRequestDto1.setEmail("ay9564naver.com");
//        userSaveRequestDto1.setName("서영준");
//        userSaveRequestDto1.setPassword("ay789456");

        // When
//        Long exception_code = userService.registerUser(userSaveRequestDto1);

        // exception_code : -200, message : "올바른 형식의 아이디가 아닙니다."
        // Then
//        assertEquals(exception_code, "올바른 형식의 아이디가 아닙니다.");
    }
}
