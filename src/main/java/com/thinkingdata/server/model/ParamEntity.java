package com.thinkingdata.server.model;

import lombok.Data;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/09/29 11:45 AM
 */
@Data
public class ParamEntity {
    // 自增id
    private Integer id;
    // 方法id
    private Integer methodId;
    // 参数名称
    private String paramName;
    // 参数默认值
    private String paramValue;
    // 参数注释
    private String paramMark;
    // 参数类型
    private String paramType;
    // 参数控件
    private String paramWidget;
    // 创建时间
    private String createTime;
    // 更新时间
    private String updateTime;
}
