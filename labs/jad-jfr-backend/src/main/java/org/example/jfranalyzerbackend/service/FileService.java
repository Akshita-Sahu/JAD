package org.example.jfranalyzerbackend.service;

import org.example.jfranalyzerbackend.dto.FileView;
import org.example.jfranalyzerbackend.enums.FileType;
import org.example.jfranalyzerbackend.vo.PageView;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * 
 * 
 */
public interface FileService {

    /**
     * 
     */
    PageView<FileView> retrieveUserFileViews(FileType fileType, int pageNumber, int pageSize);

    /**
     * 
     */
    Long processFileUpload(FileType fileType, MultipartFile uploadedFile) throws IOException;

    /**
     * ID
     */
    void removeFileById(Long fileId);

    /**
     * ID
     */
    FileView retrieveFileViewById(Long fileId);

    /**
     * ID
     */
    String retrieveFilePathById(Long fileId);

    // 
    PageView<FileView> getUserFileViews(FileType type, int page, int pageSize);
    Long handleUploadRequest(FileType type, MultipartFile file) throws IOException;
    void deleteById(Long fileId);
    FileView getFileViewById(Long fileId);
    String getFilePathById(Long fileId);
}
