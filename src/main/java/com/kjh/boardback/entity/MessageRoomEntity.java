package com.kjh.boardback.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "messageRoom")
@NoArgsConstructor
public class MessageRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String roomName;

    private String sender;			// 채팅방 생성자

    private String roomId;          // 중고거래 게시글 number + 생성자 nickname

    private String receiver;        // 채팅방 수신자

    private String updateDatetime;

}
