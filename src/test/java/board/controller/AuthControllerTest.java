package board.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjh.boardback.controller.AuthController;
import com.kjh.boardback.dto.request.auth.SignInRequestDto;
import com.kjh.boardback.dto.request.auth.SignUpRequestDto;
import com.kjh.boardback.dto.response.auth.SignInResponseDto;
import com.kjh.boardback.global.common.RequestDto;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.global.exception.BusinessException;
import com.kjh.boardback.global.exception.GlobalExceptionHandler;
import com.kjh.boardback.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;

    private final String commonUrl = "/api/v1/auth";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void build() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @Test
    @DisplayName(value = "회원가입 성공")
    void signUp() throws Exception {
        String url = commonUrl + "/sign-up";

        SignUpRequestDto requestDto = validSignUpRequestDto();
        ResponseDto responseDto = ResponseDto.success();

        perform(url, HttpStatus.OK.value(), requestDto, responseDto);
    }

    @Test
    @DisplayName(value = "회원가입 실패 - 잘못된 email 형식")
    void signUp_valid_email() throws Exception {
        String url = commonUrl + "/sign-up";

        SignUpRequestDto requestDto = validSignUpRequestDto();
        requestDto.setEmail("test.com");
        BusinessException exception = new BusinessException(ResponseCode.VALIDATION_FAILED);
        ResponseDto responseDto = ResponseDto.fail(exception);

        perform(url, exception.getStatus(), requestDto, responseDto);
    }

    @Test
    @DisplayName(value = "회원가입 실패 - 잘못된 telNumber 형식")
    void signUp_valid_telNumber() throws Exception {
        String url = commonUrl + "/sign-up";

        SignUpRequestDto requestDto = validSignUpRequestDto();
        requestDto.setTelNumber("abc12345678");
        BusinessException exception = new BusinessException(ResponseCode.VALIDATION_FAILED);
        ResponseDto responseDto = ResponseDto.fail(exception);

        perform(url, exception.getStatus(), requestDto, responseDto);
    }

    @Test
    @DisplayName(value = "회원가입 실패 - 중복된 email")
    void signUp_duplicate_email() throws Exception {
        String url = commonUrl + "/sign-up";

        SignUpRequestDto requestDto = validSignUpRequestDto();
        BusinessException exception = new BusinessException(ResponseCode.DUPLICATE_EMAIL);
        ResponseDto responseDto = ResponseDto.fail(exception);

        doThrow(exception).when(authService).signUp(any(SignUpRequestDto.class));

        perform(url, exception.getStatus(), requestDto, responseDto);
    }

    @Test
    @DisplayName(value = "회원가입 실패 - 중복된 nickname")
    void signUp_duplicate_nickname() throws Exception {
        String url = commonUrl + "/sign-up";

        SignUpRequestDto requestDto = validSignUpRequestDto();
        BusinessException exception = new BusinessException(ResponseCode.DUPLICATE_NICKNAME);
        ResponseDto responseDto = ResponseDto.fail(exception);

        doThrow(exception).when(authService).signUp(any(SignUpRequestDto.class));

        perform(url, exception.getStatus(), requestDto, responseDto);
    }

    @Test
    @DisplayName(value = "회원가입 실패 - 중복된 telNumber")
    void signUp_duplicate_telNumber() throws Exception {
        String url = commonUrl + "/sign-up";

        SignUpRequestDto requestDto = validSignUpRequestDto();
        BusinessException exception = new BusinessException(ResponseCode.DUPLICATE_TEL_NUMBER);
        ResponseDto responseDto = ResponseDto.fail(exception);

        doThrow(exception).when(authService).signUp(any(SignUpRequestDto.class));

        perform(url, exception.getStatus(), requestDto, responseDto);
    }

    @Test
    @DisplayName(value = "로그인 성공")
    void signIn() throws Exception {
        String url = commonUrl + "/sign-in";
        String jwtToken = "token";

        SignInRequestDto requestDto = validSignInRequestDto();
        SignInResponseDto signInResponseDto = new SignInResponseDto(jwtToken);
        ResponseDto responseDto = ResponseDto.success(signInResponseDto);

        doReturn(signInResponseDto).when(authService).signIn(any(SignInRequestDto.class));

        MvcResult mvcResult = perform(url, HttpStatus.OK.value(), requestDto, responseDto);

        String content = mvcResult.getResponse().getContentAsString();
        JsonNode dataNode = objectMapper.readTree(content).path("data");

        if (!dataNode.isNull()) {
            SignInResponseDto extractedData = objectMapper.convertValue(dataNode, SignInResponseDto.class);
            assertThat(extractedData.getToken()).isEqualTo(jwtToken);
        }
    }

    @Test
    @DisplayName(value = "로그인 실패 - 로그인 정보 불일치")
    void signIn_mismatch() throws Exception {
        String url = commonUrl + "/sign-in";

        SignInRequestDto requestDto = validSignInRequestDto();
        BusinessException exception = new BusinessException(ResponseCode.SIGN_IN_FAIL);
        ResponseDto responseDto = ResponseDto.fail(exception);

        doThrow(exception).when(authService).signIn(any(SignInRequestDto.class));

        perform(url, exception.getStatus(), requestDto, responseDto);
    }

    SignUpRequestDto validSignUpRequestDto() {
        return SignUpRequestDto.builder()
                .email("test@test.com")
                .telNumber("01012345678")
                .password("password")
                .nickname("test")
                .address("address")
                .agreedPersonal(true)
                .build();
    }

    SignInRequestDto validSignInRequestDto() {
        return SignInRequestDto.builder()
                .email("test@test.com")
                .password("password")
                .build();
    }

    MvcResult perform(String url, int status, RequestDto requestDto, ResponseDto responseDto) throws Exception {
        return mockMvc.perform(
                        MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().is(status))
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()))
                .andReturn();
    }
}
