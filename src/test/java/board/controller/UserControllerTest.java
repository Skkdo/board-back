package board.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjh.boardback.controller.UserController;
import com.kjh.boardback.dto.request.user.PatchNicknameRequestDto;
import com.kjh.boardback.dto.request.user.PatchProfileImageRequestDto;
import com.kjh.boardback.dto.response.user.GetSignInUserResponseDto;
import com.kjh.boardback.dto.response.user.GetUserResponseDto;
import com.kjh.boardback.entity.User;
import com.kjh.boardback.global.common.RequestDto;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.global.exception.BusinessException;
import com.kjh.boardback.global.exception.GlobalExceptionHandler;
import com.kjh.boardback.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    private final String commonUrl = "/api/v1/user";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User user() {
        return User.builder()
                .email("test@test.com")
                .password("password")
                .telNumber("01012345678")
                .nickname("test")
                .build();
    }

    @BeforeEach
    void build() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @Test
    @DisplayName("로그인 된 유저 정보 조회 성공")
    void getSignInUser() throws Exception {
        User user = user();
        GetSignInUserResponseDto getSignInUserResponseDto = new GetSignInUserResponseDto(user);
        ResponseDto responseDto = ResponseDto.success(getSignInUserResponseDto);

        doReturn(getSignInUserResponseDto).when(userService).getSignInUser(null);

        perform(commonUrl, null, HttpMethod.GET, HttpStatus.OK.value(), null, responseDto);
    }

    @Test
    @DisplayName("email 로 유저 조회 실패 - 존재하지 않는 email")
    void findByEmailOrElseThrow() throws Exception {
        BusinessException exception = new BusinessException(ResponseCode.NOT_EXISTED_USER);
        ResponseDto responseDto = ResponseDto.fail(exception);
        doThrow(exception).when(userService).getSignInUser(null);

        perform(commonUrl, null, HttpMethod.GET, exception.getStatus(), null, responseDto);
    }

    @Test
    @DisplayName("유저 정보 조회 성공")
    void getUser() throws Exception {
        String url = commonUrl + "/{email}";
        String pathVariable = "test@test.com";

        User user = user();
        GetUserResponseDto getUserResponseDto = new GetUserResponseDto(user);
        ResponseDto responseDto = ResponseDto.success(getUserResponseDto);

        doReturn(getUserResponseDto).when(userService).getUser(pathVariable);

        perform(url, pathVariable, HttpMethod.GET, HttpStatus.OK.value(), null, responseDto);
    }

    @Test
    @DisplayName("nickname 수정 성공")
    void patchNickname() throws Exception {
        String url = commonUrl + "/nickname";

        PatchNicknameRequestDto requestDto = new PatchNicknameRequestDto("test");
        ResponseDto responseDto = ResponseDto.success();

        doNothing().when(userService).patchNickname(eq(null), any(PatchNicknameRequestDto.class));

        perform(url, null, HttpMethod.PATCH, HttpStatus.OK.value(), requestDto, responseDto);
    }

    @Test
    @DisplayName("nickname 수정 실패 - nickname 중복")
    void findByNicknameOrElseThrow() throws Exception {
        String url = commonUrl + "/nickname";

        PatchNicknameRequestDto requestDto = new PatchNicknameRequestDto("test");
        BusinessException exception = new BusinessException(ResponseCode.DUPLICATE_NICKNAME);
        ResponseDto responseDto = ResponseDto.fail(exception);

        doThrow(exception).when(userService).patchNickname(eq(null), any(PatchNicknameRequestDto.class));

        perform(url, null, HttpMethod.PATCH, exception.getStatus(), requestDto, responseDto);
    }

    @Test
    @DisplayName("nickname 수정 성공")
    void patchProfileImage() throws Exception {
        String url = commonUrl + "/profile-image";

        PatchProfileImageRequestDto requestDto = new PatchProfileImageRequestDto("test");
        ResponseDto responseDto = ResponseDto.success();

        doNothing().when(userService).patchProfileImage(eq(null), any(PatchProfileImageRequestDto.class));

        perform(url, null, HttpMethod.PATCH, HttpStatus.OK.value(), requestDto, responseDto);
    }

    MvcResult perform(String url, String pathVariable, HttpMethod httpMethod, int status, RequestDto requestDto,
                      ResponseDto responseDto)
            throws Exception {
        return mockMvc.perform(
                        MockMvcRequestBuilders.request(httpMethod, url, pathVariable)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().is(status))
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()))
                .andReturn();
    }

}
