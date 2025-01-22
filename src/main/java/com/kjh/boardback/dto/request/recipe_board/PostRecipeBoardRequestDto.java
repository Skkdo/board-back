package com.kjh.boardback.dto.request.recipe_board;

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
public class PostRecipeBoardRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private List<String> boardImageList;
    @NotNull
    private int type;
    @NotNull
    private int cookingTime;
    private String step1_image;
    private String step1_content;
    private String step2_image;
    private String step2_content;
    private String step3_image;
    private String step3_content;
    private String step4_image;
    private String step4_content;
    private String step5_image;
    private String step5_content;
    private String step6_image;
    private String step6_content;
    private String step7_image;
    private String step7_content;
    private String step8_image;
    private String step8_content;
}
