package kr.kh.tableup.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import kr.kh.tableup.dao.UserDAO;
import kr.kh.tableup.model.util.PageMaker;
import kr.kh.tableup.model.util.ResCriteria;
import kr.kh.tableup.model.util.UploadFileUtils;
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
import kr.kh.tableup.model.vo.ReviewScoreVO;
import kr.kh.tableup.model.vo.ReviewVO;
import kr.kh.tableup.model.vo.ScoreTypeVO;
import kr.kh.tableup.model.vo.TagVO;
import kr.kh.tableup.model.vo.UsFollowVO;
import kr.kh.tableup.model.vo.UserVO;

@Service
public class UserService {

  @Autowired
  UserDAO userDAO;

  @Value("${spring.path.upload}")
  String uploadPath;

  @Autowired
  private PasswordEncoder passwordEncoder;


    /** 중복 검사 (ID, 이메일, 전화번호) */
    public boolean validateUser(UserVO user, RedirectAttributes re) {
    if (isDuplicate("id", user.getUs_id())) {
      re.addFlashAttribute("msg", "이미 사용 중인 아이디입니다.");
      return false;
    }
    if (isDuplicate("email", user.getUs_email())) {
      re.addFlashAttribute("msg", "이미 사용 중인 이메일입니다.");
      return false;
    }
    if (isDuplicate("phone", user.getUs_phone())) {
      re.addFlashAttribute("msg", "이미 사용 중인 전화번호입니다.");
      return false;
    }
    return true;
  }


  public boolean validateUser2(UserVO user, RedirectAttributes re) {
    if (userDAO.selectUserById(user.getUs_id())!=null) {
      re.addFlashAttribute("msg", "이미 사용 중인 아이디입니다.");
      return false;
    }
    if (userDAO.selectUserByPhone(user.getUs_email())!=null) {
      re.addFlashAttribute("msg", "이미 사용 중인 이메일입니다.");
      return false;
    }
    if (userDAO.selectUserByEmail(user.getUs_phone())!=null) {
      re.addFlashAttribute("msg", "이미 사용 중인 전화번호입니다.");
      return false;
    }
    return true;
  }

  
  public boolean isDuplicate(String type, String value) {
    if (type == null || value == null) return false;
    return switch (type) {
      case "id" -> userDAO.selectUserById(value) != null;
      case "phone" -> userDAO.selectUserByPhone(value) != null;
      case "email" -> userDAO.selectUserByEmail(value) != null;
      default -> false;
    };
  }

  public boolean insertUser(UserVO user) {
    user.setUs_pw(passwordEncoder.encode(user.getUs_pw()));
    // 닉네임이 없을 경우
    if (user.getUs_nickname() == null || user.getUs_nickname().isBlank()) {
      user.setUs_nickname("user_" + UUID.randomUUID().toString().substring(0, 8));
    }

    user.setUs_created(new Date());
    user.setUs_state(0);

    return userDAO.insertUser(user);
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
    if (user == null || user.getUs_id() == null || user.getUs_pw() == null) return false;
    user.setUs_pw(passwordEncoder.encode(user.getUs_pw()));

    return userDAO.insertUser(user);
  }

  /** 회원 정보 조회 */
  public UserVO getUserById(String us_id) {
    if (us_id == null || us_id.isBlank()) return null;
    return userDAO.selectUserById(us_id);
  }

  public UserVO getUserByNum(int us_num) {

    return userDAO.selectUser(us_num);
  }

  /** 회원 정보 수정 */
  public boolean updateUser(UserVO user) {
    if (user == null || user.getUs_id() == null) return false;

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

    System.out.println(review);
    if (review.getRev_rt_num() <= 0 || review.getRev_visit() == null || review.getRev_visitor() <= 0
        || review.getRev_content() == null || review.getRev_us_num() < 1 ) {
      return false;
    }
    System.out.println("널체크 통과");

    return userDAO.insertReview(review);
  }

