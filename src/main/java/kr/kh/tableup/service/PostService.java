package kr.kh.tableup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.kh.tableup.dao.PostDAO;

@Service
public class PostService {
  
  @Autowired
  PostDAO postDAO;
}
