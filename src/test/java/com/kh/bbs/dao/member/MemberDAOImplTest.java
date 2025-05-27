package com.kh.bbs.dao.member;

import com.kh.bbs.domain.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@SpringBootTest
@Transactional  // 테스트 후 자동 rollback
class MemberDAOImplTest {

  @Autowired
  private MemberDAO memberDAO;

  @Test
  @DisplayName("회원 저장")
  void save() {
    Member member = new Member();
    member.setEmail("test100@kh.com"); // 중복 방지
    member.setPassword("1234");
    member.setNickname("댓글테스터");

    Long savedId = memberDAO.save(member);
    log.info("savedId={}", savedId);

    Assertions.assertThat(savedId).isNotNull();

    Optional<Member> finded = memberDAO.findById(savedId);
    Assertions.assertThat(finded).isPresent();
  }

  @Test
  @DisplayName("이메일로 회원 조회")
  void findByEmail() {
    String email = "test200@kh.com";
    Member member = new Member();
    member.setEmail(email);
    member.setPassword("1234");
    member.setNickname("별칭");

    Long id = memberDAO.save(member);

    Optional<Member> finded = memberDAO.findByEmail(email);
    Assertions.assertThat(finded).isPresent();
  }

  @Test
  @DisplayName("회원 존재 확인 by 이메일")
  void existsByEmail() {
    String email = "test300@kh.com";
    Member member = new Member();
    member.setEmail(email);
    member.setPassword("1234");
    member.setNickname("별칭");

    memberDAO.save(member);

    boolean exists = memberDAO.existsByEmail(email);
    Assertions.assertThat(exists).isTrue();
  }

}
