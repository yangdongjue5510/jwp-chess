package chess.service;

import chess.dao.PieceDao;
import chess.dao.GameDao;
import chess.model.board.MoveResult;
import chess.model.gamestatus.Status;
import chess.model.gamestatus.StatusType;
import chess.model.game.ChessGame;
import chess.model.Color;
import chess.model.board.Board;
import chess.model.board.Square;
import chess.model.piece.Piece;
import chess.service.dto.BoardDto;
import chess.service.dto.GameEntity;
import chess.service.dto.GameResultDto;
import chess.service.dto.GamesDto;
import chess.service.dto.PieceEntity;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final PieceDao pieceDao;
    private final GameDao gameDao;

    public ChessService(PieceDao pieceDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
    }

    public void initGame(int gameId) {
        ChessGame chessGame = new ChessGame();
        pieceDao.initBoard(gameId);
        updateGame(chessGame, gameId);
    }

    private void updateGame(ChessGame chessGame, int id) {
        gameDao.update(new GameEntity(id, chessGame));
    }

    public void move(int id, String from, String to) {
        ChessGame chessGame = getGameFromDao(id);
        MoveResult movedResult = chessGame.move(Square.of(from), Square.of(to));
        updatePiece(id, movedResult);
        updateGame(chessGame, id);
    }

    private void updatePiece(int id, MoveResult movedResult) {
        Map<Square, Piece> affectedPiece = movedResult.getAffectedPiece();
        for (Square square : affectedPiece.keySet()) {
            pieceDao.update(new PieceEntity(square, affectedPiece.get(square)), id);
        }
    }

    private ChessGame getGameFromDao(int id) {
        GameEntity game = gameDao.findById(id);
        Status status = getStatusFromDao(game.getStatus(), getBoardFromDao(id));
        return new ChessGame(Color.valueOf(game.getTurn()), status);
    }

    public Board getBoardFromDao(int id) {
        BoardDto boardDto = pieceDao.getBoardByGameId(id);
        return new Board(boardDto);
    }

    private Status getStatusFromDao(String statusName, Board board) {
        return StatusType.createStatus(statusName, board);
    }

    public void endGame(int id) {
        ChessGame game = getGameFromDao(id);
        game.end();
        gameDao.update(new GameEntity(id, game));
        pieceDao.remove(id);
    }

    public GamesDto getAllGames() {
        return gameDao.findAll();
    }

    public void createGame(String name) {
        gameDao.createGame(name);
    }

    public GameResultDto getResult(int id) {
        return GameResultDto.of(getGameFromDao(id).getResult());
    }

    public boolean isEnd(int gameId) {
        return getGameFromDao(gameId).isEnd();
    }

    public BoardDto getBoard(int gameId) {
        return pieceDao.getBoardByGameId(gameId);
    }
}
