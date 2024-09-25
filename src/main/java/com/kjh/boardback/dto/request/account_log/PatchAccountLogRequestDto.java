package com.kjh.boardback.dto.request.account_log;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatchAccountLogRequestDto {
    @NotBlank
    private String content;
    @NotNull
    private int type;
    @NotNull
    private int moneyCustomTypeNumber;
    @NotNull
    private int money;
}
