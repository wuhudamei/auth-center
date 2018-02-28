package com.rocoinfo.dto.page;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * <dl>
 * <dd>Description: 分页查询返回结果</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/3/9 下午2:13</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class PageTable<E> implements Serializable {

    private static final PageTable EMPTY_PAGE = new PageTable<>(Collections.emptyList(), 0);

    /**
     * 数据
     */
    private List<E> rows;

    /**
     * 总页数
     */
    private long total;

    public PageTable(List<E> rows, long total) {
        this.rows = rows;
        this.total = total;

    }

    public static <E> PageTable<E> build(List<E> rows, int total) {
        return new PageTable<E>(rows, total);
    }

    public List<E> getRows() {
        return rows;
    }

    public void setRows(List<E> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
