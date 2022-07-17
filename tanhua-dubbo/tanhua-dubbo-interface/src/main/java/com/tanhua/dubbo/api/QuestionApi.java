package com.tanhua.dubbo.api;

import com.tanhua.model.domain.Question;

public interface QuestionApi {

    //保存
    void save(Question question);

    //更新
    void update(Question question);

    Question findByUserId(Long id);
}
