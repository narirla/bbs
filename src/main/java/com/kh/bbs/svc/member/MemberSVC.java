package com.kh.bbs.svc.member;

import com.kh.bbs.domain.entity.Member;

import java.util.Optional;

public interface MemberSVC {

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
   * 회원 ID로 조회
   * @param id 회원 ID
   * @return Optional<Member>
   */
  Optional<Member> findById(Long id);

  /**
   * 이메일 중복 여부 확인
   * @param email 이메일
   * @return true: 존재함, false: 존재하지 않음
   */
  boolean existsByEmail(String email);

  /**
   * 로그인 처리
   * 입력된 이메일과 비밀번호로 회원을 인증합니다.
   * @param email 로그인 이메일
   * @param password 로그인 비밀번호
   * @return Optional<Member> 일치하는 회원이 있으면 반환, 없으면 빈 Optional
   */
  Optional<Member> login(String email, String password);
}
