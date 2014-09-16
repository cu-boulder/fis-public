package edu.colorado.orcid;

public class OrcidEmailExistsException extends Exception {

    private static final long serialVersionUID = 1L;

    public OrcidEmailExistsException(String message) {
        super(message);
    }

    public OrcidEmailExistsException(Throwable e) {
        super(e);
    }
}
