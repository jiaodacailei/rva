package com.ruoyi.rva.fe.service;

public interface IRvaFeMetaService {

    /**
     * 根据数据库中table的元数据，刷新前端视图元数据：列表rva_fe_list(listx_*)等
     * @param table
     */
    void refreshList(String table);

    void cloneList(String listId);

    /**
     * 快速创建前端列表的对象及相关元数据
     * @param objName
     * @param module
     */
    void quickCreate(String objName, String objNo, String module);
}
