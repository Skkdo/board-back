package com.kjh.boardback.repository.resultSet;

public interface GetTradeBoardResultSet {
    Integer getBoardNumber();
    String getTitle();
    String getContent();
    String getWriteDatetime();
    String getWriterEmail();

    String getTradeLocation();

    String getPrice();
    String getWriterNickname();
    String getWriterProfileImage();

}
