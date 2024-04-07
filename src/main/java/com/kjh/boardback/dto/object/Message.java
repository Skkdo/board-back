package com.kjh.boardback.dto.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Message {

    private String sender;
    private String message;
    private String roomId;
    private String writeDatetime;



}
