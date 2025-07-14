package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.WritingData;
import com.example.demo.repository.WritingDataRepository;

@Service
public class WritingDataService {

    private final WritingDataRepository writingRepository;

    public WritingDataService(WritingDataRepository writingRepository) {
        this.writingRepository = writingRepository;
    }

    /**
     * ✅ 組織IDで投稿一覧を取得（最新順）
     */
    public List<WritingData> findByOrganizationKey(Integer organizationKey) {
        return writingRepository.findByOrganizationKeyOrderByWritingTimeDesc(organizationKey);
    }

    /**
     * ✅ 組織ID + 部署IDで投稿一覧を取得（最新順）
     */
    public List<WritingData> findByOrganizationKeyAndDepartmentId(Integer organizationKey, Integer departmentId) {
        return writingRepository.findByOrganizationKeyAndDepartmentIdOrderByWritingTimeDesc(organizationKey, departmentId);
    }

    /**
     * ✅ 投稿IDで1件取得
     */
    public Optional<WritingData> findById(Integer writingId) {
        return writingRepository.findById(writingId);
    }

    /**
     * ✅ 投稿データを保存（新規登録・更新）
     */
    @Transactional
    public WritingData saveWriting(WritingData writingData) {
        return writingRepository.save(writingData);
    }

    /**
     * ✅ 投稿IDでPDFファイルのバイナリを取得
     */
    public Optional<byte[]> getPdfByWritingId(Integer writingId) {
        return writingRepository.findById(writingId)
                .map(WritingData::getPdfMovie)
                .filter(pdf -> pdf != null && pdf.length > 0);
    }

    /**
     * ✅ 投稿を編集（PIN認証付き / PIN未設定なら認証スキップ）
     */
    @Transactional
    public boolean updateWriting(Integer writingId, String newMessage, String pin, byte[] pdfFileBytes) {
        Optional<WritingData> optional = writingRepository.findById(writingId);

        if (optional.isPresent()) {
            WritingData writing = optional.get();

            String storedPin = writing.getPin();
            System.out.println("[DEBUG] DBのPIN: " + storedPin + " 入力PIN: " + pin);

            // PIN認証
            if (storedPin != null && !storedPin.isEmpty()) {
                if (pin == null || !pin.equals(storedPin)) {
                    System.out.println("[DEBUG] PIN不一致 → 更新中止");
                    return false;
                }
            }

            System.out.println("[DEBUG] 更新前メッセージ: " + writing.getMessage());
            System.out.println("[DEBUG] 更新後メッセージ: " + newMessage);

            // 本文更新
            writing.setMessage(newMessage);

            // PDFが指定されていれば上書き
            if (pdfFileBytes != null && pdfFileBytes.length > 0) {
                writing.setPdfMovie(pdfFileBytes);
                System.out.println("[DEBUG] PDF更新あり");
            } else {
                System.out.println("[DEBUG] PDF更新なし");
            }

            writingRepository.save(writing);
            System.out.println("[DEBUG] 更新実行");
            return true;
        }
        System.out.println("[DEBUG] 投稿ID " + writingId + " が見つからない");
        return false;
    }

    /**
     * ✅ 投稿を削除（PIN認証付き / PIN未設定なら認証スキップ）
     */
    @Transactional
    public boolean deleteWriting(Integer writingId, String pin) {
        Optional<WritingData> optional = writingRepository.findById(writingId);

        if (optional.isPresent()) {
            WritingData writing = optional.get();

            String storedPin = writing.getPin();

            // PIN認証
            if (storedPin != null && !storedPin.isEmpty()) {
                if (pin == null || !pin.equals(storedPin)) {
                    return false; // 認証失敗
                }
            }

            writingRepository.deleteById(writingId);
            return true;
        }
        return false;
    }
    
    public List<WritingData> findByDepartmentId(Integer departmentId) {
        return writingRepository.findByDepartmentIdOrderByWritingTimeDesc(departmentId);
    }
    

}
