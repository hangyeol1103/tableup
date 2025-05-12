package kr.kh.tableup.dao;

import kr.kh.tableup.model.vo.MemberVO;

public interface MemberDAO {

  MemberVO selectMember(String id);

  boolean insertMember(MemberVO member);
  
}
