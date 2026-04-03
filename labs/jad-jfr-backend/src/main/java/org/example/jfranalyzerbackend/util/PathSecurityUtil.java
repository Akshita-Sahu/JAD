package org.example.jfranalyzerbackend.util;

import org.example.jfranalyzerbackend.exception.CommonException;
import org.example.jfranalyzerbackend.enums.ServerErrorCode;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * 
 * ，
 */
public class PathSecurityUtil {
    
    // ：、、、、
    private static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+$");
    
    // ： ..  \..  /.. 
    private static final Pattern PATH_TRAVERSAL_PATTERN = Pattern.compile(".*\\.\\..*|.*\\\\\\.\\..*|.*/\\.\\..*");
    
    /**
     * ，
     * 
     * @param basePath 
     * @param filename 
     * @return 
     * @throws CommonException 
     */
    public static Path buildSafePath(Path basePath, String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new CommonException(ServerErrorCode.INVALID_FILENAME, "");
        }
        
        // ，
        String sanitizedFilename = sanitizeFilename(filename);
        
        // 
        if (!isSafeFilename(sanitizedFilename)) {
            throw new CommonException(ServerErrorCode.INVALID_FILENAME, 
                ": " + sanitizedFilename);
        }
        
        // 
        if (containsPathTraversal(sanitizedFilename)) {
            throw new CommonException(ServerErrorCode.INVALID_FILENAME, 
                ": " + sanitizedFilename);
        }
        
        // 
        Path targetPath = basePath.resolve(sanitizedFilename);
        
        // 
        if (!targetPath.normalize().startsWith(basePath.normalize())) {
            throw new CommonException(ServerErrorCode.INVALID_FILENAME, 
                "");
        }
        
        return targetPath;
    }
    
    /**
     * 
     * 
     * @param path 
     * @param allowedBasePath 
     * @return 
     */
    public static boolean isPathSafe(Path path, Path allowedBasePath) {
        if (path == null || allowedBasePath == null) {
            return false;
        }
        
        try {
            Path normalizedPath = path.normalize();
            Path normalizedBasePath = allowedBasePath.normalize();
            
            return normalizedPath.startsWith(normalizedBasePath);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * ，
     * 
     * @param filename 
     * @return 
     */
    private static String sanitizeFilename(String filename) {
        if (filename == null) {
            return "";
        }
        
        // 
        return filename.replaceAll("[\\\\/:*?\"<>|]", "_")
                      .replaceAll("\\s+", "_")
                      .trim();
    }
    
    /**
     * 
     * 
     * @param filename 
     * @return 
     */
    private static boolean isSafeFilename(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return false;
        }
        
        // 
        if (filename.length() > 255) {
            return false;
        }
        
        // 
        return SAFE_FILENAME_PATTERN.matcher(filename).matches();
    }
    
    /**
     * 
     * 
     * @param filename 
     * @return 
     */
    private static boolean containsPathTraversal(String filename) {
        if (filename == null) {
            return false;
        }
        
        return PATH_TRAVERSAL_PATTERN.matcher(filename).matches();
    }
    
    /**
     * JFR
     * 
     * @param filePath 
     * @return 
     */
    public static boolean isValidJFRFilePath(Path filePath) {
        if (filePath == null) {
            return false;
        }
        
        try {
            // 
            if (!java.nio.file.Files.exists(filePath) || !java.nio.file.Files.isRegularFile(filePath)) {
                return false;
            }
            
            // 
            String filename = filePath.getFileName().toString();
            if (!filename.toLowerCase().endsWith(".jfr")) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
