package com.akshita.jad.core.shell.command;

/**
 * 
 */
public class ExitStatus {

    /**
     * 
     */
    public static final ExitStatus SUCCESS_STATUS = new ExitStatus(0);

    /**
     * 
     * @return
     */
    public static ExitStatus success() {
        return SUCCESS_STATUS;
    }

    /**
     * 
     * @param statusCode
     * @param message
     * @return
     */
    public static ExitStatus failure(int statusCode, String message) {
        if (statusCode == 0) {
            throw new IllegalArgumentException("failure status code cannot be 0");
        }
        return new ExitStatus(statusCode, message);
    }

    /**
     * 
     * @param exitStatus
     * @return
     */
    public static boolean isFailed(ExitStatus exitStatus) {
        return exitStatus != null && exitStatus.getStatusCode() != 0;
    }


    private int statusCode;
    private String message;

    private ExitStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    private ExitStatus(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

}
