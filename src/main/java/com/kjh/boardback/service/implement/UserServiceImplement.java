package com.kjh.boardback.service.implement;

import com.kjh.boardback.dto.request.user.PatchNicknameRequestDto;
import com.kjh.boardback.dto.request.user.PatchProfileImageRequestDto;
import com.kjh.boardback.dto.response.user.GetUserResponseDto;
import com.kjh.boardback.dto.response.user.PatchNicknameResponseDto;
import com.kjh.boardback.dto.response.user.PatchProfileImageResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.dto.response.user.GetSignInUserResponseDto;
import com.kjh.boardback.entity.User;
import com.kjh.boardback.repository.UserRepository;
import com.kjh.boardback.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<? super GetUserResponseDto> getUser(String email) {

        User user = null;

        try{

            user = userRepository.findByEmail(email);
            if(user == null) return GetUserResponseDto.noExistUser();


        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetUserResponseDto.success(user);
    }

    @Override
    public ResponseEntity<? super GetSignInUserResponseDto> getSignInUser(String email) {

        User user = null;

        try {

            user = userRepository.findByEmail(email);
            if(user == null) return GetSignInUserResponseDto.noExistUser();

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetSignInUserResponseDto.success(user);

    }

    @Override
    public ResponseEntity<? super PatchNicknameResponseDto> patchNickname(String email,PatchNicknameRequestDto dto) {

        User user = null;

        try{
            user = userRepository.findByEmail(email);
            if(user == null) return PatchNicknameResponseDto.noExistUser();

            boolean existedNickname = userRepository.existsByNickname(dto.getNickname());
            if(existedNickname) return PatchNicknameResponseDto.duplicateNickname();

            user.setNickname(dto.getNickname());
            userRepository.save(user);


        }catch (Exception exception){
            exception.printStackTrace();
            return  ResponseDto.databaseError();
        }
        return PatchNicknameResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PatchProfileImageResponseDto> patchProfileImage(String email, PatchProfileImageRequestDto dto) {

        User user = null;

        try{
            user = userRepository.findByEmail(email);
            if(user == null) return PatchProfileImageResponseDto.noExistUser();

            user.setProfileImage(dto.getProfileImage());
            userRepository.save(user);

        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return PatchProfileImageResponseDto.success();
    }
}
