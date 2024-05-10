package com.kjh.boardback.service.implement;

import com.kjh.boardback.dto.object.TradeBoardListItem;
import com.kjh.boardback.dto.request.trade_board.PatchTradeBoardRequestDto;
import com.kjh.boardback.dto.request.trade_board.PostTradeBoardRequestDto;
import com.kjh.boardback.dto.response.trade_board.*;
import com.kjh.boardback.entity.TradeBoardEntity;
import com.kjh.boardback.entity.TradeBoardListViewEntity;
import com.kjh.boardback.entity.TradeCommentEntity;
import com.kjh.boardback.entity.TradeImageEntity;
import com.kjh.boardback.repository.UserRepository;
import com.kjh.boardback.repository.resultSet.GetTradeBoardResultSet;
import com.kjh.boardback.repository.trade_board.*;
import com.kjh.boardback.service.TradeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeBoardServiceImplement implements TradeBoardService {

    private final TradeBoardRepository tradeBoardRepository;
    private final TradeImageRepository tradeImageRepository;
    private final TradeCommentRepository tradeCommentRepository;
    private final TradeFavoriteRepository tradeFavoriteRepository;
    private final TradeBoardListViewRepository tradeBoardListViewRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<? super GetLatestTradeBoardListResponseDto> getTradeBoardList() {

        List<TradeBoardListItem> tradeBoardListItemList = new ArrayList<>();

        try{
            List<TradeBoardListViewEntity> tradeBoardListViewEntities = tradeBoardListViewRepository.findByOrderByWriteDatetimeDesc();
            tradeBoardListItemList=TradeBoardListItem.getList(tradeBoardListViewEntities);

        }catch (Exception exception){
            exception.printStackTrace();
            return GetLatestTradeBoardListResponseDto.databaseError();
        }
        return GetLatestTradeBoardListResponseDto.success(tradeBoardListItemList);
    }

    @Override
    public ResponseEntity<? super DeleteTradeBoardResponseDto> deleteTradeBoard(String email, Integer boardNumber) {
        try{
            boolean existedUser = userRepository.existsByEmail(email);
            if(!existedUser) return DeleteTradeBoardResponseDto.noExistUser();

            TradeBoardEntity boardEntity = tradeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity ==null) return DeleteTradeBoardResponseDto.noExistBoard();

            boolean isWriter = boardEntity.getWriterEmail().equals(email);
            if(!isWriter) return DeleteTradeBoardResponseDto.noPermission();

            tradeImageRepository.deleteByBoardNumber(boardNumber);
            tradeCommentRepository.deleteByBoardNumber(boardNumber);
            tradeFavoriteRepository.deleteByBoardNumber(boardNumber);
            tradeBoardRepository.delete(boardEntity);



        }catch (Exception exception){
            exception.printStackTrace();
            return DeleteTradeBoardResponseDto.databaseError();
        }
        return DeleteTradeBoardResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PatchTradeBoardResponseDto> patchTradeBoard(PatchTradeBoardRequestDto requestDto, String email,Integer boardNumber) {
        try{
            boolean existedUser = userRepository.existsByEmail(email);
            if(!existedUser) return PatchTradeBoardResponseDto.noExistUser();

            TradeBoardEntity boardEntity = tradeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity ==null) return PatchTradeBoardResponseDto.noExistBoard();

            boolean isWriter = boardEntity.getWriterEmail().equals(email);
            if(!isWriter) return PatchTradeBoardResponseDto.noPermission();

            boardEntity.patchBoard(requestDto);
            tradeBoardRepository.save(boardEntity);

            tradeImageRepository.deleteByBoardNumber(boardNumber);
            List<String> imageList = requestDto.getBoardImageList();
            List<TradeImageEntity> imageEntities = new ArrayList<>();

            for(String image : imageList ){
                TradeImageEntity imageEntity = new TradeImageEntity(boardNumber, image);
                imageEntities.add(imageEntity);
            }
            tradeImageRepository.saveAll(imageEntities);

        }catch (Exception exception){
            exception.printStackTrace();
            return PatchTradeBoardResponseDto.databaseError();
        }
        return PatchTradeBoardResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PostTradeBoardResponseDto> postTradeBoard(PostTradeBoardRequestDto requestDto, String email) {
        try{
            boolean existedEmail = userRepository.existsByEmail(email);
            if(!existedEmail) return PostTradeBoardResponseDto.noExistUser();

            TradeBoardEntity tradeBoardEntity = new TradeBoardEntity(requestDto, email);
            tradeBoardRepository.save(tradeBoardEntity);

            int boardNumber = tradeBoardEntity.getBoardNumber();

            List<String> imageList = requestDto.getBoardImageList();
            List<TradeImageEntity> imageEntities = new ArrayList<>();

            for(String image : imageList ){
                TradeImageEntity imageEntity = new TradeImageEntity(boardNumber, image);
                imageEntities.add(imageEntity);
            }
            tradeImageRepository.saveAll(imageEntities);

        }catch (Exception exception){
            exception.printStackTrace();
            return PostTradeBoardResponseDto.databaseError();
        }
        return PostTradeBoardResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetTradeBoardResponseDto> getTradeBoard(Integer boardNumber) {

        GetTradeBoardResultSet resultSet = null;
        List<TradeImageEntity> tradeImageEntities = new ArrayList<>();

        try{
            resultSet = tradeBoardRepository.getTradeBoard(boardNumber);
            if (resultSet == null) return GetTradeBoardResponseDto.noExistBoard();

            tradeImageEntities = tradeImageRepository.findByBoardNumber(boardNumber);


        }catch (Exception exception){
            exception.printStackTrace();
            return GetTradeBoardResponseDto.databaseError();
        }
        return GetTradeBoardResponseDto.success(resultSet,tradeImageEntities);
    }
}
