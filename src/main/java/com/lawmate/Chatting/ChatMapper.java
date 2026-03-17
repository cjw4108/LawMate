package com.lawmate.Chatting;

import com.lawmate.dto.ChatMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChatMapper {

    /**
     * 1. 변호사 번호(PK)로 이메일 아이디(@ 앞부분) 추출
     * 설명: DB에 숫자로 저장된 변호사 번호를 실제 계정 ID 문자열로 변환합니다.
     */
    @Select("""
        SELECT SUBSTR(EMAIL, 1, INSTR(EMAIL, '@') - 1) 
        FROM TB_LAWYER 
        WHERE LAWYER_ID = #{lawyerId}
    """)
    String getLawyerIdByNo(@Param("lawyerId") String lawyerId);

    /**
     * 2. 채팅방 생성
     * 수정사항: chatWith(type)를 컬럼에 포함하거나, 쓰지 않는다면 파라미터에서도 제거해야 합니다.
     * 여기서는 에러 방지를 위해 파라미터 개수를 쿼리와 맞췄습니다.
     */
    @Insert("""
        INSERT INTO CHAT_ROOM (ROOM_ID, USER_ID, LAWYER_ID, STATUS, CREATED_AT)
        VALUES (#{roomId}, #{userId}, #{lawyerId}, 'LIVE', SYSDATE)
    """)
    void createChatRoom(@Param("roomId") String roomId,
                        @Param("userId") String userId,
                        @Param("lawyerId") String lawyerId);

    /**
     * 3. 기존 채팅방 존재 여부 확인
     * 수정사항: Service에서 String으로 넘길 때를 대비해 타입을 유연하게 맞췄습니다.
     */
    @Select("""
    SELECT ROOM_ID FROM CHAT_ROOM 
    WHERE USER_ID = #{userId} 
      AND LAWYER_ID = (
          /* 입력받은 값이 숫자(16)든 문자든 상관없이 해당 변호사의 이메일 아이디를 가져옴 */
          SELECT SUBSTR(EMAIL, 1, INSTR(EMAIL, '@') - 1) 
          FROM TB_LAWYER 
          WHERE TO_CHAR(LAWYER_ID) = TO_CHAR(#{lawyerId})
             OR SUBSTR(EMAIL, 1, INSTR(EMAIL, '@') - 1) = TO_CHAR(#{lawyerId})
      )
      AND STATUS IN ('LIVE', 'ONGOING')
      AND ROWNUM = 1
""")
    String findRoom(@Param("userId") String userId, @Param("lawyerId") String lawyerId);

    /**
     * 4. 변호사 대시보드용 상담 목록 조회
     */
    @Select("""
    SELECT 
        r.ROOM_ID as "roomId",
        NVL(u.NAME, '의뢰인') as "userName",
        r.LAWYER_ID as "lawyerId",
        r.STATUS as "status",
        (SELECT TO_CHAR(CONTENT) FROM (
            SELECT CONTENT FROM CHAT_MESSAGE m 
            WHERE m.ROOM_ID = r.ROOM_ID 
            ORDER BY CREATED_AT DESC
        ) WHERE ROWNUM = 1) as "lastMessage"
    FROM CHAT_ROOM r
    LEFT JOIN USERS u ON r.USER_ID = u.USER_ID
    WHERE r.LAWYER_ID = #{lawyerId}
    ORDER BY r.CREATED_AT DESC
    """)
    List<Map<String, Object>> getLawyerChatList(@Param("lawyerId") String lawyerId);

    /**
     * 5. 메시지 저장
     */
    @Insert("""
        INSERT INTO CHAT_MESSAGE (MSG_ID, ROOM_ID, SENDER_ID, SENDER_TYPE, CONTENT, CREATED_AT)
        VALUES (CHAT_MSG_SEQ.NEXTVAL, #{dto.roomId}, #{dto.senderId}, #{dto.senderType}, #{dto.message}, SYSDATE)
    """)
    void insertMessage(@Param("dto") ChatMessage dto);

    /**
     * 6. 대화 내역 조회
     */
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

    /**
     * 7. 상태 변경 (수락/종료)
     */
    @Update("UPDATE CHAT_ROOM SET STATUS = 'ONGOING', UPDATED_AT = SYSDATE WHERE ROOM_ID = #{roomId}")
    int updateRoomToOngoing(@Param("roomId") String roomId);

    @Update("UPDATE CHAT_ROOM SET STATUS = 'CLOSED' WHERE ROOM_ID = #{roomId}")
    void closeChatRoom(@Param("roomId") String roomId);

    /**
     * 8. 상담 중인 개수 확인 (변호사 목록 버튼 제어용)
     */
    @Select("""
    SELECT COUNT(*) FROM CHAT_ROOM 
    WHERE LAWYER_ID = (
        SELECT SUBSTR(EMAIL, 1, INSTR(EMAIL, '@') - 1) 
        FROM TB_LAWYER 
        WHERE TO_CHAR(LAWYER_ID) = TO_CHAR(#{lawyerId})
    )
    AND STATUS IN ('LIVE', 'ONGOING')
""")
    int countActiveConsultations(@Param("lawyerId") String lawyerId);

    @Select("""
    SELECT SUBSTR(EMAIL, 1, INSTR(EMAIL, '@') - 1) 
    FROM TB_LAWYER 
    WHERE TO_CHAR(LAWYER_ID) = TO_CHAR(#{lawyerId})
       OR SUBSTR(EMAIL, 1, INSTR(EMAIL, '@') - 1) = TO_CHAR(#{lawyerId})
""")
    String getLawyerEmailId(@Param("lawyerId") String lawyerId);

    @Update("UPDATE TB_LAWYER SET STATUS = #{status} WHERE EMAIL LIKE #{lawyerId} || '@%'")
    void updateLawyerStatus(@Param("lawyerId") String lawyerId, @Param("status") String status);

    @Update("UPDATE CHAT_ROOM SET STATUS = #{status} WHERE ROOM_ID = #{roomId}")
    int updateRoomStatus(@Param("roomId") String roomId, @Param("status") String status);
}