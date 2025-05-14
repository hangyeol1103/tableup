package kr.kh.tableup.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.kh.tableup.model.vo.UserVO;
import kr.kh.tableup.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        String username = principal.getName();
        UserVO user = userService.getUserById(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저 없음");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("name", user.getUs_name());
        map.put("email", user.getUs_email());

        return ResponseEntity.ok(map);
    }
}
