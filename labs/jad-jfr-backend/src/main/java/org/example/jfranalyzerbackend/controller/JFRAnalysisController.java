package org.example.jfranalyzerbackend.controller;

import org.example.jfranalyzerbackend.config.Result;
import org.example.jfranalyzerbackend.config.JADConfig;
import org.example.jfranalyzerbackend.service.JFRAnalysisService;
import org.example.jfranalyzerbackend.service.FileService;
import org.example.jfranalyzerbackend.vo.FlameGraph;
import org.example.jfranalyzerbackend.vo.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * JFR
 * JFRREST API
 */
@RestController
@RequestMapping("/api/jfr")
public class JFRAnalysisController {

    private static final Logger logger = LoggerFactory.getLogger(JFRAnalysisController.class);

    private final JFRAnalysisService analysisService;
    private final FileService fileManagementService;
    private final JADConfig configuration;

    @Autowired
    public JFRAnalysisController(JFRAnalysisService analysisService, FileService fileManagementService, JADConfig configuration) {
        this.analysisService = analysisService;
        this.fileManagementService = fileManagementService;
        this.configuration = configuration;
    }

    /**
     * IDJFR
     *
     * @param fileId ID
     * @param dimension 
     * @param include 
     * @param taskSet （）
     * @param options （）
     * @return 
     */
    @PostMapping("/analyze/{fileId}")
    public Result<FlameGraph> performAnalysisByFileId(
            @PathVariable Long fileId,
            @RequestParam String dimension,
            @RequestParam(defaultValue = "true") boolean include,
            @RequestParam(required = false) List<String> taskSet,
            @RequestParam(required = false) Map<String, String> options) {
        String filePath = fileManagementService.retrieveFilePathById(fileId);
        FlameGraph flameGraph = analysisService.performAnalysisAndGenerateFlameGraph(
            Paths.get(filePath), dimension, include, taskSet, options
        );
        logger.info("，: {}", flameGraph.getData().length);
        return Result.success(flameGraph);
    }

    /**
     * JFR
     *
     * @param filePath JFR
     * @param dimension 
     * @param include 
     * @param taskSet （）
     * @param options （）
     * @return 
     */
    @PostMapping("/analyze")
    public Result<FlameGraph> executeAnalysisByPath(
            @RequestParam String filePath,
            @RequestParam String dimension,
            @RequestParam(defaultValue = "true") boolean include,
            @RequestParam(required = false) List<String> taskSet,
            @RequestParam(required = false) Map<String, String> options) {
        FlameGraph flameGraph = analysisService.performAnalysisAndGenerateFlameGraph(
            Paths.get(filePath), dimension, include, taskSet, options
        );
        logger.info("，: {}", flameGraph.getData().length);
        return Result.success(flameGraph);
    }

    /**
     * 
     *
     * @return 
     */
    @GetMapping("/metadata")
    public Result<Metadata> retrieveAnalysisMetadata() {
        Metadata metadata = analysisService.retrieveAnalysisMetadata();
        logger.info("===  ===");
        return Result.success(metadata);
    }

    /**
     * JFR
     *
     * @param filePath JFR
     * @return 
     */
    @GetMapping("/validate")
    public Result<Boolean> validateFileIntegrity(@RequestParam String filePath) {
        boolean isValid = analysisService.validateJFRFileIntegrity(Paths.get(filePath));
        return Result.success(isValid);
    }

    /**
     * 
     *
     * @return 
     */
    @GetMapping("/dimensions")
    public Result<List<String>> retrieveSupportedDimensions() {
        List<String> dimensions = analysisService.retrieveSupportedAnalysisDimensions();
        return Result.success(dimensions);
    }

    //  - 
    @PostMapping("/analyze/legacy/{fileId}")
    public Result<FlameGraph> analyzeJFRFileById(
            @PathVariable Long fileId,
            @RequestParam String dimension,
            @RequestParam(defaultValue = "true") boolean include,
            @RequestParam(required = false) List<String> taskSet,
            @RequestParam(required = false) Map<String, String> options) {
        return performAnalysisByFileId(fileId, dimension, include, taskSet, options);
    }

    @PostMapping("/analyze/legacy")
    public Result<FlameGraph> analyzeJFRFile(
            @RequestParam String filePath,
            @RequestParam String dimension,
            @RequestParam(defaultValue = "true") boolean include,
            @RequestParam(required = false) List<String> taskSet,
            @RequestParam(required = false) Map<String, String> options) {
        return executeAnalysisByPath(filePath, dimension, include, taskSet, options);
    }

    @GetMapping("/metadata/legacy")
    public Result<Metadata> getMetadata() {
        return retrieveAnalysisMetadata();
    }

    @GetMapping("/validate/legacy")
    public Result<Boolean> validateJFRFile(@RequestParam String filePath) {
        return validateFileIntegrity(filePath);
    }

    @GetMapping("/dimensions/legacy")
    public Result<List<String>> getSupportedDimensions() {
        return retrieveSupportedDimensions();
    }

} 