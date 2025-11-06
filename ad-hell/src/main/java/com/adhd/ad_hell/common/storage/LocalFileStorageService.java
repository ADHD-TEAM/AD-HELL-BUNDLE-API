package com.adhd.ad_hell.common.storage;

import com.adhd.ad_hell.exception.BusinessException;
import com.adhd.ad_hell.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class LocalFileStorageService implements FileStorage {

    private static final Set<String> ALLOWED_EXT = Set.of(
        "png", "jpg", "jpeg", "gif",
        "mp4", "mov", "avi", "mkv", "wmv", "flv", "webm", "m4v", "ts", "mpeg", "mpg",
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
        "hwp", "txt", "csv", "rtf", "odt", "md"
    );

    private final Path uploadDir;

    /** 외부 접근용 Base URL (예: http://localhost:8080/uploads/) */
    private final String baseUrl;

    public LocalFileStorageService(
        @Value("${image.image-dir}") String uploadDir,
        @Value("${image.base-url:/uploads/}") String baseUrl
    ) {
        this.uploadDir = Paths.get(uploadDir).normalize().toAbsolutePath();
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";

        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            log.error("업로드 디렉터리 생성 실패: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.FILE_DIR_CREATE_FAILED);
        }
    }

    @Override
    public FileStorageResult store(MultipartFile file) {
        if (file == null || file.isEmpty())
            throw new BusinessException(ErrorCode.FILE_EMPTY);

        final String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isBlank())
            throw new BusinessException(ErrorCode.FILE_NAME_NOT_PRESENT);

        final String ext = Optional.of(originalName)
                                   .filter(name -> name.contains("."))
                                   .map(name -> name.substring(name.lastIndexOf('.') + 1))
                                   .map(String::toLowerCase)
                                   .orElse("");

        if (!ALLOWED_EXT.contains(ext)) {
            log.warn("허용되지 않은 확장자: {}", ext);
            throw new BusinessException(ErrorCode.FILE_EXTENSION_NOT_ALLOWED);
        }

        // 3️⃣ 파일명 랜덤화
        final String fileName = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);

        // 4️⃣ 안전한 경로 생성
        final Path target = safeResolve(fileName);

        // 5️⃣ 실제 저장
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            log.error("파일 저장 실패 [{}]: {}", fileName, ex.getMessage(), ex);
            throw new BusinessException(ErrorCode.FILE_SAVE_IO_ERROR);
        }

        // ✅ 저장된 파일명 + 접근 가능한 URL 반환
        String url = baseUrl + fileName;
        return new FileStorageResult(fileName, url);
    }

    @Override
    public void delete(String fileName) {
        final Path path = safeResolve(fileName);
        try {
            if (!Files.deleteIfExists(path)) {
                log.warn("삭제할 파일이 존재하지 않음: {}", path);
            }
        } catch (IOException ex) {
            log.error("파일 삭제 실패 [{}]: {}", fileName, ex.getMessage(), ex);
            throw new BusinessException(ErrorCode.FILE_DELETE_IO_ERROR);
        }
    }

    @Override
    public void deleteQuietly(String fileName) {
        try {
            delete(fileName);
        } catch (Exception ignore) { }
    }

    private Path safeResolve(String fileName) {
        Path p = this.uploadDir.resolve(fileName).normalize().toAbsolutePath();
        if (!p.startsWith(this.uploadDir)) {
            throw new BusinessException(ErrorCode.FILE_PATH_TRAVERSAL_DETECTED);
        }
        return p;
    }
}
