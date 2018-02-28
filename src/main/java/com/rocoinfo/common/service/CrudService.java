package com.rocoinfo.common.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rocoinfo.common.persistence.CrudDao;
import com.rocoinfo.dto.page.PageTable;
import com.rocoinfo.dto.page.Pagination;
import com.rocoinfo.entity.IdEntity;
import com.rocoinfo.entity.admin.SystemUser;
import com.rocoinfo.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * <dl>
 * <dd>描述:service基础类--获取和更新</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：15/7/30 上午11:50</dd>
 * <dd>作者： weiys</dd>
 * </dl>
 *
 * @author Aaron
 */
@SuppressWarnings("all")
public abstract class CrudService<D extends CrudDao<T>, T extends IdEntity> extends BaseService<T> {

    /**
     * 持久层对象
     */
    @Autowired
    protected D entityDao;

    public T getById(Long id) {
        if (id == null || id < 1)
            return null;
        return entityDao.getById(id);
    }

    @Transactional
    public int insert(T entity) {
        if (entity == null)
            return 0;
        entity.setCreateUser(new SystemUser(WebUtils.getLoggedUserId()));
        entity.setCreateTime(new Date());
        return entityDao.insert(entity);
    }

    public int update(T entity) {
        if (entity == null || entity.getId() == null)
            return 0;
        return entityDao.update(entity);
    }

    public int deleteById(Long id) {
        if (id == null || id < 1)
            return 0;
        return this.entityDao.deleteById(id);
    }

    public List<T> findAll() {
        return entityDao.findAll();
    }

    private static final String PAGE_SORT = "sort";

    public PageTable searchScrollPage(Map<String, Object> params, Pagination page) {
        // 设置排序条件
        params.put(PAGE_SORT, page.getSort());
        // 利用pagehelper分页查询
        PageHelper.offsetPage(page.getOffset(), page.getLimit());
        Page<T> result = (Page<T>) this.entityDao.search(params);
        // 返回查询结果
        return new PageTable<T>(result.getResult(), result.getTotal());
    }
}
