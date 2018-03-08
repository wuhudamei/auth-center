package cn.damei.dto.app;

import java.util.ArrayList;
import java.util.List;


public class PermissionTreeDto {

    private Long id;

    private String text;


    private List<PermissionTreeDto> children;


    private TreeState state;


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


    public void pushChildren(PermissionTreeDto subPermissionTreeDto) {
        if(subPermissionTreeDto != null){
            if(this.children == null){
                this.children = new ArrayList<>();
            }
            this.children.add(subPermissionTreeDto);
        }
    }



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
