package com.kjh.boardback.dto.response.user;


import com.kjh.boardback.entity.User;
import lombok.Getter;

@Getter
public class GetSignInUserResponseDto {

    private String email;
    private String nickname;
    private String profileImage;

    public GetSignInUserResponseDto(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();

    }
}
