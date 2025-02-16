package board.domain.board.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.kjh.boardback.domain.board.entity.Image;
import com.kjh.boardback.domain.board.repository.ImageRepository;
import com.kjh.boardback.domain.board.service.BoardImageService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class BoardImageServiceTest {

    @InjectMocks
    private BoardImageService imageService;

    @Mock
    private ImageRepository imageRepository;

    @Test
    @DisplayName("boardNumber 로 이미지 리스트 조회")
    void findByBoardNumber() {
        int boardNumber = 0;
        List<Image> imageList = new ArrayList<>();
        doReturn(imageList).when(imageRepository).findByBoard_BoardNumber(boardNumber);

        imageService.findByBoardNumber(boardNumber);

        verify(imageRepository,times(1)).findByBoard_BoardNumber(boardNumber);
    }

    @Test
    @DisplayName("이미지 리스트 저장")
    void saveAll() {
        List<Image> imageList = new ArrayList<>();
        doReturn(imageList).when(imageRepository).saveAll(imageList);

        imageService.saveAll(imageList);

        verify(imageRepository,times(1)).saveAll(imageList);
    }

    @Test
    @DisplayName("boardNumber 로 이미지 삭제")
    void deleteByBoardNumber() {
        int boardNumber = 0;
        doNothing().when(imageRepository).deleteByBoard_BoardNumber(boardNumber);

        imageService.deleteByBoardNumber(boardNumber);

        verify(imageRepository,times(1)).deleteByBoard_BoardNumber(boardNumber);
    }
}
