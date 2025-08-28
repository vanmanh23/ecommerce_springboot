package com.manh.ecommerce_java.dtos;

public class BaseResponse {
    private boolean isSuccess;
    private String message;
    private Object result;
    private Object errors;

    public BaseResponse() {
    }

    public BaseResponse(boolean isSuccess, String message, Object result, Object errors) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.errors = errors;
        this.result = result;

    }

    public static BaseResponse createSuccessResponse(String successMessage, Object responseData) {
        return new BaseResponse(true, successMessage, responseData, null);
    }

    public static BaseResponse createSuccessResponse(String successMessage) {
        return new BaseResponse(true, successMessage, null, null);
    }

    public static BaseResponse createErrorResponse(String errorMessage, Object responseData) {
        return new BaseResponse(false, errorMessage, null, responseData);
    }

    public static BaseResponse createErrorResponse(String errorMessage) {
        return new BaseResponse(false, errorMessage, null, null);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }
}
