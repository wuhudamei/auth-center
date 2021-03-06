package cn.damei.common.service;

import cn.damei.dto.page.PageTable;
import cn.damei.dto.page.Pagination;

import java.util.List;
import java.util.Map;


public interface IBaseService<T> {

    T getById(Long id);

    int insert(T entity);

    int update(T entity);

    int deleteById(Long id);

    List<T> findAll();

    PageTable<T> searchScrollPage(Map<String, Object> params, Pagination page);
}
