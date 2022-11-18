package com.thinkingdata.server.controller;

import com.thinkingdata.server.model.ProjectEntity;
import com.thinkingdata.server.service.ProjectService;
import com.thinkingdata.tools.aop.WebLog;
import com.thinkingdata.tools.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/10/12 4:48 PM
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = "/addProject", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了项目接口")
    public ResponseData<Object> addProject(@RequestBody ProjectEntity projectEntity) {
        return projectService.addProject(projectEntity);
    }

    @RequestMapping(value = "/deleteProject", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了删除项目接口")
    public ResponseData<Object> deleteProject(@RequestBody Map<String, Integer> map) {
        return projectService.deleteProject(map.get("id"));
    }

    @RequestMapping(value = "/projectList", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了项目列表接口")
    public ResponseData<Object> projectList(@RequestBody Map<String, Object> map) {
        return projectService.projectList(map);
    }

    @RequestMapping(value = "/updateProject", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了更新项目接口")
    public ResponseData<Object> updateProject(@RequestBody ProjectEntity projectEntity) {
        return projectService.updateProject(projectEntity);
    }


    @RequestMapping(value = "/getProjectById", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了获取项目接口")
    public ResponseData<Object> getProjectById(@RequestBody Map<String, Integer> map) {
        return projectService.getProjectById(map);
    }
}
