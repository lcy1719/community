package life.majiang.community.controller;

import life.majiang.community.dto.PagenationDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(@RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "2") Integer size,
                        Model model){
        PagenationDTO pagenationDTO = questionService.QuestionList(page,size);
        model.addAttribute("pagenation",pagenationDTO);
        return "index";
    }
}
