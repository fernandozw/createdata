package com.thinkingdata.server.dao;

import com.thinkingdata.server.model.ScriptEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/10/09 3:37 PM
 */
@Repository
public interface ScriptDao {

    public Integer addScript(ScriptEntity entity);

    public ScriptEntity getScriptById(Integer id);


    public Integer deleteScript(Integer id);


    public Integer deleteScriptSon(Integer id);


    public List<ScriptEntity> scriptList(Map<String, Object> paramMap);


    public Integer scriptTotal(Map<String, Object> paramMap);

    public Integer updateIsDone(Integer id);
    public Integer updateNoDone(Integer id);


    public Integer updateScript(ScriptEntity entity);
}
