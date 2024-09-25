package com.kjh.boardback.dto.request.account_log;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Datetime {
    @NotBlank @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$" )
    private String datetime;
}
