package com.acme.sales.model.cqrs;

/**
 * Implemented by all Command objects
 */
public interface Command {
    public abstract void process() throws CommandException;
}
