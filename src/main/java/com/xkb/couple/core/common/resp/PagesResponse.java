package com.xkb.couple.core.common.resp;

import lombok.Data;
import java.util.List;


/**
 * 分页响应
 *
 * @param <T>
 *  @author xuwatermelon
 *  @date 2026/02/06
 */
@Data
public class PagesResponse <T>{
        /**
         * 数据列表
         */
        private List<T> list;
        /**
         * 总数据量
         */
        private long total;
        /**
         * 当前页码
         */
        private int page;
        /**
         * 每页大小
         */
        private int pageSize;
        /**
         * 总页数
         */
        private int totalPages;
}
