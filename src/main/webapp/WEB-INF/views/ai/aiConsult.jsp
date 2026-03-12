<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
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
            max-width: 250px; /* 너무 크지 않게 조절 */
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
                                <div id="chatMessageArea">
                                </div>
                            </div>
                        </div>

                        <div class="card-footer bg-white">
                            <div id="filePreview" style="display:none; margin-bottom: 5px; font-size: 0.85rem; color: #555;">
                                <span id="fileNameDisplay"></span>
                                <button type="button" class="btn-close" style="font-size: 0.6rem;" onclick="cancelFile()"></button>
                            </div>

                            <div class="input-group">
                                <input type="file" id="fileInput" style="display:none;" onchange="handleFileSelect(this)">
                                <button class="btn btn-outline-secondary" type="button" onclick="$('#fileInput').click();">
                                    📎
                                </button>

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
    let userId = "${userId}";
    let userType = "${userType}";
    let wsocket = null;
    let selectedFile = null;

    // 파일 선택 핸들러
    function handleFileSelect(input) {
        if (input.files && input.files[0]) {
            selectedFile = input.files[0];
            $("#fileNameDisplay").text("업로드 준비: " + selectedFile.name);
            $("#filePreview").show();
        }
    }

    // 파일 선택 취소
    function cancelFile() {
        selectedFile = null;
        $("#fileInput").val("");
        $("#filePreview").hide();
    }

    $(document).ready(function(){
        connect();

        setTimeout(function() {
            var chatArea = document.getElementById("chatArea");
            if(chatArea) chatArea.scrollTop = chatArea.scrollHeight;
        }, 100);

        $("#sndBtn").click(function(){
            sendMsg();
        });

        $("#msg").keyup(function(e){
            if(e.keyCode == 13) sendMsg();
        });
    });

    function connect(){
        wsocket = new WebSocket(socketServer + "?roomId=" + roomId);
        wsocket.onmessage = function(evt){
            receiveMsg(evt.data);
        };
    }

    async function sendMsg() {
        var msg = $("#msg").val();
        if (msg.trim().length == 0 && !selectedFile) return;

        // 🚀 수정: 고정 로딩 영역을 보여주는 대신 버튼을 잠시 비활성화
        if (selectedFile) {
            $("#sndBtn").prop("disabled", true); // 분석 중 클릭 방지
        }

        let fileInfo = null;

        // 1. 파일이 있으면 먼저 업로드 (여기서는 저장만 하고 가상 경로만 받아옴)
        if (selectedFile) {
            let formData = new FormData();
            formData.append("file", selectedFile);
            formData.append("roomId", roomId);

            try {
                const response = await fetch("/chat/upload", {
                    method: "POST",
                    body: formData
                });
                if (response.ok) {
                    fileInfo = await response.json();
                    // fileInfo에는 이제 { fileName, filePath }만 들어있음 (분석결과 X)
                }
            } catch (e) {
                alert("파일 업로드 실패");
                return;
            }
        }

        // 2. 내 화면에 즉시 표시 (분석 결과 없이 이미지만!)
        var displayMsg = msg.replace(/\n/g, "<br>");

        if (fileInfo) {
            var ext = fileInfo.fileName.split('.').pop().toLowerCase();

            if (['jpg', 'jpeg', 'png', 'gif', 'webp'].includes(ext)) {
                displayMsg += '<div class="mt-2"><img src="' + fileInfo.filePath + '" class="chat-img"></div>';
            } else if (ext === 'pdf') {
                displayMsg += '<div class="mt-2">📄 PDF 문서 분석 요청 중...</div>';
            } else if (ext === 'txt') {
                displayMsg += '<div class="mt-2">📑 텍스트 파일 분석 요청 중...</div>';
            } else {
                displayMsg += '<div class="mt-2">📁 파일이 첨부되었습니다.</div>';
            }

            displayMsg += '<a href="' + fileInfo.filePath + '" class="file-link" download="' + fileInfo.fileName + '">💾 ' + fileInfo.fileName + '</a>';
        }

        renderMyBubble(displayMsg, fileInfo != null); // 내 말풍선 그리기 함수(별도 분리 권장)

        // 3. 서버(Websocket)로 전송
        // 여기서 AI가 상대라면, 서버(Java)가 이 데이터를 보고 AI에게 분석을 시킵니다.
        var sendData = {
            roomId: roomId,
            userId: userId,
            senderType: userType,
            senderName: "사용자",
            message: msg,
            chatWith: "AI", // 상대가 AI임을 명시
            fileName: fileInfo ? fileInfo.fileName : null,
            filePath: fileInfo ? fileInfo.filePath : null
        };

        wsocket.send(JSON.stringify(sendData));
        $("#msg").val("");
        cancelFile();
    }

    function renderMyBubble(displayMsg, hasFile) {
        // 내가 보낸 메시지 렌더링
        var html = '<div class="me mb-3 text-end">';
        html += '  <div class="p-2 rounded shadow-sm d-inline-block" style="background-color: #DCF8C6; max-width: 80%; text-align: left;">';
        html +=      displayMsg;
        html += '  </div>';
        html += '</div>';

        // 🚀 파일이 있으면 그 바로 아래에 'AI 분석 중' 로딩 박스를 추가
        if (hasFile) {
            html += '<div id="ai-loading-box" class="mb-3 text-start">';
            html += '  <div style="display:inline-block; max-width: 80%;">';
            html += '    <div style="font-size: 0.8rem; margin-bottom: 3px; font-weight: bold; color: #555;">AI상담사</div>';
            html += '    <div class="p-2 rounded shadow-sm" style="background-color: #f1f0f0;">';
            html += '      <span class="spinner-border spinner-border-sm" role="status"></span>';
            html += '      <span class="ms-2" style="font-size: 0.9rem;">파일 내용을 분석하고 있습니다...</span>';
            html += '    </div>';
            html += '  </div>';
            html += '</div>';
        }

        $("#chatMessageArea").append(html);

        // 스크롤 아래로 내리기
        var chatArea = document.getElementById("chatArea");
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    function receiveMsg(data) {
        var msgObj = JSON.parse(data);

        if (msgObj.senderType === "AI") {
            $("#ai-loading-box").remove(); // 동적 로딩 박스 제거
            $("#sndBtn").prop("disabled", false); // 버튼 다시 활성화
        }

        // 중복 방지
        if (msgObj.senderType === "USER") return;

        var alignClass = "text-start";
        var bgColor = "#f1f0f0";
        var formattedMsg = msgObj.message ? msgObj.message.replace(/\n/g, "<br>") : "";

        // [핵심] 이미지 처리 로직 추가
        if (msgObj.filePath) {
            var ext = msgObj.fileName.split('.').pop().toLowerCase();
            // 이미지 확장자인 경우 화면에 <img> 태그 추가
            if (['jpg', 'jpeg', 'png', 'gif', 'webp'].includes(ext)) {
                formattedMsg += '<div class="mt-2"><img src="' + msgObj.filePath + '" style="max-width:100%; border-radius:10px; border:1px solid #ddd;"></div>';
            }
            // 다운로드 링크도 함께 표시
            formattedMsg += '<a href="' + msgObj.filePath + '" class="file-link" download="' + msgObj.fileName + '">💾 ' + msgObj.fileName + ' (다운로드)</a>';
        }

        var bubble =
            '<div class="mb-3 ' + alignClass + '">' +
            '<div style="display:inline-block; max-width: 80%; text-align: left;">' +
            '<div style="font-size: 0.8rem; margin-bottom: 3px; font-weight: bold; color: #555;">' + msgObj.senderName + '</div>' +
            '<div class="p-2 rounded shadow-sm" style="background-color: ' + bgColor + '; word-break: break-all;">' +
            formattedMsg +
            '</div>' +
            '</div>' +
            '</div>';

        $("#chatMessageArea").append(bubble);

        // 스크롤 하단으로
        var chatArea = document.getElementById("chatArea");
        if(chatArea) chatArea.scrollTop = chatArea.scrollHeight;
    }
</script>
</body>
</html>