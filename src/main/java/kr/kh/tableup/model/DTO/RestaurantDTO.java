package kr.kh.tableup.model.DTO;

import java.util.List;

import kr.kh.tableup.model.vo.RestaurantFacilityVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import kr.kh.tableup.model.vo.TagVO;
import lombok.Data;

@Data
public class RestaurantDTO {
	private RestaurantVO restaurant;
	private List<RestaurantFacilityVO> facList;
	private List<TagVO> tagList;
}
