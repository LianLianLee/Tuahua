package com.tanhua.dubbo.api;

import com.tanhua.model.mongo.Movement;

/**
 * @author Administrator
 */
public interface MovementApi {

    /**
     * 发布动态
     */
    public void publish(Movement movement);
}
