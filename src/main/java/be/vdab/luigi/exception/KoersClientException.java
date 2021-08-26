package be.vdab.luigi.exception;

public class KoersClientException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public KoersClientException(String message) {
        super(message);
    }
    public KoersClientException(String message, Exception origineleFout) {
        super(message, origineleFout);
    }

}
