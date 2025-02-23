package board.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.kjh.boardback.domain.auth.dto.request.SignInRequestDto;
import com.kjh.boardback.domain.auth.dto.request.SignUpRequestDto;
import com.kjh.boardback.domain.auth.dto.response.SignInResponseDto;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import com.kjh.boardback.global.provider.JwtProvider;
import com.kjh.boardback.domain.auth.service.AuthService;
import com.kjh.boardback.domain.user.service.UserService;
import com.kjh.boardback.global.service.RedisService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private RedisService redisService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final String password = "password";

    private final User user = User.builder()
            .email("email@email.com")
            .password(passwordEncoder.encode(password))
            .nickname("nickname")
            .telNumber("01012345678")
            .agreedPersonal(true)
            .build();

    @Test
    @DisplayName(value = "회원가입 성공")
    void signUp() {
        SignUpRequestDto dto = SignUpRequestDto.builder()
                .email(user.getEmail())
                .password(password)
                .nickname(user.getNickname())
                .telNumber(user.getTelNumber())
                .agreedPersonal(user.isAgreedPersonal())
                .build();

        doReturn(Optional.empty()).when(userService).findByEmail(dto.getEmail());
        doNothing().when(userService).findByNicknameOrElseThrow(dto.getNickname());
        doNothing().when(userService).findByTelNumberOrElseThrow(dto.getTelNumber());

        authService.signUp(dto);

        boolean matches = passwordEncoder.matches(password, dto.getPassword());

        assertThat(true).isEqualTo(matches);
        verify(userService,times(1)).findByEmail(dto.getEmail());
        verify(userService,times(1)).findByNicknameOrElseThrow(dto.getNickname());
        verify(userService,times(1)).findByTelNumberOrElseThrow(dto.getTelNumber());
        verify(userService,times(1)).save(any(User.class));
    }

    @Test
    @DisplayName(value = "회원가입 실패 - 이메일 중복")
    void singUp_duplicateEmail() {
        SignUpRequestDto dto = SignUpRequestDto.builder()
                .email(user.getEmail())
                .build();

        doReturn(Optional.of(user)).when(userService).findByEmail(dto.getEmail());

        BusinessException exception = assertThrows(BusinessException.class, () -> authService.signUp(dto));

        assertThat(exception.getCode()).isEqualTo(ResponseCode.DUPLICATE_EMAIL.getCode());
        assertThat(exception.getMessage()).isEqualTo(ResponseCode.DUPLICATE_EMAIL.getMessage());

        verify(userService,times(1)).findByEmail(dto.getEmail());
        verify(userService,never()).findByNicknameOrElseThrow(dto.getNickname());
        verify(userService,never()).findByTelNumberOrElseThrow(dto.getTelNumber());
        verify(userService,never()).save(any(User.class));
    }

    @Test
    @DisplayName(value = "회원가입 실패 - 닉네임 중복")
    void singUp_duplicateNickname() {
        SignUpRequestDto dto = SignUpRequestDto.builder()
                .nickname(user.getNickname())
                .build();

        doReturn(Optional.empty()).when(userService).findByEmail(dto.getEmail());

        doThrow(new BusinessException(ResponseCode.DUPLICATE_NICKNAME)).when(userService).findByNicknameOrElseThrow(dto.getNickname());
        BusinessException exception = assertThrows(BusinessException.class, () -> authService.signUp(dto));

        assertThat(exception.getCode()).isEqualTo(ResponseCode.DUPLICATE_NICKNAME.getCode());
        assertThat(exception.getMessage()).isEqualTo(ResponseCode.DUPLICATE_NICKNAME.getMessage());

        verify(userService,times(1)).findByEmail(dto.getEmail());
        verify(userService,times(1)).findByNicknameOrElseThrow(dto.getNickname());
        verify(userService,never()).findByTelNumberOrElseThrow(dto.getTelNumber());
        verify(userService,never()).save(any(User.class));
    }

    @Test
    @DisplayName(value = "회원가입 실패 - 전화번호 중복")
    void singUp_duplicateTelNumber() {
        SignUpRequestDto dto = SignUpRequestDto.builder()
                .telNumber(user.getTelNumber())
                .build();

        doReturn(Optional.empty()).when(userService).findByEmail(dto.getEmail());
        doNothing().when(userService).findByNicknameOrElseThrow(dto.getNickname());

        doThrow(new BusinessException(ResponseCode.DUPLICATE_TEL_NUMBER)).when(userService).findByTelNumberOrElseThrow(dto.getTelNumber());
        BusinessException exception = assertThrows(BusinessException.class, () -> authService.signUp(dto));

        assertThat(exception.getCode()).isEqualTo(ResponseCode.DUPLICATE_TEL_NUMBER.getCode());
        assertThat(exception.getMessage()).isEqualTo(ResponseCode.DUPLICATE_TEL_NUMBER.getMessage());

        verify(userService,times(1)).findByEmail(dto.getEmail());
        verify(userService,times(1)).findByNicknameOrElseThrow(dto.getNickname());
        verify(userService,times(1)).findByTelNumberOrElseThrow(dto.getTelNumber());
        verify(userService,never()).save(any(User.class));
    }

    @Test
    @DisplayName(value = "로그인 성공")
    void signIn() {
        SignInRequestDto dto = SignInRequestDto.builder()
                .email(user.getEmail())
                .password(password)
                .build();
        String accessToken = "token";
        String refreshToken = "token";

        doReturn(Optional.of(user)).when(userService).findByEmail(dto.getEmail());
        doReturn(accessToken).when(jwtProvider).createAccessToken(dto.getEmail());
        doReturn(refreshToken).when(jwtProvider).createRefreshToken(dto.getEmail());
        doNothing().when(redisService).setRefreshToken(dto.getEmail(),refreshToken);

        SignInResponseDto response = authService.signIn(dto);

        assertThat(response.getAccessToken()).isEqualTo(accessToken);
        verify(userService,times(1)).findByEmail(dto.getEmail());
        verify(jwtProvider,times(1)).createAccessToken(dto.getEmail());
    }

    @Test
    @DisplayName(value = "로그인 실패 - 존재하지 않는 이메일")
    void signIn_NotExistUser() {
        SignInRequestDto dto = SignInRequestDto.builder()
                .email(user.getEmail())
                .build();

        doReturn(Optional.empty()).when(userService).findByEmail(dto.getEmail());

        BusinessException exception = assertThrows(BusinessException.class, () -> authService.signIn(dto));

        assertThat(exception.getCode()).isEqualTo(ResponseCode.SIGN_IN_FAIL.getCode());
        assertThat(exception.getMessage()).isEqualTo(ResponseCode.SIGN_IN_FAIL.getMessage());

        verify(userService,times(1)).findByEmail(dto.getEmail());
        verify(jwtProvider,never()).createAccessToken(dto.getEmail());
    }

    @Test
    @DisplayName(value = "로그인 실패 - 패스워드 불일치")
    void signIn_MissMatched() {
        SignInRequestDto dto = SignInRequestDto.builder()
                .password("password2")
                .build();

        doReturn(Optional.of(user)).when(userService).findByEmail(dto.getEmail());

        BusinessException exception = assertThrows(BusinessException.class, () -> authService.signIn(dto));

        assertThat(exception.getCode()).isEqualTo(ResponseCode.SIGN_IN_FAIL.getCode());
        assertThat(exception.getMessage()).isEqualTo(ResponseCode.SIGN_IN_FAIL.getMessage());

        verify(userService,times(1)).findByEmail(dto.getEmail());
        verify(jwtProvider,never()).createAccessToken(dto.getEmail());
    }
}
