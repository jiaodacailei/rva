package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaViewproperty;

/**
 * 视图属性Service接口
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
public interface IRvaViewpropertyService 
{
    void deleteWithTriggersByPropId(String id);

    void deleteWithTriggersByRelationId(String relationId);
}
