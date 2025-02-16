package board.domain.trade_board.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kjh.boardback.domain.trade_board.controller.TradeFavoriteController;
import com.kjh.boardback.domain.trade_board.dto.response.GetTradeFavoriteListResponseDto;
import com.kjh.boardback.domain.trade_board.service.TradeFavoriteService;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.global.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TradeFavoriteControllerTest {

    @InjectMocks
    private TradeFavoriteController favoriteController;

    @Mock
    private TradeFavoriteService favoriteService;

    private MockMvc mockMvc;

    private final String commonUrl = "/api/v1/trade/trade-board";

    @BeforeEach
    void build() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(favoriteController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    @DisplayName("해당 보드 좋아요한 유저 리스트 조회 성공")
    void getFavoriteList() throws Exception {
        String url = commonUrl + "/{boardNumber}/favorite-list";
        int pathVariable = 0;

        GetTradeFavoriteListResponseDto mock = mock(GetTradeFavoriteListResponseDto.class);
        ResponseDto responseDto = ResponseDto.success(mock);
        doReturn(mock).when(favoriteService).getFavoriteList(pathVariable);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(url, pathVariable)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("좋아요 성공")
    void putFavorite() throws Exception {
        String url = commonUrl + "/{boardNumber}/favorite";
        int pathVariable = 0;
        ResponseDto responseDto = ResponseDto.success();

        doNothing().when(favoriteService).putFavorite(eq(null), eq(pathVariable));

        mockMvc.perform(
                        MockMvcRequestBuilders.put(url, pathVariable)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }
}
