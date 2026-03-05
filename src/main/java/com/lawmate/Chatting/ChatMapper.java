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
    // 1. 방이 존재하는지 확인 (이 부분이 없어서 에러가 난 것입니다)
    @Select("SELECT COUNT(*) FROM CHAT_ROOM WHERE ROOM_ID = #{roomId}")
    int checkRoomExists(@Param("roomId") String roomId);

    @Select("""
        SELECT ROOM_ID 
        FROM CHAT_ROOM 
        WHERE USER_ID = #{userId} AND LAWYER_ID = #{lawyerId}
        AND ROWNUM = 1
    """)
    String findRoomIdByParticipants(@Param("userId") String userId, @Param("lawyerId") String lawyerId);

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
    (
        MSG_ID, 
        ROOM_ID, 
        SENDER_ID,   -- 👈 이 부분이 빠져있어서 "값의 수가 너무 많다"고 에러가 난 겁니다!
        SENDER_TYPE, 
        CONTENT, 
        CREATED_AT
    )
    VALUES
    (
        CHAT_MSG_SEQ.NEXTVAL,
        #{roomId},
        #{senderId}, -- 값은 이미 6개를 보내고 계셨으므로, 위 컬럼명만 추가하면 짝이 맞습니다.
        #{senderType},
        #{message},
        SYSDATE
    )
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
    SELECT 
        u.NAME as "senderName",
        msg.SENDER_TYPE as "senderType", 
        msg.CONTENT as "message", 
        msg.CREATED_AT as "createdAt",
        msg.SENDER_ID as "senderId"
    FROM CHAT_MESSAGE msg
    LEFT JOIN USERS u ON msg.SENDER_ID = u.USER_ID
    WHERE msg.ROOM_ID = #{roomId} 
    ORDER BY msg.CREATED_AT ASC
""")
    List<ChatMessage> selectChatHistory(@Param("roomId") String roomId);


}