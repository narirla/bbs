package com.kh.bbs.dao.member;

import com.kh.bbs.domain.entity.Member;
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
public class MemberDAOImpl implements MemberDAO{

  private final NamedParameterJdbcTemplate template;

  private RowMapper<Member> memberRowMapper(){
    return (rs, rowNum) -> {
      Member member = new Member();
      member.setId(rs.getLong("id"));
      member.setEmail(rs.getString("email"));
      member.setPassword(rs.getString("password"));
      member.setNickname(rs.getString("nickname"));
      member.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
      return member;
    };
  }

  //회원 등록
  @Override
  public Long save(Member member) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO member (email, password, nickname)  ");
    sql.append("       VALUES (:email, :password, :nickname)  ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("email", member.getEmail())
        .addValue("password", member.getPassword())
        .addValue("nickname", member.getNickname());

    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql.toString(), param, keyHolder, new String[]{"id"});
    return keyHolder.getKey().longValue();
  }

  //이메일로 회원 조회
  @Override
  public Optional<Member> findByEmail(String email) {
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT * FROM member ");
    sql.append("  WHERE email = :email ");

    List<Member> result = template.query(sql.toString(), Map.of("email",email),memberRowMapper());
    return result.stream().findFirst();
  }

  //ID로 회원 조회
  @Override
  public Optional<Member> findById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT * FROM member ");
    sql.append("  WHERE id = :id ");

    List<Member> result = template.query(sql.toString(), Map.of("id", id), memberRowMapper());
    return result.stream().findFirst();
  }

  //이메일 존재 여부 확인
  @Override
  public boolean existsByEmail(String email) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT count(*) ");
    sql.append("  FROM member ");
    sql.append(" WHERE email = :email ");

    Integer count = template.queryForObject(sql.toString(), Map.of("email", email), Integer.class);
    return count != null && count > 0;
  }
}
