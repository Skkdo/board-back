package com.kjh.boardback.domain.board.service;

import com.kjh.boardback.domain.board.entity.Image;
import com.kjh.boardback.domain.board.repository.ImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardImageService {

    private final ImageRepository imageRepository;

    public List<Image> findByBoardNumber(Integer boardNumber) {
        return imageRepository.findByBoard_BoardNumber(boardNumber);
    }

    public void saveAll(List<Image> imageList) {
        imageRepository.saveAll(imageList);
    }

    public void deleteByBoardNumber(Integer boardNumber) {
        imageRepository.deleteByBoard_BoardNumber(boardNumber);
    }
}
