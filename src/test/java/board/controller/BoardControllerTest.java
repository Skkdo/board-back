package board.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjh.boardback.controller.BoardController;
import com.kjh.boardback.dto.request.board.PatchBoardRequestDto;
import com.kjh.boardback.dto.request.board.PatchCommentRequestDto;
import com.kjh.boardback.dto.request.board.PostBoardRequestDto;
import com.kjh.boardback.dto.request.board.PostCommentRequestDto;
import com.kjh.boardback.dto.response.board.GetBoardListResponseDto;
import com.kjh.boardback.dto.response.board.GetBoardPageListResponseDto;
import com.kjh.boardback.dto.response.board.GetBoardResponseDto;
import com.kjh.boardback.dto.response.board.GetCommentListResponseDto;
import com.kjh.boardback.dto.response.board.GetFavoriteListResponseDto;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.global.exception.BusinessException;
import com.kjh.boardback.global.exception.GlobalExceptionHandler;
import com.kjh.boardback.service.BoardService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class BoardControllerTest {

    @InjectMocks
    private BoardController boardController;

    @Mock
    private BoardService boardService;

    private MockMvc mockMvc;

    private final String commonUrl = "/api/v1/community/board";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void build() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(boardController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .setValidator(new LocalValidatorFactoryBean())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("보드 조회 성공")
    void getBoard() throws Exception {
        String url = commonUrl + "/{boardNumber}";
        int pathVariable = 0;

        GetBoardResponseDto mock = mock(GetBoardResponseDto.class);
        ResponseDto responseDto = ResponseDto.success(mock);
        doReturn(mock).when(boardService).getBoard(pathVariable);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(url, pathVariable)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("해당 보드 좋아요한 유저 리스트 조회 성공")
    void getFavoriteList() throws Exception {
        String url = commonUrl + "/{boardNumber}/favorite-list";
        int pathVariable = 0;

        GetFavoriteListResponseDto mock = mock(GetFavoriteListResponseDto.class);
        ResponseDto responseDto = ResponseDto.success(mock);
        doReturn(mock).when(boardService).getFavoriteList(pathVariable);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(url, pathVariable)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("해당 보드 댓글 리스트 조회 성공")
    void getCommentList() throws Exception {
        String url = commonUrl + "/{boardNumber}/comment-list";
        int pathVariable = 0;

        GetCommentListResponseDto mock = mock(GetCommentListResponseDto.class);
        ResponseDto responseDto = ResponseDto.success(mock);
        doReturn(mock).when(boardService).getCommentList(pathVariable);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(url, pathVariable)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("해당 보드 조회수 증가 성공")
    void increaseViewCount() throws Exception {
        String url = commonUrl + "/{boardNumber}/increase-view-count";
        int pathVariable = 0;

        ResponseDto responseDto = ResponseDto.success();
        doNothing().when(boardService).increaseViewCount(pathVariable);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(url, pathVariable)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("최신 보드 리스트 조회 성공")
    void getLatestBoardList() throws Exception {
        String url = commonUrl + "/latest-list";

        GetBoardPageListResponseDto mock = mock(GetBoardPageListResponseDto.class);
        ResponseDto responseDto = ResponseDto.success(mock);
        doReturn(mock).when(boardService)
                .getLatestBoardList(PageRequest.of(0, 10, Sort.by(Direction.DESC, "createdAt")));

        mockMvc.perform(
                        MockMvcRequestBuilders.get(url)
                                .param("page", "0")
                                .param("size", "10")
                                .param("sort", "createdAt,DESC")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("주간 Top3 리스트 조회 성공")
    void getTop3BoardList() throws Exception {
        String url = commonUrl + "/top-3";

        GetBoardListResponseDto mock = mock(GetBoardListResponseDto.class);
        ResponseDto responseDto = ResponseDto.success(mock);
        doReturn(mock).when(boardService).getTop3BoardList();

        mockMvc.perform(
                        MockMvcRequestBuilders.get(url)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("검색어로 보드 리스트 조회 성공")
    void getSearchBoardList() throws Exception {
        String url = commonUrl + "/search-list/{searchWord}";
        String pathVariable = "searchWord";

        GetBoardListResponseDto mock = mock(GetBoardListResponseDto.class);
        ResponseDto responseDto = ResponseDto.success(mock);
        doReturn(mock).when(boardService).getSearchBoardList(pathVariable, null);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(url, pathVariable)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("유저의 보드 리스트 조회 성공")
    void getUserBoardList() throws Exception {
        String url = commonUrl + "/user-board-list/{email}";
        String pathVariable = "test@test.com";

        GetBoardListResponseDto mock = mock(GetBoardListResponseDto.class);
        ResponseDto responseDto = ResponseDto.success(mock);
        doReturn(mock).when(boardService).getUserBoardList(pathVariable);

        mockMvc.perform(
                        MockMvcRequestBuilders.get(url, pathVariable)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("보드 생성 성공")
    void postBoard() throws Exception {
        PostBoardRequestDto requestDto = PostBoardRequestDto.builder()
                .title("test")
                .content("test")
                .boardImageList(List.of())
                .build();

        ResponseDto responseDto = ResponseDto.success();

        doNothing().when(boardService).postBoard(any(PostBoardRequestDto.class), eq(null));

        mockMvc.perform(
                        MockMvcRequestBuilders.post(commonUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("보드 생성 실패 - valid")
    void postBoard_valid() throws Exception {
        PostBoardRequestDto requestDto = PostBoardRequestDto.builder().build();
        BusinessException exception = new BusinessException(ResponseCode.VALIDATION_FAILED);
        ResponseDto responseDto = ResponseDto.fail(exception);

        mockMvc.perform(
                        MockMvcRequestBuilders.post(commonUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().is(exception.getStatus()))
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("댓글 생성 성공")
    void postComment() throws Exception {
        String url = commonUrl + "/{boardNumber}/comment";
        int pathVariable = 0;
        PostCommentRequestDto requestDto = new PostCommentRequestDto("test");
        ResponseDto responseDto = ResponseDto.success();

        doNothing().when(boardService).postComment(eq(pathVariable), eq(null), any(PostCommentRequestDto.class));

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
        PostCommentRequestDto requestDto = new PostCommentRequestDto("");
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
    @DisplayName("좋아요 성공")
    void putFavorite() throws Exception {
        String url = commonUrl + "/{boardNumber}/favorite";
        int pathVariable = 0;
        ResponseDto responseDto = ResponseDto.success();

        doNothing().when(boardService).putFavorite(eq(null), eq(pathVariable));

        mockMvc.perform(
                        MockMvcRequestBuilders.put(url, pathVariable)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("보드 수정 성공")
    void patchBoard() throws Exception {
        String url = commonUrl + "/{boardNumber}";
        int pathVariable = 0;
        PatchBoardRequestDto requestDto = PatchBoardRequestDto.builder()
                .title("test")
                .content("test")
                .boardImageList(List.of())
                .build();

        ResponseDto responseDto = ResponseDto.success();

        doNothing().when(boardService).patchBoard(any(PatchBoardRequestDto.class), eq(pathVariable), eq(null));

        mockMvc.perform(
                        MockMvcRequestBuilders.patch(url, pathVariable)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }

    @Test
    @DisplayName("보드 수정 실패 - valid")
    void patchBoard_valid() throws Exception {
        String url = commonUrl + "/{boardNumber}";
        int pathVariable = 0;
        PatchBoardRequestDto requestDto = PatchBoardRequestDto.builder().build();
        BusinessException exception = new BusinessException(ResponseCode.VALIDATION_FAILED);
        ResponseDto responseDto = ResponseDto.fail(exception);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch(url, pathVariable)
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
        PatchCommentRequestDto requestDto = new PatchCommentRequestDto("test");

        ResponseDto responseDto = ResponseDto.success();

        doNothing().when(boardService)
                .patchComment(eq(pathVariable), eq(pathVariable2), eq(null), any(PatchCommentRequestDto.class));

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
        PatchCommentRequestDto requestDto = new PatchCommentRequestDto("");
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
    @DisplayName("보드 삭제 성공")
    void deleteBoard() throws Exception {
        String url = commonUrl + "/{boardNumber}";
        int pathVariable = 0;

        ResponseDto responseDto = ResponseDto.success();

        doNothing().when(boardService).deleteBoard(eq(pathVariable), eq(null));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete(url, pathVariable)
                )
                .andExpect(status().isOk())
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

        doNothing().when(boardService).deleteComment(eq(pathVariable), eq(null), eq(pathVariable2));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete(url, pathVariable, pathVariable2)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()));
    }
}
