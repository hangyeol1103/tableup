package kr.kh.tableup.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import kr.kh.tableup.model.util.CustomUser;
import kr.kh.tableup.model.vo.ReservationVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.model.vo.ReviewVO;
import kr.kh.tableup.model.vo.UserVO;
import kr.kh.tableup.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /** 로그인*/
    

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
      String prevUrl = request.getHeader("Referer");
      System.out.println(prevUrl);
      if(prevUrl != null && !prevUrl.contains("/user/login")) {		
        request.getSession().setAttribute("prevUrl", prevUrl);	
      }
    
      Object loginError = request.getSession().getAttribute("loginError");
      Object loginId = request.getSession().getAttribute("loginId");


      model.addAttribute("loginError", loginError);
      model.addAttribute("loginId", loginId);  
   
     
      if (loginError != null) {
          request.getSession().removeAttribute("loginError");
      }
      if (loginId != null) {
          request.getSession().removeAttribute("loginId");
      }
      


      System.out.println(loginId);
      System.out.println(loginError);
      return "user/login";
    }


    /** 회원가입*/
    @GetMapping("/signup")
    public String signupForm(Model model, @RequestParam(required = false) String us_id) {
      model.addAttribute("url", "/signup");
      model.addAttribute("us_id", us_id);
      return "user/signup";
    }

    @PostMapping("/signupPost")
    public String signup(@ModelAttribute UserVO user, RedirectAttributes ra) {
      boolean result = userService.registerUser(user);
      ra.addFlashAttribute("msg", result ? "회원가입이 완료되었습니다." : "회원가입에 실패했습니다.");
      return "redirect:/user/login";
    }

    /** 마이페이지 */
    @GetMapping("/mypage")
    public String mypage(Model model, Principal principal) {
        if (principal == null) return "user/mypage/indexnot";

        UserVO user = userService.getUserById(principal.getName());
        model.addAttribute("user", user);
        return "user/mypage/index";
    }

    @GetMapping("/edit")
    public String editForm(Model model, Principal principal) {
        if (principal == null) return "redirect:/user/login";

        UserVO user = userService.getUserById(principal.getName());
        model.addAttribute("user", user);
        return "user/edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute UserVO user, Principal principal, RedirectAttributes ra) {
        if (principal == null) return "redirect:/user/login";

        user.setUs_id(principal.getName()); // ID 보안
        boolean result = userService.updateUser(user);

        ra.addFlashAttribute("msg", result ? "회원정보가 수정되었습니다." : "수정에 실패했습니다.");
        return "redirect:/user/mypage";
    }

    /** 중복 검사 */
    @PostMapping("/check-duplicate")
    @ResponseBody
    public Map<String, Boolean> checkDuplicate(@RequestBody Map<String, String> map) {
        String type = map.get("type");
        String value = map.get("value");

        boolean isDuplicate = userService.isDuplicate(type, value);

        Map<String, Boolean> res = new HashMap<>();
        res.put("duplicate", isDuplicate);
        return res;

    }

  @PostMapping("/mypage/rev")
  public String reviewList(Model model, Principal principal) {
      if (principal == null) {
          return "user/mypage/sub/revsub";
      }

      String username = principal.getName();
      UserVO user = userService.getUserById(username); 
      if (user == null) {
          return "user/mypage/sub/revsub";
      }

      List<ReviewVO> list = userService.getReviewByUser(user.getUs_num());
      System.out.println("불러온 리스트 : " + list);
      model.addAttribute("reviews", list);
      return "user/mypage/sub/revsub";
  }

  
  @PostMapping("/mypage/res")
  public String reservationList(Model model, Principal principal) {
      if (principal == null) {
          return "user/mypage/sub/ressub";
      }

      String username = principal.getName();
      UserVO user = userService.getUserById(username); 
      if (user == null) {
          return "user/mypage/sub/ressub";
      }

      List<ReservationVO> list = userService.getReservationByUser(user.getUs_num());
      System.out.println("불러온 리스트 : " + list);
      model.addAttribute("reservations", list);
      return "user/mypage/sub/ressub";
  }

  @PostMapping("/mypage/flwrst")
  public String followRestaurantList(Model model, Principal principal) {
      if (principal == null) {
          return "user/mypage/sub/flwrst";
      }

      String username = principal.getName();
      UserVO user = userService.getUserById(username); 
      if (user == null) {
          return "user/mypage/sub/flwrst";
      }

      List<RestaurantVO> list = userService.getFollowedRestaurant(user.getUs_num());
      System.out.println("찜한 리스트 : " + list);
      model.addAttribute("frestaurants", list);
      return "user/mypage/sub/flwrst";
  }

  @PostMapping("/mypage/flwrvw")
  public String followReviewList(Model model, Principal principal) {
      if (principal == null) {
          return "user/mypage/sub/flwrvw";
      }

      String username = principal.getName();
      UserVO user = userService.getUserById(username); 
      if (user == null) {
          return "user/mypage/sub/flwrvw";
      }

      List<ReviewVO> list = userService.getFollowedReview(user.getUs_num());
      System.out.println("찜한 리스트 : " + list);
      model.addAttribute("freviews", list);
      return "user/mypage/sub/flwrvw";
  }

  	@GetMapping("/info")
	public String mypage(Model model, @AuthenticationPrincipal CustomUser customUser) {
		model.addAttribute("user", customUser.getUser());

        return "user/mypage/info";
	}




}
