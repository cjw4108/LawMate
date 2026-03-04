package com.lawmate;

import com.lawmate.dto.ChatMessage;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ChatMapper {

    @Insert("""
        INSERT INTO CHAT_MESSAGE
        (MSG_ID, ROOM_ID, SENDER_TYPE, CONTENT, CREATED_AT)
        VALUES
        (CHAT_MSG_SEQ.NEXTVAL, #{roomId}, #{senderType}, #{content}, SYSDATE)
    """)
    void insertMessage(ChatMessage dto);

    @Select("""
        SELECT *
        FROM CHAT_MESSAGE
        WHERE ROOM_ID = #{roomId}
        ORDER BY CREATED_AT
    """)
    List<ChatMessage> getMessages(String roomId);
}