package com.lawmate.Chatting;

import com.lawmate.dto.ChatMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatMapper {

    // 1. 방 존재 여부 확인 (방 ID로 직접 확인)
    @Select("SELECT COUNT(*) FROM CHAT_ROOM WHERE ROOM_ID = #{roomId}")
    int checkRoomExists(@Param("roomId") String roomId);

    // 2. 참여자 기반 기존 방 ID 조회 (AI와의 상담도 포함)
    // AI 상담의 경우 lawyerId 파라미터에 'GEMINI_AI'가 전달됩니다.
    @Select("""
        SELECT ROOM_ID 
        FROM CHAT_ROOM 
        WHERE USER_ID = #{userId} AND LAWYER_ID = #{lawyerId}
          AND STATUS = 'LIVE'
          AND ROWNUM = 1
    """)
    String findRoomIdByParticipants(@Param("userId") String userId, @Param("lawyerId") String lawyerId);

    // 3. 새로운 채팅방 생성
    @Insert("""
        INSERT INTO CHAT_ROOM (ROOM_ID, USER_ID, LAWYER_ID, STATUS, CREATED_AT)
        VALUES (#{roomId}, #{userId}, #{lawyerId}, 'LIVE', SYSDATE)
    """)
    void insertChatRoom(@Param("roomId") String roomId,
                        @Param("userId") String userId,
                        @Param("lawyerId") String lawyerId);

    // 4. 메시지 저장 (senderId 포함)
    @Insert("""
    INSERT INTO CHAT_MESSAGE
    (
        MSG_ID, 
        ROOM_ID, 
        SENDER_ID, 
        SENDER_TYPE, 
        CONTENT, 
        CREATED_AT
    )
    VALUES
    (
        CHAT_MSG_SEQ.NEXTVAL,
        #{dto.roomId},      -- 👈 dto. 을 붙여서 명확하게 지정
        #{dto.senderId}, 
        #{dto.senderType},
        #{dto.message},     -- 👈 DTO 필드명이 message이므로 message로 매칭
        SYSDATE
    )
""")
    void insertMessage(@Param("dto") ChatMessage dto);

    // 5. 대화 내역 조회 (이름 조인 포함)
    @Select("""
    SELECT 
        NVL(u.NAME, 'AI') as "senderName",
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