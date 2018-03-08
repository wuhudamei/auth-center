package cn.damei.common.service;

import cn.damei.entity.IdEntity;
import cn.damei.entity.admin.SystemUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import cn.damei.common.persistence.CrudDao;
import cn.damei.dto.page.PageTable;
import cn.damei.dto.page.Pagination;
import cn.damei.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;



@SuppressWarnings("all")
public abstract class CrudService<D extends CrudDao<T>, T extends IdEntity> extends BaseService<T> {


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
