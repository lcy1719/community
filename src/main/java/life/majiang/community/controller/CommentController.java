package life.majiang.community.controller;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.CommentMapper;
import life.majiang.community.mapper.QuestionExMapper;
import life.majiang.community.model.Comment;
import life.majiang.community.model.User;
import life.majiang.community.service.CommentService;
import life.majiang.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    @ResponseBody//ResponseBody返回数据以任何形式(此类中转换成json)
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request) {//RequestBody把前端的json数据获得到转换成bean类型

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            //方法Object类型，加上ResponseBody注解，可以以json格式返回一个DTO（类）
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentDTO==null|| StringUtils.isBlank(commentDTO.getContent())){
            throw new CustomizeException(CustomizeErrorCode.ContentIsNone);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setCommentator(user.getId());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setLikeCount(0L);
        comment.setType(commentDTO.getType());
        comment.setContent(commentDTO.getContent());
        questionService.addComment(1);  //增加问题的评论数
        commentService.insert(comment);
        return ResultDTO.ok();
    }

}