  public boolean insertReviewScore(ReviewVO review, int rs_st_num, int rs_score) {
    System.out.println("insertReviewScore called with review: " + review + ", rs_st_num: " + rs_st_num + ", rs_score: " + rs_score);
    if (review == null || review.getRev_num() < 1 || rs_st_num < 1 || rs_score < 1 || rs_score > 5) return false;

    ReviewScoreVO reviewScore = new ReviewScoreVO();
    reviewScore.setRs_rev_num(review.getRev_num());
    reviewScore.setRs_st_num(rs_st_num);
    reviewScore.setRs_score(rs_score);
    System.out.println("ReviewScoreVO created: " + reviewScore);
    return userDAO.insertReviewScore(reviewScore);
  }

  public boolean insertFile(ReviewVO review, List<MultipartFile> files, List<String> fileNames, List<String> fileTags) {
    if (review == null || files == null) return false;
    System.out.println("File Names: " + fileNames);
    //if (files.size() != fileNames.size() || files.size() != fileTags.size()) return false;
    System.out.println(files.size() + " files to upload.");
    System.out.println("File Tags: " + fileTags);
    if (review.getRev_num() < 1) return false;
    System.out.println("Review Number: " + review.getRev_num());

    for (int i = 0; i < Math.min(files.size(), Math.min(fileNames.size(), fileTags.size())); i++) {
      MultipartFile file = files.get(i);
      String inputFileName = fileNames.get(i);
      String fileTag = fileTags.get(i);

      if (file == null || file.isEmpty()) continue;
      if (fileTag == null || fileTag.isEmpty()) fileTag = "default";
      System.out.println("Processing file: " + inputFileName + " with tag: " + fileTag);
      // 파일이 비어있거나 이름이 비어있으면 건너뛰기
      if (file.getSize() <= 0 || inputFileName == null || inputFileName.isEmpty()) {
        System.out.println("Skipping empty file: " + inputFileName);
        continue;
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
        FileVO fileVO = new FileVO(0,uploadFileName, inputFileName, "REVIEW", /*String.valueOf(review.getRev_num())*/review.getRev_num(), fileTag, review.getRev_rt_num());
        // fileVO.setFile_name(inputFileName); // 사용자가 입력한 이름
        // fileVO.setFile_path(uploadFileName); // 서버 저장 경로
        // fileVO.setFile_type("REVIEW");
        // fileVO.setFile_foreign(review.getRev_num());
        // fileVO.setFile_tag(fileTag);
        // fileVO.setFile_res_num(review.getRev_rt_num());

        userDAO.insertFile(fileVO);
        System.out.println(i + "파일 업로드 성공: " + uploadFileName);

      } catch (Exception e) {
        e.printStackTrace();
        System.out.println(i + "파일 업로드 실패: " + e.getMessage());
        continue;
      }
    }

    return true;
  }

  private String getSuffix(String fileName) {
    int index = fileName.lastIndexOf(".");
    return index < 0 ? null : fileName.substring(index);
  }

  public List<DetailRegionVO> getRegionList() {

    return userDAO.selectRegionList();
	}
  
	public 	Map<String, List<DetailRegionVO>> getRegionMap() {
		
     List<DetailRegionVO> list = getRegionList();

    // 대분류별
    Map<String, List<DetailRegionVO>> regionMap = list.stream()
        .collect(Collectors.groupingBy(DetailRegionVO::getReg_main, LinkedHashMap::new, Collectors.toList()));

    // 각 그룹 앞에 "전체" 추가
    for (Map.Entry<String, List<DetailRegionVO>> entry : regionMap.entrySet()) {
        DetailRegionVO first = entry.getValue().get(0);

        DetailRegionVO all = new DetailRegionVO();
        all.setReg_num(first.getReg_num()); // 대분류 번호 그대로
        all.setReg_main(first.getReg_main());
        all.setDreg_num(0); // 전체 구분용
        all.setDreg_sub("전체");

        entry.getValue().add(0, all); // 맨 앞에 삽입
    }

    return regionMap;
    
	} //이게 잘안돼서 밑에걸로

