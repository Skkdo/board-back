package com.kjh.boardback.dto.response.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kjh.boardback.grobal.common.ResponseCode;
import com.kjh.boardback.grobal.common.ResponseMessage;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.entity.User;

import lombok.Getter;

@Getter
public class GetSignInUserResponseDto extends ResponseDto {
    
    private String email;
    private String nickname;
    private String profileImage;

    private GetSignInUserResponseDto(User user){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
        
    }

    public static ResponseEntity<GetSignInUserResponseDto> success(User user){
        GetSignInUserResponseDto result = new GetSignInUserResponseDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistUser(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
