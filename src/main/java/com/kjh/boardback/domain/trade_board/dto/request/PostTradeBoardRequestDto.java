package com.kjh.boardback.domain.trade_board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTradeBoardRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private List<String> boardImageList;

    private String tradeLocation;

    private String price;
}
