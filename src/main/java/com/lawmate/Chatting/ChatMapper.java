package com.lawmate.Chatting;

import com.lawmate.dto.ChatMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatMapper {
    // 1. 방 존재 여부 확인
    @Select("SELECT COUNT(*) FROM CHAT_ROOM WHERE ROOM_ID = #{roomId}")
    int checkRoomExists(String roomId);

    // 2. 방 자동 생성
    @Insert("""
        INSERT INTO CHAT_ROOM (ROOM_ID, USER_ID, LAWYER_ID, STATUS, CREATED_AT)
        VALUES (#{roomId}, #{userId}, #{lawyerId}, 'LIVE', SYSDATE)
    """)
    void insertChatRoom(@Param("roomId") String roomId,
                        @Param("userId") String userId,
                        @Param("lawyerId") String lawyerId);
    @Insert("""
    INSERT INTO CHAT_MESSAGE
    (MSG_ID, ROOM_ID, SENDER_TYPE, CONTENT, CREATED_AT)
    VALUES
    (CHAT_MSG_SEQ.NEXTVAL, 
     #{roomId, jdbcType=VARCHAR}, 
     #{senderType, jdbcType=VARCHAR}, 
     #{message, jdbcType=VARCHAR}, 
     SYSDATE)
""")
    void insertMessage(ChatMessage dto);

    @Select("""
        SELECT *
        FROM CHAT_MESSAGE
        WHERE ROOM_ID = #{roomId}
        ORDER BY CREATED_AT
    """)
    List<ChatMessage> getMessages(String roomId);

    // 조회
    @Select("""
        SELECT ROOM_ID as roomId, SENDER_TYPE as senderType, CONTENT as message, CREATED_AT as createdAt
        FROM CHAT_MESSAGE 
        WHERE ROOM_ID = #{roomId} 
        ORDER BY CREATED_AT ASC
    """)
    List<ChatMessage> selectChatHistory(String roomId);


}