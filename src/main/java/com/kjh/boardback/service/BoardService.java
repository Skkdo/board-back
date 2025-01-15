package com.kjh.boardback.service;

import com.kjh.boardback.dto.request.board.PatchBoardRequestDto;
import com.kjh.boardback.dto.request.board.PatchCommentRequestDto;
import com.kjh.boardback.dto.request.board.PostBoardRequestDto;
import com.kjh.boardback.dto.request.board.PostCommentRequestDto;
import com.kjh.boardback.dto.response.board.GetBoardResponseDto;
import com.kjh.boardback.dto.response.board.GetCommentListResponseDto;
import com.kjh.boardback.dto.response.board.GetFavoriteListResponseDto;
import com.kjh.boardback.dto.response.board.GetLatestBoardListResponseDto;
import com.kjh.boardback.dto.response.board.GetSearchBoardListResponseDto;
import com.kjh.boardback.dto.response.board.GetTop3BoardListResponseDto;
import com.kjh.boardback.dto.response.board.GetUserBoardListResponseDto;
import com.kjh.boardback.entity.SearchLog;
import com.kjh.boardback.entity.User;
import com.kjh.boardback.entity.board.Board;
import com.kjh.boardback.entity.board.Comment;
import com.kjh.boardback.entity.board.Favorite;
import com.kjh.boardback.entity.board.Image;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import com.kjh.boardback.repository.SearchLogRepository;
import com.kjh.boardback.repository.board.BoardRepository;
import com.kjh.boardback.repository.board.CommentRepository;
import com.kjh.boardback.repository.board.FavoriteRepository;
import com.kjh.boardback.repository.board.ImageRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;
    private final ImageRepository imageRepository;
    private final FavoriteRepository favoriteRepository;
    private final CommentRepository commentRepository;
    private final SearchLogRepository searchLogRepository;

    public Board findByBoardNumber(Integer boardNumber) {
        return boardRepository.findByBoardNumber(boardNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_BOARD));
    }

    public Comment findByCommentNumber(Integer commentNumber) {
        return commentRepository.findByCommentNumber(commentNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_COMMENT));
    }

    @Transactional
    public void deleteComment(Integer boardNumber, String email, Integer commentNumber) {

        Board board = findByBoardNumber(boardNumber);
        userService.findByEmail(email);
        Comment comment = findByCommentNumber(commentNumber);

        String writerEmail = board.getWriter().getEmail();
        String commentWriterEmail = comment.getWriter().getEmail();

        boolean isWriter = writerEmail.equals(email);
        boolean isCommentWriter = commentWriterEmail.equals(email);
        if (!isWriter && !isCommentWriter) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        commentRepository.delete(comment);
        board.decreaseCommentCount();
        boardRepository.save(board);
    }

    @Transactional
    public void patchComment(Integer boardNumber, Integer commentNumber, String email, PatchCommentRequestDto dto) {

        userService.findByEmail(email);
        findByBoardNumber(boardNumber);

        Comment comment = findByCommentNumber(commentNumber);
        String commentWriterEmail = comment.getWriter().getEmail();
        boolean isCommentWriter = commentWriterEmail.equals(email);
        if (!isCommentWriter) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        comment.patchComment(dto);
        commentRepository.save(comment);
    }

    public GetUserBoardListResponseDto getUserBoardList(String email) {
        User user = userService.findByEmail(email);
        List<Board> boardList = boardRepository.findByWriter_EmailOrderByCreatedAtDesc(email);
        return new GetUserBoardListResponseDto(boardList, user);
    }

    public GetSearchBoardListResponseDto getSearchBoardList(String searchWord, String preSearchWord) {

        List<Board> boardList = boardRepository.getBySearchWord(searchWord, searchWord);

        SearchLog searchLog = new SearchLog(searchWord, preSearchWord, false);
        searchLogRepository.save(searchLog);

        boolean relation = preSearchWord != null;
        if (relation) {
            searchLog = new SearchLog(preSearchWord, searchWord, relation);
            searchLogRepository.save(searchLog);
        }

        return new GetSearchBoardListResponseDto(boardList);
    }

    public GetTop3BoardListResponseDto getTop3BoardList() {

        Pageable pageable = PageRequest.of(0, 3, Sort.by(Order.desc("viewCount"), Order.desc("favoriteCount")));
        List<Board> top3List = boardRepository.getTop3Within7Days(pageable);

        return new GetTop3BoardListResponseDto(top3List);
    }

    public GetLatestBoardListResponseDto getLatestBoardList() {
        List<Board> latestBoardList = boardRepository.getLatestBoardList();
        return new GetLatestBoardListResponseDto(latestBoardList);
    }

    @Transactional
    public void patchBoard(PatchBoardRequestDto dto, Integer boardNumber, String email) {

        Board board = findByBoardNumber(boardNumber);
        userService.findByEmail(email);

        String writerEmail = board.getWriter().getEmail();
        boolean isWriter = writerEmail.equals(email);
        if (!isWriter) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        board.patchBoard(dto);
        boardRepository.save(board);

        imageRepository.deleteByBoard_BoardNumber(boardNumber);
        List<Image> imageEntities = new ArrayList<>();
        List<String> boardImageList = dto.getBoardImageList();
        for (String image : boardImageList) {
            Image imageEntity = Image.from(board, image);
            imageEntities.add(imageEntity);
        }
        imageRepository.saveAll(imageEntities);
    }

    @Transactional
    public void deleteBoard(Integer boardNumber, String email) {

        Board board = findByBoardNumber(boardNumber);
        userService.findByEmail(email);

        String writerEmail = board.getWriter().getEmail();
        boolean isWriter = writerEmail.equals(email);
        if (!isWriter) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        imageRepository.deleteByBoard_BoardNumber(boardNumber);
        favoriteRepository.deleteByBoard_BoardNumber(boardNumber);
        commentRepository.deleteByBoard_BoardNumber(boardNumber);
        boardRepository.delete(board);
    }

    @Transactional
    public void increaseViewCount(Integer boardNumber) {
        Board board = findByBoardNumber(boardNumber);
        board.increaseViewCount();
        boardRepository.save(board);
    }

    public GetCommentListResponseDto getCommentList(Integer boardNumber) {

        findByBoardNumber(boardNumber);
        List<Comment> commentList = commentRepository.getCommentList(boardNumber);

        return new GetCommentListResponseDto(commentList);
    }

    @Transactional
    public void postComment(Integer boardNumber, String email, PostCommentRequestDto dto) {
        Board board = findByBoardNumber(boardNumber);
        User user = userService.findByEmail(email);

        Comment comment = new Comment(board, user, dto);
        commentRepository.save(comment);

        board.increaseCommentCount();
        boardRepository.save(board);
    }

    public GetFavoriteListResponseDto getFavoriteList(Integer boardNumber) {

        findByBoardNumber(boardNumber);
        List<Favorite> favoriteList = favoriteRepository.getFavoriteList(boardNumber);

        return new GetFavoriteListResponseDto(favoriteList);
    }

    @Transactional
    public void putFavorite(String email, Integer boardNumber) {

        Board board = findByBoardNumber(boardNumber);
        User user = userService.findByEmail(email);

        Optional<Favorite> optional = favoriteRepository.findByBoard_BoardNumberAndUser_Email(
                boardNumber, email);

        if (optional.isEmpty()) {
            Favorite favorite = new Favorite(board, user);
            favoriteRepository.save(favorite);
            board.increaseFavoriteCount();
        } else {
            Favorite favorite = optional.get();
            favoriteRepository.delete(favorite);
            board.decreaseFavoriteCount();
        }

        boardRepository.save(board);
    }

    public GetBoardResponseDto getBoard(Integer boardNumber) {
        Board board = boardRepository.getBoardWithWriter(boardNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_BOARD));
        List<Image> imageList = imageRepository.findByBoard_BoardNumber(boardNumber);
        return new GetBoardResponseDto(board, imageList);
    }

    public void postBoard(PostBoardRequestDto dto, String email) {
        User user = userService.findByEmail(email);
        Board board = Board.from(dto, user);
        boardRepository.save(board);

        List<String> boardImageList = dto.getBoardImageList();
        List<Image> imageEntities = new ArrayList<>();

        for (String image : boardImageList) {
            Image imageEntity = Image.from(board, image);
            imageEntities.add(imageEntity);
        }
        imageRepository.saveAll(imageEntities);
    }
}
