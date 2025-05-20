package kr.kh.tableup.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.kh.tableup.dao.UserDAO;
import kr.kh.tableup.model.util.Criteria;
import kr.kh.tableup.model.util.PageMaker;
import kr.kh.tableup.model.util.UploadFileUtils;
import kr.kh.tableup.model.vo.FileVO;
import kr.kh.tableup.model.vo.FoodCategoryVO;
import kr.kh.tableup.model.vo.RegionVO;
import kr.kh.tableup.model.vo.ReservationVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.model.vo.ReviewScoreVO;
import kr.kh.tableup.model.vo.ReviewVO;
import kr.kh.tableup.model.vo.ScoreTypeVO;
import kr.kh.tableup.model.vo.UserVO;

@Service
public class UserService {

  @Autowired
  UserDAO userDAO;

  @Value("${spring.path.upload}")
  String uploadPath;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public boolean validateUser(UserVO user, RedirectAttributes ra) {
    // 아이디 중복
    if (isDuplicate("id", user.getUs_id())) {
      ra.addFlashAttribute("msg", "이미 사용 중인 아이디입니다.");
      return false;
    }

    // 이메일 중복
    if (isDuplicate("email", user.getUs_email())) {
      ra.addFlashAttribute("msg", "이미 사용 중인 이메일입니다.");
      return false;
    }

    // 전화번호 중복
    if (isDuplicate("phone", user.getUs_phone())) {
      ra.addFlashAttribute("msg", "이미 사용 중인 전화번호입니다.");
      return false;
    }

    return true;
  }

  /** 중복 검사 (ID, 이메일, 전화번호) */
  public boolean isDuplicate(String type, String value) {
    if (type == null || value == null) {
      return false;
    }

    return switch (type) {
      case "id" -> userDAO.selectUserById(value) != null;
      case "phone" -> userDAO.selectUserByPhone(value) != null;
      case "email" -> userDAO.selectUserByEmail(value) != null;
      default -> false;
    };
  }

  public void insertUser(UserVO user) {
    // 비밀번호 암호화
    user.setUs_pw(passwordEncoder.encode(user.getUs_pw()));

    // 닉네임이 없을 경우 랜덤으로 생성
    if (user.getUs_nickname() == null || user.getUs_nickname().isBlank()) {
      user.setUs_nickname("user_" + UUID.randomUUID().toString().substring(0, 8));
    }

    user.setUs_created(new Date());
    user.setUs_state(0);

    userDAO.insertUser(user);
  }

  /*
   * public boolean insertUser(UserVO user) {
   * if(user==null || user.getUs_id() == null || user.getUs_pw() == null) {
   * return false;
   * }
   * return userDAO.insertUser(user);
   * }
   */

  public boolean updateUserInfo(UserVO user) {
    if (user == null || user.getUs_id() == null) {
      return false;
    }
    return userDAO.updateUserInfo(user) > 0;
  }

  /*
   * public boolean isIdDuplicate(String id) {
   * if (id == null || id.isBlank()) {
   * return false;
   * }
   * return userDAO.selectUserById(id) != null;
   * }
   * 
   * public boolean isPhoneDuplicate(String phone) {
   * if (phone == null || phone.isBlank()) {
   * return false;
   * }
   * return userDAO.selectUserByPhone(phone) != null;
   * }
   * 
   * public boolean isEmailDuplicate(String email) {
   * if (email == null || email.isBlank()) {
   * return false;
   * }
   * return userDAO.selectUserByEmail(email) != null;
   * }
   * 
   */
  /** 회원가입 */
  public boolean registerUser(UserVO user) {
    if (user == null || user.getUs_id() == null || user.getUs_pw() == null) {
      return false;
    }

    user.setUs_pw(passwordEncoder.encode(user.getUs_pw()));
    return userDAO.insertUser(user);
  }

  /** 회원 정보 조회 */
  public UserVO getUserById(String us_id) {
    if (us_id == null || us_id.isBlank()) {
      return null;
    }
    return userDAO.selectUserById(us_id);
  }

  public UserVO getUserByNum(int us_num) {

    return userDAO.selectUser(us_num);
  }

