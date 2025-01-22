package board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.kjh.boardback.dto.request.recipe_board.PatchRecipeBoardRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PatchRecipeCommentRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PostRecipeBoardRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PostRecipeCommentRequestDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeBoardListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeBoardResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeCommentListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeFavoriteListResponseDto;
import com.kjh.boardback.entity.SearchLog;
import com.kjh.boardback.entity.User;
import com.kjh.boardback.entity.recipe_board.RecipeBoard;
import com.kjh.boardback.entity.recipe_board.RecipeComment;
import com.kjh.boardback.entity.recipe_board.RecipeFavorite;
import com.kjh.boardback.entity.recipe_board.RecipeImage;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import com.kjh.boardback.repository.SearchLogRepository;
import com.kjh.boardback.repository.recipe_board.RecipeBoardRepository;
import com.kjh.boardback.repository.recipe_board.RecipeCommentRepository;
import com.kjh.boardback.repository.recipe_board.RecipeFavoriteRepository;
import com.kjh.boardback.repository.recipe_board.RecipeImageRepository;
import com.kjh.boardback.service.RecipeBoardService;
import com.kjh.boardback.service.UserService;
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
    private RecipeImageRepository imageRepository;

    @Mock
    private RecipeFavoriteRepository favoriteRepository;

    @Mock
    private RecipeCommentRepository commentRepository;

    @Mock
    private SearchLogRepository searchLogRepository;

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

    private RecipeComment comment() {
        return RecipeComment.builder()
                .commentNumber(0)
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
    @DisplayName("commentNumber 로 댓글 찾기 실패 - 존재하지 않는 댓글")
    void findByCommentNumber() {
        int commentNumber = 0;
        doReturn(Optional.empty()).when(commentRepository).findByCommentNumber(commentNumber);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> boardService.findByCommentNumber(commentNumber));

        assertThat(exception.getCode()).isEqualTo(ResponseCode.NOT_EXISTED_COMMENT.getCode());
        assertThat(exception.getMessage()).isEqualTo(ResponseCode.NOT_EXISTED_COMMENT.getMessage());
        verify(commentRepository, times(1)).findByCommentNumber(commentNumber);
    }

    @Test
    @DisplayName("boardNumber 로 보드 찾기 성공")
    void getBoard() {
        RecipeBoard board = board();
        List<RecipeImage> imageList = List.of();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(imageList).when(imageRepository).findByBoard_BoardNumber(board.getBoardNumber());

        GetRecipeBoardResponseDto responseDto = boardService.getBoard(board.getBoardNumber());

        assertThat(responseDto.getBoardNumber()).isEqualTo(board.getBoardNumber());
        verify(boardRepository, times(1)).findByBoardNumber(board.getBoardNumber());
        verify(imageRepository, times(1)).findByBoard_BoardNumber(board.getBoardNumber());
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
        verify(searchLogRepository, times(2)).save(any(SearchLog.class));
    }

    @Test
    @DisplayName("주간 Top3 보드 리스트 조회 성공")
    void getTop3BoardList() {
        int type = 1;
        List<RecipeBoard> boardList = List.of();
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Order.desc("viewCount"), Order.desc("favoriteCount")));

        doReturn(boardList).when(boardRepository).getTop3ListWithin7Days(eq(type),any(LocalDateTime.class), eq(pageable));

        GetRecipeBoardListResponseDto responseDto = boardService.getTop3BoardList(type);

        assertThat(responseDto.getBoardList()).isEqualTo(boardList);
        verify(boardRepository, times(1)).getTop3ListWithin7Days(eq(type),any(LocalDateTime.class), eq(pageable));
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
        PostRecipeBoardRequestDto postBoardRequestDto = PostRecipeBoardRequestDto.builder()
                .title("test")
                .content("test")
                .boardImageList(list)
                .build();

        boardService.postBoard(postBoardRequestDto, user.getEmail());

        verify(imageRepository, times(1)).saveAll(List.of());
        verify(boardRepository, times(1)).save(any(RecipeBoard.class));
    }

    @Test
    @DisplayName("보드 수정 성공")
    void patchBoard() {
        RecipeBoard board = board();
        List<String> list = List.of();
        PatchRecipeBoardRequestDto patchBoardRequestDto = PatchRecipeBoardRequestDto.builder()
                .title("test")
                .content("test")
                .boardImageList(list)
                .build();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());

        boardService.patchBoard(patchBoardRequestDto, board.getBoardNumber(), user.getEmail());

        verify(boardRepository, times(1)).save(board);
        verify(imageRepository, times(1)).deleteByBoard_BoardNumber(board.getBoardNumber());
        verify(imageRepository, times(1)).saveAll(List.of());
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
        verify(imageRepository, never()).deleteByBoard_BoardNumber(board.getBoardNumber());
        verify(imageRepository, never()).saveAll(List.of());
    }

    @Test
    @DisplayName("보드 삭제 성공")
    void deleteBoard() {
        RecipeBoard board = board();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());

        boardService.deleteBoard(board.getBoardNumber(), user.getEmail());

        verify(imageRepository, times(1)).deleteByBoard_BoardNumber(board.getBoardNumber());
        verify(favoriteRepository, times(1)).deleteByBoard_BoardNumber(board.getBoardNumber());
        verify(commentRepository, times(1)).deleteByBoard_BoardNumber(board.getBoardNumber());
        verify(boardRepository, times(1)).delete(board);
    }

    @Test
    @DisplayName("댓글 리스트 조회 성공")
    void getCommentList() {
        RecipeBoard board = board();
        List<RecipeComment> commentList = List.of();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(commentList).when(commentRepository).getCommentListWithUser(board.getBoardNumber());

        GetRecipeCommentListResponseDto responseDto = boardService.getCommentList(board.getBoardNumber());

        assertThat(responseDto.getCommentList()).isEqualTo(commentList);
    }

    @Test
    @DisplayName("댓글 작성 성공")
    void postComment() {
        RecipeBoard board = board();
        int commentCount = board.getCommentCount();
        PostRecipeCommentRequestDto postCommentRequestDto = new PostRecipeCommentRequestDto("test");

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(user).when(userService).findByEmailOrElseThrow(user.getEmail());

        boardService.postComment(board.getBoardNumber(), user.getEmail(), postCommentRequestDto);

        assertThat(board.getCommentCount()).isEqualTo(commentCount + 1);
        verify(commentRepository, times(1)).save(any(RecipeComment.class));
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void patchComment() {
        RecipeBoard board = board();
        RecipeComment comment = comment();
        PatchRecipeCommentRequestDto patchCommentRequestDto = new PatchRecipeCommentRequestDto("test");

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(Optional.of(comment)).when(commentRepository).findByCommentNumber(comment.getCommentNumber());

        boardService.patchComment(board.getBoardNumber(), comment.getCommentNumber(), user.getEmail(),
                patchCommentRequestDto);

        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment() {
        RecipeBoard board = board();
        int commentCount = board.getCommentCount();
        RecipeComment comment = comment();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(Optional.of(comment)).when(commentRepository).findByCommentNumber(comment.getCommentNumber());

        boardService.deleteComment(board.getBoardNumber(), comment.getCommentNumber(), user.getEmail());

        assertThat(board.getCommentCount()).isEqualTo(commentCount - 1);
        verify(commentRepository, times(1)).delete(comment);
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    @DisplayName("좋아요한 유저 리스트 조회 성공")
    void getFavoriteList() {
        RecipeBoard board = board();
        List<RecipeFavorite> favoriteList = List.of();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(favoriteList).when(favoriteRepository).getFavoriteListWithUser(board.getBoardNumber());

        GetRecipeFavoriteListResponseDto responseDto = boardService.getFavoriteList(board.getBoardNumber());

        assertThat(responseDto.getFavoriteList()).isEqualTo(favoriteList);
    }

    @Test
    @DisplayName("좋아요 성공")
    void putFavorite() {
        RecipeBoard board = board();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(user).when(userService).findByEmailOrElseThrow(user.getEmail());
        doReturn(Optional.empty()).when(favoriteRepository)
                .findByBoard_BoardNumberAndUser_Email(board.getBoardNumber(), user.getEmail());

        boardService.putFavorite(user.getEmail(), board.getBoardNumber());

        verify(favoriteRepository, times(1)).save(any(RecipeFavorite.class));
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    void deleteFavorite() {
        RecipeBoard board = board();
        RecipeFavorite favorite = RecipeFavorite.from(user, board);

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(user).when(userService).findByEmailOrElseThrow(user.getEmail());
        doReturn(Optional.of(favorite)).when(favoriteRepository)
                .findByBoard_BoardNumberAndUser_Email(board.getBoardNumber(), user.getEmail());

        boardService.putFavorite(user.getEmail(), board.getBoardNumber());

        verify(favoriteRepository, times(1)).delete(favorite);
        verify(boardRepository, times(1)).save(board);
    }

}
