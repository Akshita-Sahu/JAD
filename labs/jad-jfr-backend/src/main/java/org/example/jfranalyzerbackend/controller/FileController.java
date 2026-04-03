package org.example.jfranalyzerbackend.controller;

import org.example.jfranalyzerbackend.config.Result;
import org.example.jfranalyzerbackend.dto.FileView;
import org.example.jfranalyzerbackend.enums.FileType;
import org.example.jfranalyzerbackend.service.FileService;
import org.example.jfranalyzerbackend.vo.PageView;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * 、、REST API
 */
@RestController
public class FileController {

    private final FileService fileManagementService;

    /**
     * 
     * @param fileManagementService 
     */
    public FileController(FileService fileManagementService) {
        this.fileManagementService = fileManagementService;
    }

    /**
     * 
     *
     * @param fileType 
     * @param pageNumber ，1
     * @param pageSize 
     * @return 
     */
    @GetMapping("/files")
    public Result<PageView<FileView>> retrieveUserFiles(@RequestParam(required = false) FileType fileType,
                                         @RequestParam(defaultValue = "1") int pageNumber,
                                         @RequestParam(defaultValue = "10") int pageSize) {
        System.out.println("");
        PageView<FileView> pageView = fileManagementService.retrieveUserFileViews(fileType, pageNumber, pageSize);
        return Result.success(pageView);
    }

    /**
     * 
     *
     * @param fileType 
     * @param uploadedFile 
     * @return ID
     * @throws Throwable 
     */
    @PostMapping(value = "/files/upload")
    public Result<Long> processFileUpload(@RequestParam FileType fileType,
                       @RequestParam MultipartFile uploadedFile) throws Throwable {
        System.out.println("");
        long fileId = fileManagementService.processFileUpload(fileType, uploadedFile);
        return Result.success(fileId);
    }

    /**
     * ID
     *
     * @param fileId ID
     */
    @DeleteMapping("/files/{file-id}")
    public Result<Void> removeFile(@PathVariable("file-id") long fileId) {
        fileManagementService.removeFileById(fileId);
        return Result.success();
    }

    //  - 
    @GetMapping("/files/legacy")
    public Result<PageView<FileView>> queryFiles(@RequestParam(required = false) FileType type,
                                         @RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int pageSize) {
        return retrieveUserFiles(type, page, pageSize);
    }

    @PostMapping(value = "/files/upload/legacy")
    public Result<Long> upload(@RequestParam FileType type,
                       @RequestParam MultipartFile file) throws Throwable {
        return processFileUpload(type, file);
    }

    @DeleteMapping("/files/legacy/{file-id}")
    public Result<Void> deleteFile(@PathVariable("file-id") long fileId) {
        return removeFile(fileId);
    }
}
