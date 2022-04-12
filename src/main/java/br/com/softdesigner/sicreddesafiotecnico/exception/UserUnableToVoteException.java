package br.com.softdesigner.sicreddesafiotecnico.exception;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class UserUnableToVoteException extends ResponseStatusException {
    public UserUnableToVoteException() {
        super(BAD_REQUEST, "O associado não está apto para votar");
    }
}
