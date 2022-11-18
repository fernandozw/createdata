package com.thinkingdata.server.dao;

import com.thinkingdata.server.model.MethodEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/09/30 4:39 PM
 */
@Repository
public interface MethodDao {
    public Integer batchAddMethod(@Param("methodList") List<MethodEntity> methodList);

    public List<MethodEntity> getMethodByScriptId(Integer scriptId);

    public Integer batchUpdateMethod(@Param("methodList") List<MethodEntity> methodList);

    public Integer deleteMethodById(@Param("id") Integer id);

    public Integer updateParamSet(@Param("status") Integer status, @Param("id") Integer id);
}
