package board.domain.recipe_board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.kjh.boardback.domain.recipe_board.dto.request.PatchRecipeCommentRequestDto;
import com.kjh.boardback.domain.recipe_board.dto.request.PostRecipeCommentRequestDto;
import com.kjh.boardback.domain.recipe_board.dto.response.GetRecipeCommentListResponseDto;
import com.kjh.boardback.domain.recipe_board.entity.RecipeBoard;
import com.kjh.boardback.domain.recipe_board.entity.RecipeComment;
import com.kjh.boardback.domain.recipe_board.repository.RecipeBoardRepository;
import com.kjh.boardback.domain.recipe_board.repository.RecipeCommentRepository;
import com.kjh.boardback.domain.recipe_board.service.RecipeCommentService;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.user.service.UserService;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class RecipeCommentServiceTest {

    @InjectMocks
    private RecipeCommentService commentService;

    @Mock
    private RecipeCommentRepository commentRepository;

    @Mock
    private RecipeBoardRepository boardRepository;

    @Mock
    private UserService userService;

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
    @DisplayName("commentNumber 로 댓글 찾기 실패 - 존재하지 않는 댓글")
    void findByCommentNumber() {
        int commentNumber = 0;
        doReturn(Optional.empty()).when(commentRepository).findByCommentNumber(commentNumber);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> commentService.findByCommentNumber(commentNumber));

        assertThat(exception.getCode()).isEqualTo(ResponseCode.NOT_EXISTED_COMMENT.getCode());
        assertThat(exception.getMessage()).isEqualTo(ResponseCode.NOT_EXISTED_COMMENT.getMessage());
        verify(commentRepository, times(1)).findByCommentNumber(commentNumber);
    }

    @Test
    @DisplayName("댓글 리스트 조회 성공")
    void getCommentList() {
        RecipeBoard board = board();
        List<RecipeComment> commentList = List.of();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(commentList).when(commentRepository).getCommentListWithUser(board.getBoardNumber());

        GetRecipeCommentListResponseDto responseDto = commentService.getCommentList(board.getBoardNumber());

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

        commentService.postComment(board.getBoardNumber(), user.getEmail(), postCommentRequestDto);

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

        commentService.patchComment(board.getBoardNumber(), comment.getCommentNumber(), user.getEmail(),
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

        commentService.deleteComment(board.getBoardNumber(), comment.getCommentNumber(), user.getEmail());

        assertThat(board.getCommentCount()).isEqualTo(commentCount - 1);
        verify(commentRepository, times(1)).delete(comment);
        verify(boardRepository, times(1)).save(board);
    }
}
