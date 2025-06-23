package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Custom handler for file size exceptions
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException ex, WebRequest request) {
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            "File Size Exceeded",
            "File size exceeds the maximum limit of 10MB",
            request
        );
    }

    // Custom handler for resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "Resource Not Found",
            ex.getMessage(),
            request
        );
    }

    // General exception handler (catches all other exceptions)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception ex, WebRequest request) {
        return buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal Server Error",
            "An unexpected error occurred",
            request
        );
    }

    // Helper method to build consistent error responses
    private ResponseEntity<ErrorResponse> buildErrorResponse(
        HttpStatus status, 
        String error, 
        String message, 
        WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            status.value(),
            error,
            message,
            System.currentTimeMillis(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(status).body(errorResponse);
    }

    // Standardized error response format
    static class ErrorResponse {
        private final int status;
        private final String error;
        private final String message;
        private final long timestamp;
        private final String path;

        public ErrorResponse(int status, String error, String message, long timestamp, String path) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.timestamp = timestamp;
            this.path = path;
        }

        // Getters
        public int getStatus() { return status; }
        public String getError() { return error; }
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
        public String getPath() { return path; }
    }
}