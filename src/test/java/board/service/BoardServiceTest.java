package board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.kjh.boardback.dto.request.board.PatchBoardRequestDto;
import com.kjh.boardback.dto.request.board.PatchCommentRequestDto;
import com.kjh.boardback.dto.request.board.PostBoardRequestDto;
import com.kjh.boardback.dto.request.board.PostCommentRequestDto;
import com.kjh.boardback.dto.response.board.GetBoardListResponseDto;
import com.kjh.boardback.dto.response.board.GetBoardPageListResponseDto;
import com.kjh.boardback.dto.response.board.GetBoardResponseDto;
import com.kjh.boardback.dto.response.board.GetCommentListResponseDto;
import com.kjh.boardback.dto.response.board.GetFavoriteListResponseDto;
import com.kjh.boardback.entity.SearchLog;
import com.kjh.boardback.entity.User;
import com.kjh.boardback.entity.board.Board;
import com.kjh.boardback.entity.board.Comment;
import com.kjh.boardback.entity.board.Favorite;
import com.kjh.boardback.entity.board.Image;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import com.kjh.boardback.repository.RedisRepository;
import com.kjh.boardback.repository.SearchLogRepository;
import com.kjh.boardback.repository.board.BoardRepository;
import com.kjh.boardback.repository.board.CommentRepository;
import com.kjh.boardback.repository.board.FavoriteRepository;
import com.kjh.boardback.repository.board.ImageRepository;
import com.kjh.boardback.service.AsyncService;
import com.kjh.boardback.service.BoardService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserService userService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private SearchLogRepository searchLogRepository;

    @Mock
    private RedisRepository redisRepository;

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

    private Comment comment() {
        return Comment.builder()
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
        Board board = board();
        List<Image> imageList = List.of();

        doReturn(Optional.of(board)).when(boardRepository).getBoardWithWriter(board.getBoardNumber());
        doReturn(imageList).when(imageRepository).findByBoard_BoardNumber(board.getBoardNumber());

        GetBoardResponseDto responseDto = boardService.getBoard(board.getBoardNumber());

        assertThat(responseDto.getBoardNumber()).isEqualTo(board.getBoardNumber());
        verify(boardRepository, times(1)).getBoardWithWriter(board.getBoardNumber());
        verify(imageRepository, times(1)).findByBoard_BoardNumber(board.getBoardNumber());
    }

    @Test
    @DisplayName("유저가 작성한 보드 리스트 조회 성공")
    void getUserBoardList() {
        List<Board> boardList = List.of();

        doReturn(user).when(userService).findByEmailOrElseThrow(user.getEmail());
        doReturn(boardList).when(boardRepository).findByWriter_EmailOrderByCreatedAtDesc(user.getEmail());

        GetBoardListResponseDto responseDto = boardService.getUserBoardList(user.getEmail());

        assertThat(responseDto.getBoardList()).isEqualTo(boardList);
        verify(boardRepository, times(1)).findByWriter_EmailOrderByCreatedAtDesc(user.getEmail());
    }

    @Test
    @DisplayName("검색어로 보드 리스트 조회 성공")
    void getSearchBoardList() {
        String searchWord = "test";
        String preSearchWord = "preTest";
        List<Board> boardList = List.of();

        doReturn(boardList).when(boardRepository).getBySearchWord(searchWord, searchWord);

        GetBoardListResponseDto responseDto = boardService.getSearchBoardList(searchWord, preSearchWord);

        assertThat(responseDto.getBoardList()).isEqualTo(boardList);
        verify(boardRepository, times(1)).getBySearchWord(searchWord, searchWord);
        verify(searchLogRepository, times(2)).save(any(SearchLog.class));
    }

    @Test
    @DisplayName("주간 Top3 보드 리스트 조회 성공")
    void getTop3BoardList() {
        List<Board> boardList = List.of();
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Order.desc("viewCount"), Order.desc("favoriteCount")));

        doReturn(boardList).when(redisRepository).getBoardTop3Values();
        doReturn(boardList).when(boardRepository).getTop3Within7Days(any(LocalDateTime.class), eq(pageable));

        GetBoardListResponseDto responseDto = boardService.getTop3BoardList();

        assertThat(responseDto.getBoardList()).isEqualTo(boardList);
        verify(boardRepository, times(1)).getTop3Within7Days(any(LocalDateTime.class), eq(pageable));
    }

    @Test
    @DisplayName("최신 보드 리스트 조회 성공")
    void getLatestBoardList() {
        Page<Board> boardList = new PageImpl<>(List.of());
        PageRequest pageRequest = PageRequest.of(0, 10);

        doReturn(boardList).when(boardRepository).getLatestBoardList(pageRequest);

        GetBoardPageListResponseDto responseDto = boardService.getLatestBoardList(pageRequest);

        assertThat(responseDto.getBoardList()).isEqualTo(boardList);
        verify(boardRepository, times(1)).getLatestBoardList(pageRequest);
    }

    @Test
    @DisplayName("조회수 증가 성공")
    void increaseViewCount() {
        Board board = board();
        int viewCount = board.getViewCount();

        doNothing().when(asyncService).updateTop3IfNeed(board);
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
        PostBoardRequestDto postBoardRequestDto = PostBoardRequestDto.builder()
                .title("test")
                .content("test")
                .boardImageList(list)
                .build();

        boardService.postBoard(postBoardRequestDto, user.getEmail());

        verify(imageRepository, times(1)).saveAll(List.of());
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    @DisplayName("보드 수정 성공")
    void patchBoard() {
        Board board = board();
        List<String> list = List.of();
        PatchBoardRequestDto patchBoardRequestDto = PatchBoardRequestDto.builder()
                .title("test")
                .content("test")
                .boardImageList(list)
                .build();

        doNothing().when(asyncService).patchBoardIfTop3(board);
        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());

        boardService.patchBoard(patchBoardRequestDto, board.getBoardNumber(), user.getEmail());

        verify(boardRepository, times(1)).save(board);
        verify(imageRepository, times(1)).deleteByBoard_BoardNumber(board.getBoardNumber());
        verify(imageRepository, times(1)).saveAll(List.of());
    }

    @Test
    @DisplayName("보드 수정 실패 - 권한 없음")
    void patchBoard_no_permission() {
        Board board = board();
        List<String> list = List.of();
        PatchBoardRequestDto patchBoardRequestDto = PatchBoardRequestDto.builder()
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
        Board board = board();

        doNothing().when(asyncService).deleteBoardIfTop3(board.getBoardNumber());
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
        Board board = board();
        List<Comment> commentList = List.of();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(commentList).when(commentRepository).getCommentList(board.getBoardNumber());

        GetCommentListResponseDto responseDto = boardService.getCommentList(board.getBoardNumber());

        assertThat(responseDto.getCommentList()).isEqualTo(commentList);
    }

    @Test
    @DisplayName("댓글 작성 성공")
    void postComment() {
        Board board = board();
        int commentCount = board.getCommentCount();
        PostCommentRequestDto postCommentRequestDto = new PostCommentRequestDto("test");

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(user).when(userService).findByEmailOrElseThrow(user.getEmail());

        boardService.postComment(board.getBoardNumber(), user.getEmail(), postCommentRequestDto);

        assertThat(board.getCommentCount()).isEqualTo(commentCount + 1);
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void patchComment() {
        Board board = board();
        Comment comment = comment();
        PatchCommentRequestDto patchCommentRequestDto = new PatchCommentRequestDto("test");

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(Optional.of(comment)).when(commentRepository).findByCommentNumber(comment.getCommentNumber());

        boardService.patchComment(board.getBoardNumber(), comment.getCommentNumber(), user.getEmail(),
                patchCommentRequestDto);

        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment() {
        Board board = board();
        int commentCount = board.getCommentCount();
        Comment comment = comment();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(Optional.of(comment)).when(commentRepository).findByCommentNumber(comment.getCommentNumber());

        boardService.deleteComment(board.getBoardNumber(), user.getEmail(), comment.getCommentNumber());

        assertThat(board.getCommentCount()).isEqualTo(commentCount - 1);
        verify(commentRepository, times(1)).delete(comment);
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    @DisplayName("좋아요한 유저 리스트 조회 성공")
    void getFavoriteList() {
        Board board = board();
        List<Favorite> favoriteList = List.of();

        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(favoriteList).when(favoriteRepository).getFavoriteList(board.getBoardNumber());

        GetFavoriteListResponseDto responseDto = boardService.getFavoriteList(board.getBoardNumber());

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

        boardService.putFavorite(user.getEmail(), board.getBoardNumber());

        verify(favoriteRepository, times(1)).save(any(Favorite.class));
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    void deleteFavorite() {
        Board board = board();
        Favorite favorite = new Favorite(board, user);

        doNothing().when(asyncService).updateTop3IfNeed(board);
        doReturn(Optional.of(board)).when(boardRepository).findByBoardNumber(board.getBoardNumber());
        doReturn(user).when(userService).findByEmailOrElseThrow(user.getEmail());
        doReturn(Optional.of(favorite)).when(favoriteRepository)
                .findByBoard_BoardNumberAndUser_Email(board.getBoardNumber(), user.getEmail());

        boardService.putFavorite(user.getEmail(), board.getBoardNumber());

        verify(favoriteRepository, times(1)).delete(favorite);
        verify(boardRepository, times(1)).save(board);
    }

}
