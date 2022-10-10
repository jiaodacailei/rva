package com.ruoyi.rva.framework.service;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaChart;
import com.ruoyi.rva.framework.domain.RvaMap;

/**
 * 图Service接口
 * 
 * @author jiaodacailei
 * @date 2022-03-05
 */
public interface IRvaChartService 
{
    /**
     * 查询图
     * 
     * @param id 图主键
     * @return 图
     */
    public RvaChart selectRvaChartById(String id);

    /**
     * 根据图表元数据，获取其结果集
     * @param datasetId
     * @param req
     * @return {dsMeta: {}, dsData: [[],[]]}
     */
    RvaMap<String, Object> getDataset(String datasetId, RvaMap<String, Object> req);
}
