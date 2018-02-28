package com.rocoinfo.common.service;

import com.rocoinfo.dto.page.PageTable;
import com.rocoinfo.dto.page.Pagination;

import java.util.List;
import java.util.Map;

/**
 * @author weiys
 * @author Aaron
 */
public interface IBaseService<T> {

    T getById(Long id);

    int insert(T entity);

    int update(T entity);

    int deleteById(Long id);

    List<T> findAll();

    PageTable<T> searchScrollPage(Map<String, Object> params, Pagination page);
}
