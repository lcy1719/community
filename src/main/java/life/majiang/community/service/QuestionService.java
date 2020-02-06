package life.majiang.community.service;

import life.majiang.community.dto.PagenationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.QuestionExMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExMapper questionExMapper;

    public PagenationDTO QuestionList(Integer page, Integer size) {
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());         //获得总条数
        Integer totalPage;                                         //总页数
        PagenationDTO pagenationDTO = new PagenationDTO();  //创建一个包含user,question,page的dto
        //获得总页数
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //判断分页是否小于0或者大于最大页数
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        pagenationDTO.setPagenation(totalPage, page);  //接受前端要看的第几页，进行处理
        Integer offset = size * (page - 1);  //设置从第几条开始查询,page=1时从0查，page=2时从5查...
        List<QuestionDTO> questionDTOList = new ArrayList<>();//创建一个包含user的dto(实体)
        //获得分页显示的数据
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        for (Question question : questions) {//遍历实体
            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andAccountIdEqualTo(String.valueOf(question.getCreator()));
            List<User> user = userMapper.selectByExample(userExample);//获得dto里面的user实体
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);//将不含user的实体转换成含user的实体，相当于多条set方法
            questionDTO.setUser(user.get(0));                      //设置实体中的user
            questionDTOList.add(questionDTO);               //将实体添加进list集合
        }
        pagenationDTO.setQuestionDTO(questionDTOList);      //将只包含question的实体转换成pagenation实体
        return pagenationDTO;
    }

    public PagenationDTO QuestionList(Integer id, Integer page, Integer size) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(id);
        Integer totalCount= (int)questionMapper.countByExample(questionExample);
        Integer totalPage;                                         //总页数
        PagenationDTO pagenationDTO = new PagenationDTO();  //创建一个包含user,question,page的dto
        //获得总页数
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //判断分页是否小于0或者大于最大页数
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        pagenationDTO.setPagenation(totalPage, page);  //接受前端要看的第几页，进行处理
        Integer offset = size * (page - 1);  //设置从第几条开始查询,page=1时从0查，page=2时从5查...
        List<QuestionDTO> questionDTOList = new ArrayList<>();//创建一个包含user的dto(实体)
        QuestionExample questionExample1 = new QuestionExample();
        questionExample1.createCriteria()
                .andCreatorEqualTo(id);
        //获得分页显示的数据
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample1, new RowBounds(offset,size));

        for (Question question : questions) {//遍历实体
            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andAccountIdEqualTo(String.valueOf(question.getCreator()));
            List<User> user = userMapper.selectByExample(userExample);//获得dto里面的user实体
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);//将不含user的实体转换成含user的实体，相当于多条set方法
            questionDTO.setUser(user.get(0));                      //设置实体中的user
            questionDTOList.add(questionDTO);               //将实体添加进list集合
        }
        pagenationDTO.setQuestionDTO(questionDTOList);      //将只包含question的实体转换成pagenation实体
        return pagenationDTO;
    }

    public QuestionDTO QuestionDetail(Integer Id) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question getQuestion = questionMapper.selectByPrimaryKey(Id);
        if(getQuestion==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        //
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(getQuestion.getCreator().toString());
        List<User> users = userMapper.selectByExample(userExample);
        BeanUtils.copyProperties(getQuestion, questionDTO);
        questionDTO.setUser(users.get(0));
        return questionDTO;
    }
    public void UpdateOrInsert(Question question,User user) {
        if (question.getId() == null) {
            //插入
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setViewCount(0);
            question.setCreator(Integer.parseInt(user.getAccountId()));
            questionMapper.insert(question);
        } else {
            //更新
            if(Integer.parseInt(user.getAccountId())!=question.getCreator()){
                throw new CustomizeException(CustomizeErrorCode.USER_NOT_QUESTIONTOR);
            }
            question.setCreator(Integer.parseInt(user.getAccountId()));
            question.setGmtModified(System.currentTimeMillis());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andCreatorEqualTo(question.getId());
            int i = questionMapper.updateByPrimaryKeySelective(question);
            if(i!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }
    public void addView(int id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        question.setId(id);
        question.setViewCount(1);
        questionExMapper.addView(question);
    }
    public void addComment(int id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        question.setId(id);
        question.setCommentCount(1);
        questionExMapper.addView(question);
    }
}
