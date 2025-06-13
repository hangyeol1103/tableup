package kr.kh.tableup.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.kh.tableup.service.RestaurantService;


@Controller
@RequestMapping("/restaurant")
public class RestaurantController {

	@Autowired
	private RestaurantService restaurantService;
	
	@PostMapping("/search/recommend")
	public ResponseEntity<List<Map<String, String>>> searchRecommend(String input) {
		if (input == null || input.trim().length() < 2) {
				return ResponseEntity.ok(Collections.emptyList());
		}
		List<Map<String, String>> rec = restaurantService.getSearchRecommend(input.trim());
		return ResponseEntity.ok(rec);
	}
	

}
