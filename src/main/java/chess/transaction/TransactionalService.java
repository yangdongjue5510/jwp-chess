package chess.transaction;

import chess.model.room.Room;
import chess.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionalService {

    private final RoomRepository roomRepository;

    public TransactionalService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional(propagation = Propagation.NESTED)
    public void throwSomething() {
        roomRepository.createRoom(Room.fromPlainPassword("other", "password"));
        throw new IllegalArgumentException();
    }
}
