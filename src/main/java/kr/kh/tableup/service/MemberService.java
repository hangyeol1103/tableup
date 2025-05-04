package kr.kh.tableup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.MemberDAO;
import kr.kh.tableup.model.vo.MemberVO;

@Service
public class MemberService {
  
  @Autowired
  MemberDAO memberDAO;

  public boolean insertMember(MemberVO member) {
    return memberDAO.insertMember(member);
  }
}
