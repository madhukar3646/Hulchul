package com.app.hulchul.fileuploadservice;

public enum MIMEType {
    IMAGE("image/*"), VIDEO("video/*");
    public String value;

    MIMEType(String value) {
        this.value = value;
    }
}
