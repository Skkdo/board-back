package com.kjh.boardback.domain.user.dto.response;

import com.kjh.boardback.domain.user.entity.User;
import lombok.Getter;

@Getter
public class GetUserResponseDto {

    private String email;
    private String nickname;
    private String profileImage;

    public GetUserResponseDto(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }
}
