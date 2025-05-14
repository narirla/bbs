package com.kh.bbs.dao;

import com.kh.bbs.domain.entity.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BoardDAOImpl implements BoardDAO{

  final private NamedParameterJdbcTemplate template;

  //수동 매핑
  RowMapper<Board> boardRowMapper(){

    return (rs, rowNum) -> {
      Board board = new Board();
      board.setId(rs.getLong("Id"));
      board.setTitle(rs.getString("title"));
      board.setContent(rs.getString("content"));
      board.setAuthor(rs.getString("author"));
      board.setCreatedDate(rs.getTimestamp("created_date"));
      board.setUpdatedDate(rs.getTimestamp("updated_date"));
      return board;
    };
  }

  /**
   * 게시글 등록
   * @param board
   * @return 게시글 번호
   */
  @Override
  public Long save(Board board) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO board (id, title, content, author, created_date, updated_date) ");
    sql.append("      VALUES (board_seq.nextval, :title, :content, :author, :createdDate, :updatedDate)");

    // BeanPropertySqlParameterSource : 자바 객체 필드명과 SQL 파라미터명이 같을 때 자동 매핑
    BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(board);

    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql.toString(),param,keyHolder, new String[]{"id"});

    // 생성된 ID 값 반환
    Number bidNumber = (Number) keyHolder.getKeys().get("id");
    return bidNumber.longValue();
  }

  @Override
  public List<Board> findAll() {
    return List.of();
  }

  @Override
  public Optional<Board> findById(Long id) {
    return Optional.empty();
  }

  @Override
  public int deleteById(Long id) {
    return 0;
  }

  @Override
  public int deleteByIds(List<Long> ids) {
    return 0;
  }

  @Override
  public int updateById(Long boardId, Board board) {
    return 0;
  }
}
