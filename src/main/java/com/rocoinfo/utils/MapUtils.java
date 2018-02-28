package com.rocoinfo.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <dl>
 * <dd>Description:</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2016/6/28 13:42</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class MapUtils {

    private MapUtils() {
    }

    /**
     * 向map中传入参数 需判断空值
     *
     * @param map   map
     * @param key   key
     * @param value value
     */
    public static void putNotNull(Map<String, Object> map, String key, Object value) {
        if (map == null || value == null)
            return;
        if (value instanceof String) {
            if (StringUtils.isNotBlank((String) value))
                map.put(key, ((String) value).trim());
        } else {
            map.put(key, value);
        }
    }

    /**
     * 向map中传入参数 需判断空值,如果是空值 则放入默认值
     *
     * @param map   map
     * @param key   key
     * @param value value
     */
    public static void putOrElse(Map<String, Object> map, String key, Object value, Object defaultVal) {
        if (map == null || StringUtils.isBlank(key))
            return;
        if (value == null) {
            map.put(key, defaultVal);
            return;
        }
        if (value instanceof String) {
            if (StringUtils.isNotBlank((String) value))
                map.put(key, ((String) value).trim());
            else
                map.put(key, defaultVal);
        } else {
            map.put(key, value);
        }
    }

    /**
     * 构建一个hashmap，存放传入的k，v
     *
     * @param k   key
     * @param v   value
     * @param <K> key的泛型
     * @param <V> value的泛型
     * @return
     */
    public static <K, V> Map<K, V> of(K k, V v) {
        Map<K, V> map = new HashMap<>();
        checkNotNullAndPut(map, k, v);
        return map;
    }

    /**
     * 构建一个hashmap，存放传入的k，v
     *
     * @param k1  key1
     * @param v2  value2
     * @param k2  key1
     * @param v2  value2
     * @param <K> key的泛型
     * @param <V> value的泛型
     * @return
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        Map<K, V> map = new HashMap<>();
        checkNotNullAndPut(map, k1, v1);
        checkNotNullAndPut(map, k2, v2);
        return map;
    }

    /**
     * 如果key不为null,value不为null，则将key/value放入map
     *
     * @param map map
     * @param k   key
     * @param v   value
     * @param <K> key类型
     * @param <V> value类型
     */
    private static <K, V> void checkNotNullAndPut(Map<K, V> map, K k, V v) {
        if (k != null && v != null) {
            map.put(k, v);
        }
    }
}
