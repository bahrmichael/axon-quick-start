package io.axoniq.labs.chat.query.rooms.summary;

import io.axoniq.labs.chat.coreapi.AllRoomsQuery;
import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent;
import io.axoniq.labs.chat.coreapi.RoomCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Component
public class RoomSummaryProjection {

    private final RoomSummaryRepository roomSummaryRepository;

    public RoomSummaryProjection(RoomSummaryRepository roomSummaryRepository) {
        this.roomSummaryRepository = roomSummaryRepository;
    }

    @EventHandler
    public void on(RoomCreatedEvent evt) {
        roomSummaryRepository.save(new RoomSummary(evt.getRoomId(), evt.getName()));
    }

    @EventHandler
    public void on(ParticipantJoinedRoomEvent evt) {
        updateRoom(evt.getRoomId(), RoomSummary::addParticipant);
    }

    @EventHandler
    public void on(ParticipantLeftRoomEvent evt) {
        updateRoom(evt.getRoomId(), RoomSummary::removeParticipant);
    }

    private void updateRoom(String roomId, Consumer<RoomSummary> action) {
        final RoomSummary room = roomSummaryRepository.findByRoomId(roomId);
        action.accept(room);
        roomSummaryRepository.save(room);
    }

    @QueryHandler
    public List<RoomSummary> on(AllRoomsQuery query) {
        return roomSummaryRepository.findAll();
    }

}
