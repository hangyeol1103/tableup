package kr.kh.tableup.config.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.kh.tableup.model.util.CustomUser;
import kr.kh.tableup.model.vo.UserVO;
import kr.kh.tableup.service.UserDetailService;

@Component
@Order(4)
public class OAuth2AutoLoginFilter extends OncePerRequestFilter {

    private final UserDetailService userDetailService;

    public OAuth2AutoLoginFilter(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null &&
            authentication.isAuthenticated() &&
            authentication.getPrincipal() instanceof OAuth2User &&
            !(authentication.getPrincipal() instanceof CustomUser)) {

            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            String uri = request.getRequestURI();

            UserVO userVO = userDetailService.selectUserByEmail(email);

            if (userVO != null) {
                CustomUser customUser = new CustomUser(userVO);

                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                response.sendRedirect("/user/login");
                return;
            }

        }

        filterChain.doFilter(request, response);
    }
}
