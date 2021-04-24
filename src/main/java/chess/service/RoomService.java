package chess.service;

import chess.domain.ChessRepository;
import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.piece.PieceFactory;
import chess.domain.room.Room;
import chess.web.dto.RoomDto;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class RoomService {

    private final ChessRepository chessRepository;

    public RoomService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public List<RoomDto> getAllRooms() {
        return chessRepository.allRooms().stream()
                .map(RoomDto::new)
                .collect(toList());
    }

    public RoomDto createNewRoom(String roomName) {
        Long roomId = saveGameToDB(roomName);

        return new RoomDto(new Room(roomId, roomName, null));
    }

    private Long saveGameToDB(String roomName) {
        ChessGame chessGame = new ChessGame(
                null,
                new Board(PieceFactory.createPieces())
        );
        chessGame.start();

        return chessRepository.save(
                new Room(
                        null,
                        roomName,
                        chessGame
                )
        );
    }

}
