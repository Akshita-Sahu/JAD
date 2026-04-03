package org.example.jfranalyzerbackend.service.impl;

import jakarta.transaction.Transactional;
import org.example.jfranalyzerbackend.config.JADConfig;
import org.example.jfranalyzerbackend.dto.FileView;
import org.example.jfranalyzerbackend.entity.shared.file.DeletedFileEntity;
import org.example.jfranalyzerbackend.entity.shared.file.FileEntity;
import org.example.jfranalyzerbackend.enums.FileType;
import org.example.jfranalyzerbackend.exception.CommonException;
import org.example.jfranalyzerbackend.repository.FileRepo;
import org.example.jfranalyzerbackend.repository.DeletedFileRepo;
import org.example.jfranalyzerbackend.service.FileService;
import org.example.jfranalyzerbackend.util.FileViewConverter;
import org.example.jfranalyzerbackend.util.PathSecurityUtil;
import org.example.jfranalyzerbackend.vo.PageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * 
 * 、、
 */
@Component
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);


    @Autowired
    private FileRepo fileRepo;

    @Autowired
    private JADConfig jadConfig;

    @Autowired
    private DeletedFileRepo deletedFileRepo;


    @Override
    public PageView<FileView> retrieveUserFileViews(FileType fileType, int pageNumber, int pageSize) {
        logger.info("");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        Long currentUserId = 1L; // ID

        // 
        Page<FileEntity> fileEntities = (fileType == null)
                ? fileRepo.findByUserIdOrderByCreatedTimeDesc(currentUserId, pageRequest)
                : fileRepo.findByUserIdAndTypeOrderByCreatedTimeDesc(currentUserId, fileType, pageRequest);

        List<FileView> fileViews = fileEntities.getContent().stream()
                .map(FileViewConverter::convert)
                .toList();

        return new PageView<>(pageNumber, pageSize, (int) fileEntities.getTotalElements(), fileViews);
    }

    @Override
    @Transactional
    public Long processFileUpload(FileType fileType, MultipartFile uploadedFile) throws IOException {
        String originalFileName = uploadedFile.getOriginalFilename() != null ? uploadedFile.getOriginalFilename() : "unknown.file";
        long fileSize = uploadedFile.getSize();

        Long currentUserId = 1L; // ID

        // 
        Path basePath = Paths.get(jadConfig.getJfrStoragePath());
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
        Path targetPath = PathSecurityUtil.buildSafePath(basePath, uniqueFileName);
        
        // 
        Files.createDirectories(targetPath.getParent());
        
        // 
        uploadedFile.transferTo(targetPath.toFile());

        // 
        FileEntity fileEntity = new FileEntity();
        fileEntity.setUniqueName(uniqueFileName);
        fileEntity.setOriginalName(originalFileName);
        fileEntity.setType(fileType);
        fileEntity.setSize(fileSize);
        fileEntity.setUserId(currentUserId);

        return fileRepo.save(fileEntity).getId();
    }


    @Override
    public void removeFileById(Long fileId) {
        FileEntity fileEntity = fileRepo.findById(fileId)
                .orElseThrow(() -> new CommonException(org.example.jfranalyzerbackend.enums.ServerErrorCode.FILE_NOT_FOUND));

        if (!fileEntity.getUserId().equals(1L)) { // ID
            throw new CommonException(org.example.jfranalyzerbackend.enums.ServerErrorCode.FILE_NOT_FOUND);
        }

        // 
        DeletedFileEntity deletedFileRecord = new DeletedFileEntity();
        deletedFileRecord.setUniqueName(fileEntity.getUniqueName());
        deletedFileRecord.setOriginalName(fileEntity.getOriginalName());
        deletedFileRecord.setType(fileEntity.getType());
        deletedFileRecord.setSize(fileEntity.getSize());
        deletedFileRecord.setOriginalCreatedTime(fileEntity.getCreatedTime());

        fileRepo.deleteById(fileId);
        deletedFileRepo.save(deletedFileRecord);

        // 
        try {
            Path physicalFilePath = Paths.get(jadConfig.getJfrStoragePath(), fileEntity.getUniqueName());
            Files.deleteIfExists(physicalFilePath);
        } catch (Exception e) {
            logger.warn(": {}", e.getMessage());
        }
    }

    @Override
    public FileView retrieveFileViewById(Long fileId) {
        FileEntity fileEntity = fileRepo.findById(fileId)
                .orElse(null);
        
        if (fileEntity == null) {
            return null;
        }
        
        // 
        if (!fileEntity.getUserId().equals(1L)) { // ID
            return null;
        }
        
        return FileViewConverter.convert(fileEntity);
    }

    @Override
    public String retrieveFilePathById(Long fileId) {
        FileEntity fileEntity = fileRepo.findById(fileId)
                .orElseThrow(() -> new CommonException(org.example.jfranalyzerbackend.enums.ServerErrorCode.FILE_NOT_FOUND));
        return Paths.get(jadConfig.getJfrStoragePath(), fileEntity.getUniqueName()).toString();
    }

    // 
    @Override
    public PageView<FileView> getUserFileViews(FileType type, int page, int pageSize) {
        return retrieveUserFileViews(type, page, pageSize);
    }

    @Override
    public Long handleUploadRequest(FileType type, MultipartFile file) throws IOException {
        return processFileUpload(type, file);
    }

    @Override
    public void deleteById(Long fileId) {
        removeFileById(fileId);
    }

    @Override
    public FileView getFileViewById(Long fileId) {
        return retrieveFileViewById(fileId);
    }

    @Override
    public String getFilePathById(Long fileId) {
        return retrieveFilePathById(fileId);
    }
}
