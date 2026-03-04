<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                        <c:forEach var="chat" items="${chatHistory}">
                            <c:set var="isMe" value="${chat.senderId eq sessionScope.loginUser.userId}" />
                            <div class="mb-3 ${isMe ? 'text-end' : 'text-start'}">
                                <div style="display:inline-block; max-width: 80%;">
                                    <c:if test="${!isMe}">
                                        <div style="font-size: 0.8rem; margin-bottom: 3px; font-weight: bold;">${chat.senderName}</div>
                                    </c:if>
                                    <div class="p-2 rounded shadow-sm" style="background-color:${isMe ? '#007bff' : '#f1f0f0'}; color:${isMe ? '#ffffff' : '#000000'}; border:1px solid #dee2e6;">
                                            ${chat.content}
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
    // 컨트롤러에서 넘어온 데이터
    let socketServer = "${socketServer}";
    let roomId = "${roomId}";
    let userId = "${userId}";
    let chatWith = "${chatWith}"; // "LAWYER"가 넘어옴
    let wsocket = null;

    $(document).ready(function(){
        connect();
        $("#sndBtn").click(sendMsg);
        $("#msg").keyup(e => { if(e.keyCode == 13) sendMsg(); });
    });

    function connect(){
        // roomId를 쿼리스트링으로 전달하여 핸들러의 afterConnectionEstablished에서 인식하게 함
        wsocket = new WebSocket(socketServer + "?roomId=" + roomId);
        wsocket.onmessage = evt => receiveMsg(evt.data);
    }

    function sendMsg(){
        var msg = $("#msg").val();
        if(msg.trim().length == 0) return;

        // 화면에 내 메시지 즉시 표시 (기존 로직 활용)
        renderBubble({ senderType: "USER", message: msg, senderName: "나" }, true);

        // 서버로 전송 시 chatWith 정보를 포함
        var sendData = {
            roomId : roomId,
            userId : userId,
            message : msg,
            chatWith : chatWith // 중요: 핸들러가 AI인지 변호사인지 판단하는 기준
        };

        wsocket.send(JSON.stringify(sendData));
        $("#msg").val("");
    }

    function receiveMsg(data){
        console.log("수신 데이터:", data);
        var msgObj = JSON.parse(data);

        // [해결 포인트]
        // 내가 보낸 메시지(USER)는 이미 sendMsg()에서 화면에 그렸으므로,
        // 서버에서 돌아온 내 메시지는 무시하고 상대방(AI 또는 LAWYER)의 메시지만 화면에 출력합니다.
        if(msgObj.senderType === "USER") {
            return; // 내가 보낸 거라면 함수 종료 (두 번 그려지는 것 방지)
        }

        // 아래는 기존의 메시지 그리는 로직 유지...
        renderBubble(msgObj, false);
    }

    function renderBubble(msgObj, isMe) {
        var align = isMe ? "text-end" : "text-start";
        var bgColor = isMe ? "#007bff" : "#f1f0f0";
        var textColor = isMe ? "#ffffff" : "#000000";
        var nameTag = isMe ? "" : '<div style="font-size: 0.8rem; margin-bottom: 3px; font-weight: bold;">' + msgObj.senderName + '</div>';

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