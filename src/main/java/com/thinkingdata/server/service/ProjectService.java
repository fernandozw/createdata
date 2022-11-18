package com.thinkingdata.server.service;

import com.thinkingdata.server.dao.ProjectDao;
import com.thinkingdata.server.model.ProjectEntity;
import com.thinkingdata.tools.response.ResponseData;
import com.thinkingdata.tools.response.ResponseDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/10/12 4:39 PM
 */
@Service
public class ProjectService {
    @Autowired
    private ProjectDao projectDao;

    /**
     * 新增项目
     *
     * @param projectEntity 项目实体
     * @return 返回新增结果
     */
    public ResponseData<Object> addProject(ProjectEntity projectEntity) {
        ResponseData<Object> responseData;
        responseData = ResponseDataUtils.buildAddSuccess(projectDao.addProject(projectEntity));
        return responseData;
    }

    /**
     * 编辑项目
     *
     * @param projectEntity 项目实体
     * @return 返回编辑结果
     */
    public ResponseData<Object> updateProject(ProjectEntity projectEntity) {
        ResponseData<Object> responseData;
        responseData = ResponseDataUtils.buildUpdateSuccess(projectDao.updateProject(projectEntity));
        return responseData;
    }

    /**
     * 根据id删除项目
     *
     * @param id 项目id
     * @return 返回删除结果
     */
    public ResponseData<Object> deleteProject(Integer id) {
        ResponseData<Object> responseData;
        responseData = ResponseDataUtils.buildDeleteSuccess(projectDao.deleteProject(id));
        return responseData;
    }

    /**
     * 根据查询条件获取项目列表并分页
     *
     * @param paramMap 查询条件
     * @return 返回项目列表
     */
    public ResponseData<Object> projectList(Map<String, Object> paramMap) {
        ResponseData<Object> responseData;
        Integer total = projectDao.projectTotal(paramMap);
        Integer pageSize = (Integer) paramMap.get("pageSize");
        Integer start = (Integer) paramMap.get("pageNum") * pageSize - pageSize;
        paramMap.put("start", start);
        paramMap.put("end", pageSize);
        List<ProjectEntity> scriptList = projectDao.projectList(paramMap);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("total", total);
        dataMap.put("projectList", scriptList);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }

    /**
     * 根据项目id获取项目
     * @param map 项目id
     * @return 项目实体
     */
    public ResponseData<Object> getProjectById(Map<String, Integer> map) {
        ResponseData<Object> responseData;
        responseData = ResponseDataUtils.buildSuccess(projectDao.getProjectById(map.get("id")));
        return responseData;
    }


}
