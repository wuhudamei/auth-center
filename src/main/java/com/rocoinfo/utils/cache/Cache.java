package com.rocoinfo.utils.cache;

/**
 * <dl>
 * <dd>Description: 缓存接口 </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/4/11 下午1:36</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public interface Cache<K, V> {

    void put(K k, V v);

    V get(K k);

    V remove(K k);

    int size();
}
