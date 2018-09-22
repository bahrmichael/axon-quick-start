package io.axoniq.labs.chat.query.rooms.participants;

import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class RoomParticipantsProjection {

    private final RoomParticipantsRepository repository;

    public RoomParticipantsProjection(RoomParticipantsRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(ParticipantJoinedRoomEvent evt) {
        repository.save(new RoomParticipant(evt.getRoomId(), evt.getParticipant()));
    }

    @EventHandler
    public void on(ParticipantLeftRoomEvent evt) {
        repository.deleteByParticipantAndRoomId(evt.getParticipant(), evt.getRoomId());
    }

    // TODO: Create the query handler to read data from this model
}
