package kr.kh.tableup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.MemberDAO;

@Service
public class MemberService {
  
  @Autowired
  MemberDAO memberDAO;
}
