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
            <h2 class="fw-bold mb-4 text-center">AI 법률 상담</h2>

            <c:choose>
                <c:when test="${not empty sessionScope.loginUser}">
                    <div class="card shadow-sm">
                        <div class="card-body" id="chatArea">
                            <div id="chatMessageArea">
                                <div class="mb-3 text-start">
                                    <div style="display:inline-block; max-width: 80%; text-align: left;">
                                        <div style="font-size: 0.8rem; margin-bottom: 3px; font-weight: bold; color: #555;">AI상담사</div>
                                        <div class="p-2 rounded shadow-sm" style="background-color: #f1f0f0; color: #000000; border: 1px solid #dee2e6;">
                                            <strong>${sessionScope.loginUser.name}</strong>님 반갑습니다! <br>
                                            궁금하신 법률 사항이 있다면 무엇이든 물어보세요.
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="card-footer bg-white">
                            <div class="input-group">
                                <input type="text" id="msg" class="form-control" placeholder="궁금한 법률 질문을 입력하세요">
                                <button class="btn btn-primary" id="sndBtn">전송</button>
                            </div>
                        </div>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="card shadow-sm p-5 text-center">
                        <h4 class="mb-3">로그인이 필요한 서비스입니다.</h4>
                        <p>AI 법률 상담을 이용하시려면 먼저 로그인을 해주세요.</p>
                        <a href="/login" class="btn btn-primary w-25 mx-auto">로그인하러 가기</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </section>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script>
    let socketServer = "${socketServer}";
    let roomId = "${roomId}";
    let userType = "${userType}";
    let wsocket = null;

    $(document).ready(function(){
        connect();

        $("#sndBtn").click(function(){
            sendMsg();
        });

        $("#msg").keyup(function(e){
            if(e.keyCode == 13){
                sendMsg();
            }
        });
    });

    function connect(){
        wsocket = new WebSocket(socketServer + "?roomId=" + roomId);
        wsocket.onmessage = function(evt){
            receiveMsg(evt.data);
        };
    }

    function sendMsg(){
        var msg = $("#msg").val();
        if(msg.trim().length == 0) return;

        // 1. [추가] 내가 보낸 메시지를 화면에 즉시 표시
        // 서버에서 다시 보내주지 않더라도 내가 보낸 것이 보이게 합니다.
        var userBubble =
            '<div class="mb-3 text-end">' +
            '<div style="display:inline-block; max-width: 80%; text-align: left;">' +
            '<div class="p-2 rounded shadow-sm" style="background-color: #007bff; color: #ffffff; word-break: break-all;">' +
            msg.replace(/\n/g, "<br>") +
            '</div>' +
            '</div>' +
            '</div>';

        $("#chatMessageArea").append(userBubble);

        // 스크롤 하단 이동
        var chatArea = document.getElementById("chatArea");
        if(chatArea) chatArea.scrollTop = chatArea.scrollHeight;

        // 2. 서버로 전송
        var sendData = {
            roomId : roomId,
            senderType : userType,
            senderName : "사용자",
            message : msg,
            chatWith : "AI"
        };

        wsocket.send(JSON.stringify(sendData));
        $("#msg").val(""); // 입력창 비우기
    }

    function receiveMsg(data){
        console.log("수신 데이터:", data);
        var msgObj = JSON.parse(data);

        // [중복 방지] 내가 보낸 메시지가 서버를 타고 다시 돌아온 경우, 화면에 또 그리지 않음
        if (msgObj.senderType === "USER") {
            return;
        }



        // 1. 사용자 구분 (보내주신 로그에 따라 msgObj.senderType이 "USER"인 경우)
        // 만약 내 메시지가 오른쪽으로 안 가면 콘솔의 senderType 대소문자를 확인해보세요.
        var isUser = (msgObj.senderType === "USER");

        // 2. 배치 및 스타일 설정
        var alignClass = isUser ? "text-end" : "text-start";
        var bgColor = isUser ? "#007bff" : "#f1f0f0";
        var textColor = isUser ? "#ffffff" : "#000000";
        var borderStyle = isUser ? "none" : "1px solid #dee2e6";

        // 3. 줄바꿈 처리
        var formattedMsg = msgObj.message.replace(/\n/g, "<br>");

        // 4. 이름 표시 (AI일 때만 표시)
        var nameTag = "";
        if (!isUser) {
            nameTag = '<div style="font-size: 0.8rem; margin-bottom: 3px; font-weight: bold; color: #555;">' + msgObj.senderName + '</div>';
        }

        // 5. 말풍선 HTML 조립 (JSP 충돌 방지를 위해 + 연산자 사용)
        var bubble =
            '<div class="mb-3 ' + alignClass + '">' +
            '<div style="display:inline-block; max-width: 80%; text-align: left;">' +
            nameTag +
            '<div class="p-2 rounded shadow-sm" style="background-color: ' + bgColor + '; color: ' + textColor + '; border: ' + borderStyle + '; word-break: break-all;">' +
            formattedMsg +
            '</div>' +
            '</div>' +
            '</div>';

        $("#chatMessageArea").append(bubble);

        // 6. 스크롤 하단 이동
        var chatArea = document.getElementById("chatArea");
        if(chatArea) {
            chatArea.scrollTop = chatArea.scrollHeight;
        }
    }
</script>

</body>
</html>