package com.jstarcraft.cloud.registration;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

/**
 * 服务管理器
 * 
 * @author Birdy
 *
 * @param <T>
 */
public interface ServerManager<T extends Server> extends ServerList<T> {

    /**
     * 获取服务标识
     * 
     * @return
     */
    String getServiceId();

}
