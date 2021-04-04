package com.acme.sales.model.utils.event.messaging;

public class MessagingException extends Exception {
    /** May wrap specific attributes **/
    public MessagingException(String message) {
        super(message);
    }

    public MessagingException(String message, Throwable cause) {
        super(message, cause);
    }
}
