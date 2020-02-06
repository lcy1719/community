package life.majiang.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MapAPIController {
    @RequestMapping("/ws")
    public String getCredit(HttpServletResponse response,HttpServletRequest request){
        response.setContentType("text/html; charset=utf-8");
        String callback = request.getQueryString();
        return callback;
    }
}
