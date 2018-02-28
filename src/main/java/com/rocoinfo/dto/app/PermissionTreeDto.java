package com.rocoinfo.dto.app;

import java.util.ArrayList;
import java.util.List;

/**
 * <dl>
 * <dd>Description: 应用权限的树结构</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/1 15:57</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
public class PermissionTreeDto {
    /**
     * 节点id
     */
    private Long id;
    /**
     * 节点文本
     */
    private String text;

    /**
     * 子节点列表
     */
    private List<PermissionTreeDto> children;

    /**
     * 状态信息
     */
    private TreeState state;

    /**
     * 排序值
     */
    private Integer sort;

    public PermissionTreeDto setOpened(Boolean opened) {
        if (this.state == null) {
            this.state = new TreeState(opened);
        } else {
            this.state.setOpened(opened);
        }
        return this;
    }

    public Long getId() {
        return id;
    }

    public PermissionTreeDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return text;
    }

    public PermissionTreeDto setText(String text) {
        this.text = text;
        return this;
    }

    public List<PermissionTreeDto> getChildren() {
        return children;
    }

    public PermissionTreeDto setChildren(List<PermissionTreeDto> children) {
        this.children = children;
        return this;
    }

    public TreeState getState() {
        return state;
    }

    public PermissionTreeDto setState(TreeState state) {
        this.state = state;
        return this;
    }

    public Integer getSort() {
        return sort;
    }

    public PermissionTreeDto setSort(Integer sort) {
        this.sort = sort;
        return this;
    }

    /**
     * 添加子节点
      @param subPermissionTreeDto 子节点
     */
    public void pushChildren(PermissionTreeDto subPermissionTreeDto) {
        if(subPermissionTreeDto != null){
            if(this.children == null){
                this.children = new ArrayList<>();
            }
            this.children.add(subPermissionTreeDto);
        }
    }


    /**
     * 树的状态类
     */
    private static class TreeState {

        private Boolean opened;

        public TreeState() {
        }

        public TreeState(Boolean opened) {
            this.opened = opened;
        }

        public Boolean getOpened() {
            return opened;
        }

        public void setOpened(Boolean opened) {
            this.opened = opened;
        }
    }
}
