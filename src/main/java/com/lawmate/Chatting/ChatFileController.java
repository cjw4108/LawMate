package com.lawmate.Chatting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.*;

@RestController
public class ChatFileController {

    @Autowired
    private A03_ChattingService chattingService;

    @PostMapping("/chat/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("roomId") String roomId) {
        try {
            String projectRoot = System.getProperty("user.dir");
            String uploadPath = projectRoot + File.separator + "storage" + File.separator + "uploads";

            File dir = new File(uploadPath);
            if (!dir.exists()) dir.mkdirs();

            String originalName = file.getOriginalFilename();
            String saveName = UUID.randomUUID().toString() + "_" + originalName;

            // 파일을 물리적으로 저장만 합니다. (AI 분석 호출 삭제!)
            File dest = new File(uploadPath + File.separator + saveName);
            file.transferTo(dest);

            Map<String, String> result = new HashMap<>();
            result.put("fileName", originalName);
            result.put("filePath", "/temp_uploads/" + saveName);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("파일 저장 실패");
        }
    }
}