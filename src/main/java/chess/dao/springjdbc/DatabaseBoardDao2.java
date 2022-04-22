package chess.dao.springjdbc;

import chess.dao.BoardDao;
import chess.dao.jdbc.jdbcutil.JdbcUtil;
import chess.dao.jdbc.jdbcutil.StatementExecutor;
import chess.service.dto.BoardDto;
import chess.service.dto.PieceWithSquareDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseBoardDao2 implements BoardDao {

    private static final String PIECE_COLOR = "piece_color";
    private static final String PIECE_TYPE = "piece_type";
    private static final String SQUARE = "square";

    private JdbcTemplate jdbcTemplate;
    private final RowMapper<PieceWithSquareDto> pieceRowMapper = (resultSet, rowNum) ->
            new PieceWithSquareDto(
                    resultSet.getString("square"),
                    resultSet.getString("piece_type"),
                    resultSet.getString("piece_color")
            );
    public DatabaseBoardDao2(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void initBoard(int gameId) {
        String sql = "INSERT INTO board (game_id, piece_type, piece_color, square)\n"
                + "SELECT ?, init.piece_type, init.piece_color, init.square FROM init_board AS init\n"
                + "ON duplicate KEY UPDATE piece_type = init.piece_type, piece_color = init.piece_color";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public BoardDto getBoardByGameId(int id) {
        String sql = "select piece_type, piece_color, square from board where game_id = ?";
        return new BoardDto(jdbcTemplate.query(sql, pieceRowMapper, id));
    }

    @Override
    public void remove(int id) {
        String sql = "delete from board where game_id = ?";
        new StatementExecutor(JdbcUtil.getConnection(), sql)
                .setInt(id)
                .executeUpdate();
    }

    @Override
    public void update(PieceWithSquareDto piece, int gameId) {
        String sql = "update board set piece_type = ?, piece_color = ? where square = ? and game_id = ?";
        new StatementExecutor(JdbcUtil.getConnection(), sql)
                .setString(piece.getType())
                .setString(piece.getColor())
                .setString(piece.getSquare())
                .setInt(gameId)
                .executeUpdate();
    }
}
