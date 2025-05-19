package kr.kh.tableup.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.kh.tableup.model.util.CustomUser;
import kr.kh.tableup.model.vo.FileVO;
import kr.kh.tableup.model.vo.FoodCategoryVO;
import kr.kh.tableup.model.vo.RegionVO;
import kr.kh.tableup.model.vo.ReservationVO;
import kr.kh.tableup.model.vo.RestaurantDetailVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.model.vo.ReviewVO;
import kr.kh.tableup.model.vo.ScoreTypeVO;
import kr.kh.tableup.model.vo.UserVO;
import kr.kh.tableup.service.ManagerService;
import kr.kh.tableup.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ManagerService managerService;

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
    public String signupForm(Model model, @RequestParam(required = false, defaultValue = "false") boolean social, @RequestParam(required = false) String us_id) {
        model.addAttribute("social", social);
        model.addAttribute("url", "/signup");

        if (!model.containsAttribute("userVO")) {
            UserVO user = new UserVO();
            if (us_id != null) user.setUs_id(us_id);
            model.addAttribute("userVO", user);
        }
        return "user/signup";
    }


    @PostMapping("/signupPost")
    public String signup(@Valid @ModelAttribute("userVO") UserVO user, BindingResult result, RedirectAttributes ra) {

        System.out.println("유효성 검사 오류 수: " + result.getErrorCount());

        if (!user.getUs_pw().equals(user.getUs_pw_check())) {
            result.rejectValue("us_pw_check", "password.mismatch", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        if (result.hasErrors()) {
            String msg = result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(" "));
            ra.addFlashAttribute("msg", msg);
            ra.addFlashAttribute("userVO", user);
            return "redirect:/user/signup";
        }

        boolean dbResult = userService.validateUser(user, ra);
        if (!dbResult) {
            ra.addFlashAttribute("userVO", user);
            return "redirect:/user/signup";
        }

        userService.insertUser(user);
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
	public String myinfo(Model model, @AuthenticationPrincipal CustomUser customUser) {
		model.addAttribute("user", customUser.getUser());

        //model.addAttribute("errorMsg", "에러입니다.");        //post에서 이런식으로 에러 넘기면 될듯

        return "user/mypage/info";
	}

    @GetMapping("/review/insert")
	public String reviewpage(@RequestParam(required = false) Integer rt_num, Model model, @AuthenticationPrincipal CustomUser customUser) {
		model.addAttribute("user", customUser.getUser());

        //model.addAttribute("errorMsg", "에러입니다.");        //post에서 이런식으로 에러 넘기면 될듯

        
        List<ScoreTypeVO> scoreTypeList = userService.getScoreType();
        System.out.println("scoreTypeList: " + scoreTypeList);          


        if(rt_num != null){
            model.addAttribute("restaurant", managerService.getResDetail(rt_num)); // 식당 정보
            model.addAttribute("rev_rt_num", rt_num);       // 식당 번호            (만약 내 리뷰나 식당 페이지에서 넘어올땐 이거 이용해서 식당 번호 띄우기)
        }else{

        }
        model.addAttribute("scoreTypeList", scoreTypeList);    // 평점 항목

        return "user/review/insert";
	}

    @GetMapping("/review/insertsub")
    public String getRestaurantInfo(@RequestParam("rt_num") int rt_Num, Model model) {
        RestaurantVO restaurant = managerService.getResDetail(rt_Num);
        if (restaurant == null) {
            model.addAttribute("error", "해당 식당 정보를 찾을 수 없습니다.");
        } else {
            model.addAttribute("restaurant", restaurant);
        }
        return "user/review/insertsub";  
    }


    @PostMapping("/review/insertPost")
    public String insertReview(
        Model model,
        @ModelAttribute ReviewVO review,
        Map<String, String> allParams,@RequestParam(required=false) List<MultipartFile> files,@RequestParam(required=false) List<String> fileNames,@RequestParam(required=false) List<String> fileTags,
        @AuthenticationPrincipal CustomUser user
    ) {
        
        // 사용자 ID 또는 번호를 수동 세팅
        review.setRev_us_num(user.getUser().getUs_num()); 
        System.out.println("리뷰 작성자 번호 : " + review.getRev_us_num());
        if (!userService.insertReview(review)) {
            model.addAttribute("errorMsg", "빈 내용이 있거나 문제가 생겨 리뷰 저장에 실패했습니다.");
            model.addAttribute("review", review);
            return "user/review/insert";
        }


        // 점수 저장
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("score[")) {
                try {
                    int rs_st_num = Integer.parseInt(key.substring(6, key.length() - 1));
                    int rs_score = Integer.parseInt(entry.getValue());

                    if (!userService.insertReviewScore(review, rs_st_num, rs_score)) {
                        model.addAttribute("errorMsg", "리뷰는 등록됐지만 점수 저장에 실패했습니다.");
                        return "user/review/insert";
                    }
                } catch (NumberFormatException e) {
                    model.addAttribute("errorMsg", "잘못된 점수 형식이 입력되었습니다.");
                    return "user/review/insert";
                }
            }
        }

        // 파일 저장
        if(!userService.insertFile(review, files, fileNames, fileTags)) {
            String errorMsg = "리뷰는 등록됐지만 파일 저장에 실패했습니다.";
            System.out.println(errorMsg);
            model.addAttribute("errorMsg", errorMsg);
            return "user/review/insert"; // 리뷰 등록 실패
        }

        return "user/review/list";
    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(required = false) Integer dreg_num, @RequestParam(required = false) Integer dfc_num) {

        List<RegionVO> regionList = userService.getRegionList();
        List<FoodCategoryVO> foodList = userService.getFoodCategoryList();

        model.addAttribute("regionList", regionList);
        model.addAttribute("foodList", foodList);

        
        //model.addAttribute("param", new FilterParam(regionNum, foodNum)); 

        return "user/list/list"; 
    }

}
