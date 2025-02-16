package com.kjh.boardback.domain.trade_board.service;

import com.kjh.boardback.domain.trade_board.entity.TradeImage;
import com.kjh.boardback.domain.trade_board.repository.TradeImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TradeImageService {

    private final TradeImageRepository imageRepository;

    public List<TradeImage> findByBoardNumber(Integer boardNumber) {
        return imageRepository.findByBoard_BoardNumber(boardNumber);
    }

    public void saveAll(List<TradeImage> imageList) {
        imageRepository.saveAll(imageList);
    }

    public void deleteByBoardNumber(Integer boardNumber) {
        imageRepository.deleteByBoard_BoardNumber(boardNumber);
    }
}
