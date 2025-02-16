package board.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.kjh.boardback.domain.user.dto.request.PatchNicknameRequestDto;
import com.kjh.boardback.domain.user.dto.request.PatchProfileImageRequestDto;
import com.kjh.boardback.domain.user.dto.response.GetUserResponseDto;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import com.kjh.boardback.domain.user.repository.UserRepository;
import com.kjh.boardback.domain.user.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user() {
        return User.builder()
                .email("test@test.com")
                .password("password")
                .telNumber("01012345678")
                .nickname("test")
                .build();
    }

    @Test
    @DisplayName("존재하지 않는 email 일 시 예외 반환")
    void findByEmailOrElseThrow() {
        User user = user();

        doReturn(Optional.empty()).when(userRepository).findByEmail(user.getEmail());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.findByEmailOrElseThrow(user.getEmail()));

        assertThat(exception.getCode()).isEqualTo(ResponseCode.NOT_EXISTED_USER.getCode());
        assertThat(exception.getMessage()).isEqualTo(ResponseCode.NOT_EXISTED_USER.getMessage());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    @DisplayName("이미 존재하는 nickname 일 시 예외 반환")
    void findByNicknameOrElseThrow() {
        User user = user();

        doReturn(Optional.of(user)).when(userRepository).findByNickname(user.getNickname());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.findByNicknameOrElseThrow(user.getNickname()));

        assertThat(exception.getCode()).isEqualTo(ResponseCode.DUPLICATE_NICKNAME.getCode());
        assertThat(exception.getMessage()).isEqualTo(ResponseCode.DUPLICATE_NICKNAME.getMessage());
        verify(userRepository, times(1)).findByNickname(user.getNickname());
    }

    @Test
    @DisplayName("이미 존재하는 telNumber 일 시 예외 반환")
    void findByTelNumberOrElseThrow() {
        User user = user();

        doReturn(Optional.of(user)).when(userRepository).findByTelNumber(user.getTelNumber());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.findByTelNumberOrElseThrow(user.getTelNumber()));

        assertThat(exception.getCode()).isEqualTo(ResponseCode.DUPLICATE_TEL_NUMBER.getCode());
        assertThat(exception.getMessage()).isEqualTo(ResponseCode.DUPLICATE_TEL_NUMBER.getMessage());
        verify(userRepository, times(1)).findByTelNumber(user.getTelNumber());
    }

    @Test
    @DisplayName("유저 정보 조회 성공")
    void getUser() {
        User user = user();

        doReturn(Optional.of(user)).when(userRepository).findByEmail(user.getEmail());

        GetUserResponseDto getUserResponseDto = userService.getUser(user.getEmail());

        assertThat(getUserResponseDto.getEmail()).isEqualTo(user.getEmail());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    @DisplayName("nickname 변경 성공")
    void patchNickname() {
        User user = user();
        String newNickname = "new";

        doReturn(Optional.of(user)).when(userRepository).findByEmail(user.getEmail());
        doReturn(Optional.empty()).when(userRepository).findByNickname(newNickname);

        PatchNicknameRequestDto patchNicknameRequestDto = new PatchNicknameRequestDto(newNickname);
        userService.patchNickname(user.getEmail(),patchNicknameRequestDto);

        assertThat(user.getNickname()).isEqualTo(newNickname);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).findByNickname(user.getNickname());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("profileImage 변경 성공")
    void patchProfileImage() {
        User user = user();
        String newProfileImage = "new";

        doReturn(Optional.of(user)).when(userRepository).findByEmail(user.getEmail());

        PatchProfileImageRequestDto patchProfileImageRequestDto = new PatchProfileImageRequestDto(newProfileImage);
        userService.patchProfileImage(user.getEmail(),patchProfileImageRequestDto);

        assertThat(user.getProfileImage()).isEqualTo(newProfileImage);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }
}
