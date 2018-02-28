package com.rocoinfo.utils.cache;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * <dl>
 * <dd>Description:
 * 字符串类型的key，由于扫码登录过程中通过String作为key从GuavaCache中取数据会出现取出value为null的现象,
 * 产生这个问题的原因是由于在Guava在取数据时计算的字符串的hash和put进去时，计算结果不一致，具体原因没找到
 * 所以自己实现一个StringKey 做为GuavaCache的Key
 * </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/4/7 下午1:58</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class StringKey {

    private final String s;

    public StringKey(String s) {
        this.s = s;
    }

    public String getValue() {
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StringKey stringKey = (StringKey) o;

        return new EqualsBuilder()
                .append(s, stringKey.s)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(s)
                .toHashCode();
    }
}
