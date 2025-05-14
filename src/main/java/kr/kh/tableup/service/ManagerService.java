package kr.kh.tableup.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.kh.tableup.dao.ManagerDAO;
import kr.kh.tableup.model.util.UploadFileUtils;
import kr.kh.tableup.model.vo.DetailFoodCategoryVO;
import kr.kh.tableup.model.vo.DetailRegionVO;
import kr.kh.tableup.model.vo.FileVO;
import kr.kh.tableup.model.vo.FoodCategoryVO;
import kr.kh.tableup.model.vo.MenuTypeVO;
import kr.kh.tableup.model.vo.MenuVO;
import kr.kh.tableup.model.vo.RegionVO;
import kr.kh.tableup.model.vo.RestaurantManagerVO;
import kr.kh.tableup.model.vo.RestaurantVO;

@Service
public class ManagerService {
	
	@Autowired
	ManagerDAO managerDAO;

	@Value("${spring.path.upload}")
	String uploadPath;

	public boolean insertManager(RestaurantManagerVO rm) {
		return managerDAO.insertManager(rm);
	}

	public RestaurantManagerVO getManagerId(String rm_id) {
		
		return managerDAO.selectManager(rm_id);
	}

	public RestaurantVO selectRestaurant(int rm_num) {
		return managerDAO.selectRestaurant(rm_num);
	}

	public boolean insertRestaurant(RestaurantVO restaurant, RestaurantManagerVO manager, MultipartFile[] fileList) {
		if(restaurant == null || manager == null){
			return false;
		}
		boolean res=managerDAO.insertRestaurant(restaurant);
		if(!res){
			return false;
		}

		System.out.println(restaurant.getRt_num());
		
		//매장 이미지 작업
		if(fileList != null) {
        for(MultipartFile file : fileList) {
            if(file.isEmpty()) continue;

            try {
                // 저장 경로 설정
                String uploadDir = "D:\\uploads";
                String uuid = UUID.randomUUID().toString();
                String fileName = uuid + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);

                // 실제 파일 저장
                Files.copy(file.getInputStream(), filePath);

                // FileVO 생성
                FileVO fvo = new FileVO();
                fvo.setFile_path(uploadDir);
                fvo.setFile_name(fileName);
                fvo.setFile_type("RestaurantDetail");
                fvo.setFile_foreign(String.valueOf(restaurant.getRt_num())); // 식당 번호
                fvo.setFile_tag("음식");

                // DB 저장
                boolean dbInsert = managerDAO.insertFile(fvo);
        				if (!dbInsert) {
										System.err.println("파일 DB 저장 실패: " + fileName);
								}

            } catch(IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
		
		//매장 정보 등록후 점주에게 매장 정보 부여
		boolean res1 =managerDAO.updateManagerRtNum(manager.getRm_num(),restaurant.getRt_num());
		if(!res1){
			return false;
		}
		
		return true;
	}


	//삽입한 사진수만큼 등록하는 작업
	private void uploadFileList(int rt_num, MultipartFile[] fileList) {
		if(fileList == null || fileList.length == 0){
			return;
		}
		for(MultipartFile file : fileList){
			uploadFile(rt_num,file);
		}
	}

	private void uploadFile(int rt_num, MultipartFile file) {
		if(file==null||file.getOriginalFilename().isEmpty()){
			return;
		}
		String fi_ori_name=file.getOriginalFilename();
		try{
			String fi_name=UploadFileUtils.uploadFile(uploadPath, fi_ori_name, file.getBytes());

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public List<FoodCategoryVO> getFoodCategory() {
		return managerDAO.selectFoodCategoryList();
	}

	public List<RegionVO> getRegion() {
	return managerDAO.selectRegionList();
	}

	public List<DetailRegionVO> getDetailRegion() {
		return managerDAO.selectDetailRegionList();
	}

	public List<DetailFoodCategoryVO> getDetailFood() {
	return managerDAO.selectDetailFoodList();
	}

	public List<DetailFoodCategoryVO> getDetailByFcNum(int fc_num) {
		return managerDAO.selectDetailByFcNum(fc_num);
	}

	public List<DetailRegionVO> getDetailByRegNum(int reg_num) {
		return managerDAO.selectDetailByRegNum(reg_num);
	}

	public List<MenuVO> getMenuList(int rt_num) {
		return managerDAO.selectMenuList(rt_num);
	}

	public List<MenuTypeVO> getMenuTypeList() {
		return managerDAO.selectMenuTypeList();
	}

	public List<FileVO> getFileListByRtNum(int rt_num) {
		return managerDAO.selectFilesByRtNum(rt_num);
	}

}
