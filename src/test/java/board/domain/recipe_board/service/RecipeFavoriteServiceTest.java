package board.domain.recipe_board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.kjh.boardback.domain.recipe_board.dto.response.GetRecipeFavoriteListResponseDto;
import com.kjh.boardback.domain.recipe_board.entity.RecipeBoard;
import com.kjh.boardback.domain.recipe_board.entity.RecipeFavorite;
import com.kjh.boardback.domain.recipe_board.entity.RecipeFavoritePk;
import com.kjh.boardback.domain.recipe_board.repository.RecipeBoardRepository;
import com.kjh.boardback.domain.recipe_board.repository.RecipeFavoriteRepository;
import com.kjh.boardback.domain.recipe_board.service.RecipeFavoriteService;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.user.service.UserService;
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
public class RecipeFavoriteServiceTest {

    @InjectMocks
    private RecipeFavoriteService favoriteService;

    @Mock
    private RecipeFavoriteRepository favoriteRepository;

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

    @Test
    @DisplayName("좋아요한 유저 리스트 조회 성공")
    void getFavoriteList() {
        RecipeBoard board = board();
        List<RecipeFavorite> favoriteList = List.of();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(favoriteList).when(favoriteRepository).getFavoriteListWithUser(board.getBoardNumber());

        GetRecipeFavoriteListResponseDto responseDto = favoriteService.getFavoriteList(board.getBoardNumber());

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

        favoriteService.putFavorite(user.getEmail(), board.getBoardNumber());

        verify(favoriteRepository, times(1)).save(any(RecipeFavorite.class));
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    void deleteFavorite() {
        RecipeBoard board = board();
        RecipeFavoritePk favoritePk = new RecipeFavoritePk(user.getEmail(), board.getBoardNumber());
        RecipeFavorite favorite = new RecipeFavorite(favoritePk, user, board);

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(user).when(userService).findByEmailOrElseThrow(user.getEmail());
        doReturn(Optional.of(favorite)).when(favoriteRepository)
                .findByBoard_BoardNumberAndUser_Email(board.getBoardNumber(), user.getEmail());

        favoriteService.putFavorite(user.getEmail(), board.getBoardNumber());

        verify(favoriteRepository, times(1)).delete(favorite);
        verify(boardRepository, times(1)).save(board);
    }
}
