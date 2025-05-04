package kr.kh.tableup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kr.kh.tableup.model.vo.PostVO;
import kr.kh.tableup.service.PostService;


@Controller
public class PostController {
  
  @Autowired
  PostService postService;

  @GetMapping("/post/list/{bo_num}")
  public String postList(Model model, @PathVariable int bo_num) {
    List<PostVO> list = postService.getPostList(bo_num);
    model.addAttribute("list", list);
    return "post/list";
  }
  
}
