package com.example.aesservice.dto;

public class CryptoRequest {
    private String text;
    private String key;

    public CryptoRequest() {}

    public CryptoRequest(String text, String key) {
        this.text = text;
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
