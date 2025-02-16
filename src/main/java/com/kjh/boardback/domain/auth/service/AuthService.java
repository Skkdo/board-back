package com.kjh.boardback.domain.auth.service;

import com.kjh.boardback.domain.auth.dto.request.SignInRequestDto;
import com.kjh.boardback.domain.auth.dto.request.SignUpRequestDto;
import com.kjh.boardback.domain.auth.dto.response.SignInResponseDto;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import com.kjh.boardback.global.provider.JwtProvider;
import com.kjh.boardback.domain.user.service.UserService;
import com.kjh.boardback.global.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    @Value("${access.token.expiration.time}")
    private int accessToken_expirationTime;

    @Value("${refresh.token.expiration.time}")
    private int refreshToken_expirationTime;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public void signUp(SignUpRequestDto dto) {

        if(userService.findByEmail(dto.getEmail()).isPresent()) {
            throw new BusinessException(ResponseCode.DUPLICATE_EMAIL);
        }
        userService.findByNicknameOrElseThrow(dto.getNickname());
        userService.findByTelNumberOrElseThrow(dto.getTelNumber());

        String password = dto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        dto.setPassword(encodedPassword);

        User user = new User(dto);
        userService.save(user);
    }

    public SignInResponseDto signIn(SignInRequestDto dto) {

        String email = dto.getEmail();
        Optional<User> optional = userService.findByEmail(email);
        if(optional.isEmpty()){
            throw  new BusinessException(ResponseCode.SIGN_IN_FAIL);
        }

        User user = optional.get();
        String password = dto.getPassword();
        String encodedPassword = user.getPassword();

        boolean isMatched = passwordEncoder.matches(password, encodedPassword);
        if (!isMatched) {
            throw new BusinessException(ResponseCode.SIGN_IN_FAIL);
        }

        String accessToken = jwtProvider.createAccessToken(email);
        String refreshToken = jwtProvider.createRefreshToken(email);
        redisService.setRefreshToken(email, refreshToken);

        return new SignInResponseDto(accessToken,accessToken_expirationTime,refreshToken,refreshToken_expirationTime);
    }

    public SignInResponseDto getNewTokenByRefreshToken(HttpServletRequest request) {

        String refreshToken = jwtProvider.getRefreshToken(request);
        if(refreshToken == null) {
            throw new BusinessException(ResponseCode.REFRESH_TOKEN_MISSING);
        }

        String email = redisService.getRefreshTokenByEmail(refreshToken);

        String newAccessToken = jwtProvider.createAccessToken(email);
        String newRefreshToken = jwtProvider.createRefreshToken(email);
        redisService.setRefreshToken(email, newRefreshToken);

        return new SignInResponseDto(newAccessToken,accessToken_expirationTime,newRefreshToken,refreshToken_expirationTime);
    }
}
