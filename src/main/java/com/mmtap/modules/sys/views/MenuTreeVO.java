package com.mmtap.modules.sys.views;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mmtap.modules.sys.model.Menu;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author mmtap.com
 * @date 2019/1/11
 **/
@Getter
@Setter
public class MenuTreeVO  implements Comparable<MenuTreeVO> {

    private Long id;

    private String name;

    private String icon;

    private String description;

    private String url;

    private Long sort;

    /**
     * 菜单类别对应的深度
     */
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Menu.Type depth;

    /**
     * 许可用于认证标识
     */
    private String authority;

    /**
     *
     皮肤 查看skins.css对应的样式
     */
    private String skin;

    private Set<MenuTreeVO> children;

    @JsonIgnore
    private MenuTreeVO parent;


    public void addChildren(MenuTreeVO menuTreeVO) {
        if (Objects.isNull(this.children)) {
            synchronized (this) {
                if (Objects.isNull(this.children)) {
                    children = new TreeSet<>();
                }
            }
        }
        this.children.add(menuTreeVO);
    }

    public Long getParentId() {
        return Objects.nonNull(parent) ? parent.getId() : -1L;
    }

    @Override
    public int compareTo(MenuTreeVO o) {
        return (int) (Objects.nonNull(getSort())&&Objects.nonNull(o.getSort()) ? getSort() - o.getSort() : getId() - o.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        MenuTreeVO that = (MenuTreeVO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
