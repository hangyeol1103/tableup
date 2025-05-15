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
import kr.kh.tableup.model.vo.FacilityVO;


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
    return "admin/login";
  }

  @GetMapping("/region")
  public String regionPage() {
    return "admin/region";
  }
  @GetMapping("/detailregion")
  public String regionDetailPage() {
    return "admin/detailregion";
  }
  @GetMapping("/tag")
  public String tagPage() {
    return "admin/tag";
  }
  @GetMapping("/tagtype")
  public String tagTypePage() {
    return "admin/tagtype";
  }
  @GetMapping("/facility")
  public String facilityPage() {
    return "admin/facility";
  }
  @GetMapping("/menutype")
  public String menuTypePage() {
    return "admin/menutype";
  }
  @GetMapping("/foodcategory")
  public String foodCategoryPage() {
    return "admin/foodcategory";  
  }
  @GetMapping("/detailfoodcategory")
  public String detailFoodCategoryPage() {
    return "admin/detailfoodcategory";  
  }

  //지역 및 기타 태그들 추가
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

  @PostMapping("/detailregion/insert")
  @ResponseBody
  public Map<String, Object> insertDetailRegion(@RequestBody Map<String, String> param) {
    String name = param.get("detailRegionName");
    Map<String, Object> map = new HashMap<>();
    
    if (name == null || name.trim().isEmpty()) {
      map.put("success", false);
      map.put("message", "이름이 비어있습니다.");
      return map;
    }
    
    boolean result = adminService.insertDetailRegion(name);
    map.put("success", result);
    map.put("message", result ? "등록 완료" : "등록 실패");
    return map;
  }

  @PostMapping("/tag/insert")
  @ResponseBody
  public Map<String, Object> insertTag(@RequestBody Map<String, String> param) {
    String name = param.get("tagName");
    Map<String, Object> map = new HashMap<>();

    if (name == null || name.trim().isEmpty()) {
        map.put("success", false);
        map.put("message", "이름이 비어있습니다.");
        return map;
    }

    boolean result = adminService.insertTag(name);
    map.put("success", result);
    map.put("message", result ? "등록 완료" : "등록 실패");
    return map;
  }

  @PostMapping("/tagtype/insert")
  @ResponseBody
  public Map<String, Object> insertTagType(@RequestBody Map<String, String> param) {
    String name = param.get("tagTypeName");
    Map<String, Object> map = new HashMap<>();

    if (name == null || name.trim().isEmpty()) {
        map.put("success", false);
        map.put("message", "이름이 비어있습니다.");
        return map;
    }

    boolean result = adminService.insertTagType(name);
    map.put("success", result);
    map.put("message", result ? "등록 완료" : "등록 실패");
    return map;
  }

  @PostMapping("/facility/insert")
  @ResponseBody
  public Map<String, Object> insertFacility(@RequestBody FacilityVO facility) {
    Map<String, Object> map = new HashMap<>();
    if (facility.getFa_name() == null || facility.getFa_title() == null ||
        facility.getFa_name().trim().isEmpty() || facility.getFa_title().trim().isEmpty()) {
        map.put("success", false);
        map.put("message", "입력값 누락");
        return map;
    }

    boolean result = adminService.insertFacility(facility);
    map.put("success", result);
    map.put("message", result ? "등록 완료" : "등록 실패");
    return map;
  }

  @PostMapping("/menutype/insert")
  @ResponseBody
  public Map<String, Object> insertMenuType(@RequestBody Map<String, String> param) {
    String name = param.get("menuTypeName");
    Map<String, Object> map = new HashMap<>();

    if (name == null || name.trim().isEmpty()) {
        map.put("success", false);
        map.put("message", "이름이 비어있습니다.");
        return map;
    }

    boolean result = adminService.insertMenuType(name);
    map.put("success", result);
    map.put("message", result ? "등록 완료" : "등록 실패");
    return map;
  }

  @PostMapping("/foodcategory/insert")
  @ResponseBody
  public Map<String, Object> insertFoodCategory(@RequestBody Map<String, String> param) {
    String name = param.get("foodCategoryName");
    Map<String, Object> map = new HashMap<>();

    if (name == null || name.trim().isEmpty()) {
        map.put("success", false);
        map.put("message", "이름이 비어있습니다.");
        return map;
    }

    boolean result = adminService.insertFoodCategory(name);
    map.put("success", result);
    map.put("message", result ? "등록 완료" : "등록 실패");
    return map;
  }
  
  
  //조회
  @GetMapping("/region/list")
  @ResponseBody
  public List<String> getRegionList() {
    return adminService.getRegionList();
  }
  @GetMapping("/detailregion/list")
  @ResponseBody
  public List<String> getDetailRegionList() {
    return adminService.getDetailRegionList();
  }
  @GetMapping("/tag/list")
  @ResponseBody
  public List<String> getTagList() {
    return adminService.getTagList();
  }
  @GetMapping("/tagtype/list")
  @ResponseBody
  public List<String> getTagTypeList() {
    return adminService.getTagTypeList();
  }
  @GetMapping("/facility/list")
  @ResponseBody
  public List<FacilityVO> getFacilityList() {
    return adminService.getFacilityList();
  }
  @GetMapping("/menutype/list")
  @ResponseBody
  public List<String> getMenuTypeList() {
    return adminService.getMenuTypeList();
  }
  @GetMapping("/foodcategory/list")
  @ResponseBody
  public List<String> getFoodCategoryList() {
    return adminService.getFoodCategoryList();
  }
  
}
