package io.axoniq.labs.chat.query.rooms.messages;

import io.axoniq.labs.chat.coreapi.MessagePostedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Component
public class ChatMessageProjection {

    private final ChatMessageRepository repository;

    private final QueryUpdateEmitter updateEmitter;

    public ChatMessageProjection(ChatMessageRepository repository,
                                 QueryUpdateEmitter updateEmitter) {
        this.repository = repository;
        this.updateEmitter = updateEmitter;
    }

    @EventHandler
    public void on(MessagePostedEvent evt, @Timestamp Instant timestamp) {
        repository.save(new ChatMessage(evt.getParticipant(), evt.getRoomId(), evt.getMessage(), timestamp.toEpochMilli()));
    }

    // TODO: Create the query handler to read data from this model

    // TODO: Emit updates when new message arrive to notify subscription query by modifying the event handler

}
