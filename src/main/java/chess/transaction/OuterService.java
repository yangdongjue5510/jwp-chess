package chess.transaction;

import chess.model.room.Room;
import chess.repository.RoomRepository;
import java.sql.SQLException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OuterService {

    private final TransactionalService transactionalService;
    private final RoomRepository roomRepository;

    public OuterService(TransactionalService transactionalService, RoomRepository roomRepository) {
        this.transactionalService = transactionalService;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public void doSomething() {
        roomRepository.createRoom(Room.fromPlainPassword("name", "password"));
        transactionalService.throwSomething();
        System.out.println("done! with throw");
    }
}
