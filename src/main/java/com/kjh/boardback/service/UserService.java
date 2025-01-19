package com.kjh.boardback.service;

import com.kjh.boardback.dto.request.user.PatchNicknameRequestDto;
import com.kjh.boardback.dto.request.user.PatchProfileImageRequestDto;
import com.kjh.boardback.dto.response.user.GetSignInUserResponseDto;
import com.kjh.boardback.dto.response.user.GetUserResponseDto;
import com.kjh.boardback.entity.User;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import com.kjh.boardback.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByEmailOrElseThrow(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_USER));
    }

    public void findByNicknameOrElseThrow(String nickname) {
        userRepository.findByNickname(nickname).orElseThrow(
                () -> new BusinessException(ResponseCode.DUPLICATE_NICKNAME));
    }

    public void findByTelNumberOrElseThrow(String telNumber) {
        userRepository.findByTelNumber(telNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.DUPLICATE_TEL_NUMBER));
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public GetUserResponseDto getUser(String email) {
        User user = findByEmailOrElseThrow(email);
        return new GetUserResponseDto(user);
    }

    public GetSignInUserResponseDto getSignInUser(String email) {
        User user = findByEmailOrElseThrow(email);
        return new GetSignInUserResponseDto(user);
    }

    @Transactional
    public void patchNickname(String email, PatchNicknameRequestDto dto) {
        User user = findByEmailOrElseThrow(email);
        findByNicknameOrElseThrow(dto.getNickname());
        user.setNickname(dto.getNickname());
        save(user);
    }

    @Transactional
    public void patchProfileImage(String email, PatchProfileImageRequestDto dto) {
        User user = findByEmailOrElseThrow(email);
        user.setProfileImage(dto.getProfileImage());
        save(user);
    }
}
