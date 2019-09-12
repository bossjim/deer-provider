package com.jimboss.deer.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jimboss.deer.common.domain.DeerConstant;
import com.jimboss.deer.common.domain.QueryRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName SortUtil
 * @Description TODO
 * @Author jinyong
 * @DATE 2019/9/9 10:57
 * @Version 1.0
 **/
public class SortUtil {
    /**
     * 处理排序 for mybatis-plus
     *
     * @param request           QueryRequest
     * @param wrapper           wrapper
     * @param defaultSort       默认排序的字段
     * @param defaultOrder      默认排序规则
     * @param camelToUnderscore 是否开启驼峰转下划线
     */
    public static void handleWrapperSort(QueryRequest request, QueryWrapper wrapper, String defaultSort, String defaultOrder, boolean camelToUnderscore) {
        String sortField = request.getSortField();
        if (camelToUnderscore) {
            sortField = DeerUtil.camelToUnderscore(sortField);
            defaultSort = DeerUtil.camelToUnderscore(defaultSort);
        }
        if (StringUtils.isNotBlank(request.getSortField())
                && StringUtils.isNotBlank(request.getSortOrder())
                && !StringUtils.equalsIgnoreCase(request.getSortField(), "undefined")
                && !StringUtils.equalsIgnoreCase(request.getSortOrder(), "undefined")) {
            if (StringUtils.equals(request.getSortOrder(), DeerConstant.ORDER_DESC))
                wrapper.orderByDesc(sortField);
            else
                wrapper.orderByAsc(sortField);
        } else {
            if (StringUtils.isNotBlank(defaultSort)) {
                if (StringUtils.equals(defaultOrder, DeerConstant.ORDER_DESC))
                    wrapper.orderByDesc(defaultSort);
                else
                    wrapper.orderByAsc(defaultSort);
            }
        }
    }
}
