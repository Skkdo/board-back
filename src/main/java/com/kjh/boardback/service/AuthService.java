package com.kjh.boardback.service;

import com.kjh.boardback.dto.request.auth.SignInRequestDto;
import com.kjh.boardback.dto.request.auth.SignUpRequestDto;
import com.kjh.boardback.dto.response.auth.SignInResponseDto;
import com.kjh.boardback.entity.User;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import com.kjh.boardback.global.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public void signUp(SignUpRequestDto dto) {

        userService.findByEmail(dto.getEmail());
        userService.existsByNickname(dto.getNickname());
        userService.existsByTelNumber(dto.getTelNumber());

        String password = dto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        dto.setPassword(encodedPassword);

        User user = new User(dto);
        userService.save(user);
    }

    public SignInResponseDto signIn(SignInRequestDto dto) {

        String email = dto.getEmail();
        User user = userService.findByEmail(email);

        String password = dto.getPassword();
        String encodedPassword = user.getPassword();

        boolean isMatched = passwordEncoder.matches(password, encodedPassword);
        if (!isMatched) {
            throw new BusinessException(ResponseCode.SIGN_IN_FAIL);
        }

        String token = jwtProvider.create(email);

        return new SignInResponseDto(token);
    }
}
