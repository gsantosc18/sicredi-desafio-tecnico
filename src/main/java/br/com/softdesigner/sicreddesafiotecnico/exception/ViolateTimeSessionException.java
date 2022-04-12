package br.com.softdesigner.sicreddesafiotecnico.exception;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ViolateTimeSessionException extends ResponseStatusException {
    public ViolateTimeSessionException() {
        super(BAD_REQUEST,"A sessão expirou e não pode mais ser votada.");
    }
}
