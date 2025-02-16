package board.domain.recipe_board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.kjh.boardback.domain.recipe_board.dto.object.RecipeStepItem;
import com.kjh.boardback.domain.recipe_board.dto.request.PatchRecipeBoardRequestDto;
import com.kjh.boardback.domain.recipe_board.dto.request.PostRecipeBoardRequestDto;
import com.kjh.boardback.domain.recipe_board.dto.response.GetRecipeBoardListResponseDto;
import com.kjh.boardback.domain.recipe_board.dto.response.GetRecipeBoardResponseDto;
import com.kjh.boardback.domain.recipe_board.entity.RecipeBoard;
import com.kjh.boardback.domain.recipe_board.repository.RecipeBoardRepository;
import com.kjh.boardback.domain.recipe_board.service.RecipeBoardService;
import com.kjh.boardback.domain.recipe_board.service.RecipeCommentService;
import com.kjh.boardback.domain.recipe_board.service.RecipeFavoriteService;
import com.kjh.boardback.domain.recipe_board.service.RecipeImageService;
import com.kjh.boardback.domain.search_log.entity.SearchLog;
import com.kjh.boardback.domain.search_log.service.SearchLogService;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.user.service.UserService;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class RecipeBoardServiceTest {

    @InjectMocks
    private RecipeBoardService boardService;

    @Mock
    private RecipeBoardRepository boardRepository;

    @Mock
    private UserService userService;

    @Mock
    private RecipeImageService imageService;

    @Mock
    private RecipeFavoriteService favoriteService;

    @Mock
    private RecipeCommentService commentService;

    @Mock
    private SearchLogService searchLogService;

    private final User user = User.builder()
            .email("email@email.com")
            .password("password")
            .nickname("nickname")
            .telNumber("01012345678")
            .agreedPersonal(true)
            .build();

    private RecipeBoard board() {
        return RecipeBoard.builder()
                .boardNumber(0)
                .title("test")
                .content("test")
                .favoriteCount(0)
                .commentCount(0)
                .viewCount(0)
                .writer(user)
                .build();
    }

    @Test
    @DisplayName("boardNumber 로 보드 찾기 실패 - 존재하지 않는 보드")
    void findByBoardNumber() {
        int boardNumber = 0;
        doReturn(Optional.empty()).when(boardRepository).findByBoardNumber(boardNumber);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> boardService.findByBoardNumber(boardNumber));

        assertThat(exception.getCode()).isEqualTo(ResponseCode.NOT_EXISTED_BOARD.getCode());
        assertThat(exception.getMessage()).isEqualTo(ResponseCode.NOT_EXISTED_BOARD.getMessage());
        verify(boardRepository, times(1)).findByBoardNumber(boardNumber);
    }

    @Test
    @DisplayName("boardNumber 로 보드 찾기 성공")
    void getBoard() {
        RecipeBoard board = board();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());

        GetRecipeBoardResponseDto responseDto = boardService.getBoard(board.getBoardNumber());

        assertThat(responseDto.getBoardNumber()).isEqualTo(board.getBoardNumber());
        verify(boardRepository, times(1)).findByBoardNumber(board.getBoardNumber());
    }

    @Test
    @DisplayName("유저가 작성한 보드 리스트 조회 성공")
    void getUserBoardList() {
        List<RecipeBoard> boardList = List.of();

        doReturn(user).when(userService).findByEmailOrElseThrow(user.getEmail());
        doReturn(boardList).when(boardRepository).getUserBoardList(user.getEmail());

        GetRecipeBoardListResponseDto responseDto = boardService.getUserBoardList(user.getEmail());

        assertThat(responseDto.getBoardList()).isEqualTo(boardList);
        verify(boardRepository, times(1)).getUserBoardList(user.getEmail());
    }

    @Test
    @DisplayName("검색어로 보드 리스트 조회 성공")
    void getSearchBoardList() {
        String searchWord = "test";
        String preSearchWord = "preTest";
        List<RecipeBoard> boardList = List.of();

        doReturn(boardList).when(boardRepository).getBySearchWord(searchWord, searchWord);

        GetRecipeBoardListResponseDto responseDto = boardService.getSearchBoardList(searchWord, preSearchWord);

        assertThat(responseDto.getBoardList()).isEqualTo(boardList);
        verify(boardRepository, times(1)).getBySearchWord(searchWord, searchWord);
        verify(searchLogService, times(2)).save(any(SearchLog.class));
    }

    @Test
    @DisplayName("주간 Top3 보드 리스트 조회 성공")
    void getTop3BoardList() {
        int type = 1;
        List<RecipeBoard> boardList = List.of();
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Order.desc("viewCount"), Order.desc("favoriteCount")));

        doReturn(boardList).when(boardRepository)
                .getTop3ListWithin7Days(eq(type), any(LocalDateTime.class), eq(pageable));

        GetRecipeBoardListResponseDto responseDto = boardService.getTop3BoardList(type);

        assertThat(responseDto.getBoardList()).isEqualTo(boardList);
        verify(boardRepository, times(1)).getTop3ListWithin7Days(eq(type), any(LocalDateTime.class), eq(pageable));
    }

    @Test
    @DisplayName("최신 보드 리스트 조회 성공")
    void getLatestBoardList() {
        int type = 1;
        List<RecipeBoard> boardList = List.of();

        doReturn(boardList).when(boardRepository).getLatestList(type);

        GetRecipeBoardListResponseDto responseDto = boardService.getLatestBoardList(type);

        assertThat(responseDto.getBoardList()).isEqualTo(boardList);
        verify(boardRepository, times(1)).getLatestList(type);
    }

    @Test
    @DisplayName("최신 보드 리스트 조회 성공")
    void increaseViewCount() {
        RecipeBoard board = board();
        int viewCount = board.getViewCount();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());

        boardService.increaseViewCount(board.getBoardNumber());

        assertThat(board.getViewCount()).isEqualTo(viewCount + 1);
        verify(boardRepository, times(1)).findByBoardNumber(board.getBoardNumber());
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    @DisplayName("보드 작성 성공")
    void postBoard() {
        List<String> list = List.of();
        List<RecipeStepItem> steps = List.of();
        PostRecipeBoardRequestDto postBoardRequestDto = PostRecipeBoardRequestDto.builder()
                .title("test")
                .content("test")
                .boardImageList(list)
                .steps(steps)
                .build();

        RecipeBoard savedBoard = board();
        doReturn(savedBoard).when(boardRepository).save(any(RecipeBoard.class));

        boardService.postBoard(postBoardRequestDto, user.getEmail());

        verify(imageService, times(1)).saveAll(List.of());
        verify(boardRepository, times(2)).save(any(RecipeBoard.class));
    }

    @Test
    @DisplayName("보드 수정 성공")
    void patchBoard() {
        RecipeBoard board = board();
        List<String> list = List.of();
        List<RecipeStepItem> steps = List.of();
        PatchRecipeBoardRequestDto patchBoardRequestDto = PatchRecipeBoardRequestDto.builder()
                .title("test")
                .content("test")
                .boardImageList(list)
                .steps(steps)
                .build();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());

        boardService.patchBoard(patchBoardRequestDto, board.getBoardNumber(), user.getEmail());

        verify(boardRepository, times(1)).save(board);
        verify(imageService, times(1)).deleteByBoardNumber(board.getBoardNumber());
        verify(imageService, times(1)).saveAll(List.of());
    }

    @Test
    @DisplayName("보드 수정 실패 - 권한 없음")
    void patchBoard_no_permission() {
        RecipeBoard board = board();
        List<String> list = List.of();
        PatchRecipeBoardRequestDto patchBoardRequestDto = PatchRecipeBoardRequestDto.builder()
                .title("test")
                .content("test")
                .boardImageList(list)
                .build();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());

        assertThrows(BusinessException.class,
                () -> boardService.patchBoard(patchBoardRequestDto, board.getBoardNumber(), "noPermission@email.com"));

        verify(boardRepository, never()).save(board);
        verify(imageService, never()).deleteByBoardNumber(board.getBoardNumber());
        verify(imageService, never()).saveAll(List.of());
    }

    @Test
    @DisplayName("보드 삭제 성공")
    void deleteBoard() {
        RecipeBoard board = board();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());

        boardService.deleteBoard(board.getBoardNumber(), user.getEmail());

        verify(imageService, times(1)).deleteByBoardNumber(board.getBoardNumber());
        verify(favoriteService, times(1)).deleteByBoardNumber(board.getBoardNumber());
        verify(commentService, times(1)).deleteByBoardNumber(board.getBoardNumber());
        verify(boardRepository, times(1)).delete(board);
    }
}
