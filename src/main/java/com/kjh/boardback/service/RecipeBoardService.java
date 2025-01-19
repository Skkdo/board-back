package com.kjh.boardback.service;


import com.kjh.boardback.dto.request.board.PatchCommentRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PatchRecipeBoardRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PostRecipeBoardRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PostRecipeCommentRequestDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeBoardListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeBoardResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeCommentListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeFavoriteListResponseDto;
import com.kjh.boardback.entity.SearchLog;
import com.kjh.boardback.entity.User;
import com.kjh.boardback.entity.board.Favorite;
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
import java.time.LocalDateTime;
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
public class RecipeBoardService {

    private final RecipeBoardRepository boardRepository;
    private final UserService userService;
    private final RecipeImageRepository imageRepository;
    private final RecipeFavoriteRepository favoriteRepository;
    private final RecipeCommentRepository commentRepository;
    private final SearchLogRepository searchLogRepository;

    public RecipeBoard findByBoardNumber(Integer boardNumber) {
        return boardRepository.findByBoardNumber(boardNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_BOARD));
    }

    public RecipeComment findByCommentNumber(Integer commentNumber) {
        return commentRepository.findByCommentNumber(commentNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_COMMENT));
    }

    public GetRecipeBoardResponseDto getBoard(Integer boardNumber) {

        RecipeBoard board = findByBoardNumber(boardNumber);
        List<RecipeImage> imageList = imageRepository.findByBoard_BoardNumber(boardNumber);

        return new GetRecipeBoardResponseDto(board, imageList);
    }

    public GetRecipeBoardListResponseDto getLatestBoardList(int type) {
        List<RecipeBoard> boardList = boardRepository.getLatestList(type);
        return new GetRecipeBoardListResponseDto(boardList);
    }

    public GetRecipeBoardListResponseDto getTop3BoardList(int type) {

        Pageable pageable = PageRequest.of(0, 3, Sort.by(Order.desc("viewCount"), Order.desc("favoriteCount")));
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<RecipeBoard> boardList = boardRepository.getTop3ListWithin7Days(type, sevenDaysAgo, pageable);

        if (type == 0 || type == 1) {
            return new GetRecipeBoardListResponseDto(boardList);
        } else {
            throw new BusinessException(ResponseCode.NOT_EXISTED_BOARD);
        }
    }

    public GetRecipeBoardListResponseDto getSearchBoardList(String searchWord, String preSearchWord) {

        List<RecipeBoard> boardList = boardRepository.getBySearchWord(searchWord, searchWord);

        SearchLog searchLog = new SearchLog(searchWord, preSearchWord, false);
        searchLogRepository.save(searchLog);

        boolean relation = preSearchWord != null;
        if (relation) {
            searchLog = new SearchLog(preSearchWord, searchWord, relation);
            searchLogRepository.save(searchLog);
        }

        return new GetRecipeBoardListResponseDto(boardList);
    }

    public GetRecipeBoardListResponseDto getUserBoardList(String email) {

        userService.findByEmailOrElseThrow(email);
        List<RecipeBoard> boardList = boardRepository.getUserBoardList(email);

        return new GetRecipeBoardListResponseDto(boardList);
    }

    @Transactional
    public void increaseViewCount(Integer boardNumber) {
        RecipeBoard board = findByBoardNumber(boardNumber);
        board.increaseViewCount();
        boardRepository.save(board);
    }

    @Transactional
    public void postBoard(PostRecipeBoardRequestDto dto, String email) {

        User user = userService.findByEmailOrElseThrow(email);
        RecipeBoard board = RecipeBoard.from(dto, user);
        boardRepository.save(board);

        List<RecipeImage> imageList = new ArrayList<>();
        postBoardImageList(dto, board, imageList);
        imageRepository.saveAll(imageList);
    }

    private static void postBoardImageList(PostRecipeBoardRequestDto dto, RecipeBoard board,
                                           List<RecipeImage> imageList) {
        List<String> boardImageList = dto.getBoardImageList();
        for (String image : boardImageList) {
            RecipeImage imageEntity = RecipeImage.from(board, image, 0);
            imageList.add(imageEntity);
        }
        if (dto.getStep1_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep1_image(), 1));
        }
        if (dto.getStep2_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep2_image(), 2));
        }
        if (dto.getStep3_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep3_image(), 3));
        }
        if (dto.getStep4_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep4_image(), 4));
        }
        if (dto.getStep5_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep5_image(), 5));
        }
        if (dto.getStep6_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep6_image(), 6));
        }
        if (dto.getStep7_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep7_image(), 7));
        }
        if (dto.getStep8_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep8_image(), 8));
        }
    }

    private static void patchBoardImageList(PatchRecipeBoardRequestDto dto, RecipeBoard board,
                                            List<RecipeImage> imageList) {
        List<String> boardImageList = dto.getBoardImageList();
        for (String image : boardImageList) {
            RecipeImage imageEntity = RecipeImage.from(board, image, 0);
            imageList.add(imageEntity);
        }
        if (dto.getStep1_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep1_image(), 1));
        }
        if (dto.getStep2_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep2_image(), 2));
        }
        if (dto.getStep3_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep3_image(), 3));
        }
        if (dto.getStep4_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep4_image(), 4));
        }
        if (dto.getStep5_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep5_image(), 5));
        }
        if (dto.getStep6_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep6_image(), 6));
        }
        if (dto.getStep7_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep7_image(), 7));
        }
        if (dto.getStep8_image() != null) {
            imageList.add(RecipeImage.from(board, dto.getStep8_image(), 8));
        }
    }


    @Transactional
    public void patchBoard(PatchRecipeBoardRequestDto dto, Integer boardNumber, String email) {

        RecipeBoard board = findByBoardNumber(boardNumber);
        userService.findByEmailOrElseThrow(email);

        String writerEmail = board.getWriter().getEmail();
        boolean isWriter = writerEmail.equals(email);
        if (!isWriter) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        board.patch(dto);
        boardRepository.save(board);
        imageRepository.deleteByBoard_BoardNumber(boardNumber);

        List<RecipeImage> imageEntities = new ArrayList<>();
        patchBoardImageList(dto, board, imageEntities);
        imageRepository.saveAll(imageEntities);
    }

    @Transactional
    public void deleteBoard(Integer boardNumber, String email) {

        RecipeBoard board = findByBoardNumber(boardNumber);
        userService.findByEmailOrElseThrow(email);

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

    public GetRecipeCommentListResponseDto getCommentList(Integer boardNumber) {
        findByBoardNumber(boardNumber);
        List<RecipeComment> commentList = commentRepository.getCommentListWithUser(boardNumber);
        return new GetRecipeCommentListResponseDto(commentList);
    }

    @Transactional
    public void postComment(Integer boardNumber, String email, PostRecipeCommentRequestDto dto) {

        RecipeBoard board = findByBoardNumber(boardNumber);
        User user = userService.findByEmailOrElseThrow(email);

        RecipeComment comment = RecipeComment.from(board, user, dto);
        commentRepository.save(comment);

        board.increaseCommentCount();
        boardRepository.save(board);
    }

    @Transactional
    public void patchComment(Integer boardNumber, Integer commentNumber, String email, PatchCommentRequestDto dto) {

        userService.findByEmailOrElseThrow(email);
        findByBoardNumber(boardNumber);
        RecipeComment comment = findByCommentNumber(commentNumber);

        String commentWriterEmail = comment.getWriter().getEmail();
        boolean isCommentWriter = commentWriterEmail.equals(email);
        if (!isCommentWriter) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        comment.patchComment(dto);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Integer boardNumber, Integer commentNumber, String email) {

        userService.findByEmailOrElseThrow(email);
        RecipeBoard board = findByBoardNumber(boardNumber);
        RecipeComment comment = findByCommentNumber(commentNumber);

        String boardWriterEmail = board.getWriter().getEmail();
        String commentWriterEmail = comment.getWriter().getEmail();

        boolean isBoardWriter = boardWriterEmail.equals(email);
        boolean isCommentWriter = commentWriterEmail.equals(email);

        if (!isCommentWriter && !isBoardWriter) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        commentRepository.delete(comment);
        board.decreaseCommentCount();
        boardRepository.save(board);
    }

    public GetRecipeFavoriteListResponseDto getFavoriteList(Integer boardNumber) {
        findByBoardNumber(boardNumber);
        List<Favorite> favoriteList = favoriteRepository.getFavoriteListWithUser(boardNumber);
        return new GetRecipeFavoriteListResponseDto(favoriteList);
    }

    @Transactional
    public void putFavorite(String email, Integer boardNumber) {

        User user = userService.findByEmailOrElseThrow(email);
        RecipeBoard board = findByBoardNumber(boardNumber);

        Optional<RecipeFavorite> optional = favoriteRepository.findByBoard_BoardNumberAndUser_Email(
                boardNumber, email);

        if (optional.isEmpty()) {
            RecipeFavorite favorite = RecipeFavorite.from(user, board);
            favoriteRepository.save(favorite);
            board.increaseFavoriteCount();
        } else {
            favoriteRepository.delete(optional.get());
            board.decreaseFavoriteCount();
        }
        boardRepository.save(board);
    }
}
