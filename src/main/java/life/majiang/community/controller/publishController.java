package life.majiang.community.controller;

import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class publishController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @GetMapping("/publish/{id}")//数据由question.html28行传过来,点编辑过来的
    public String Editquestion(@PathVariable(name = "id") Integer id,
                               Model model) {
        QuestionDTO questionDTO = questionService.QuestionDetail(id);
        //发布前验证问题的完整性
        model.addAttribute("title", questionDTO.getTitle());
        model.addAttribute("description", questionDTO.getDescription());
        model.addAttribute("tag", questionDTO.getTag());
        model.addAttribute("id", questionDTO.getId());
        return "publish";
    }

    //发布信息
    @PostMapping("/publish")
    public String dopublish(@RequestParam(value = "title", required = false) String title,//地址栏的参数由publish.html的input标签传递过来
                            @RequestParam(value = "id", required = false) Integer id,
                            @RequestParam(value = "tag", required = false) String tag,
                            @Param("description") String description,
                            Model model, HttpServletRequest request) {

        Question question = new Question();
        if(id!=null){
            question=questionMapper.selectByPrimaryKey(id);
        }
        User user = (User) request.getSession().getAttribute("user");
        //发布前验证问题的完整性
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("id", id);
        if (title.isEmpty()) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description.isEmpty()) {
            model.addAttribute("error", "问题描述不能为空");
            return "publish";
        }
        if (tag.isEmpty()) {
            model.addAttribute("error", "问题标签不能为空");
            return "publish";
        }
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setId(id);
        questionService.UpdateOrInsert(question,user);
        return "redirect:/";
    }
}

