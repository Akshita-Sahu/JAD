package org.example.jfranalyzerbackend.service;

import org.example.jfranalyzerbackend.vo.FlameGraph;
import org.example.jfranalyzerbackend.vo.Metadata;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * JFR
 * JFR
 */
public interface JFRAnalysisService {
    /**
     * 
     */
    Metadata retrieveAnalysisMetadata();
    
    /**
     * JFR
     */
    boolean validateJFRFileIntegrity(Path filePath);
    
    /**
     * 
     */
    FlameGraph performAnalysisAndGenerateFlameGraph(Path filePath, String analysisDimension, 
                                                   boolean includeTasks, List<String> taskFilter, 
                                                   Map<String, String> analysisOptions);
    
    /**
     * 
     */
    List<String> retrieveSupportedAnalysisDimensions();
    
    // 
    Metadata getMetadata();
    boolean isValidJFRFile(Path path);
    FlameGraph analyzeAndGenerateFlameGraph(Path path, String dimension, boolean include, List<String> taskSet, Map<String, String> options);
    List<String> getSupportedDimensions();
}
