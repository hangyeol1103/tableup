package kr.kh.tableup.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.kh.tableup.model.DTO.ReviewDTO;
import kr.kh.tableup.model.util.CustomUser;
import kr.kh.tableup.model.util.PageMaker;
import kr.kh.tableup.model.util.ResCriteria;
import kr.kh.tableup.model.vo.BusinessDateVO;
import kr.kh.tableup.model.vo.BusinessHourVO;
import kr.kh.tableup.model.vo.DefaultResTimeVO;
import kr.kh.tableup.model.vo.DetailFoodCategoryVO;
import kr.kh.tableup.model.vo.DetailRegionVO;
import kr.kh.tableup.model.vo.FacilityVO;
import kr.kh.tableup.model.vo.FileVO;
import kr.kh.tableup.model.vo.FoodCategoryVO;
import kr.kh.tableup.model.vo.MenuTypeVO;
import kr.kh.tableup.model.vo.MenuVO;
import kr.kh.tableup.model.vo.ResNewsVO;
import kr.kh.tableup.model.vo.ReservationVO;
import kr.kh.tableup.model.vo.RestaurantDetailVO;
import kr.kh.tableup.model.vo.RestaurantFacilityVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.model.vo.ReviewVO;
import kr.kh.tableup.model.vo.ScoreTypeVO;
import kr.kh.tableup.model.vo.TagVO;
import kr.kh.tableup.model.vo.UsFollowVO;
import kr.kh.tableup.model.vo.UserVO;
import kr.kh.tableup.service.FileService;
import kr.kh.tableup.service.MailService;
import kr.kh.tableup.service.ManagerService;
import kr.kh.tableup.service.ReservationService;
import kr.kh.tableup.service.RestaurantService;
import kr.kh.tableup.service.ReviewService;
import kr.kh.tableup.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private RestaurantService restaurantService;

  @Autowired
  private ReservationService reservationService;

  @Autowired
  private ManagerService managerService;

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private FileService fileService;

  @Autowired
  private MailService mailService;

  @Value("${my-api-key}")
  private String apiKey;
  
  /** 로그인 */

  @GetMapping("/login")
  public String login(Model model, HttpServletRequest request, @AuthenticationPrincipal CustomUser customUser) {
    String prevUrl = request.getHeader("Referer");
    
    System.out.println(prevUrl);
    if (prevUrl != null && !prevUrl.contains("/user/login")) {
      request.getSession().setAttribute("prevUrl", prevUrl);
    }

    if(customUser!=null&&customUser.getUser()!=null)  return "redirect:/";

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
      @RequestParam(required = false) String us_id, @AuthenticationPrincipal CustomUser customUser) {

    if(customUser!=null&&customUser.getUser()!=null)  return "redirect:/";


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

  @PostMapping("/email/dup")
  @ResponseBody
  public String emailIsDuplicated(@RequestParam String us_email) {
    boolean exists = userService.isDuplicate("email", us_email);
    return exists ? "duplicate" : null;
  }

  @PostMapping("/email/send")
  @ResponseBody
  public String emailSend(@RequestParam String us_email) {
    System.out.println(us_email);
    String ev_key = mailService.sendEmail(us_email.trim());
    System.out.println(ev_key);
    if(ev_key == null || ev_key.length() < 5) return null;
    return ev_key;
  }

  @PostMapping("/email/check")
  @ResponseBody
  public String emailCheck(@RequestParam String ev_key, @RequestParam String code) {
    return mailService.checkEmail(ev_key, code);
  }

  @PostMapping("/signupPost")
  public String signup(@Valid @ModelAttribute UserVO user, BindingResult result, RedirectAttributes ra) {

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
  public String mypage(Model model, @AuthenticationPrincipal CustomUser customUser, @RequestParam(required = false) String tab) {
    if(customUser==null||customUser.getUser()==null)
      return "user/mypage/indexnot";

    UserVO user = userService.getUserById(customUser.getUser().getUs_id());
    model.addAttribute("user", user);
    if(tab!=null)model.addAttribute("tab", tab);
    
    return "user/mypage/index";
  }

  @GetMapping("/edit")
  public String editForm(Model model, @AuthenticationPrincipal CustomUser customUser) {
    if (customUser == null || customUser.getUser() == null)
      return "redirect:/user/login";

    UserVO user = userService.getUserById(customUser.getUser().getUs_id());
    model.addAttribute("user", user);
    
    String token = UUID.randomUUID().toString().substring(0, 6); // 인증번호 6자리

    // QR 코드용 URI
    String smsUri = "sms:01012345678?body=" + 100000000 + " " + token;

    // QR 코드 생성 후 base64로 인코딩된 이미지 반환
    String qrImage = userService.generateQrBase64(smsUri);

    model.addAttribute("map", Map.of("token", token, "qr", qrImage));

    return "user/mypage/edit";
  }

  @PostMapping("/edit")
  public String edit(@ModelAttribute UserVO user, @AuthenticationPrincipal CustomUser customUser, RedirectAttributes ra) {
    if (customUser == null || customUser.getUser() == null)
      return "redirect:/user/login";

    user.setUs_id(customUser.getUser().getUs_id()); // ID 보안
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
  public String reviewList(Model model, @AuthenticationPrincipal CustomUser customUser) {
    if (customUser == null || customUser.getUser() == null) {
      return "user/mypage/sub/revsub";
    }

    String us_id = customUser.getUser().getUs_id();
    UserVO user = userService.getUserById(us_id);
    if (user == null) {
      return "user/mypage/sub/revsub";
    }

    List<ReviewVO> list = userService.getReviewByUser(user.getUs_num());
    System.out.println("불러온 리스트 : " + list);
    model.addAttribute("reviews", list);
    return "user/mypage/sub/revsub";
  }

  @PostMapping("/mypage/res")
  public String reservationList(Model model, @AuthenticationPrincipal CustomUser customUser) {
    if (customUser == null || customUser.getUser() == null) {
      return "user/mypage/sub/ressub";
    }

    String us_id = customUser.getUser().getUs_id();
    UserVO user = userService.getUserById(us_id);
    if (user == null) {
      return "user/mypage/sub/ressub";
    }

    List<ReservationVO> list = userService.getReservationByUser(user.getUs_num());
    System.out.println("불러온 리스트 : " + list);
    model.addAttribute("reservations", list);
    return "user/mypage/sub/ressub";
  }

  @PostMapping("/mypage/flwrst")
  public String followRestaurantList(Model model, @AuthenticationPrincipal CustomUser customUser) {
    if (customUser == null || customUser.getUser() == null) {
      return "user/mypage/sub/flwrst";
    }

    String us_id = customUser.getUser().getUs_id();
    UserVO user = userService.getUserById(us_id);
    if (user == null) {
      return "user/mypage/sub/flwrst";
    }

    List<RestaurantVO> list = userService.getFollowedRestaurant(user.getUs_num());
    System.out.println("찜한 리스트 : " + list);
    model.addAttribute("frestaurants", list);
    return "user/mypage/sub/flwrst";
  }

  @PostMapping("/mypage/flwrvw")
  public String followReviewList(Model model, @AuthenticationPrincipal CustomUser customUser) {
    if (customUser == null || customUser.getUser() == null) {
      return "user/mypage/sub/flwrvw";
    }

    String us_id = customUser.getUser().getUs_id();
    UserVO user = userService.getUserById(us_id);
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

    //어차피 user null이면 못들어오잖아
    model.addAttribute("user", userService.getUserProfileImage(customUser.getUser().getUs_num()));
    System.out.println(userService.getUserProfileImage(customUser.getUser().getUs_num()));
    // model.addAttribute("errorMsg", "에러입니다."); //post에서 이런식으로 에러 넘기면 될듯

    return "user/mypage/info";
  }

  @PostMapping("/updateInfo")
  public String updateInfo(@ModelAttribute UserVO user, @AuthenticationPrincipal CustomUser customUser) {
    if (customUser == null || customUser.getUser() == null)
      return "redirect:/user/login";

    user.setUs_id(customUser.getUser().getUs_id()); // ID 보안
    boolean result = userService.updateUserInfo(user);

    return "user/mypage/info";
  }

  @GetMapping("/detail")
  public String detailPage() {
    return "user/detail/detail";
  }

  @GetMapping("/review/insert/{res_num}")
  public String prepareReview(@PathVariable int res_num, @AuthenticationPrincipal CustomUser customUser, RedirectAttributes redirect) {
      try {
          int us_num = userService.getUserById(customUser.getUsername()).getUs_num();
          int rev_num = reviewService.revres(res_num, us_num);  
          return "redirect:/user/review/insert/post/" + res_num;
      } catch (IllegalStateException e) {
          redirect.addFlashAttribute("msg", e.getMessage());
          return "redirect:/mypage/reservation";
      }
  }
/*
  @GetMapping("/review/insert/post/{res_num}")
  public String reviewpage(Model model,@PathVariable Integer res_num, @ModelAttribute String errorMsg,
      @ModelAttribute ReviewVO review, @AuthenticationPrincipal CustomUser customUser) {
        
        // model.addAttribute("errorMsg", "에러입니다."); //post에서 이런식으로 에러 넘기면 될듯
    //if(res_num == null) return "redirect:/user/mypage";
    model.addAttribute("user", customUser.getUser());

    int rt_num = reservationService.getReservation(res_num).getRes_rt_num();


    model.addAttribute("restaurant", managerService.getResDetail(rt_num)); // 식당 정보
    model.addAttribute("rev_rt_num", rt_num); // 식당 번호 (만약 내 리뷰나 식당 페이지에서 넘어올땐 이거 이용해서 식당 번호 띄우기)

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
  }*/


  @GetMapping("/review/insert/post/{res_num}")
  public String showReviewForm(@PathVariable int res_num, Model model, @AuthenticationPrincipal CustomUser customUser, RedirectAttributes redirect, @ModelAttribute String errorMsg, @RequestParam(required = false) ReviewDTO reviewDTO) {
      ReservationVO reservation = reservationService.getReservation(res_num);
      if (reservation == null) {
          redirect.addFlashAttribute("msg", "예약 정보가 없습니다.");
          return "redirect:/mypage/reservation";
        }
      if(reviewDTO != null) model.addAttribute("reviewDTO", reviewDTO);
      
      if(errorMsg != null && !errorMsg.isEmpty()) model.addAttribute("errorMsg", errorMsg);
      
      List<ScoreTypeVO> scoreTypeList = userService.getScoreType();
      System.out.println("scoreTypeList: " + scoreTypeList);
      model.addAttribute("scoreTypeList", scoreTypeList); 


      model.addAttribute("user", customUser.getUser());
      model.addAttribute("reservation", reservation); 
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
/*
  @PostMapping("/review/insertPost")
  //@ResponseBody
  public String insertReview(
      RedirectAttributes rttr,
      ReviewVO review,
      Map<String, String> scores, @RequestParam(required = false) List<MultipartFile> fileList,
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
      return "redirect:/user/review/insert/post?rt_num=" + review.getRev_rt_num();
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
            return "redirect:/user/review/insert/post?rt_num=" + review.getRev_rt_num();
          }
        } catch (NumberFormatException e) {
          rttr.addFlashAttribute("errorMsg", "잘못된 점수 형식이 입력되었습니다.");
          rttr.addFlashAttribute("review", review);
          return "redirect:/user/review/insert/post?rt_num=" + review.getRev_rt_num();
        }
      }
    }

    // 파일 저장
    if (!userService.insertFile(review, fileList, fileNames, fileTags)) {
      String errorMsg = "리뷰는 등록됐지만 파일 저장에 실패했습니다.";
      System.out.println(errorMsg);
      rttr.addFlashAttribute("errorMsg", "리뷰는 등록됐지만 파일 저장에 실패했습니다.");
      rttr.addFlashAttribute("review", review);
      return "redirect:/user/review/insert/post?rt_num=" + review.getRev_rt_num();
    }

    return "redirect:/user/review/view";
  }
*/

  
    @GetMapping("/list")
    public String list(Model model, 
            @RequestParam(required = false) Integer dreg_num, 
            @RequestParam(required = false) Integer dfc_num,
            @RequestParam(required = false) Integer reg_num,
            @RequestParam(required = false) Integer fc_num,
            @RequestParam(required = false) String keyword,
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
        if(keyword != null && keyword.length() > 1)model.addAttribute("keyword", keyword);
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
    System.out.println("filepath:"+list.get(0).getFile_path());
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
  public String restaurantDetail(@PathVariable int rt_num, Model model) {
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



    List<FileVO> tapFileList = restaurantService.getTapFileList(rt_num);
    List<ReviewVO> reviewList = restaurantService.getReviewList(rt_num);
    List<BusinessHourVO> businessHour = restaurantService.getBusinessHour(rt_num);
    List<BusinessDateVO> businessDate = restaurantService.getBusinessDate(rt_num);
    Map<String, Integer> remain = restaurantService.remain(rt_num);

    int photoCount = tapFileList.size();
    int reviewCount = reviewList.size();
    
    //System.out.println(apiKey);
    // System.out.println("restaurant: " + restaurant);
    // System.out.println("businessHour: "+ businessHour);
    // System.out.println("remain: "+ remain);
    
    model.addAttribute("restaurant", restaurant);
    model.addAttribute("foodCategory", foodCategory);
    model.addAttribute("detailFoodCategory", detailFoodCategory);
    model.addAttribute("tag", tag);
    model.addAttribute("facilityList", facilityList);
    model.addAttribute("restaurantFacilityList", restaurantFacilityList);
    model.addAttribute("businessHour", businessHour);
    model.addAttribute("businessDate", businessDate);
    model.addAttribute("remain", remain);

    model.addAttribute("photoCount", photoCount);
    model.addAttribute("reviewCount", reviewCount);
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

  @GetMapping("/review/view/{rt_num}")
  public String resReviews(Model model, @PathVariable int rt_num) {
      List<ReviewVO> reviewList = userService.getReviewListByRes(rt_num);
      model.addAttribute("reviewList", reviewList);
      model.addAttribute("rt_num", rt_num);
      System.out.println("리뷰 리스트 : " + reviewList);
      return "user/review/viewByRes";
  }


  @GetMapping("/review/detail/{rev_num}")
  public String myReview(Model model, @PathVariable int rev_num) {
      
      ReviewVO review = reviewService.getReview(rev_num);
      model.addAttribute("review", review);
      System.out.println(review);
      return "user/review/detail/view";
  }
  
  @GetMapping("/list/detail/outline")
  public String outline() {
      return "user/detail/outline";
  }
  @PostMapping("/list/news/{rt_num}")
  public String postrestaurantDetail(@PathVariable int rt_num, Model model) {
    List<ResNewsVO> tapResNewsList = restaurantService.getTapResNewsList(rt_num);

    model.addAttribute("tapResNewsList", tapResNewsList);
    return "user/detail/news";
  }
  
  @PostMapping("/list/menu/{rt_num}")
  public String postrestaurantmenu(@PathVariable int rt_num, Model model) {
    //List<MenuVO> tapMenuList = restaurantService.getTapMenuList(rt_num);
    List<MenuTypeVO> tapMenuTypeList = restaurantService.getMenuTypeList(rt_num);
    List<MenuVO> menuDivList = restaurantService.getMenuDivList(rt_num);
    Map<String, List<MenuVO>> groupedMenu = menuDivList.stream()
        .collect(Collectors.groupingBy(
          MenuVO::getMn_div,
          LinkedHashMap::new,
          Collectors.toList()          
          ));

    model.addAttribute("groupedMenu", groupedMenu);
    // model.addAttribute("tapMenuList", tapMenuList);
    model.addAttribute("tapMenuTypeList", tapMenuTypeList);
    return "user/detail/menu";
  }

  @PostMapping("/list/picture/{rt_num}")
  public String postrestaurantpicture(@PathVariable("rt_num") int rt_num, Model model) {
    // 전체 파일 목록 가져오기
    List<FileVO> tapFileList = restaurantService.getTapFileList(rt_num);

    // file_tag 기준으로 그룹핑
    // Map<String, List<FileVO>> groupedFiles = tapFileList.stream()
    //     .collect(Collectors.groupingBy(FileVO::getFile_tag));

    // 모델에 추가
    // model.addAttribute("groupedFiles", groupedFiles);
    model.addAttribute("tapFileList", tapFileList);
    return "user/detail/picture";
  } 

  @PostMapping("/list/review/{rt_num}")
  public String postMethodName(@PathVariable("rt_num") int rt_num, Model model) {
    List<ReviewVO> reviewList = restaurantService.getReviewList(rt_num);

    model.addAttribute("reviewList", reviewList);
    return "user/detail/review";
  }
  
  
  @PostMapping("/list/home/{rt_num}")
  public String listHome(@PathVariable int rt_num, Model model) {
    String today = new SimpleDateFormat("E", Locale.KOREA).format(new Date());
    model.addAttribute("today", today);
    
    RestaurantVO restaurant = userService.getRestaurantDetail(rt_num);
    FoodCategoryVO foodCategory = userService.getFoodCategoryByRestaurant(rt_num);
    DetailFoodCategoryVO detailFoodCategory = userService.getDetailFoodCategoryByRestaurant(rt_num);
    TagVO tag = userService.getTagByRestaurant(rt_num);
    double starScore = restaurantService.getCountScoreByRtNum(rt_num);
    int countReview = restaurantService.getCountReviewByRtNum(rt_num);
    // List<FacilityVO> facilityList = userService.getFacilityList(rt_num);
    List<RestaurantFacilityVO> restaurantFacilityList = userService.getRestaurantFacilityList(rt_num);
    List<ResNewsVO> resNewsList = userService.getResNewsList(rt_num);
    List<FileVO> fileList = userService.getFileList(rt_num);
    List<MenuVO> menuList = userService.getMenuList(rt_num);
    List<DefaultResTimeVO> defaultResTimeList = userService.getDefaultResTimeList(rt_num);
    List<RestaurantDetailVO> restaurantDetailList = restaurantService.getRestaurantDetailList(rt_num);
    List<ReviewVO> reviewList = restaurantService.getReviewList(rt_num);
    
    DefaultResTimeVO todayResTime = null;
    for (DefaultResTimeVO drt : defaultResTimeList) {
        if (drt.getDrt_date().equals(today)) {
            todayResTime = drt;
            break;
        }
    }
    model.addAttribute("todayResTime", todayResTime);

    model.addAttribute("restaurant", restaurant);
    model.addAttribute("foodCategory", foodCategory);
    model.addAttribute("detailFoodCategory", detailFoodCategory);
    model.addAttribute("tag", tag);
    model.addAttribute("starScore", starScore);
    model.addAttribute("countReview", countReview);
    // model.addAttribute("facilityList", facilityList);
    model.addAttribute("restaurantFacilityList", restaurantFacilityList);

    // model.addAttribute("apiKey", apiKey); // API 키
    model.addAttribute("resNewsList", resNewsList);
    model.addAttribute("restaurantFileList", fileList);
    model.addAttribute("menuList", menuList);
    model.addAttribute("defaultResTimeList", defaultResTimeList);
    model.addAttribute("restaurantDetailList", restaurantDetailList);
    model.addAttribute("reviewList", reviewList);
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


  /////////////////////////////////////////////////////////
 
  @PostMapping("/review/insertSamplePost")
    public String insertSample(
          RedirectAttributes rttr,
          @RequestPart ReviewDTO reviewDTO,
          boolean preview,
          @AuthenticationPrincipal CustomUser user) {
        if (preview) {
            System.out.println("리뷰 미리보기 요청" + reviewDTO.getReview() + " " + reviewDTO.getScoreList());
            rttr.addAttribute("review", reviewDTO.getReview());
            rttr.addAttribute("scores", reviewDTO.getScoreList());
            rttr.addAttribute("fileList", reviewDTO.getFileList());
            return "user/review/insertsample"; // 리뷰 미리보기 템플릿
        }
        return "redirect:/error"; // 잘못된 접근
    }

    @PostMapping("/review/insertFinalPost")
    @ResponseBody
    public ResponseEntity<?> insertFinal(
          RedirectAttributes rttr,
          @RequestPart ReviewDTO reviewDTO,
          @RequestPart(value = "files", required = false)List<MultipartFile> fileList,
          boolean preview,
          @AuthenticationPrincipal CustomUser user) {
      System.out.println("리뷰 최종 저장 요청: " + reviewDTO.getReview() + " " + reviewDTO.getScoreList());
      System.out.println("파일리스트: " +  reviewDTO.getFileList());
      if(user.getUser() == null) return ResponseEntity.badRequest().body("확인되지 않은 이용자 접근");

      reviewDTO.getReview().setRev_us_num(user.getUser().getUs_num()); // 사용자 ID 또는 번호를 수동 세팅
      reviewDTO.getReview().setUs_name(user.getUser().getUs_name()); // 사용자 이름 세팅
      System.out.println("리뷰 작성자 번호 : " + reviewDTO.getReview().getRev_us_num());           
      //if (!preview) {/*일단 false로 받아오긴 하는데*/ }


      if(fileList!=null)for(int i=0; i<fileList.size();i++)reviewDTO.getFileList().get(i).setUploadFile(fileList.get(i));

      try{
          rttr.addFlashAttribute("review", reviewDTO.getReview());
          rttr.addFlashAttribute("scores", reviewDTO.getScoreList());

          reviewService.insertReview(reviewDTO);
        }catch (RuntimeException e) {
          System.out.println("리뷰 저장 중 오류 발생: " + e.getMessage());
          rttr.addFlashAttribute("errorMsg", e.getMessage());
          return ResponseEntity.badRequest().body("잘못된 요청");
        }catch (Exception e) {
          return ResponseEntity.badRequest().body("알수없는 오류");
        }
        System.out.println("리뷰 저장 완료");
        rttr.addFlashAttribute("successMsg", "리뷰가 성공적으로 저장되었습니다.");
        return ResponseEntity.ok().body("리뷰가 성공적으로 저장되었습니다.");
        
    }



  

    @PostMapping("/uploadProfile")
    public String uploadProfile(@AuthenticationPrincipal CustomUser customUser,
                                @RequestParam("profileImage") MultipartFile file,
                                RedirectAttributes redirect) {
        userService.updateUserProfileImage(customUser.getUser(), file, redirect);
        return "redirect:/user/info";
    }
    
    @GetMapping("/help")
    public String helpIndex() {
        return "user/help/index";
    }
    

}
