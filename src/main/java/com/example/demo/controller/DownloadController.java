package com.example.demo.controller;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.WritingData;
import com.example.demo.service.WritingDataService;

@RestController
public class DownloadController {

    private final WritingDataService writingService;

    public DownloadController(WritingDataService writingService) {
        this.writingService = writingService;
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable("id") Integer writingId) throws IOException {
        // 書き込みデータを取得
        WritingData writing = writingService.findById(writingId)
                .orElse(null);

        if (writing == null || writing.getPdfMovie() == null || writing.getPdfMovie().length == 0) {
            // ファイルが存在しない場合
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // PDFバイト配列を取得
        byte[] pdfData = writing.getPdfMovie();
        ByteArrayResource resource = new ByteArrayResource(pdfData);

        // ファイル名生成（例: writing_123.pdf）
        String fileName = "writing_" + writingId + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfData.length)
                .body(resource);
    }
}
