package com.thinkingdata.server.dao;

import com.thinkingdata.server.model.ProjectEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/10/12 3:37 PM
 */
@Repository
public interface ProjectDao {

    public Integer addProject(ProjectEntity projectEntity);

    public Integer updateProject(ProjectEntity projectEntity);

    public Integer deleteProject(Integer id);

    public List<ProjectEntity> projectList(Map<String, Object> paramMap);

    public Integer projectTotal(Map<String, Object> paramMap);

    public List<Map<String, Object>> projectSelectList();

    public ProjectEntity getProjectById(Integer id);
}
