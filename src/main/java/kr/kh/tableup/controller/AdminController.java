package kr.kh.tableup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.kh.tableup.service.AdminService;

import kr.kh.tableup.model.vo.AdminVO;


@Controller
@RequestMapping("/admin")
public class AdminController {

  @Autowired
  AdminService adminService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @GetMapping("")
  public String adminHome() {
    return "admin/admin";
  }

  @GetMapping("/signup")
  public String adminSignupForm(Model model) {
    model.addAttribute("url", "/admin/signup");
    return "admin/signup";
  }

  @PostMapping("/signup")
  public String adminSignup(AdminVO admin) {
    admin.setAd_pw(passwordEncoder.encode(admin.getAd_pw()));
    adminService.insertAdmin(admin);
    return "redirect:/admin";
  }
  @GetMapping("/login")
  public String adminLogin() {
    return "/admin/login";
  }

  @GetMapping("/region")
  public String regionPage() {
    return "admin/region";
  }

  //지역 추가
  @PostMapping("/region/insert")
  @ResponseBody
  public Map<String, Object> insertRegion(@RequestBody Map<String, String> param) {
    String name = param.get("regionName");
    Map<String, Object> map = new HashMap<>();

    if (name == null || name.trim().isEmpty()) {
        map.put("success", false);
        map.put("message", "이름이 비어있습니다.");
        return map;
    }

    boolean result = adminService.insertRegion(name);
    map.put("success", result);
    map.put("message", result ? "등록 완료" : "등록 실패");
    return map;
  }

  //지역 조회
  @GetMapping("/region/list")
  @ResponseBody
  public List<String> getRegionList() {
    return adminService.getRegionList();
  }

}
