package kr.kh.tableup.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
import kr.kh.tableup.model.util.PageMaker;
import kr.kh.tableup.model.util.ResCriteria;
import kr.kh.tableup.model.vo.DefaultResTimeVO;
import kr.kh.tableup.model.vo.DetailFoodCategoryVO;
import kr.kh.tableup.model.vo.DetailRegionVO;
import kr.kh.tableup.model.vo.FacilityVO;
import kr.kh.tableup.model.vo.FileVO;
import kr.kh.tableup.model.vo.FoodCategoryVO;
import kr.kh.tableup.model.vo.MenuVO;
import kr.kh.tableup.model.vo.ResNewsVO;
import kr.kh.tableup.model.vo.ReservationVO;
import kr.kh.tableup.model.vo.RestaurantFacilityVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.model.vo.ReviewVO;
import kr.kh.tableup.model.vo.ScoreTypeVO;
import kr.kh.tableup.model.vo.TagVO;
import kr.kh.tableup.model.vo.UsFollowVO;
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

  @Value("${my-api-key}")
  private String apiKey;

  /** 로그인 */

  @GetMapping("/login")
  public String login(Model model, HttpServletRequest request) {
    String prevUrl = request.getHeader("Referer");
    System.out.println(prevUrl);
    if (prevUrl != null && !prevUrl.contains("/user/login")) {
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

  /** 회원가입 */
  @GetMapping("/signup")
  public String signupForm(Model model, @RequestParam(required = false, defaultValue = "false") boolean social,
      @RequestParam(required = false) String us_id) {
    model.addAttribute("social", social);
    model.addAttribute("url", "/signup");

    if (!model.containsAttribute("userVO")) {
      UserVO user = new UserVO();
      if (us_id != null)
        user.setUs_id(us_id);
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

    if(!userService.insertUser(user)){
        ra.addFlashAttribute("msg", "회원가입에 실패했습니다.");
        ra.addFlashAttribute("userVO", user);
        return "redirect:/user/signup";
    }
    return "redirect:/user/login";
  }

  /** 마이페이지 */
  @GetMapping("/mypage")
  public String mypage(Model model, Principal principal) {
    if (principal == null)
      return "user/mypage/indexnot";

    UserVO user = userService.getUserById(principal.getName());
    model.addAttribute("user", user);
    return "user/mypage/index";
  }

  @GetMapping("/edit")
  public String editForm(Model model, Principal principal) {
    if (principal == null)
      return "redirect:/user/login";

    UserVO user = userService.getUserById(principal.getName());
    model.addAttribute("user", user);
    return "user/edit";
  }

  @PostMapping("/edit")
  public String edit(@ModelAttribute UserVO user, Principal principal, RedirectAttributes ra) {
    if (principal == null)
      return "redirect:/user/login";

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

    // model.addAttribute("errorMsg", "에러입니다."); //post에서 이런식으로 에러 넘기면 될듯

    return "user/mypage/info";
  }

  @GetMapping("/detail")
  public String detailPage() {
    return "user/detail/detail";
  }

  @GetMapping("/review/insert")
  public String reviewpage(Model model, @RequestParam(required = false) Integer rt_num, @ModelAttribute String errorMsg,
      @ModelAttribute ReviewVO review, @AuthenticationPrincipal CustomUser customUser) {
    model.addAttribute("user", customUser.getUser());

    // model.addAttribute("errorMsg", "에러입니다."); //post에서 이런식으로 에러 넘기면 될듯
    
    if (rt_num != null) {
      model.addAttribute("restaurant", managerService.getResDetail(rt_num)); // 식당 정보
      model.addAttribute("rev_rt_num", rt_num); // 식당 번호 (만약 내 리뷰나 식당 페이지에서 넘어올땐 이거 이용해서 식당 번호 띄우기)
    }
    List<ScoreTypeVO> scoreTypeList = userService.getScoreType();
    System.out.println("scoreTypeList: " + scoreTypeList);
    model.addAttribute("scoreTypeList", scoreTypeList); // 평점 항목


    if(errorMsg != null && !errorMsg.isEmpty()) {
      model.addAttribute("errorMsg", errorMsg);
    }
    if (review != null) {
      model.addAttribute("review", review); // 리뷰 정보
    } else {
      model.addAttribute("review", new ReviewVO()); // 빈 리뷰 객체
    }

    return "user/review/insert";
  }

  @GetMapping("/review/insertsub")
  public String getRestaurantInfo(int rt_Num, Model model) {
    System.out.println("리뷰 작성 페이지로 이동, 식당 번호: " + rt_Num);
    RestaurantVO restaurant = managerService.getResDetail(rt_Num);
    if (restaurant == null) {
      model.addAttribute("error", "해당 식당 정보를 찾을 수 없습니다.");
    } else {
      model.addAttribute("restaurant", restaurant);
    }
    
    return "user/review/insertsub";
  }

  @PostMapping("/review/insertPost")
  //@ResponseBody
  public String insertReview(
      RedirectAttributes rttr,
      ReviewVO review,
      Map<String, String> scores, @RequestParam(required = false) List<MultipartFile> files,
      @RequestParam(required = false) List<String> fileNames, @RequestParam(required = false) List<String> fileTags,
      @AuthenticationPrincipal CustomUser user) {

    if(user.getUser() == null) {
      rttr.addFlashAttribute("errorMsg", "로그인이 필요합니다.");
      return "redirect:/user/login";
    }
    // 사용자 ID 또는 번호를 수동 세팅
    review.setRev_us_num(user.getUser().getUs_num());
    review.setUs_name(user.getUser().getUs_name());
    System.out.println("리뷰 작성자 번호 : " + review.getRev_us_num());
    if (!userService.insertReview(review)) {
      rttr.addFlashAttribute("errorMsg", "빈 내용이 있거나 문제가 생겨 리뷰 저장에 실패했습니다.");
      rttr.addFlashAttribute("review", review);
      return "redirect:/user/review/insert?rt_num=" + review.getRev_rt_num();
    }

    // 점수 저장
    for (Map.Entry<String, String> score : scores.entrySet()) {
      String key = score.getKey();
      if (key.startsWith("score[")) {
        try {
          int rs_st_num = Integer.parseInt(key.substring(6, key.length() - 1));
          int rs_score = Integer.parseInt(score.getValue());

          if (!userService.insertReviewScore(review, rs_st_num, rs_score)) {
            rttr.addFlashAttribute("errorMsg", "리뷰는 등록됐지만 점수 저장에 실패했습니다.");
            rttr.addFlashAttribute("review", review);
            return "redirect:/user/review/insert?rt_num=" + review.getRev_rt_num();
          }
        } catch (NumberFormatException e) {
          rttr.addFlashAttribute("errorMsg", "잘못된 점수 형식이 입력되었습니다.");
          rttr.addFlashAttribute("review", review);
          return "redirect:/user/review/insert?rt_num=" + review.getRev_rt_num();
        }
      }
    }

    // 파일 저장
    if (!userService.insertFile(review, files, fileNames, fileTags)) {
      String errorMsg = "리뷰는 등록됐지만 파일 저장에 실패했습니다.";
      System.out.println(errorMsg);
      rttr.addFlashAttribute("errorMsg", "리뷰는 등록됐지만 파일 저장에 실패했습니다.");
      rttr.addFlashAttribute("review", review);
      return "redirect:/user/review/insert?rt_num=" + review.getRev_rt_num();
    }

    return "redirect:/user/review/list";
  }


  
    @GetMapping("/list")
    public String list(Model model, 
            @RequestParam(required = false) Integer dreg_num, 
            @RequestParam(required = false) Integer dfc_num,
            @RequestParam(required = false) Integer reg_num,
            @RequestParam(required = false) Integer fc_num,
            @AuthenticationPrincipal CustomUser customUser) {
          //@RequestParam Map<String, Integer> paramMap 로 한꺼번에 받아와서 paramMap.get("dreg_num") 이런식으로 쓰는게 나을수도

        List<DetailRegionVO> regionList = userService.getRegionListWithWhole();
        List<DetailFoodCategoryVO> foodList = userService.getFoodCategoryListWithWhole();

        Map<String, List<TagVO>> tagList = userService.getTagList();
        List<FacilityVO> facilityList = userService.getFacilityList();

        List<ScoreTypeVO> scoreTypeList = userService.getScoreTypeList();

        System.out.println(dreg_num + " " + dfc_num + " " + reg_num + " " + fc_num);

        model.addAttribute("regionList", regionList);
        model.addAttribute("foodList", foodList);

        model.addAttribute("tagList", tagList);
        model.addAttribute("facilityList", facilityList);

        model.addAttribute("scoreTypeList", scoreTypeList);

        if(dreg_num != null)model.addAttribute("dreg_num", dreg_num);
        if(reg_num != null) model.addAttribute("reg_num", reg_num);
        if(dfc_num != null)model.addAttribute("dfc_num", dfc_num);  
        if(fc_num != null)model.addAttribute("fc_num", fc_num);  
        if(customUser != null && customUser.getUser() != null) {
            model.addAttribute("user", customUser.getUser());
        } else {
            model.addAttribute("user", new UserVO()); // 로그인하지 않은 경우 빈 UserVO 객체를 추가
        }

        System.out.println("리스트 호출");
        //System.out.println(dreg_num + " " + dfc_num + " " + reg_num); 
        //System.out.println(regionList);
        //System.out.println(scoreTypeList);

        return "user/list/list"; 
    }

  @PostMapping("/list/sub")
  public String listPost(Model model, @RequestBody ResCriteria cri,
      @AuthenticationPrincipal CustomUser customUser) {
    cri.setPerPageNum(2);   //차후 삭제
    // num를 서비스에게 주면서 게시판 번호에 맞는 게시글 목록 중 2개를 가져오라고 요청.
    System.out.println(cri);
    // System.out.println("태그: " + cri.getTagList());
    // System.out.println("시설: " + cri.getFacilityList());
    // System.out.println(cri.getPriceType());
    // System.out.println(cri.getMinPrice());
    // System.out.println(cri.getMaxPrice());
    // System.out.println(cri.getOrderBy());
    List<RestaurantVO> list = userService.getRestaurantList(cri);
    //System.out.println(list);
    // 서비스에게 현재 페이지 정보를 주고 PageMaker 객체를 달라고 요청
    PageMaker pm = userService.getPageMaker(cri);

    // 가져온 게시글 목록을 화면에 전송
    model.addAttribute("list", list);
    model.addAttribute("pm", pm);
    if(customUser != null && customUser.getUser() != null) {
        model.addAttribute("user", customUser.getUser());
    } else {
        model.addAttribute("user", new UserVO()); // 로그인하지 않은 경우 빈 UserVO 객체를 추가
    }

    // System.out.println("상세 호출");
    return "user/list/sublist";
  }

  @GetMapping("/list/detail/{rt_num}")
  public String restaurantDetail(@PathVariable("rt_num") int rt_num, Model model) {
    // RestaurantVO restaurant = userService.getRestaurantDetail(rt_num);
    // FoodCategoryVO foodCategory = userService.getFoodCategoryByRestaurant(rt_num);
    // DetailFoodCategoryVO detailFoodCategory = userService.getDetailFoodCategoryByRestaurant(rt_num);
    // TagVO tag = userService.getTagByRestaurant(rt_num);

    // model.addAttribute("restaurant", restaurant);
    // model.addAttribute("foodCategory", foodCategory);
    // model.addAttribute("detailFoodCategory", detailFoodCategory);
    // model.addAttribute("tag", tag);
    RestaurantVO restaurant = userService.getRestaurantDetail(rt_num);
    FoodCategoryVO foodCategory = userService.getFoodCategoryByRestaurant(rt_num);
    DetailFoodCategoryVO detailFoodCategory = userService.getDetailFoodCategoryByRestaurant(rt_num);
    TagVO tag = userService.getTagByRestaurant(rt_num);
    List<FacilityVO> facilityList = userService.getFacilityList();
    List<RestaurantFacilityVO> restaurantFacilityList = userService.getRestaurantFacilityList(rt_num);

    //System.out.println(apiKey);
    System.out.println("restaurant: " + restaurant);

    model.addAttribute("restaurant", restaurant);
    model.addAttribute("foodCategory", foodCategory);
    model.addAttribute("detailFoodCategory", detailFoodCategory);
    model.addAttribute("tag", tag);
    model.addAttribute("facilityList", facilityList);
    model.addAttribute("restaurantFacilityList", restaurantFacilityList);



    model.addAttribute("apiKey", apiKey); // API 키를 모델에 추가
    return "user/detail/detail";
  }

  

  @GetMapping("/review/view")
  public String allReviews(Model model) {
      List<ReviewVO> reviewList = userService.getReviewList();
      model.addAttribute("reviewList", reviewList);
      System.out.println("리뷰 리스트 : " + reviewList);
      return "user/review/view";
  }
  
  @GetMapping("/list/detail/outline")
  public String outline() {
      return "user/detail/outline";
  }
  @PostMapping("/list/news/{rt_num}")
  public String postrestaurantDetail(@PathVariable("rt_num") int rt_num) {
    System.out.println(rt_num);
    return "user/detail/news";
  }
  @PostMapping("/list/home/{rt_num}")
  public String listHome(@PathVariable("rt_num") int rt_num, Model model) {
    String today = new SimpleDateFormat("E", Locale.KOREA).format(new Date());
    model.addAttribute("today", today);
    
    RestaurantVO restaurant = userService.getRestaurantDetail(rt_num);
    FoodCategoryVO foodCategory = userService.getFoodCategoryByRestaurant(rt_num);
    DetailFoodCategoryVO detailFoodCategory = userService.getDetailFoodCategoryByRestaurant(rt_num);
    TagVO tag = userService.getTagByRestaurant(rt_num);
    // List<FacilityVO> facilityList = userService.getFacilityList(rt_num);
    List<RestaurantFacilityVO> restaurantFacilityList = userService.getRestaurantFacilityList(rt_num);
    List<ResNewsVO> resNewsList = userService.getResNewsList(rt_num);
    List<FileVO> fileList = userService.getFileList(rt_num);
    List<MenuVO> menuList = userService.getMenuList(rt_num);
    List<DefaultResTimeVO> defaultResTimeList = userService.getDefaultResTimeList(rt_num);

    //System.out.println(apiKey);
    System.out.println("restaurant: " + restaurant);

    model.addAttribute("restaurant", restaurant);
    model.addAttribute("foodCategory", foodCategory);
    model.addAttribute("detailFoodCategory", detailFoodCategory);
    model.addAttribute("tag", tag);
    // model.addAttribute("facilityList", facilityList);
    model.addAttribute("restaurantFacilityList", restaurantFacilityList);

    // model.addAttribute("apiKey", apiKey); // API 키
    model.addAttribute("resNewsList", resNewsList);
    model.addAttribute("fileList", fileList);
    model.addAttribute("menuList", menuList);
    model.addAttribute("defaultResTimeList", defaultResTimeList);
    return "user/detail/home";
  }

  @PostMapping("/follow")
  @ResponseBody
  public Map<String, Object> toggleLike(@RequestBody UsFollowVO follow, @AuthenticationPrincipal CustomUser customUser) {
    System.out.println("follow: " + follow);
    if (customUser == null || customUser.getUser() == null) {
      System.out.println("로그인하지 않은 사용자");
      return Map.of("error", "로그인이 필요합니다.");
    }
    if(follow == null || follow.getUf_type() == null || follow.getUf_foreign() <= 0) {
      System.out.println("잘못된 follow 정보: " + follow);
      return Map.of("error", "잘못된 요청입니다.");
    }
    follow.setUf_us_num(customUser.getUser().getUs_num()); // 현재 로그인한 사용자의 번호를 설정
    int num = userService.toggleFollow(follow);
    System.out.println("찜 처리 결과: " + num);

    if(num>0)return Map.of("liked", true);
    if(num<0)return Map.of("liked", false);
    return Map.of("error", "찜 처리에 실패했습니다.");
  }

  // 찜 목록 조회
  @GetMapping("/follow/list")
  @ResponseBody
  public List<UsFollowVO> getLikeList(String type, @RequestParam int us_num) {
    return userService.getFollowByUser(us_num); // 예: [3, 7, 12]
  }

  @PostMapping("/follow/check")
  @ResponseBody
  public ResponseEntity<Map<Integer, Boolean>> checkFollow(
      @RequestBody Map<String, Object> param,
      @AuthenticationPrincipal CustomUser customUser) {
    System.out.println(param);
    if (customUser == null || customUser.getUser() == null) {
      return ResponseEntity.badRequest().build();
    }
    System.out.println("111111111111111111111");
    String uf_type = (String) param.get("uf_type");
    List<Integer> uf_foreign_list;
    try {
      uf_foreign_list = (List<Integer>) param.get("uf_foreign_list");
    } catch (ClassCastException e) {
      uf_foreign_list = ((List<?>) param.get("uf_foreign_list"))
                          .stream()
                          .map(obj -> Integer.parseInt(obj.toString()))
                          .collect(Collectors.toList());
    }
    System.out.println(uf_foreign_list);

    if (uf_type == null || uf_foreign_list == null || uf_foreign_list.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    System.out.println("222222222222222222222");
    Map<Integer, Boolean> result = new HashMap<>();
    for (Integer rtNum : uf_foreign_list) {
      boolean liked = userService.isFollow(customUser.getUser().getUs_num(), uf_type, rtNum);
      System.out.println(liked);
      result.put(rtNum, liked);
    }
    System.out.println("333333333333333333333");

    return ResponseEntity.ok(result);
  }


  
}
