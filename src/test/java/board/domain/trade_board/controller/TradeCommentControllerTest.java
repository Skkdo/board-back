package board.domain.trade_board.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjh.boardback.domain.trade_board.controller.TradeCommentController;
import com.kjh.boardback.domain.trade_board.dto.request.PatchTradeCommentRequestDto;
import com.kjh.boardback.domain.trade_board.dto.request.PostTradeCommentRequestDto;
import com.kjh.boardback.domain.trade_board.dto.response.GetTradeCommentListResponseDto;
import com.kjh.boardback.domain.trade_board.service.TradeCommentService;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.global.exception.BusinessException;
import com.kjh.boardback.global.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TradeCommentControllerTest {

    @InjectMocks
    private TradeCommentController commentController;

    @Mock
    private TradeCommentService commentService;

    private MockMvc mockMvc;

    private final String commonUrl = "/api/v1/trade/trade-board";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void build() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(commentController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @Test
    @DisplayName("해당 보드 댓글 리스트 조회 성공")
    void getCommentList() throws Exception {
        String url = commonUrl + "/{boardNumber}/comment-list";
        int pathVariable = 0;

        GetTradeCommentListResponseDto mock = mock(GetTradeCommentListResponseDto.class);
        ResponseDto responseDto = ResponseDto.success(mock);
        doReturn(mock).when(commentService).getCommentList(pathVariable);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(url, pathVariable)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("댓글 생성 성공")
    void postComment() throws Exception {
        String url = commonUrl + "/{boardNumber}/comment";
        int pathVariable = 0;
        PostTradeCommentRequestDto requestDto = new PostTradeCommentRequestDto("test");
        ResponseDto responseDto = ResponseDto.success();

        doNothing().when(commentService).postComment(eq(pathVariable), eq(null), any(PostTradeCommentRequestDto.class));

        mockMvc.perform(
                        MockMvcRequestBuilders.post(url, pathVariable)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("댓글 생성 실패 - valid")
    void postComment_valid() throws Exception {
        String url = commonUrl + "/{boardNumber}/comment";
        int pathVariable = 0;
        PostTradeCommentRequestDto requestDto = new PostTradeCommentRequestDto("");
        BusinessException exception = new BusinessException(ResponseCode.VALIDATION_FAILED);
        ResponseDto responseDto = ResponseDto.fail(exception);

        mockMvc.perform(
                        MockMvcRequestBuilders.post(url, pathVariable)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().is(exception.getStatus()))
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }


    @Test
    @DisplayName("댓글 수정 성공")
    void patchComment() throws Exception {
        String url = commonUrl + "/{boardNumber}/{commentNumber}";
        int pathVariable = 0;
        int pathVariable2 = 0;
        PatchTradeCommentRequestDto requestDto = new PatchTradeCommentRequestDto("test");

        ResponseDto responseDto = ResponseDto.success();

        doNothing().when(commentService)
                .patchComment(eq(pathVariable), eq(pathVariable2), eq(null), any(PatchTradeCommentRequestDto.class));

        mockMvc.perform(
                        MockMvcRequestBuilders.patch(url, pathVariable, pathVariable2)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("댓글 수정 실패 - valid")
    void patchComment_valid() throws Exception {
        String url = commonUrl + "/{boardNumber}/{commentNumber}";
        int pathVariable = 0;
        int pathVariable2 = 0;
        PatchTradeCommentRequestDto requestDto = new PatchTradeCommentRequestDto("");
        BusinessException exception = new BusinessException(ResponseCode.VALIDATION_FAILED);
        ResponseDto responseDto = ResponseDto.fail(exception);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch(url, pathVariable, pathVariable2)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().is(exception.getStatus()))
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment() throws Exception {
        String url = commonUrl + "/{boardNumber}/{comment}";
        int pathVariable = 0;
        int pathVariable2 = 0;
        ResponseDto responseDto = ResponseDto.success();

        doNothing().when(commentService).deleteComment(eq(pathVariable), eq(null), eq(pathVariable2));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete(url, pathVariable, pathVariable2)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }
}
