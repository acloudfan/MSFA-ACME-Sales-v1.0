package com.acme.sales.model.cqrs.v3.command.events;

/**
 * Represents the proposal event
 */
public class ProposalEvent {
    public final int event_id;
    public final int proposal_id;
    public final String guid;
    public final String payloadBase64;

    public ProposalEvent(int event_id, int proposal_id, String guid, String payloadBase64) {
        this.event_id = event_id;
        this.proposal_id = proposal_id;
        this.guid = guid;
        this.payloadBase64 = payloadBase64;
    }
}
