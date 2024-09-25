package com.kjh.boardback.dto.request.account_log;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostMoneyCustomTypeRequestDto {
    @NotBlank
    private String customTypeName;
}
