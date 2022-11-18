package com.thinkingdata.server.model;

import lombok.Data;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/08/25 11:22 AM
 */
// 执行脚本实体
@Data
public class ScriptEntity {
    // 自增ID
    private Integer id;
    // 脚本名称
    private String name;
    // 关联项目ID
    private Integer projectId;
    // 脚本内容
    private String content;
    // 创建时间
    private String createTime;
    // 更新时间
    private String updateTime;
    //创建人
    private String owner;
    // 是否完成配置
    private Integer isDone;
    // 脚本类型
    private String type;

}
