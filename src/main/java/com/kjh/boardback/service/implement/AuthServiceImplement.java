package com.kjh.boardback.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kjh.boardback.dto.request.auth.SignInRequestDto;
import com.kjh.boardback.dto.request.auth.SignUpRequestDto;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.dto.response.auth.SignInResponseDto;
import com.kjh.boardback.dto.response.auth.SignUpResponseDto;
import com.kjh.boardback.entity.User;
import com.kjh.boardback.grobal.provider.JwtProvider;
import com.kjh.boardback.repository.UserRepository;
import com.kjh.boardback.service.AuthService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {



        try {
            String email = dto.getEmail();
            boolean existedEmail = userRepository.existsByEmail(email);
            if(existedEmail) return SignUpResponseDto.duplicateEmail();

            String nickname = dto.getNickname();
            boolean existedNickname = userRepository.existsByNickname(nickname);
            if(existedNickname) return SignUpResponseDto.duplicateNickname();

            String telNumber = dto.getTelNumber();
            boolean existedTelNumber = userRepository.existsByTelNumber(telNumber);
            if(existedTelNumber) return SignUpResponseDto.duplicateTelNumber();

            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

            User user = new User(dto);
            userRepository.save(user);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return SignUpResponseDto.success();

    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto){

        String token = null;

        try {

             String email = dto.getEmail();
             User user = userRepository.findByEmail(email);
             if (user == null) return SignInResponseDto.signInFail();

             String password = dto.getPassword();
             String encodedPassword = user.getPassword();

             boolean isMatched = passwordEncoder.matches(password, encodedPassword);
             if(!isMatched) return SignInResponseDto.signInFail();

             token = jwtProvider.create(email);
             
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return SignInResponseDto.success(token);

    }

}
