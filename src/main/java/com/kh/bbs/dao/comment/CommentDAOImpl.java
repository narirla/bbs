package com.kh.bbs.dao.comment;

import com.kh.bbs.domain.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentDAOImpl implements CommentDAO {

  private final NamedParameterJdbcTemplate template;

  //수동매핑
  private RowMapper<Comment> commentRowMapper() {
    return (rs, rowNum) -> {
      Comment comment = new Comment();
      comment.setId(rs.getLong("id"));
      comment.setBoardId(rs.getLong("board_id"));
      comment.setCommenterId(rs.getLong("commenter_id"));
      comment.setContent(rs.getString("content"));
      comment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
      if (rs.getTimestamp("updated_at") != null) {
        comment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
      }
      comment.setNickname(rs.getString("nickname"));
      return comment;
    };
  }

  /**
   * 댓글 저장
   * @param comment 저장할 댓글
   * @return 생성된 댓글 ID
   */
  @Override
  public Long save(Comment comment) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO comments (board_id, commenter_id, content) ");
    sql.append("      VALUES (:boardId, :commenterId, :content ) ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("boardId", comment.getBoardId())
        .addValue("commenterId", comment.getCommenterId())
        .addValue("content", comment.getContent());

    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql.toString(),param,keyHolder, new String[]{"id"});
    return keyHolder.getKey().longValue();
  }

  /**
   * 특정 게시글의 댓글 목록 조회
   * @param boardId 게시글 ID
   * @return 댓글 목록
   */
  @Override
  public List<Comment> findByBoardId(Long boardId) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT c.id, c.board_id, c.commenter_id, c.content, c.created_at, c.updated_at, m.nickname ");
    sql.append("  FROM comments c ");
    sql.append(" INNER JOIN member m ON c.commenter_id = m.id ");
    sql.append(" WHERE c.board_id = :boardId ");
    sql.append(" ORDER BY c.id ASC ");

    return template.query(sql.toString(), Map.of("boardId",boardId),commentRowMapper());
  }

  /**
   * 특정 게시글의 댓글 목록 조회 (페이징)
   * @param boardId 게시글 ID
   * @param startRow 시작 행 번호
   * @param endRow 끝 행 번호
   * @return 댓글 목록
   */
  @Override
  public List<Comment> findByBoardId(Long boardId, int startRow, int endRow) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT * FROM ( ");
    sql.append("  SELECT ROWNUM rnum, a.* FROM ( ");
    sql.append("    SELECT c.id, c.board_id, c.commenter_id, c.content, c.created_at, c.updated_at, m.nickname ");
    sql.append("      FROM comments c ");
    sql.append("     INNER JOIN member m ON c.commenter_id = m.id ");
    sql.append("     WHERE c.board_id = :boardId ");
    sql.append("     ORDER BY c.id DESC ");
    sql.append("  ) a WHERE ROWNUM <= :endRow ");
    sql.append(") WHERE rnum >= :startRow ");

    Map<String, Object> param = Map.of(
        "boardId", boardId,
        "startRow", startRow,
        "endRow", endRow
    );

    return template.query(sql.toString(), param, commentRowMapper());
  }

  /**
   * 특정 게시글의 총 댓글 수 조회
   * @param boardId 게시글 ID
   * @return 댓글 수
   */
  @Override
  public int totalCountByBoardId(Long boardId) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT COUNT(*) ");
    sql.append("  FROM comments ");
    sql.append(" WHERE board_id = :boardId ");

    return template.queryForObject(sql.toString(), Map.of("boardId", boardId), Integer.class);
  }


  /**
   * 댓글 단건 조회
   * @param id 댓글 ID
   * @return Optional<Comment>
   */
  @Override
  public Optional<Comment> findById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT c.id, c.board_id, c.commenter_id, c.content, c.created_at, c.updated_at, m.nickname ");
    sql.append("  FROM comments c ");
    sql.append("  INNER JOIN member m ON c.commenter_id = m.id ");
    sql.append(" WHERE c.id = :id ");

    List<Comment> result = template.query(sql.toString(), Map.of("id", id), commentRowMapper());
    return result.stream().findFirst();
  }


  /**
   * 댓글 수정
   * @param id 댓글 ID
   * @param content 수정할 내용
   * @return 수정된 행의 수
   */
  @Override
  public int update(Long id, String content) {
    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE comments ");
    sql.append("   SET content = :content, updated_at = SYSDATE ");
    sql.append(" WHERE id = :id ");

    return template.update(sql.toString(),Map.of("content",content,"id",id));
  }

  /**
   * 댓글 삭제
   * @param id 댓글 ID
   * @return 삭제된 행 수
   */
  @Override
  public int delete(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append(" DELETE FROM comments ");
    sql.append(" WHERE id = :id ");

    return template.update(sql.toString(), Map.of("id",id));
  }
}
