package board.domain.trade_board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.kjh.boardback.domain.trade_board.dto.response.GetTradeFavoriteListResponseDto;
import com.kjh.boardback.domain.trade_board.entity.TradeBoard;
import com.kjh.boardback.domain.trade_board.entity.TradeFavorite;
import com.kjh.boardback.domain.trade_board.entity.TradeFavoritePk;
import com.kjh.boardback.domain.trade_board.repository.TradeBoardRepository;
import com.kjh.boardback.domain.trade_board.repository.TradeFavoriteRepository;
import com.kjh.boardback.domain.trade_board.service.TradeFavoriteService;
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
public class TradeFavoriteServiceTest {

    @InjectMocks
    private TradeFavoriteService favoriteService;

    @Mock
    private TradeFavoriteRepository favoriteRepository;

    @Mock
    private TradeBoardRepository boardRepository;

    @Mock
    private UserService userService;

    private final User user = User.builder()
            .email("email@email.com")
            .password("password")
            .nickname("nickname")
            .telNumber("01012345678")
            .agreedPersonal(true)
            .build();

    private TradeBoard board() {
        return TradeBoard.builder()
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
        TradeBoard board = board();
        List<TradeFavorite> favoriteList = List.of();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(favoriteList).when(favoriteRepository).getFavoriteList(board.getBoardNumber());

        GetTradeFavoriteListResponseDto responseDto = favoriteService.getFavoriteList(board.getBoardNumber());

        assertThat(responseDto.getFavoriteList()).isEqualTo(favoriteList);
    }

    @Test
    @DisplayName("좋아요 성공")
    void putFavorite() {
        TradeBoard board = board();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(user).when(userService).findByEmailOrElseThrow(user.getEmail());
        doReturn(Optional.empty()).when(favoriteRepository)
                .findByBoard_BoardNumberAndUser_Email(board.getBoardNumber(), user.getEmail());

        favoriteService.putFavorite(user.getEmail(), board.getBoardNumber());

        verify(favoriteRepository, times(1)).save(any(TradeFavorite.class));
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    void deleteFavorite() {
        TradeBoard board = board();
        TradeFavoritePk favoritePk = new TradeFavoritePk(user.getEmail(), board.getBoardNumber());
        TradeFavorite favorite = new TradeFavorite(favoritePk, user, board);

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(user).when(userService).findByEmailOrElseThrow(user.getEmail());
        doReturn(Optional.of(favorite)).when(favoriteRepository)
                .findByBoard_BoardNumberAndUser_Email(board.getBoardNumber(), user.getEmail());

        favoriteService.putFavorite(user.getEmail(), board.getBoardNumber());

        verify(favoriteRepository, times(1)).delete(favorite);
        verify(boardRepository, times(1)).save(board);
    }
}
