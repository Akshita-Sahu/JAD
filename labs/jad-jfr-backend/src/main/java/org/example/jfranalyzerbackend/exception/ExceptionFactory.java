package org.example.jfranalyzerbackend.exception;

/**
 * ，
 */
public class ExceptionFactory {
    
    /**
     * 
     */
    public static CommonException createCommonException(ErrorCode errorCode) {
        return new CommonException(errorCode);
    }
    
    /**
     * 
     */
    public static CommonException createCommonException(ErrorCode errorCode, String message) {
        return new CommonException(errorCode, message);
    }
    
    /**
     * 
     */
    public static CommonException createCommonException(ErrorCode errorCode, Throwable cause) {
        return new CommonException(errorCode, cause);
    }
    
    /**
     * （）
     */
    public static CommonException createCommonException(Throwable cause) {
        return new CommonException(cause);
    }
    
    /**
     * 
     */
    public static CommonException createCommonException(ErrorCode errorCode, String message, Throwable cause) {
        return new CommonException(errorCode, message, cause);
    }
    
    /**
     * 
     */
    public static ProfileAnalysisException createProfileAnalysisException(String message) {
        return new ProfileAnalysisException(message);
    }
    
    /**
     * 
     */
    public static ProfileAnalysisException createProfileAnalysisException(String message, Throwable cause) {
        return new ProfileAnalysisException(message, cause);
    }
    
    /**
     * 
     */
    public static ProfileAnalysisException createProfileAnalysisException(Throwable cause) {
        return new ProfileAnalysisException(cause);
    }
}
