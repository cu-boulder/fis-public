package edu.colorado.orcid;

import org.springframework.http.HttpStatus;

public class OrcidHttpException extends Exception {

    private static final long serialVersionUID = 1L;
    private String document;
    private HttpStatus statusCode;
    private String orcidPrimaryRecord;

    public OrcidHttpException(String message) {
        super(message);
    }

    public OrcidHttpException(Throwable e) {
        super(e);
    }

    public String getDocumentText() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getOrcidPrimaryRecord() {
        return orcidPrimaryRecord;
    }

    public void setOrcidPrimaryRecord(String orcidPrimaryRecord) {
        this.orcidPrimaryRecord = orcidPrimaryRecord;
    }
}
