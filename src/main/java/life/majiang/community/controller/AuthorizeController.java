package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.provider.GitHubProvider;
import life.majiang.community.service.QuestionService;
import life.majiang.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.misc.UUDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GitHubProvider gitHubProvider;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Value("${github.client.id}")
    private String client_id;
    @Value("${github.client.secret}")
    private String secret;
    @Value("${github.redirect.url}")
    private String url;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String call(@RequestParam(name = "code") String code,
                       @RequestParam(name = "state") String state,
                        HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();

        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(secret);
        accessTokenDTO.setRedirect_uri(url);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);

        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        GithubUser getuser = gitHubProvider.Getuser(accessToken);
        if (getuser != null && getuser.getId() != null) {
            //登录成功
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAccountId(getuser.getId().toString());
            user.setName(getuser.getName());
           // user.setId(Integer.parseInt(String.valueOf(getuser.getId())));
            user.setAvatarUrl(getuser.getAvatar_url());
            userService.findByid(user);
            response.addCookie(new Cookie("token", token));//登录成功添加一个标识(cookie)
            return "redirect:/";
        } else {
            //登录失败
            return "redirect:/";
        }
    }

    @GetMapping("/lougout")
    public String lougout(HttpServletRequest request,HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }
}
