package com.rocoinfo.utils.cache;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <dl>
 * <dd>Description: 实现一个先进先出的缓存，防止缓存不及时清理出现内存溢出的现象</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/4/11 上午11:32</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class FIFOCache<K, V> implements Cache<K, V> {

    private FIFOCache(int threshold) {
        this.threshold = threshold;
    }

    /**
     * 阈值，默认为整数最大值
     */
    private int threshold = Integer.MAX_VALUE;

    /**
     * 用来保存key进入的顺序，因为频繁删除，所以使用LinkedList
     */
    private List<K> keys = new LinkedList<>();

    /**
     * 用于移除先进元素时采用的锁
     */
    private ReentrantLock lock = new ReentrantLock();

    /**
     * 利用ConcurrentHashMap实现key/value的缓存，保证现成安全
     */
    private Map<K, V> cache = new ConcurrentHashMap<>();

    /**
     * 缓存构建器
     */
    public static class Builder {

        private int threshold = Integer.MAX_VALUE;

        public Builder threshold(int threshold) {
            this.threshold = threshold;
            return this;
        }

        public <K, V> FIFOCache<K, V> builder() {
            return new FIFOCache<>(threshold);
        }
    }

    @Override
    public void put(K k, V v) {
        checkThresholdAndSetKey(k);
        this.cache.put(k, v);
    }

    /**
     * 检查元素数量是否达到阈值，如果达到阈值，那么需要将LinkedList的第一个元素移除，并且将cache中相应的元素移除
     * 利用可重入锁保证现成安全
     *
     * @param k k
     */
    private void checkThresholdAndSetKey(K k) {
        if (k != null) {
            final ReentrantLock lock = this.lock;
            lock.lock();
            try {
                if (keys.size() == threshold) {
                    K oldK = this.keys.remove(0);
                    this.remove(oldK);
                }
                keys.add(k);
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public V get(K k) {
        if (k == null)
            return null;
        return cache.get(k);
    }

    @Override
    public V remove(K k) {
        return this.cache.remove(k);
    }

    @Override
    public int size() {
        return cache.size();
    }
}
