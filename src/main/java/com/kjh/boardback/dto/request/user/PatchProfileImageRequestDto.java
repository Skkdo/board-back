package com.kjh.boardback.dto.request.user;

import com.kjh.boardback.global.common.RequestDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchProfileImageRequestDto extends RequestDto {

    private String profileImage;
}
