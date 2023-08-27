package eu.europa.eeas.currencyexchange.common;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String message, Exception e) {
        super(message, e);
    }
}
