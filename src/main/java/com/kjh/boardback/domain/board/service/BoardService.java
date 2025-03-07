package com.kjh.boardback.domain.board.service;

import com.kjh.boardback.domain.board.dto.object.BoardDto;
import com.kjh.boardback.domain.board.dto.request.PatchBoardRequestDto;
import com.kjh.boardback.domain.board.dto.request.PostBoardRequestDto;
import com.kjh.boardback.domain.board.dto.response.GetBoardListResponseDto;
import com.kjh.boardback.domain.board.dto.response.GetBoardPageListResponseDto;
import com.kjh.boardback.domain.board.dto.response.GetBoardResponseDto;
import com.kjh.boardback.domain.board.entity.Board;
import com.kjh.boardback.domain.board.entity.Image;
import com.kjh.boardback.domain.board.repository.BoardRepository;
import com.kjh.boardback.domain.search_log.entity.SearchLog;
import com.kjh.boardback.domain.search_log.service.SearchLogService;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.user.service.UserService;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import com.kjh.boardback.global.service.RedisService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final BoardCommentService boardCommentService;
    private final BoardFavoriteService boardFavoriteService;
    private final BoardImageService imageService;
    private final UserService userService;
    private final SearchLogService searchLogService;
    private final RedisService redisService;

    public Board findByBoardNumber(Integer boardNumber) {
        return boardRepository.findByBoardNumber(boardNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_BOARD));
    }

    public GetBoardResponseDto getBoard(Integer boardNumber) {
        Board board = boardRepository.getBoardWithWriter(boardNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_BOARD));
        List<Image> imageList = imageService.findByBoardNumber(boardNumber);
        return new GetBoardResponseDto(board, imageList);
    }

    public GetBoardListResponseDto getUserBoardList(String email) {
        User user = userService.findByEmailOrElseThrow(email);
        List<Board> boardList = boardRepository.findByWriter_EmailOrderByCreatedAtDesc(email);
        return GetBoardListResponseDto.from(boardList, user);
    }

    public GetBoardListResponseDto getSearchBoardList(String searchWord, String preSearchWord) {

        List<Board> boardList = boardRepository.getBySearchWord(searchWord, searchWord);

        SearchLog searchLog = new SearchLog(searchWord, preSearchWord, false);
        searchLogService.save(searchLog);

        boolean relation = preSearchWord != null;
        if (relation) {
            searchLog = new SearchLog(preSearchWord, searchWord, relation);
            searchLogService.save(searchLog);
        }

        return GetBoardListResponseDto.from(boardList);
    }

    public GetBoardListResponseDto getTop3BoardList() {
        List<BoardDto> boardList = redisService.getBoardTop3();

        if (boardList.size() < 3) {
            Pageable pageable = PageRequest.of(0, 3);
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

            List<Board> top3List = boardRepository.getTop3Within7Days(sevenDaysAgo, pageable);
            List<BoardDto> dtoList = BoardDto.getList(top3List);
            redisService.setBoardTop3(dtoList);

            return GetBoardListResponseDto.from(top3List);
        } else {

            return GetBoardListResponseDto.fromDto(boardList);
        }
    }

    public GetBoardPageListResponseDto getLatestBoardList(Pageable pageable) {
        Page<Board> latestBoardList = boardRepository.getLatestBoardList(pageable);
        return new GetBoardPageListResponseDto(latestBoardList);
    }

    @Transactional
    public void increaseViewCount(Integer boardNumber) {
        Board board = findByBoardNumber(boardNumber);
        board.increaseViewCount();
        boardRepository.save(board);
    }


    @Transactional
    public void postBoard(PostBoardRequestDto dto, String email) {
        User user = userService.findByEmailOrElseThrow(email);
        Board board = Board.from(dto, user);
        boardRepository.save(board);

        List<String> boardImageList = dto.getBoardImageList();
        List<Image> imageEntities = new ArrayList<>();

        for (String image : boardImageList) {
            Image imageEntity = Image.from(board, image);
            imageEntities.add(imageEntity);
        }
        imageService.saveAll(imageEntities);
    }

    @Transactional
    public void patchBoard(PatchBoardRequestDto dto, Integer boardNumber, String email) {

        Board board = findByBoardNumber(boardNumber);
        userService.findByEmailOrElseThrow(email);

        String writerEmail = board.getWriter().getEmail();
        boolean isWriter = writerEmail.equals(email);
        if (!isWriter) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        board.patchBoard(dto);
        boardRepository.save(board);

        imageService.deleteByBoardNumber(boardNumber);
        List<Image> imageEntities = new ArrayList<>();
        List<String> boardImageList = dto.getBoardImageList();
        for (String image : boardImageList) {
            Image imageEntity = Image.from(board, image);
            imageEntities.add(imageEntity);
        }
        imageService.saveAll(imageEntities);
        updateCacheIfInclude(boardNumber);
    }

    @Transactional
    public void deleteBoard(Integer boardNumber, String email) {

        Board board = findByBoardNumber(boardNumber);
        userService.findByEmailOrElseThrow(email);

        String writerEmail = board.getWriter().getEmail();
        boolean isWriter = writerEmail.equals(email);
        if (!isWriter) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        imageService.deleteByBoardNumber(boardNumber);
        boardFavoriteService.deleteByBoardNumber(boardNumber);
        boardCommentService.deleteByBoardNumber(boardNumber);
        boardRepository.delete(board);
        updateCacheIfInclude(boardNumber);
    }

    private void updateCacheIfInclude(Integer boardNumber) {
        List<BoardDto> boardList = redisService.getBoardTop3();
        for(BoardDto dto : boardList) {
            if(dto.getBoardNumber() == boardNumber) {
                Pageable pageable = PageRequest.of(0, 3);
                LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
                List<Board> top3List = boardRepository.getTop3Within7Days(sevenDaysAgo, pageable);
                List<BoardDto> dtoList = BoardDto.getList(top3List);
                redisService.setBoardTop3(dtoList);
                return;
            }
        }
    }

}
