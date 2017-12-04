package me.robnette.spring_angular.model;

public class RestResponse {
    private String message;

    public RestResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
