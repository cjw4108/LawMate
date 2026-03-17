<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.1/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <link rel="stylesheet" href="/css/custom.css">
    <style>
        /* 채팅창 스타일 보강 */
        #chatArea {
            height: 500px;
            overflow-y: auto;
            background-color: #f8f9fa;
            border: 1px solid #ddd;
            padding: 15px;
        }
        .msg-bubble {
            max-width: 80%;
            display: inline-block;
            word-break: break-all;
        }
        .ai-msg { background-color: #ffffff; color: #333; border: 1px solid #dee2e6; }
        .user-msg { background-color: #007bff; color: #ffffff !important; }
    </style>
</head>

<body class="index-page">

<main class="main" style="padding-top: 120px;">
    <section class="py-5 bg-light">
        <div class="container">
            <h2 class="fw-bold mb-4 text-center">변호사 1:1 법률 상담</h2>
            <div class="card shadow-sm">
                <div class="card-header bg-white py-3">
                    <span class="badge bg-success">실시간 연결됨</span>
                    <strong class="ms-2">${lawyerName} 변호사님과의 대화</strong>
                </div>
                <div class="card-body" id="chatArea">
                    <div id="chatMessageArea">
                        <%-- 기존 대화 내역 출력 시작 --%>
                        <c:forEach var="h" items="${chatHistory}">
                            <c:set var="isMe" value="${h.senderId eq userId}" />
                            <div class="mb-3 ${isMe ? 'text-end' : 'text-start'}">
                                <div style="display:inline-block; max-width: 80%; text-align: left;">
                                    <c:if test="${!isMe}">
                                        <div style="font-size: 0.8rem; margin-bottom: 3px; font-weight: bold; color: #555;">
                                                ${h.senderName}
                                        </div>
                                    </c:if>
                                    <div class="p-2 rounded shadow-sm"
                                         style="background-color: ${isMe ? '#007bff' : '#f1f0f0'};
                                                 color: ${isMe ? '#ffffff' : '#000000'};">
                                            ${h.message}
                                    </div>
                                </div>
                            </div>
                        </c:forEach>

                        <div class="text-center my-3">
                            <small class="bg-secondary text-white px-3 py-1 rounded-pill">새로운 상담이 시작되었습니다.</small>
                        </div>
                    </div>
                </div>
                <div class="card-footer bg-white">
                    <div class="input-group">
                        <input type="text" id="msg" class="form-control" placeholder="변호사님께 메시지를 입력하세요">
                        <button class="btn btn-primary" id="sndBtn">전송</button>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<script>
    let roomId = "${roomId}";
    let userId = "${userId}" || "${sessionScope.loginUser.userId}";
    let userType = "${userType}"; // 'USER' 또는 'LAWYER'
    let stompClient = null;

    $(document).ready(function(){
        connect(); // 실행 시 연결
        $("#sndBtn").click(sendMsg);
        $("#msg").keyup(e => { if(e.keyCode == 13) sendMsg(); });
    });

    function connect() {
        // 1. Config에서 설정한 엔드포인트(/ws-stomp)로 연결
        var socket = new SockJS('/ws-stomp');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            console.log('연결 성공: ' + frame);

            // 2. 구독(Subscribe): RedisSubscriber가 쏴주는 경로를 감시합니다.
            stompClient.subscribe('/sub/chat/room/' + roomId, function (response) {
                var msgObj = JSON.parse(response.body);
                receiveMsg(msgObj);
            });
        }, function(error) {
            console.error('STOMP 연결 에러: ' + error);
        });
    }

    function sendMsg() {
        var msg = $("#msg").val();
        if(msg.trim().length == 0 || !stompClient) return;

        // 3. 발행(Publish): StompChatController의 @MessageMapping으로 전송
        var sendData = {
            roomId: roomId,
            senderId: userId,
            message: msg,
            senderType: userType
        };

        stompClient.send("/pub/chat/message", {}, JSON.stringify(sendData));
        $("#msg").val("");
    }

    function receiveMsg(msgObj) {
        // 내가 보낸 메시지인지 확인 (본인 아이디와 비교)
        let isMe = (msgObj.senderId === userId);

        // 화면에 그리기
        renderBubble(msgObj, isMe);
    }

    function renderBubble(msgObj, isMe) {
        var align = isMe ? "text-end" : "text-start";
        var bgColor = isMe ? "#007bff" : "#f1f0f0";
        var textColor = isMe ? "#ffffff" : "#000000";

        // 상대방 이름 표시 (내가 아닐 때만)
        var senderName = msgObj.senderName || (isMe ? "" : "상대방");
        var nameTag = isMe ? "" : '<div style="font-size: 0.8rem; margin-bottom: 3px; font-weight: bold;">' + senderName + '</div>';

        var bubble =
            '<div class="mb-3 ' + align + '">' +
            '<div style="display:inline-block; max-width: 80%;">' +
            nameTag +
            '<div class="p-2 rounded shadow-sm" style="background-color:'+bgColor+'; color:'+textColor+'; border:1px solid #dee2e6;">' +
            msgObj.message.replace(/\n/g, "<br>") +
            '</div>' +
            '</div>' +
            '</div>';

        $("#chatMessageArea").append(bubble);
        $("#chatArea").scrollTop($("#chatArea")[0].scrollHeight);
    }
</script>

</body>
</html>