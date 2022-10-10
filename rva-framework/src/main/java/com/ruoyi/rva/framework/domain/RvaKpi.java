package com.ruoyi.rva.framework.domain;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.rva.framework.mapper.RvaDataMapper;
import com.ruoyi.rva.framework.mapper.RvaKpiItemMapper;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.rva.framework.service.IRvaSystemService;
import com.ruoyi.rva.framework.util.RvaJsonUtils;
import com.ruoyi.rva.framework.util.RvaUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * KPI对象 rva_kpi
 *
 * @author jiaodacailei
 * @date 2022-03-22
 */
public class RvaKpi extends RvaBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 名称
     */
    @Excel(name = "名称")
    private String name;

    /**
     * 查询视图
     */
    @Excel(name = "查询视图")
    private String searchId;

    /**
     * 所属对象
     */
    @Excel(name = "所属对象")
    private String objId;

    /**
     * SQL
     */
    @Excel(name = "SQL")
    private String dataSql;

    /**
     * 模板
     */
    @Excel(name = "模板")
    private String template;

    /**
     * 其他数据
     */
    @Excel(name = "其他数据")
    private String data;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getObjId() {
        return objId;
    }

    public void setDataSql(String dataSql) {
        this.dataSql = dataSql;
    }

    public String getDataSql() {
        return dataSql;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    private List<RvaKpiItem> items = null;

    public RvaKpi loadData() {
        if (items == null) {
            RvaKpiItem search = new RvaKpiItem();
            search.setKpiId(this.getId());
            items = SpringUtils.getBean(RvaKpiItemMapper.class).selectRvaKpiItemList(search);
            SysUser loginUser = SpringUtils.getBean(IRvaSystemService.class).getLoginUser();
            String itemIds = SpringUtils.getBean(RvaDataMapper.class).selectString("select GROUP_CONCAT(kpixiang_153d) from sys_role_kpi_item_68ff where jiaosexinxibiao_167a = " + loginUser.getRoleId());
            if (itemIds != null) {
                List<RvaKpiItem> newItems = new ArrayList<>();
                items.forEach(item -> {
                    if (Arrays.asList(itemIds.split(",")).contains(item.getId())) {
                        return;
                    }
                    newItems.add(item);
                });
                items = newItems;
            }
            Collections.sort(items);
        }
        return this;
    }

    public List<RvaKpiItem> getItems() {
        loadData();
        return items;
    }

    public void setItems(List<RvaKpiItem> items) {
        this.items = items;
    }

    public static final String NAME = "kpi";

    public void setModule(String module) {
        this.setJsonProperty("module", module);
    }

    public String getModule() {
        return this.getJsonPropertyString("module");
    }

    public List<String> getDataSqlList() {
        if (RvaUtils.isEmpty(dataSql)) {
            return new ArrayList<>();
        }
        return RvaJsonUtils.readAsList(dataSql, String.class);
    }

    public void setRows(int rows) {
        this.setJsonProperty("rows", rows);
    }

    public int getRows() {
        return getJsonPropertyInt("rows", 1);
    }

    private List<List<RvaKpiItem>> itemsList = null;

    public void setItemsList(List<List<RvaKpiItem>> itemsList) {
        this.itemsList = itemsList;
    }

    public List<List<RvaKpiItem>> getItemsList() {
        return itemsList;
    }
}
