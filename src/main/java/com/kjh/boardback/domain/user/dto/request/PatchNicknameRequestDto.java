package com.kjh.boardback.domain.user.dto.request;

import com.kjh.boardback.global.common.RequestDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchNicknameRequestDto extends RequestDto {

    @NotBlank
    private String nickname;
}
