package com.rocoinfo.dto.page;

/**
 * <dl>
 * <dd>Description: 分页查询封装的数据</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/3/9 下午2:13</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class Pagination {

    private final static int DEFALUT_OFFSET = 0;
    private final static int DEFALUT_LIMIT = 10;

    /**
     * 偏移量
     */
    private int offset;

    /**
     * 每页数据量
     */
    private int limit;

    /**
     * 排序方式：升序、降序
     */
    private Sort sort;

    /**
     * 使用默认的分页条件
     */
    public Pagination() {
        this.offset = DEFALUT_OFFSET;
        this.limit = DEFALUT_LIMIT;
    }

    /**
     * 提供offset和limit
     *
     * @param offset offset
     * @param limit  limit
     */
    public Pagination(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    /**
     * 提供offset、limit和order
     *
     * @param offset offset
     * @param limit  limit
     * @param sort   sort
     */
    public Pagination(int offset, int limit, Sort sort) {
        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }
}
