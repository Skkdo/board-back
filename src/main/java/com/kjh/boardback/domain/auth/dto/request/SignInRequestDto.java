package com.kjh.boardback.domain.auth.dto.request;

import com.kjh.boardback.global.common.RequestDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequestDto extends RequestDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
