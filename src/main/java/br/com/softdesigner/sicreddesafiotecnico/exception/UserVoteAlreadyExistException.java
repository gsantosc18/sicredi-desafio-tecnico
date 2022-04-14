package br.com.softdesigner.sicreddesafiotecnico.exception;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class UserVoteAlreadyExistException extends ResponseStatusException {
    public UserVoteAlreadyExistException() {
        super(BAD_REQUEST, "O associado já realizou a votação para sessão");
    }
}
