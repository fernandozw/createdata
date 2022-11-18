package com.thinkingdata.server.model;

import lombok.Data;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/09/29 11:40 AM
 */
@Data
public class MethodEntity {
    // 自增id
    private Integer id;
    // 脚本id
    private Integer scriptId;
    // 方法实际名称
    private String methodName;
    // 方法标签名称
    private String methodLabel;
    // 当前方法有无参数
    private Integer existParam;
    // 当前方法时候设置参数
    private Integer setParam;
    // 创建时间
    private String createTime;
    // 更新时间
    private String updateTime;
}