  public List<DetailRegionVO> getRegionListWithWhole() {
    List<DetailRegionVO> original = getRegionList(); // 기본 목록

    Map<String, List<DetailRegionVO>> grouped = original.stream()
        .collect(Collectors.groupingBy(DetailRegionVO::getReg_main, LinkedHashMap::new, Collectors.toList()));

    List<DetailRegionVO> result = new ArrayList<>();
    for (Map.Entry<String, List<DetailRegionVO>> entry : grouped.entrySet()) {
        String regMain = entry.getKey();
        List<DetailRegionVO> list = entry.getValue();

        // 첫 DetailRegionVO 하나를 복사해서 "전체"로 만들기
        DetailRegionVO whole = new DetailRegionVO();
        whole.setReg_main(regMain);
        whole.setDreg_sub("전체");
        whole.setDreg_num(0);
        whole.setReg_num(list.get(0).getReg_num()); // 대분류 번호 복사

        result.add(whole);      // "서울 전체"
        result.addAll(list);    // 서울:강남, 서울:관악 ...
    }

    return result;
}



  

  public List<DetailFoodCategoryVO> getFoodCategoryList() {

    return userDAO.selectFoodCategoryList();
  }

  public List<DetailFoodCategoryVO> getFoodCategoryListWithWhole(){
    List<DetailFoodCategoryVO> original = getFoodCategoryList(); // 기본 목록

    Map<String, List<DetailFoodCategoryVO>> grouped = original.stream()
        .collect(Collectors.groupingBy(DetailFoodCategoryVO::getFc_main, LinkedHashMap::new, Collectors.toList()));

    List<DetailFoodCategoryVO> result = new ArrayList<>();
    for (Map.Entry<String, List<DetailFoodCategoryVO>> entry : grouped.entrySet()) {
        String fcMain = entry.getKey();
        List<DetailFoodCategoryVO> list = entry.getValue();

        // 첫 DetailFoodCategoryVO 하나를 복사해서 "전체"로 만들기
        DetailFoodCategoryVO whole = new DetailFoodCategoryVO();
        whole.setFc_main(fcMain);
        whole.setDfc_sub("전체");
        whole.setDfc_num(0);
        whole.setFc_num(list.get(0).getFc_num()); // 대분류 번호 복사

        result.add(whole);      // "한식 전체"
        result.addAll(list);    // 한식:불고기, 한식:비빔밥 ...
    }

    return result;
  }


