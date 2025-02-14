package com.kjh.boardback.domain.recipe_board.service;


import com.kjh.boardback.domain.recipe_board.dto.request.PatchRecipeBoardRequestDto;
import com.kjh.boardback.domain.recipe_board.dto.request.PostRecipeBoardRequestDto;
import com.kjh.boardback.domain.recipe_board.dto.response.GetRecipeBoardListResponseDto;
import com.kjh.boardback.domain.recipe_board.dto.response.GetRecipeBoardResponseDto;
import com.kjh.boardback.domain.recipe_board.entity.RecipeBoard;
import com.kjh.boardback.domain.recipe_board.entity.RecipeImage;
import com.kjh.boardback.domain.recipe_board.repository.RecipeBoardRepository;
import com.kjh.boardback.domain.search_log.entity.SearchLog;
import com.kjh.boardback.domain.search_log.service.SearchLogService;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.user.service.UserService;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.List;
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
    private final RecipeCommentService commentService;
    private final RecipeFavoriteService favoriteService;
    private final RecipeImageService imageService;
    private final UserService userService;
    private final SearchLogService searchLogService;

    public RecipeBoard findByBoardNumber(Integer boardNumber) {
        return boardRepository.findByBoardNumber(boardNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_BOARD));
    }

    public RecipeBoard save(RecipeBoard recipeBoard) {
        return boardRepository.save(recipeBoard);
    }

    public GetRecipeBoardResponseDto getBoard(Integer boardNumber) {

        RecipeBoard board = findByBoardNumber(boardNumber);
        List<RecipeImage> imageList = imageService.findByBoardNumber(boardNumber);

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
        searchLogService.save(searchLog);

        boolean relation = preSearchWord != null;
        if (relation) {
            searchLog = new SearchLog(preSearchWord, searchWord, relation);
            searchLogService.save(searchLog);
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
        RecipeBoard savedBoard = boardRepository.save(board);

        List<RecipeImage> imageList = RecipeImage.toList(board, dto.getBoardImageList(), dto.getSteps());
        imageService.saveAll(imageList);

        boardRepository.save(savedBoard);
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

        imageService.deleteByBoardNumber(boardNumber);

        List<RecipeImage> imageList = RecipeImage.toList(board, dto.getBoardImageList(), dto.getSteps());
        imageService.saveAll(imageList);

        board.patch(dto);
        boardRepository.save(board);
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

        imageService.deleteByBoardNumber(boardNumber);
        favoriteService.deleteByBoardNumber(boardNumber);
        commentService.deleteByBoardNumber(boardNumber);
        boardRepository.delete(board);
    }
}
