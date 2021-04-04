package com.acme.sales.model.cqrs;

public class CommandException extends Exception {
    public CommandException(Throwable cause) {
        super(cause);
    }
}
