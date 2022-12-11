package com.example.jattui.models;

public class Document extends Super {
    String id;
    String documentName;
    String extension;
    String documentUrl;
    String absolutePath;

    public Document() {
    }

    public Document(String id, String documentName, String documentUrl, String extension, String absolutePath) {
        this.id = id;
        this.extension = extension;
        this.documentName = documentName;
        this.documentUrl = documentUrl;
        this.absolutePath = absolutePath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }
}
