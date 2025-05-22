package com.kh.bbs.svc.member;

import com.kh.bbs.dao.member.MemberDAO;
import com.kh.bbs.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberSVCImpl implements MemberSVC{

  private final MemberDAO memberDAO;

  /**
   * 회원 등록
   * @param member 등록할 회원 정보
   * @return 생성된 회원 ID
   */
  @Override
  public Long save(Member member) {
    return memberDAO.save(member);
  }

  /**
   * 이메일로 회원 조회
   * @param email 이메일
   * @return Optional<Member>
   */
  @Override
  public Optional<Member> findByEmail(String email) {
    return memberDAO.findByEmail(email);
  }

  /**
   * ID로 회원 조회
   * @param id 회원 ID
   * @return Optional<Member>
   */
  @Override
  public Optional<Member> findById(Long id) {
    return memberDAO.findById(id);
  }

  /**
   * 이메일 중복 여부 확인
   * @param email 이메일
   * @return true: 존재함, false: 존재하지 않음
   */
  @Override
  public boolean existsByEmail(String email) {
    return memberDAO.existsByEmail(email);
  }
}
