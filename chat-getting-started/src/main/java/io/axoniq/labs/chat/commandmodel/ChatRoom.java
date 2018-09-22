package io.axoniq.labs.chat.commandmodel;

import io.axoniq.labs.chat.coreapi.CreateRoomCommand;
import io.axoniq.labs.chat.coreapi.RoomCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class ChatRoom {

    @AggregateIdentifier
    private String roomId;

    public ChatRoom() {
    }

    @CommandHandler
    public ChatRoom(CreateRoomCommand cmd) {
        AggregateLifecycle.apply(new RoomCreatedEvent(cmd.getRoomId(), cmd.getName()));
    }

    @EventSourcingHandler
    public void on(RoomCreatedEvent evt) {
        roomId = evt.getRoomId();
    }

    // TODO: This class has just been created to make the test compile. It's missing, well, everything...

}
