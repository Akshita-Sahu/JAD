
package org.example.jfranalyzerbackend.exception;


import static org.example.jfranalyzerbackend.enums.CommonErrorCode.INTERNAL_ERROR;

/**
 * 
 * ，
 */
public class CommonException extends ErrorCodeException {

    /**
     * 
     *
     * @param errorCode 
     * @param message   
     */
    public CommonException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * 
     *
     * @param errorCode 
     */
    public CommonException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * 
     *
     * @param message 
     */
    public CommonException(String message) {
        super(INTERNAL_ERROR, message);
    }

    /**
     * 
     *
     * @param throwable 
     */
    public CommonException(Throwable throwable) {
        super(INTERNAL_ERROR, throwable);
    }

    /**
     * 
     *
     * @param errorCode 
     * @param cause     
     */
    public CommonException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * 
     *
     * @param errorCode 
     * @param message   
     * @param cause     
     */
    public CommonException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * CommonException
     *
     * @param errorCode 
     * @return 
     */
    public static CommonException CE(ErrorCode errorCode) {
        return new CommonException(errorCode);
    }
}
