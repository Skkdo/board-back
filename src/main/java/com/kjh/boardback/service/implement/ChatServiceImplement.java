package com.kjh.boardback.service.implement;

import com.kjh.boardback.dto.response.chat.EnterMessageRoomResponseDto;
import com.kjh.boardback.service.ChatService;
import org.springframework.http.ResponseEntity;


public class ChatServiceImplement implements ChatService {
    @Override
    public ResponseEntity<? super EnterMessageRoomResponseDto> enterMessageRoom(String email) {



        try{




        }catch (Exception exception){
            exception.printStackTrace();
            return EnterMessageRoomResponseDto.databaseError();
        }
        return EnterMessageRoomResponseDto.success();
    }
}