  public List<RestaurantVO> getRestaurantList(ResCriteria cri) {
    if (cri == null) {
      return null;
    }
    if (cri.getKeyword() != null && !cri.getKeyword().isBlank()) {
      List<String> keywordList = Arrays.stream(cri.getKeyword().split(","))
              .map(String::trim)
              .filter(k -> !k.isEmpty())
              .collect(Collectors.toList());
      cri.setKeywordList(keywordList);
    }

    
    return userDAO.selectRestaurantList(cri);
  }
  
/* public List<RestaurantVO> getRestaurantList(Criteria cri) {
    if (cri == null) return null;

    Map<String, Object> criMap = new HashMap<>();
    criMap.put("cri", cri);
    criMap.put("tagList", cri instanceof ResCriteria resCri ? new ArrayList<>(resCri.getTagList()) : null);
    criMap.put("facilityList", cri instanceof ResCriteria resCri ? new ArrayList<>(resCri.getTagList()) : null);
    criMap.put("rt_dreg_num", cri instanceof ResCriteria resCri ? resCri.getRt_dreg_num() : null);
    criMap.put("dreg_reg_num", cri instanceof ResCriteria resCri ? resCri.getDreg_reg_num() : null);
    criMap.put("rt_dfc_num", cri instanceof ResCriteria resCri ?  resCri.getRt_dfc_num() : null);
    criMap.put("orderBy", cri instanceof ResCriteria resCri ? resCri.getOrderby() : null);
    criMap.put("pageStart", cri instanceof ResCriteria resCri ? resCri.getPageStart() : null);
    criMap.put("perPageNum", cri instanceof ResCriteria resCri ? resCri.getPerPageNum() : null);

    return userDAO.selectRestaurantList(criMap);
  }*/
/*
  public PageMaker getPageMaker(Criteria cri) {
    if (cri == null) return null;

    Map<String, Object> criMap = new HashMap<>();
    criMap.put("cri", cri);
    criMap.put("tagList", cri instanceof ResCriteria resCri ? new ArrayList<>(resCri.getTagList()) : null);
    criMap.put("facilityList", cri instanceof ResCriteria resCri ? new ArrayList<>(resCri.getTagList()) : null);
    criMap.put("rt_dreg_num", cri instanceof ResCriteria resCri ? resCri.getRt_dreg_num() : null);
    criMap.put("dreg_reg_num", cri instanceof ResCriteria resCri ? resCri.getDreg_reg_num() : null);
    criMap.put("rt_dfc_num", cri instanceof ResCriteria resCri ?  resCri.getRt_dfc_num() : null);
    criMap.put("orderBy", cri instanceof ResCriteria resCri ? resCri.getOrderBy() : null);
    criMap.put("pageStart", cri instanceof ResCriteria resCri ? resCri.getPageStart() : null);
    criMap.put("perPageNum", cri instanceof ResCriteria resCri ? resCri.getPerPageNum() : null);

    int count = userDAO.selectCountRestaurantList(criMap);
    return new PageMaker(1, cri, count);
  }
*/

  	public PageMaker getPageMaker(ResCriteria cri) {
    if(cri == null) {
			return null;
		}
    
    if (cri.getKeyword() != null && !cri.getKeyword().isBlank()) {
      List<String> keywordList = Arrays.stream(cri.getKeyword().split(","))
              .map(String::trim)
              .filter(k -> !k.isEmpty())
              .collect(Collectors.toList());
      cri.setKeywordList(keywordList);
    }
		
		int count = userDAO.selectCountRestaurantList(cri);
		return new PageMaker(1, cri, count);
	}

  public RestaurantVO getRestaurantDetail(int rt_num) {
    return userDAO.selectRestaurantDetail(rt_num);
  }

  public FoodCategoryVO getFoodCategoryByRestaurant(int rt_num) {
    return userDAO.selectFoodCategoryByRestaurant(rt_num);
  }

  public DetailFoodCategoryVO getDetailFoodCategoryByRestaurant(int rt_num) {
    return userDAO.selectDetailFoodCategoryByRestaurant(rt_num);
  }

  public TagVO getTagByRestaurant(int rt_num) {
    return userDAO.selectTagByRestaurant(rt_num);
  }

  
	public List<ReviewVO> getReviewList() {
		
    return userDAO.selectReviewList();
	}
  
  public List<ReviewVO> getReviewListByRes(int rt_num) {
  
     return userDAO.selectReviewListbyNum(rt_num);
  }


  public Map<String, List<TagVO>> getTagList() {
   
      List<TagVO> tagList = userDAO.selectTagList();
    
      // tt_name 기준 그룹화
      Map<String, List<TagVO>> tagMap = tagList.stream()
        .collect(Collectors.groupingBy(TagVO::getTt_name, LinkedHashMap::new, Collectors.toList()));
    
      return tagMap;
  }

	public List<FacilityVO> getFacilityList() {
		
      return userDAO.selectFacilityList();
	}


  public List<ScoreTypeVO> getScoreTypeList() {

    return userDAO.selectScoreTypeList();
  }

  

  public List<RestaurantFacilityVO> getRestaurantFacilityList(int rt_num) {
    return userDAO.selectRestaurantFacilityList(rt_num);
  }


