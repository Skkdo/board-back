package com.kjh.boardback.domain.user.service;

import com.kjh.boardback.domain.user.dto.request.PatchNicknameRequestDto;
import com.kjh.boardback.domain.user.dto.request.PatchProfileImageRequestDto;
import com.kjh.boardback.domain.user.dto.response.GetUserResponseDto;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.user.repository.UserRepository;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
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
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new BusinessException(ResponseCode.DUPLICATE_NICKNAME);
        }
    }

    public void findByTelNumberOrElseThrow(String telNumber) {
        if (userRepository.findByTelNumber(telNumber).isPresent()) {
            throw new BusinessException(ResponseCode.DUPLICATE_TEL_NUMBER);
        }

    }

    public void save(User user) {
        userRepository.save(user);
    }

    public GetUserResponseDto getUser(String email) {
        User user = findByEmailOrElseThrow(email);
        return new GetUserResponseDto(user);
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
