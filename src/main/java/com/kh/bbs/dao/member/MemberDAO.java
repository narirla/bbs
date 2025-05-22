package com.kh.bbs.dao.member;

import com.kh.bbs.domain.entity.Member;
import java.util.Optional;

public interface MemberDAO {

  /**
   * 회원 등록
   * @param member 회원 정보
   * @return 생성된 회원 ID
   */
  Long save(Member member);

  /**
   * 이메일로 회원 조회
   * @param email 이메일
   * @return Optional<Member>
   */
  Optional<Member> findByEmail(String email);

  /**
   * 회원 ID로 회원 조회
   * @param id 회원 ID
   * @return Optional<Member>
   */
  Optional<Member> findById(Long id);

  /**
   * 이메일 존재 여부 확인
   * @param email 이메일
   * @return true: 존재함, false: 존재하지 않음
   */
  boolean existsByEmail(String email);
}
