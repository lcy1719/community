package life.majiang.community.controller;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class helloController {
    @Autowired
    private UserMapper mapper;
    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies=request.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("token")){
                String token=cookie.getValue();
                User user=mapper.findByToken(token);
                if(user!=null){
                    request.getSession().setAttribute("user",user);
                }
            }
        }
        return "index";
    }
}
