package life.majiang.community.controller;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.service.CommentService;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class questionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{action}")
    public String question(@PathVariable(name = "action") String action,
                           Model model) {
        //问题详情
        QuestionDTO questionDTO = questionService.QuestionDetail(Integer.parseInt(action));
        questionService.addView(Integer.parseInt(action));//可以更改
        //评论详情
        List<CommentDTO> commentDTOS = commentService.getCommentListById(Long.parseLong(action));
        model.addAttribute("commentDTOS", commentDTOS);
        model.addAttribute("question_detail", questionDTO);
        return "question";
    }
}