  public UserVO selectUserById(String loginId) {
    return userDAO.selectUserById(loginId);
  }


	public List<UsFollowVO> getFollowByUser(int us_num) {
		if(us_num <= 0) {
      return Collections.emptyList();
    }
    
    List<UsFollowVO> followList = userDAO.selectFollowByUser(us_num);
    if (followList == null) {
      return Collections.emptyList();
    }
    System.out.println("Follow List: " + followList);
    return followList;
	}


  public int toggleFollow(UsFollowVO follow) {
    UsFollowVO dbFollow = userDAO.selectUsFollow(follow);
    if (dbFollow != null) {
      int result = userDAO.deleteUsFollow(follow);
      return result * (-1);
    } else {
      int result = userDAO.insertUsFollow(follow);
      return result;
    }
  }


	public boolean isFollow(int uf_us_num, String uf_type, int uf_foreign) {
		
    return userDAO.selectFollowByUser(uf_us_num).stream()
        .anyMatch(follow -> follow.getUf_type().equals(uf_type) && follow.getUf_foreign() == uf_foreign);
    
	}
	public List<ResNewsVO> getResNewsList(int rt_num) {
		return userDAO.selectResNewsList(rt_num);
	}


	public List<FileVO> getFileList(int rt_num) {
		return userDAO.selectFileList(rt_num);
	}


  public List<MenuVO> getMenuList(int rt_num) {
    return userDAO.selectMenuList(rt_num);
  }


  public List<DefaultResTimeVO> getDefaultResTimeList(int rt_num) {
    return userDAO.selectDefaultResTimeList(rt_num);
  }


  public String generateQrBase64(String content) {
      try {
          BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 200, 200);
          BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);

          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ImageIO.write(image, "png", baos);
          return Base64.getEncoder().encodeToString(baos.toByteArray());
      } catch (Exception e) {
          throw new RuntimeException("QR 생성 실패", e);
      }
  }


  public void updateUserProfileImage(UserVO user, MultipartFile file, RedirectAttributes redirect) {
    try {
        if (file == null || file.isEmpty()) {
            redirect.addFlashAttribute("errorMsg", "업로드할 파일이 없습니다.");
            return;
        }

        UserVO dbImg = getUserProfileImage(user.getUs_num());
        if (dbImg != null && dbImg.hasUPI()) {
            UploadFileUtils.delteFile(uploadPath, dbImg.getUpi_file_path());
        }


        String fileName = file.getOriginalFilename();
        String filePath = UploadFileUtils.uploadFile(uploadPath, fileName, file.getBytes());

        UserVO userImg = new UserVO();
        userImg.setUs_num(user.getUs_num());
        System.out.println("유저 번호 출력 : "+user.getUs_num());
        userImg.setUpi_us_num(user.getUs_num());
        userImg.setUpi_file_name(fileName);
        userImg.setUpi_file_path(filePath);

        if (dbImg == null || !dbImg.hasUPI()) {
            System.out.println("기존에 사진이 존재하지 않아 새 이미지 등록");
            userDAO.insertUserProfileImage(userImg);
        } else {
            System.out.println("기존에 존재하는 이미지를 변경");
            userImg.setUpi_num(dbImg.getUpi_num());
            userDAO.updateUserProfileImage(userImg);
        }

        redirect.addFlashAttribute("msg", "프로필 이미지가 변경되었습니다.");
    } catch (Exception e) {
        redirect.addFlashAttribute("errorMsg", "파일 업로드 중 오류가 발생했습니다.");
    }
  }




  public UserVO getUserProfileImage(int us_num) {
    return userDAO.selectUserProfileImage(us_num);
  }


	public List<String> getKeywords() {
		return userDAO.selectKeywords();
	}


    public int getKeywordCount() {
      return userDAO.selectKeywordCount();
    }


    public List<String> getKeywords(int start, int end) {
      return userDAO.selectCriKeywords(start, end);
    }



}
