package com.thinkingdata.server.dao;

import com.thinkingdata.server.model.MethodEntity;
import com.thinkingdata.server.model.ParamEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/10/08 3:05 PM
 */
@Repository
public interface ParamDao {

    public List<ParamEntity> getParamsByMethodId(Integer methodId);

    public Integer batchAddParam(@Param("paramList") List<ParamEntity> paramList);

    public Integer batchUpdateParam(@Param("paramList") List<ParamEntity> paramList);

    public MethodEntity getMethodById(Integer methodId);

    public List<ParamEntity> getParamByScriptId(Integer scriptId);

    public Integer deleteByMethodId(@Param("methodId") Integer methodId);
}
