package com.kh.bbs.dao;

import com.kh.bbs.domain.entity.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BoardDAOImpl implements BoardDAO {

  private final NamedParameterJdbcTemplate template;

  private RowMapper<Board> boardRowMapper() {
    return (rs, rowNum) -> {
      Board board = new Board();
      board.setId(rs.getLong("id"));
      board.setTitle(rs.getString("title"));
      board.setContent(rs.getString("content"));
      board.setAuthor(rs.getString("author"));
      board.setCreatedDate(rs.getTimestamp("created_date"));
      board.setUpdatedDate(rs.getTimestamp("updated_date"));
      return board;
    };
  }

  @Override
  public Long save(Board board) {
    String sql = "INSERT INTO board (id, title, content, author, created_date, updated_date) " +
        "VALUES (BOARD_SEQ.nextval, :title, :content, :author, SYSTIMESTAMP, SYSTIMESTAMP)";
    SqlParameterSource param = new BeanPropertySqlParameterSource(board);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql, param, keyHolder, new String[]{"id"});
    Number key = keyHolder.getKey();
    return key != null ? key.longValue() : null;
  }

  @Override
  public List<Board> findAll() {
    String sql = "SELECT id, title, content, author, created_date, updated_date FROM board ORDER BY id DESC";
    return template.query(sql, boardRowMapper());
  }

  @Override
  public Optional<Board> findById(Long id) {
    String sql = "SELECT id, title, content, author, created_date, updated_date FROM board WHERE id = :id";
    SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
    try {
      Board board = template.queryForObject(sql, param, boardRowMapper());
      return Optional.of(board);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public int deleteById(Long id) {
    String sql = "DELETE FROM board WHERE id = :id";
    Map<String, Long> param = Map.of("id", id);
    return template.update(sql, param);
  }

  @Override
  public int deleteByIds(List<Long> ids) {
    String sql = "DELETE FROM board WHERE id IN (:ids)";
    Map<String, List<Long>> param = Map.of("ids", ids);
    return template.update(sql, param);
  }

  @Override
  public int updateById(Long boardId, Board board) {
    String sql = "UPDATE board SET title = :title, content = :content, author = :author, updated_date = SYSTIMESTAMP " +
        "WHERE id = :id";
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("title", board.getTitle())
        .addValue("content", board.getContent())
        .addValue("author", board.getAuthor())
        .addValue("id", boardId);
    return template.update(sql, param);
  }
}

