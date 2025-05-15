package com.kh.bbs.dao;

import com.kh.bbs.domain.entity.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BoardDAOImpl implements BoardDAO {

  private final NamedParameterJdbcTemplate template;

  // 수동 매핑 (snake_case 컬럼 → camelCase 필드)
  private RowMapper<Board> boardRowMapper() {
    return (rs, rowNum) -> {
      Board board = new Board();
      board.setId(rs.getLong("id"));
      board.setTitle(rs.getString("title"));
      board.setContent(rs.getString("content"));
      board.setWriter(rs.getString("writer"));
      board.setCreatedAt(rs.getTimestamp("created_at"));
      board.setUpdatedAt(rs.getTimestamp("updated_at"));
      return board;
    };
  }

  // 게시글 등록
  @Override
  public Long save(Board board) {
    String sql = """
      INSERT INTO board (id, title, content, writer, created_at, updated_at)
      VALUES (board_seq.nextval, :title, :content, :writer, :createdAt, :updatedAt)
    """;

    MapSqlParameterSource param = new MapSqlParameterSource()
        .addValue("title", board.getTitle())
        .addValue("content", board.getContent())
        .addValue("writer", board.getWriter())
        .addValue("createdAt", board.getCreatedAt())
        .addValue("updatedAt", board.getUpdatedAt());

    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql, param, keyHolder, new String[]{"id"});

    Number key = keyHolder.getKey();
    return (key != null) ? key.longValue() : null;
  }

  // 게시글 목록 조회
  @Override
  public List<Board> findAll() {
    String sql = """
      SELECT id, title, content, writer, created_at, updated_at
      FROM board
      ORDER BY id DESC
    """;

    return template.query(sql, boardRowMapper());
  }

  // 게시글 단건 조회
  @Override
  public Optional<Board> findById(Long id) {
    String sql = """
      SELECT id, title, content, writer, created_at, updated_at
      FROM board
      WHERE id = :id
    """;

    MapSqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
    List<Board> result = template.query(sql, param, boardRowMapper());

    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  // 게시글 단건 삭제
  @Override
  public int deleteById(Long id) {
    String sql = "DELETE FROM board WHERE id = :id";
    MapSqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
    return template.update(sql, param);
  }

  // 게시글 여러 건 삭제
  @Override
  public int deleteByIds(List<Long> ids) {
    String sql = "DELETE FROM board WHERE id IN (:ids)";
    MapSqlParameterSource param = new MapSqlParameterSource().addValue("ids", ids);
    return template.update(sql, param);
  }

  // 게시글 수정
  @Override
  public int updateById(Long boardId, Board board) {
    String sql = """
      UPDATE board
      SET title = :title,
          content = :content,
          writer = :writer,
          updated_at = :updatedAt
      WHERE id = :boardId
    """;

    MapSqlParameterSource param = new MapSqlParameterSource()
        .addValue("title", board.getTitle())
        .addValue("content", board.getContent())
        .addValue("writer", board.getWriter())
        .addValue("updatedAt", board.getUpdatedAt())
        .addValue("boardId", boardId);

    return template.update(sql, param);
  }
}
