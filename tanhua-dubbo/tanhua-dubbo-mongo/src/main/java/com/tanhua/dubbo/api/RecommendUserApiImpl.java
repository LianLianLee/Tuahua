package com.tanhua.dubbo.api;

import com.tanhua.model.mongo.RecommendUser;
import com.tanhua.model.vo.PageResult;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@DubboService
public class RecommendUserApiImpl implements RecommendUserApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询缘分值最高的人
     * @param toUserId
     * @return
     */
    @Override
    public RecommendUser queryWithMaxScore(Long toUserId) {
        Criteria criteria = Criteria.where("toUserId").is(toUserId);
        Query query = Query.query(criteria).with(Sort.by(Sort.Order.desc("score"))).limit(1);
        return mongoTemplate.findOne(query,RecommendUser.class);
    }

    @Override
    public PageResult queryRecommendUserList(Integer page, Integer pagesize, Long userId) {

        //构建Query对象
        Query query = Query.query(Criteria.where("toUserId").is(userId));
        Long count = mongoTemplate.count(query,RecommendUser.class);
        query.skip((page-1)*pagesize)
                .limit(pagesize)
                .with(Sort.by(Sort.Order.desc("score")));
        //查询数据列表
        List<RecommendUser> recommendUser =mongoTemplate.find(query,RecommendUser.class);
        return new PageResult(page,pagesize,count,recommendUser);
    }
}
