package kr.kh.tableup.config;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

@Component
@ControllerAdvice
public class GlobalModelAdvice {

		/**
		 * 모든 컨트롤러에서 공통적으로 사용할 수 있는 모델 속성을 추가합니다.
		 * @param model
		 * @param request
		 */
    @ModelAttribute
    public void addUrlToModel(Model model, HttpServletRequest request) {
        model.addAttribute("url", request.getRequestURI());
    }
}
