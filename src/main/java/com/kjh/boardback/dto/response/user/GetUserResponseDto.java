package com.kjh.boardback.dto.response.user;

import com.kjh.boardback.entity.User;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import lombok.Getter;

@Getter
public class GetUserResponseDto extends ResponseDto {

    private String email;
    private String nickname;
    private String profileImage;

    public GetUserResponseDto(User user) {
        super(ResponseCode.SUCCESS);
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }
}
