package com.kh.bbs.dao.board;

import com.kh.bbs.domain.entity.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO board (title, content, writer, created_at, updated_at) ");
    sql.append("VALUES (:title, :content, :writer, :createdAt, :updatedAt)");

    MapSqlParameterSource param = new MapSqlParameterSource()
        .addValue("title", board.getTitle())
        .addValue("content", board.getContent())
        .addValue("writer", board.getWriter())
        .addValue("createdAt", board.getCreatedAt())
        .addValue("updatedAt", board.getUpdatedAt());

    template.update(sql.toString(), param);

    return null;
  }

  // 게시글 목록 조회
  @Override
  public List<Board> findAll() {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT id, title, content, writer, created_at, updated_at ");
    sql.append("FROM board ");
    sql.append("ORDER BY id DESC");

    return template.query(sql.toString(), boardRowMapper());
  }

  // 게시글 단건 조회
  @Override
  public Optional<Board> findById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT id, title, content, writer, created_at, updated_at ");
    sql.append("FROM board ");
    sql.append("WHERE id = :id");

    MapSqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
    List<Board> result = template.query(sql.toString(), param, boardRowMapper());

    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  // 게시글 단건 삭제
  @Override
  public int deleteById(Long id) {
    StringBuffer sql = new StringBuffer("DELETE FROM board WHERE id = :id");
    MapSqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
    return template.update(sql.toString(), param);
  }

  // 게시글 여러 건 삭제
  @Override
  public int deleteByIds(List<Long> ids) {
    StringBuffer sql = new StringBuffer("DELETE FROM board WHERE id IN (:ids)");
    MapSqlParameterSource param = new MapSqlParameterSource().addValue("ids", ids);
    return template.update(sql.toString(), param);
  }

  // 게시글 수정
  @Override
  public int updateById(Long boardId, Board board) {
    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE board SET ");
    sql.append("title = :title, ");
    sql.append("content = :content, ");
    sql.append("writer = :writer, ");
    sql.append("updated_at = :updatedAt ");
    sql.append("WHERE id = :boardId");

    MapSqlParameterSource param = new MapSqlParameterSource()
        .addValue("title", board.getTitle())
        .addValue("content", board.getContent())
        .addValue("writer", board.getWriter())
        .addValue("updatedAt", board.getUpdatedAt())
        .addValue("boardId", boardId);

    return template.update(sql.toString(), param);
  }

  // 페이징 처리를 위한 게시글 목록 조회
  @Override
  public List<Board> findAll(int startRow, int endRow) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT * FROM (");
    sql.append(" SELECT ROWNUM rn, t.* FROM (");
    sql.append(" SELECT id, title, content, writer, created_at, updated_at FROM board ORDER BY id DESC");
    sql.append(") t WHERE ROWNUM <= :endRow");
    sql.append(") WHERE rn >= :startRow");

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("startRow", startRow)
        .addValue("endRow", endRow);

    return template.query(sql.toString(), params, boardRowMapper());
  }

  // 전체 게시글 수 반환
  @Override
  public int totalCount() {
    StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM board");
    return template.queryForObject(sql.toString(), new MapSqlParameterSource(), Integer.class);
  }

}
