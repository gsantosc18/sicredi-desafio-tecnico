package br.com.softdesigner.sicreddesafiotecnico.exception;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class InvalidSessionException extends ResponseStatusException {
    public InvalidSessionException() {
        super(BAD_REQUEST, "A sessão informada é inválida");
    }
}
