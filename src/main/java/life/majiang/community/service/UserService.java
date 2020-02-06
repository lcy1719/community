package life.majiang.community.service;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public void findByid(User user) {
        UserExample userExample=new UserExample();
        userExample.createCriteria()//createCriteria：创建条件
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            //新增用户
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //更新用户
            UserExample userExample1 = new UserExample();
            userExample1.createCriteria()
                    .andAccountIdEqualTo(user.getAccountId());
            userMapper.updateByExampleSelective(user,userExample1);
        }
    }
}
