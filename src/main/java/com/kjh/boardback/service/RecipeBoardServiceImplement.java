package com.kjh.boardback.service;


import com.kjh.boardback.dto.request.board.PatchCommentRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PatchRecipeBoardRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PostRecipeBoardRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PostRecipeCommentRequestDto;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.dto.response.recipe_board.DeleteRecipeBoardResponseDto;
import com.kjh.boardback.dto.response.recipe_board.DeleteRecipeCommentResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetLatestRecipeBoardListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeBoardResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeCommentListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeFavoriteListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetSearchRecipeBoardListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetTop3ConvenienceRecipeBoardListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetTop3GeneralRecipeBoardListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetTop3RecipeBoardListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetUserRecipeBoardListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.IncreaseRecipeViewCountResponseDto;
import com.kjh.boardback.dto.response.recipe_board.PatchRecipeBoardResponseDto;
import com.kjh.boardback.dto.response.recipe_board.PatchRecipeCommentResponseDto;
import com.kjh.boardback.dto.response.recipe_board.PostRecipeBoardResponseDto;
import com.kjh.boardback.dto.response.recipe_board.PostRecipeCommentResponseDto;
import com.kjh.boardback.dto.response.recipe_board.PutRecipeFavoriteResponseDto;
import com.kjh.boardback.entity.SearchLogEntity;
import com.kjh.boardback.entity.recipe_board.RecipeBoardEntity;
import com.kjh.boardback.entity.recipe_board.RecipeBoardListViewEntity;
import com.kjh.boardback.entity.recipe_board.RecipeCommentEntity;
import com.kjh.boardback.entity.recipe_board.RecipeFavoriteEntity;
import com.kjh.boardback.entity.recipe_board.RecipeImageEntity;
import com.kjh.boardback.repository.SearchLogRepository;
import com.kjh.boardback.repository.UserRepository;
import com.kjh.boardback.repository.recipe_board.RecipeBoardListViewRepository;
import com.kjh.boardback.repository.recipe_board.RecipeBoardRepository;
import com.kjh.boardback.repository.recipe_board.RecipeCommentRepository;
import com.kjh.boardback.repository.recipe_board.RecipeFavoriteRepository;
import com.kjh.boardback.repository.recipe_board.RecipeImageRepository;
import com.kjh.boardback.repository.resultSet.GetRecipeBoardResultSet;
import com.kjh.boardback.repository.resultSet.GetRecipeCommentListResultSet;
import com.kjh.boardback.repository.resultSet.GetRecipeFavoriteListResultSet;
import com.kjh.boardback.service.RecipeBoardService;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeBoardServiceImplement implements RecipeBoardService {

    private final RecipeBoardRepository recipeBoardRepository;
    private final UserRepository userRepository;
    private final RecipeImageRepository recipeImageRepository;
    private final RecipeFavoriteRepository recipeFavoriteRepository;
    private final RecipeCommentRepository recipeCommentRepository;
    private final RecipeBoardListViewRepository recipeBoardListViewRepository;
    private final SearchLogRepository searchLogRepository;

    @Override
    public ResponseEntity<? super GetRecipeBoardResponseDto> getBoard(Integer boardNumber) {
        GetRecipeBoardResultSet resultSet = null;
        List<RecipeImageEntity> imageEntities = new ArrayList<>();

        try {
            resultSet = recipeBoardRepository.getRecipeBoard(boardNumber);
            if (resultSet == null) return GetRecipeBoardResponseDto.noExistBoard();

            imageEntities = recipeImageRepository.findByBoardNumber(boardNumber);


        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetRecipeBoardResponseDto.success(resultSet, imageEntities);
    }

    @Override
    public ResponseEntity<? super PostRecipeBoardResponseDto> postBoard(PostRecipeBoardRequestDto dto, String email) {
        try {
            boolean existedEmail = userRepository.existsByEmail(email);
            if (!existedEmail) return PostRecipeBoardResponseDto.noExistUser();

            RecipeBoardEntity boardEntity = new RecipeBoardEntity(dto, email);
            recipeBoardRepository.save(boardEntity);

            int boardNumber = boardEntity.getBoardNumber();

            List<RecipeImageEntity> imageEntities = new ArrayList<>();
            postBoardImageList(dto, boardNumber, imageEntities);
            recipeImageRepository.saveAll(imageEntities);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return PostRecipeBoardResponseDto.success();
    }

    private static void postBoardImageList(PostRecipeBoardRequestDto dto, int boardNumber, List<RecipeImageEntity> imageEntities) {
        List<String> boardImageList = dto.getBoardImageList();
        for (String image : boardImageList) {
            RecipeImageEntity imageEntity = new RecipeImageEntity(boardNumber, image,0);
            imageEntities.add(imageEntity);
        }
        if(dto.getStep1_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep1_image(),1));
        if(dto.getStep2_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep2_image(),2));
        if(dto.getStep3_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep3_image(),3));
        if(dto.getStep4_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep4_image(),4));
        if(dto.getStep5_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep5_image(),5));
        if(dto.getStep6_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep6_image(),6));
        if(dto.getStep7_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep7_image(),7));
        if(dto.getStep8_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep8_image(),8));
    }


    @Override
    public ResponseEntity<? super PutRecipeFavoriteResponseDto> putFavorite(String email, Integer boardNumber) {
        try {
            boolean existedEmail = userRepository.existsByEmail(email);
            if (!existedEmail) return PutRecipeFavoriteResponseDto.noExistUser();

            RecipeBoardEntity boardEntity = recipeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return PutRecipeFavoriteResponseDto.noExistBoard();

            RecipeFavoriteEntity favoriteEntity = recipeFavoriteRepository.findByBoardNumberAndUserEmail(boardNumber, email);
            if (favoriteEntity == null) {
                favoriteEntity = new RecipeFavoriteEntity(email, boardNumber);
                recipeFavoriteRepository.save(favoriteEntity);
                boardEntity.increaseFavoriteCount();
            } else {
                recipeFavoriteRepository.delete(favoriteEntity);
                boardEntity.decreaseFavoriteCount();
            }
            recipeBoardRepository.save(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return PutRecipeFavoriteResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetRecipeFavoriteListResponseDto> getFavoriteList(Integer boardNumber) {
        List<GetRecipeFavoriteListResultSet> resultSets = new ArrayList<>();

        try {
            Boolean existedBoard = recipeBoardRepository.existsByBoardNumber(boardNumber);
            if (!existedBoard) return GetRecipeFavoriteListResponseDto.noExistBoard();

            resultSets = recipeFavoriteRepository.getFavoriteList(boardNumber);


        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetRecipeFavoriteListResponseDto.success(resultSets);
    }

    @Override
    public ResponseEntity<? super GetRecipeCommentListResponseDto> getCommentList(Integer boardNumber) {
        List<GetRecipeCommentListResultSet> resultSets = new ArrayList<>();

        try {
            Boolean existedBoard = recipeBoardRepository.existsByBoardNumber(boardNumber);
            if (!existedBoard) return GetRecipeCommentListResponseDto.noExistBoard();

            resultSets = recipeCommentRepository.getCommentList(boardNumber);


        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetRecipeCommentListResponseDto.success(resultSets);
    }

    @Override
    public ResponseEntity<? super IncreaseRecipeViewCountResponseDto> increaseViewCount(Integer boardNumber) {
        try {
            RecipeBoardEntity boardEntity = recipeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return IncreaseRecipeViewCountResponseDto.noExistBoard();
            boardEntity.increaseViewCount();
            recipeBoardRepository.save(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return IncreaseRecipeViewCountResponseDto.success();
    }

    @Override
    public ResponseEntity<? super DeleteRecipeBoardResponseDto> deleteBoard(Integer boardNumber, String email) {
        try {
            RecipeBoardEntity boardEntity = recipeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return DeleteRecipeBoardResponseDto.noExistBoard();

            boolean existedEmail = userRepository.existsByEmail(email);
            if (!existedEmail) return DeleteRecipeBoardResponseDto.noExistUser();

            String writerEmail = boardEntity.getWriterEmail();
            boolean isWriter = writerEmail.equals(email);
            if (!isWriter) return DeleteRecipeBoardResponseDto.noPermission();


            recipeImageRepository.deleteByBoardNumber(boardNumber);
            recipeFavoriteRepository.deleteByBoardNumber(boardNumber);
            recipeCommentRepository.deleteByBoardNumber(boardNumber);
            recipeBoardRepository.delete(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return DeleteRecipeBoardResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PatchRecipeBoardResponseDto> patchBoard(PatchRecipeBoardRequestDto dto, Integer boardNumber, String email) {
        try {
            RecipeBoardEntity boardEntity = recipeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return PatchRecipeBoardResponseDto.noExistBoard();

            boolean existedEmail = userRepository.existsByEmail(email);
            if (!existedEmail) return PatchRecipeBoardResponseDto.noExistUser();

            String writerEmail = boardEntity.getWriterEmail();
            boolean isWriter = writerEmail.equals(email);
            if (!isWriter) return PatchRecipeBoardResponseDto.noPermission();

            boardEntity.patchBoard(dto);
            recipeBoardRepository.save(boardEntity);
            recipeImageRepository.deleteByBoardNumber(boardNumber);

            List<RecipeImageEntity> imageEntities = new ArrayList<>();
            patchBoardImageList(dto, boardNumber, imageEntities);
            recipeImageRepository.saveAll(imageEntities);



        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return PatchRecipeBoardResponseDto.success();
    }
    private static void patchBoardImageList(PatchRecipeBoardRequestDto dto, int boardNumber, List<RecipeImageEntity> imageEntities) {
        List<String> boardImageList = dto.getBoardImageList();
        for (String image : boardImageList) {
            RecipeImageEntity imageEntity = new RecipeImageEntity(boardNumber, image,0);
            imageEntities.add(imageEntity);
        }
        if(dto.getStep1_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep1_image(),1));
        if(dto.getStep2_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep2_image(),2));
        if(dto.getStep3_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep3_image(),3));
        if(dto.getStep4_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep4_image(),4));
        if(dto.getStep5_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep5_image(),5));
        if(dto.getStep6_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep6_image(),6));
        if(dto.getStep7_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep7_image(),7));
        if(dto.getStep8_image()!=null) imageEntities.add(new RecipeImageEntity(boardNumber, dto.getStep8_image(),8));
    }

    @Override
    public ResponseEntity<? super GetLatestRecipeBoardListResponseDto> getLatestBoardList(int type) {
        List<RecipeBoardListViewEntity> boardListViewEntities = new ArrayList<>();
        try {
            boardListViewEntities = recipeBoardListViewRepository.findByOrderByWriteDatetimeDesc(type);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetLatestRecipeBoardListResponseDto.success(boardListViewEntities);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getTop3BoardList(int type) {
        List<RecipeBoardListViewEntity> boardListViewEntities = new ArrayList<>();

        try {
            Date beforeWeek = Date.from(Instant.now().minus(7, ChronoUnit.DAYS));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sevenDaysAgo = simpleDateFormat.format(beforeWeek);
            boardListViewEntities = recipeBoardListViewRepository.getTop3BoardList(sevenDaysAgo,type);

        } catch (Exception exception) {
            exception.printStackTrace();
            return GetTop3RecipeBoardListResponseDto.getTop3DatabaseError();
        }
        if (type == 0){
            return GetTop3GeneralRecipeBoardListResponseDto.success(boardListViewEntities);
        } else if (type == 1) {
            return GetTop3ConvenienceRecipeBoardListResponseDto.success(boardListViewEntities);
        }
        return GetTop3RecipeBoardListResponseDto.getTop3TypeError();
    }

    @Override
    public ResponseEntity<? super GetSearchRecipeBoardListResponseDto> getSearchBoardList(String searchWord, String preSearchWord) {

        List<RecipeBoardListViewEntity> boardListViewEntities = new ArrayList<>();

        try {
            boardListViewEntities = recipeBoardListViewRepository.getSearchBoardList(searchWord, searchWord);
            SearchLogEntity searchLogEntity = new SearchLogEntity(searchWord, preSearchWord, false);
            searchLogRepository.save(searchLogEntity);

            boolean relation = preSearchWord != null;
            if (relation) {
                searchLogEntity = new SearchLogEntity(preSearchWord, searchWord, relation);
                searchLogRepository.save(searchLogEntity);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetSearchRecipeBoardListResponseDto.success(boardListViewEntities);
    }

    @Override
    public ResponseEntity<? super GetUserRecipeBoardListResponseDto> getUserBoardList(String email) {
        List<RecipeBoardListViewEntity> boardListViewEntities = new ArrayList<>();

        try {
            boolean existedEmail = userRepository.existsByEmail(email);
            if (!existedEmail) return GetUserRecipeBoardListResponseDto.noExistUser();

            boardListViewEntities = recipeBoardListViewRepository.findByWriterEmailOrderByWriteDatetimeDesc(email);


        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetUserRecipeBoardListResponseDto.success(boardListViewEntities);
    }

    @Override
    public ResponseEntity<? super PostRecipeCommentResponseDto> postComment(Integer boardNumber, String email, PostRecipeCommentRequestDto dto) {
        try {
            RecipeBoardEntity boardEntity = recipeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return PostRecipeCommentResponseDto.noExistBoard();

            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return PostRecipeCommentResponseDto.noExistUser();

            RecipeCommentEntity commentEntity = new RecipeCommentEntity(boardNumber, email, dto);
            recipeCommentRepository.save(commentEntity);

            boardEntity.increaseCommentCount();
            recipeBoardRepository.save(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return PostRecipeCommentResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PatchRecipeCommentResponseDto> patchComment(Integer boardNumber, Integer commentNumber ,String email, PatchCommentRequestDto dto) {
        try{
            boolean existedEmail = userRepository.existsByEmail(email);
            if(!existedEmail) return PatchRecipeCommentResponseDto.noExistUser();

            RecipeBoardEntity boardEntity = recipeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return PatchRecipeCommentResponseDto.noExistBoard();

            RecipeCommentEntity commentEntity = recipeCommentRepository.findByCommentNumber(commentNumber);
            if(commentEntity == null) return PatchRecipeCommentResponseDto.noExistBoard();

            String commentWriterEmail = commentEntity.getUserEmail();
            boolean isCommentWriter = commentWriterEmail.equals(email);

            if(!isCommentWriter) return PatchRecipeCommentResponseDto.noPermission();

            commentEntity.patchComment(dto);
            recipeCommentRepository.save(commentEntity);
        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return PatchRecipeCommentResponseDto.success();
    }

    @Override
    public ResponseEntity<? super DeleteRecipeCommentResponseDto> deleteComment(Integer boardNumber, Integer commentNumber, String email) {
        try {
            RecipeBoardEntity boardEntity = recipeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return DeleteRecipeCommentResponseDto.noExistBoard();

            boolean existedEmail = userRepository.existsByEmail(email);
            if (!existedEmail) return DeleteRecipeCommentResponseDto.noExistUser();

            RecipeCommentEntity commentEntity = recipeCommentRepository.findByCommentNumber(commentNumber);
            if (commentEntity == null) return DeleteRecipeCommentResponseDto.noExistBoard();

            String writerEmail = boardEntity.getWriterEmail();
            String commentWriterEmail = commentEntity.getUserEmail();
            boolean isWriter = writerEmail.equals(email);
            boolean isCommentWriter = commentWriterEmail.equals(email);

            if (!isWriter && !isCommentWriter) return DeleteRecipeCommentResponseDto.noPermission();


            recipeCommentRepository.delete(commentEntity);
            boardEntity.decreaseCommentCount();
            recipeBoardRepository.save(boardEntity);

        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return DeleteRecipeCommentResponseDto.success();
    }
}
