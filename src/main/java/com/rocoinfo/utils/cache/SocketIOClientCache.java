package com.rocoinfo.utils.cache;

import com.corundumstudio.socketio.SocketIOClient;

/**
 * <dl>
 * <dd>Description: 用来缓存前台传递的uuid和SockectIOClient的映射关系</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/4/6 下午4:14</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class SocketIOClientCache {

    /**
     * 自己实现了一个简单的FIFO缓存，设定一个阈值，防止缓存过大
     */
    private static FIFOCache<String, SocketIOClient> cache = null;

    static {
        cache = new FIFOCache.Builder()
                .threshold(100)
                .builder();
    }

    public static void put(String k, SocketIOClient v) {
        cache.put(k, v);
    }

    public static SocketIOClient get(String k) {
        return cache.get(k);
    }

    public static SocketIOClient remove(String k) {
        return cache.remove(k);
    }

    public static int size() {
        return cache.size();
    }
}
