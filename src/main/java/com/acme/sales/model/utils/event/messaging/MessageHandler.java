package com.acme.sales.model.utils.event.messaging;

public interface MessageHandler {
    /**
     * Function implemented by the handler
     * @return
     */
    public void handleMessage(String message);

}
