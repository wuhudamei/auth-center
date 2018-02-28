package com.rocoinfo.utils.cache;

/**
 * <dl>
 * <dd>Description: 利用Guava实现字符串缓存 </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/4/5 下午8:37</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class StringCache {

    /**
     * 自己实现了一个简单的FIFO缓存，设定一个阈值，防止缓存过大
     */
    private static FIFOCache<String, String> cache = null;

    static {
        cache = new FIFOCache.Builder()
                .threshold(100)
                .builder();
    }

    public static void put(String k, String v) {
        cache.put(k, v);
    }

    public static String get(String k) {
        return cache.get(k);
    }

    public static String remove(String k) {
        return cache.remove(k);
    }

    public static int size() {
        return cache.size();
    }
}
