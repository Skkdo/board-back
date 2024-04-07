package com.kjh.boardback.service;

import com.kjh.boardback.dto.response.chat.EnterMessageRoomResponseDto;
import org.springframework.http.ResponseEntity;


public interface ChatService {

    ResponseEntity<? super EnterMessageRoomResponseDto> enterMessageRoom(String email);

}
