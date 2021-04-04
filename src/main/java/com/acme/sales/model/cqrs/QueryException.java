package com.acme.sales.model.cqrs;

public class QueryException extends Exception{
    public QueryException(Throwable cause) {
        super(cause);
    }
}
