package com.thinkingdata.server.model;


import lombok.Data;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/08/25 10:55 AM
 */

// 项目类型实体
@Data
public class ProjectEntity {

    // 数据库自增id
    private Integer id;
    // 项目名称
    private String name;
    // 创建时间
    private String createTime;
    // 更新时间
    private String updateTime;

}
