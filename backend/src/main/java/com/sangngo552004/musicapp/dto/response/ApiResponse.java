package com.sangngo552004.musicapp.dto.response;

public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;
    private long timestamp;

    public ApiResponse() {
    }

    public ApiResponse(int status, String message, T data, long timestamp) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    public static <T> ApiResponse<T> of(int status, String message, T data) {
        return new ApiResponse<>(status, message, data, System.currentTimeMillis());
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
