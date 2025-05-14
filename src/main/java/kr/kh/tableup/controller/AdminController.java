package kr.kh.tableup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.kh.tableup.service.AdminService;

import kr.kh.tableup.model.vo.AdminVO;


@Controller
@RequestMapping("/admin")
public class AdminController {

  @Autowired
  AdminService adminService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @GetMapping("/")
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

}
