package com.example.jattui.models;

public class Document extends Super {
    String id;
    String documentName;
    String documentUrl;

    public Document() {
    }

    public Document(String id, String documentName, String documentUrl) {
        this.id = id;
        this.documentName = documentName;
        this.documentUrl = documentUrl;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
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
