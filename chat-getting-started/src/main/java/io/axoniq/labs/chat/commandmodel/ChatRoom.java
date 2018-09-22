package io.axoniq.labs.chat.commandmodel;

import io.axoniq.labs.chat.coreapi.CreateRoomCommand;
import io.axoniq.labs.chat.coreapi.JoinRoomCommand;
import io.axoniq.labs.chat.coreapi.LeaveRoomCommand;
import io.axoniq.labs.chat.coreapi.MessagePostedEvent;
import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent;
import io.axoniq.labs.chat.coreapi.PostMessageCommand;
import io.axoniq.labs.chat.coreapi.RoomCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Aggregate
public class ChatRoom {

    @AggregateIdentifier
    private String roomId;

    private List<String> participantIds = new CopyOnWriteArrayList<>();

    public ChatRoom() {
    }

    @CommandHandler
    public ChatRoom(CreateRoomCommand cmd) {
        AggregateLifecycle.apply(new RoomCreatedEvent(cmd.getRoomId(), cmd.getName()));
    }

    @CommandHandler
    public void on(JoinRoomCommand cmd) {
        if (!participantIds.contains(cmd.getParticipant())) {
            AggregateLifecycle.apply(new ParticipantJoinedRoomEvent(cmd.getParticipant(), cmd.getRoomId()));
        }
    }

    @CommandHandler
    public void on(LeaveRoomCommand cmd) {
        if (participantIds.contains(cmd.getParticipant())) {
            AggregateLifecycle.apply(new ParticipantLeftRoomEvent(cmd.getParticipant(), cmd.getRoomId()));
        }
    }

    @CommandHandler
    public void on(PostMessageCommand cmd) {
        if (participantIds.contains(cmd.getParticipant())) {
            AggregateLifecycle.apply(new MessagePostedEvent(cmd.getParticipant(), cmd.getRoomId(), cmd.getMessage()));
        } else {
            throw new IllegalStateException("Participant " + cmd.getParticipant() + "has not joined the room " + roomId);
        }
    }

    @EventSourcingHandler
    public void on(RoomCreatedEvent evt) {
        roomId = evt.getRoomId();
    }

    @EventSourcingHandler
    public void on(ParticipantJoinedRoomEvent evt) {
        this.participantIds.add(evt.getParticipant());
    }

    @EventSourcingHandler
    public void on(ParticipantLeftRoomEvent evt) {
        this.participantIds.remove(evt.getParticipant());
    }

}
