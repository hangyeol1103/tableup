package kr.kh.tableup.model.DTO;

import java.util.List;

import kr.kh.tableup.model.vo.DefaultResTimeVO;
import kr.kh.tableup.model.vo.RestaurantVO;
import lombok.Data;

@Data
public class DefaultResTimeListDTO {
	List<DefaultResTimeVO> list;
	RestaurantVO resdetail;
}
