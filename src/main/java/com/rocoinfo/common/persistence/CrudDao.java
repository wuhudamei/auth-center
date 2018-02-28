/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.rocoinfo.common.persistence;

import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>描述:Dao Crud基础类</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：15/7/30 上午11:50</dd>
 * <dd>创建人： weiys</dd>
 * </dl>
 *
 * @author Aaron
 */
public interface CrudDao<T> extends BaseDao {

    /**
     * 查询关键字
     */
    String KEYWORD = "keyword";

    /**
     * 查询状态
     */
    String STATUS = "status";

    /**
     * 获取单条数据
     *
     * @param id
     */
    T getById(Long id);

    /**
     * 插入数据
     *
     * @param entity
     */
    int insert(T entity);

    /**
     * 更新数据
     *
     * @param entity
     */
    int update(T entity);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 查询所有
     *
     * @return
     */
    List<T> findAll();

    /**
     * 根据条件查询
     *
     * @param params 查询条件
     * @return
     */
    List<T> search(Map<String, Object> params);

    /**
     * 查询记录数
     *
     * @param params 查询条件
     * @return
     */
    Long searchTotal(Map<String, Object> params);
}