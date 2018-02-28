package com.rocoinfo.dto.page;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * <dl>
 * <dd>Description: from spring framework，delete unuseful code</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/3/9 下午2:25</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class Sort implements Iterable<Sort.Order>, Serializable {

    private static final long serialVersionUID = 5737186511678863905L;
    public static final Direction DEFAULT_DIRECTION = Direction.ASC;

    private final List<Order> orders;

    /**
     * Creates a new {@link Sort} instance using the given {@link Order}s.
     *
     * @param orders must not be {@literal null}.
     */
    public Sort(Order... orders) {
        this(Arrays.asList(orders));
    }

    /**
     * Creates a new {@link Sort} instance.
     *
     * @param orders must not be {@literal null} or contain {@literal null}.
     */
    public Sort(List<Order> orders) {

        if (null == orders || orders.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one sort property to sort by!");
        }

        this.orders = orders;
    }


    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return StringUtils.collectionToCommaDelimitedString(orders);
    }

    @Override
    public Iterator<Order> iterator() {
        return this.orders.iterator();
    }

    /**
     * Enumeration for sort directions.
     *
     * @author Oliver Gierke
     */
    public enum Direction {

        ASC, DESC;

        /**
         * 根据字符串构建枚举对象
         *
         * @param value value
         * @return
         */
        public static Direction fromString(String value) {
            try {
                return Direction.valueOf(value.toUpperCase(Locale.US));
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), e);
            }
        }
    }

    /**
     * PropertyPath implements the pairing of an {@link Direction} and a property. It is used to provide input for
     * {@link Sort}
     *
     * @author Oliver Gierke
     * @author Kevin Raymond
     */
    public static class Order implements Serializable {

        private static final long serialVersionUID = 1522511010900108987L;

        /**
         * 升序 or 降序
         */
        private final Direction direction;

        /**
         * 排序字段
         */
        private final String property;

        public Order(Direction direction, String property) {
            this.direction = direction;
            this.property = property;
        }

        public static Order build(Direction direction, String property) {
            return new Order(direction, property);
        }

        public Direction getDirection() {
            return direction;
        }

        public String getProperty() {
            return property;
        }
    }
}
