<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>

    <link rel="stylesheet" href="/css/custom.css">
    <style>
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
        .file-link {
            display: block;
            margin-top: 8px;
            padding: 8px;
            background: #fff;
            border: 1px solid #ddd;
            border-radius: 5px;
            text-decoration: none;
            color: #333;
            font-size: 0.9rem;
        }
        .file-link:hover { background: #f1f1f1; }

        .chat-img {
            max-width: 250px;
            height: auto;
            border-radius: 10px;
            margin-bottom: 5px;
            border: 1px solid #ddd;
        }
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
                                <c:if test="${empty chatHistory}">
                                    <div style="text-align:center; color:gray; font-size:0.8rem;">기존 대화 내역이 없습니다. (방ID: ${roomId})</div>
                                </c:if>

                                <c:forEach var="h" items="${chatHistory}">
                                    <c:set var="isUser" value="${h.senderType == 'USER'}" />
                                    <div class="mb-3 ${isUser ? 'text-end' : 'text-start'}">
                                        <div style="display:inline-block; max-width: 80%; text-align: left;">
                                            <c:if test="${!isUser}">
                                                <div style="font-size: 0.8rem; margin-bottom: 3px; font-weight: bold; color: #555;">
                                                        ${h.senderType == 'AI' ? 'AI상담사' : '변호사'}
                                                </div>
                                            </c:if>
                                            <div class="p-2 rounded shadow-sm"
                                                 style="background-color: ${isUser ? '#007bff' : '#f1f0f0'};
                                                         color: ${isUser ? '#ffffff' : '#000000'};
                                                         border: ${isUser ? 'none' : '1px solid #dee2e6'};
                                                         word-break: break-all;
                                                         white-space: pre-wrap;">${h.message}</div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="card-footer bg-white">
                            <div id="filePreview" style="display:none; margin-bottom: 5px; font-size: 0.85rem; color: #555;">
                                <span id="fileNameDisplay"></span>
                                <button type="button" class="btn-close" style="font-size: 0.6rem;" onclick="cancelFile()"></button>
                            </div>

                            <div class="input-group">
                                <input type="file" id="fileInput" style="display:none;" onchange="handleFileSelect(this)">
                                <button class="btn btn-outline-secondary" type="button" onclick="$('#fileInput').click();">📎</button>
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
    // 1. 설정값 준비
    var socketServer = window.location.origin + "/ws-stomp";
    var socket = new SockJS(socketServer);
    var roomId = "${roomId}";
    var userId = "${userId}";
    var userType = "${userType}";
    var stompClient = null;
    var selectedFile = null;

    // 문서 로드 시 실행
    $(document).ready(function(){
        connect(); // 연결 시작

        // 스크롤 하단 이동
        setTimeout(scrollToBottom, 200);

        // 이벤트 바인딩
        $("#sndBtn").off("click").on("click", function(){ sendMsg(); });
        $("#msg").off("keyup").on("keyup", function(e){ if(e.keyCode == 13) sendMsg(); });
    });

    // 🚀 연결 함수 (딱 하나만 남겨두었습니다)
    function connect() {
        console.log("🔗 웹소켓 연결 시도 중... : " + socketServer);
        var socket = new SockJS(socketServer);
        stompClient = Stomp.over(socket);

        // 디버그 로그가 너무 지저분하면 주석 해제하세요
        // stompClient.debug = null;

        stompClient.connect({}, function (frame) {
            console.log('✅ STOMP 연결 성공! (실시간 수신 가능)');

            // 🚀 실시간 메시지 수신 (구독)
            stompClient.subscribe('/sub/chat/room/' + roomId, function (chat) {
                console.log("📩 새 메시지 도착!");
                var msgObj = JSON.parse(chat.body); // chat.body가 실제 데이터입니다.

                // 화면에 말풍선 추가
                renderBubble(msgObj);
                // 자동 스크롤
                scrollToBottom();
            });
        }, function(error) {
            console.error('❌ 연결 끊김: ', error);
            // 5초 후 자동으로 재연결 시도
            setTimeout(connect, 5000);
        });
    }

    // 메시지 전송
    async function sendMsg() {
        var msg = $("#msg").val();
        if (msg.trim().length == 0 && !selectedFile) return;

        let fileInfo = null;

        // 파일 업로드 처리
        if (selectedFile) {
            $("#sndBtn").prop("disabled", true);
            let formData = new FormData();
            formData.append("file", selectedFile);
            formData.append("roomId", roomId);

            try {
                const response = await fetch("/chat/upload", { method: "POST", body: formData });
                if (response.ok) fileInfo = await response.json();
            } catch (e) {
                alert("파일 업로드 실패");
                $("#sndBtn").prop("disabled", false);
                return;
            }
        }

        // 서버 전송 데이터 구성
        var sendData = {
            roomId: roomId,
            senderId: userId,
            senderType: userType,
            senderName: (userType === 'USER' ? '나' : '변호사'),
            message: msg,
            fileName: fileInfo ? fileInfo.fileName : null,
            filePath: fileInfo ? fileInfo.filePath : null
        };

        // 🚀 서버로 발송 (발송하면 서버를 거쳐 위 subscribe로 다시 내려옵니다)
        if(stompClient && stompClient.connected) {
            stompClient.send("/pub/chat/message", {}, JSON.stringify(sendData));
            $("#msg").val("");
            cancelFile();
        } else {
            alert("연결이 원활하지 않습니다. 잠시 후 다시 시도해주세요.");
        }

        $("#sndBtn").prop("disabled", false);
    }

    // 말풍선 그리기
    function renderBubble(msgObj) {
        // 내 메시지인지 확인 (문자열/숫자 타입 다를 수 있어 == 사용)
        var isMe = (msgObj.senderId == userId);
        var isAI = (msgObj.senderType === 'AI' || msgObj.senderId === 'GEMINI_AI');

        var alignClass = isMe ? "text-end" : "text-start";
        var bgColor = isMe ? "#007bff" : (isAI ? "#e9ecef" : "#f1f0f0");
        var textColor = isMe ? "#ffffff" : "#000000";
        var senderTitle = isAI ? "AI 법률상담사" : (msgObj.senderName || "상대방");

        var formattedMsg = msgObj.message ? msgObj.message.replace(/\n/g, "<br>") : "";

        // 이미지 처리
        if (msgObj.filePath) {
            var ext = msgObj.fileName.split('.').pop().toLowerCase();
            if (['jpg', 'jpeg', 'png', 'gif', 'webp'].includes(ext)) {
                formattedMsg += '<div class="mt-2"><img src="' + msgObj.filePath + '" class="chat-img"></div>';
            }
            formattedMsg += '<a href="' + msgObj.filePath + '" class="file-link" download="' + msgObj.fileName + '">💾 ' + msgObj.fileName + '</a>';
        }

        var bubble =
            '<div class="mb-3 ' + alignClass + '">' +
            '<div style="display:inline-block; max-width: 80%; text-align: left;">' +
            (!isMe ? '<div style="font-size: 0.8rem; margin-bottom: 3px; font-weight: bold; color: #555;">' + senderTitle + '</div>' : '') +
            '<div class="p-2 rounded shadow-sm" style="background-color: ' + bgColor + '; color: ' + textColor + '; word-break: break-all; white-space: pre-wrap;">' +
            formattedMsg +
            '</div>' +
            '</div>' +
            '</div>';

        $("#chatMessageArea").append(bubble);
    }

    function scrollToBottom() {
        var chatArea = document.getElementById("chatArea");
        if (chatArea) chatArea.scrollTop = chatArea.scrollHeight;
    }

    function handleFileSelect(input) {
        if (input.files && input.files[0]) {
            selectedFile = input.files[0];
            $("#fileNameDisplay").text("업로드 준비: " + selectedFile.name);
            $("#filePreview").show();
        }
    }

    function cancelFile() {
        selectedFile = null;
        $("#fileInput").val("");
        $("#filePreview").hide();
    }
</script>
</body>
</html>