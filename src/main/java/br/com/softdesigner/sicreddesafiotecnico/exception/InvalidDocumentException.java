package br.com.softdesigner.sicreddesafiotecnico.exception;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class InvalidDocumentException extends ResponseStatusException {
    public InvalidDocumentException() {
        super(BAD_REQUEST, "O documento informado é invalido.");
    }

    public InvalidDocumentException(String message) {
        super(BAD_REQUEST, message);
    }
}
