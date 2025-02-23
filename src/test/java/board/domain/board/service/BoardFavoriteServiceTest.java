package board.domain.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.kjh.boardback.domain.board.dto.response.GetFavoriteListResponseDto;
import com.kjh.boardback.domain.board.entity.Board;
import com.kjh.boardback.domain.board.entity.Favorite;
import com.kjh.boardback.domain.board.entity.FavoritePk;
import com.kjh.boardback.domain.board.repository.BoardRepository;
import com.kjh.boardback.domain.board.repository.FavoriteRepository;
import com.kjh.boardback.domain.board.service.BoardFavoriteService;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.user.service.UserService;
import com.kjh.boardback.global.service.AsyncService;
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
public class BoardFavoriteServiceTest {

    @InjectMocks
    private BoardFavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserService userService;

    @Mock
    private AsyncService asyncService;

    private final User user = User.builder()
            .email("email@email.com")
            .password("password")
            .nickname("nickname")
            .telNumber("01012345678")
            .agreedPersonal(true)
            .build();

    private Board board() {
        return Board.builder()
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
        Board board = board();
        List<Favorite> favoriteList = List.of();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(favoriteList).when(favoriteRepository).getFavoriteList(board.getBoardNumber());

        GetFavoriteListResponseDto responseDto = favoriteService.getFavoriteList(board.getBoardNumber());

        assertThat(responseDto.getFavoriteList()).isEqualTo(favoriteList);
    }

    @Test
    @DisplayName("좋아요 성공")
    void putFavorite() {
        Board board = board();

        doNothing().when(asyncService).updateTop3IfNeed(board);
        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(user).when(userService).findByEmailOrElseThrow(user.getEmail());
        doReturn(Optional.empty()).when(favoriteRepository)
                .findByBoard_BoardNumberAndUser_Email(board.getBoardNumber(), user.getEmail());

        favoriteService.putFavorite(user.getEmail(), board.getBoardNumber());

        verify(favoriteRepository, times(1)).save(any(Favorite.class));
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    void deleteFavorite() {
        Board board = board();
        FavoritePk favoritePk = new FavoritePk(user.getEmail(), board.getBoardNumber());
        Favorite favorite = new Favorite(favoritePk, user, board);

        doNothing().when(asyncService).updateTop3IfNeed(board);
        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(user).when(userService).findByEmailOrElseThrow(user.getEmail());
        doReturn(Optional.of(favorite)).when(favoriteRepository)
                .findByBoard_BoardNumberAndUser_Email(board.getBoardNumber(), user.getEmail());

        favoriteService.putFavorite(user.getEmail(), board.getBoardNumber());

        verify(favoriteRepository, times(1)).delete(favorite);
        verify(boardRepository, times(1)).save(board);
    }
}
