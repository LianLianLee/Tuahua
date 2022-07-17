package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.dubbo.mappers.QuestionMapper;
import com.tanhua.model.domain.Question;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class QuestionApiImpl implements QuestionApi{

    @Autowired
    private QuestionMapper questionMapper;
    //保存问题
    @Override
    public void save(Question question) {
        questionMapper.insert(question);
    }

    //更新问题
    @Override
    public void update(Question question) {
        questionMapper.updateById(question);
    }

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @Override
    public Question findByUserId(Long id) {
        QueryWrapper<Question> qw = new QueryWrapper<>();
        qw.eq("user_id",id);
        Question question = questionMapper.selectOne(qw);

        return question;
    }
}
