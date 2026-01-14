package br.com.prestcontas.pipatrack.exception;

public class UnprocessableContentException extends RuntimeException {
    public UnprocessableContentException(String message) {
        super(message);
    }   
}