  /** 회원 정보 수정 */
  public boolean updateUser(UserVO user) {
    if (user == null || user.getUs_id() == null) {
      return false;
    }

    // 비밀번호 입력이 있을 경우만 변경
    if (user.getUs_pw() != null && !user.getUs_pw().isBlank()) {
      user.setUs_pw(passwordEncoder.encode(user.getUs_pw()));
    }

    return userDAO.updateUserInfo(user) > 0;
  }

  public List<ReviewVO> getReviewByUser(int us_num) {

    return userDAO.selectReviewByUser(us_num);
  }

  public List<ReservationVO> getReservationByUser(int us_num) {

    return userDAO.selectReservationByUser(us_num);
  }

  public List<RestaurantVO> getFollowedRestaurant(int us_num) {

    return userDAO.selectFollowedRestaurant(us_num);
  }

  public List<ReviewVO> getFollowedReview(int us_num) {

    return userDAO.selectFollowedReview(us_num);
  }

  public List<ScoreTypeVO> getScoreType() {
    return userDAO.selectScoreTypeList();
  }

  public boolean insertReview(ReviewVO review) {

    if (review.getRev_rt_num() <= 0 || review.getRev_visit() == null || review.getRev_visitor() <= 0
        || review.getRev_content() == null || review.getUs_name() == null) {
      return false;
    }

    return userDAO.insertReview(review);
  }

  public boolean insertReviewScore(ReviewVO review, int rs_st_num, int rs_score) {
    if (review == null || review.getRev_num() < 1 || rs_st_num < 1 || rs_score < 1 || rs_score > 5) {
      return false;
    }

    ReviewScoreVO reviewScore = new ReviewScoreVO();
    reviewScore.setRs_rev_num(review.getRev_num());
    reviewScore.setRs_st_num(rs_st_num);
    reviewScore.setRs_score(rs_score);
    return userDAO.insertReviewScore(reviewScore);
  }

  public boolean insertFile(ReviewVO review, List<MultipartFile> files, List<String> fileNames, List<String> fileTags) {
    if (review == null || files == null)
      return false;
    if (files.size() != fileNames.size() || files.size() != fileTags.size())
      return false;
    if (review.getRev_num() < 1)
      return false;

    for (int i = 0; i < files.size(); i++) {
      MultipartFile file = files.get(i);
      String inputFileName = fileNames.get(i);
      String fileTag = fileTags.get(i);

      if (file == null || file.isEmpty()) {
        return false;
      }

      try {
        // 파일 확장자 추출
        String originalFileName = inputFileName;
        if (originalFileName == null || originalFileName.isEmpty()) {
          originalFileName = file.getOriginalFilename();
        }
        String suffix = getSuffix(originalFileName);

        // 새로운 파일명 지정
        String newFileName = review.getRev_num() + suffix;

        // 파일 업로드
        String uploadFileName = UploadFileUtils.uploadFile(uploadPath, newFileName, file.getBytes(), "review");

        // DB에 넣을 FileVO 생성
        FileVO fileVO = new FileVO();
        fileVO.setFile_name(inputFileName); // 사용자가 입력한 이름
        fileVO.setFile_path(uploadFileName); // 서버 저장 경로
        fileVO.setFile_type("REVIEW");
        fileVO.setFile_foreign(String.valueOf(review.getRev_num()));
        fileVO.setFile_tag(fileTag);
        fileVO.setFile_res_num(review.getRev_rt_num());

        userDAO.insertFile(fileVO);

      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }

    return true;
  }

  private String getSuffix(String fileName) {
    int index = fileName.lastIndexOf(".");
    return index < 0 ? null : fileName.substring(index);
  }

  public List<RegionVO> getRegionList() {

    return userDAO.selectRegionList();
  }

  public List<FoodCategoryVO> getFoodCategoryList() {

    return userDAO.selectFoodCategoryList();
  }

  public List<RestaurantVO> getRestaurantList(Criteria cri) {
    if (cri == null) {
      return null;
    }
    return userDAO.selectRestaurantList(cri);
  }

  public PageMaker getPageMaker(Criteria cri) {
    if (cri == null) {
      return null;
    }

    int count = userDAO.selectCountRestaurantList(cri);
    return new PageMaker(1, cri, count);
  }

  public RestaurantVO getRestaurantDetail(int rt_num) {
    return userDAO.selectRestaurantDetail(rt_num);
  }

}
