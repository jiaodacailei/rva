package com.ruoyi.rva.framework.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaSQL;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.util.RvaConstants;
import com.ruoyi.rva.framework.util.RvaJsonUtils;
import com.ruoyi.rva.framework.util.RvaUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * 视图Service接口
 *
 * @author jiaodacailei
 * @date 2021-07-27
 */
public interface IRvaViewService
{

    default List<String> parseSqls(String loadAfterSql) {
        List<String> sqls = new ArrayList<>();
        if (RvaUtils.isEmpty(loadAfterSql)) {
            return sqls;
        }
        if (loadAfterSql.trim().indexOf('[') == 0) {
            sqls = RvaJsonUtils.readAsList(loadAfterSql, String.class);
        } else {
            sqls.add(loadAfterSql);
        }
        return sqls;
    }

    default void setKeyNameValue(String keyPropId, String namePropId, RvaMap<String, Object> row) {
        row.put(RvaConstants.PROP_KEY_ID, keyPropId);
        row.put(RvaConstants.PROP_KEY_VALUE, row.get(keyPropId));
        if (namePropId == null) {
            row.put(RvaConstants.PROP_NAME_ID, null);
            row.put(RvaConstants.PROP_NAME_VALUE, null);
        } else {
            row.put(RvaConstants.PROP_NAME_ID, namePropId);
            row.put(RvaConstants.PROP_NAME_VALUE, row.get(namePropId));
        }
    }

    public void loadBeforeSql(RvaMap req, String loadBeforeSql);

    public void loadAfterSql(RvaMap req, String loadAfterSql, List<RvaMap<String, Object>> selectList);

    /**
     * 根据ID获取视图元数据
     *
     * @param viewId 视图ID
     * @param req
     * @return 视图
     */
    RvaView selectRvaViewById(String viewId, Map<String, Object> req);

    /**
     * 获取列表视图元数据和数据
     * @param listId 列表视图ID
     * @param searchId 查询视图ID
     * @param req
     * @return 包含Key：viewData-视图元数据RvaView，listData-视图数据List<RvaMap<String, Object>>
     */
    RvaMap selectListViewData (String listId, String searchId, RvaMap req);

    /**
     * 获取列表视图数据
     * @param listId
     * @param searchId
     * @param req
     * @param wheres
     * @return
     */
    List<RvaMap<String, Object>> selectList (String listId, String searchId, RvaMap req, String... wheres);

    /**
     * 构造列表视图的sql语句
     * @param req
     * @param listId
     * @param searchId
     * @param wheres
     * @return
     */
    RvaSQL getListSQL(RvaMap req, String listId, String searchId, String... wheres);

    /**
     * 获取【创建】表单视图元数据和数据
     * @param viewId
     * @param req
     * @return 包含Key：viewData-视图元数据RvaView，formData-视图数据RvaMap<String, Object>
     */
    RvaMap selectCreateViewData (String viewId, RvaMap req);

    /**
     * 获取【克隆】表单视图元数据和数据
     * @param viewId
     * @param req
     * @return 包含Key：viewData-视图元数据RvaView，formData-视图数据RvaMap<String, Object>
     */
    RvaMap selectCloneViewData(String viewId, RvaMap req);

    /**
     * 提交【创建】表单。克隆表单提交适用。
     * @param viewId
     * @param req
     */
    String submitCreateView (String viewId, RvaMap req);

    /**
     * 获取【修改】表单视图元数据和数据
     * @param viewId
     * @param req
     * @return 包含Key：viewData-视图元数据RvaView，formData-视图数据RvaMap<String, Object>
     */
    RvaMap selectUpdateViewData (String viewId, RvaMap req, Boolean noDataThrow);

    /**
     * 提交【修改】表单
     * @param viewId
     * @param req
     */
    String submitUpdateView (String viewId, RvaMap req);

    /**
     * 删除
     * @param viewId
     * @param req
     */
    void delete(String viewId, RvaMap req);

    /**
     * 加载多个表单视图（元）数据.
     * @param createView 创建表单视图ID
     * @param updateView 修改表单视图ID
     * @param req
     * @return 包括创建表单（元）数据和修改表单（元）数据列表. 格式：{ create: {viewData, formData}, update: {viewData, formDataList:[]} }
     */
    RvaMap selectFormsViewData(String createView, String updateView, RvaMap req);

    /**
     * 提交多个表单视图数据.
     * @param createView 创建表单视图ID
     * @param updateView 修改表单视图ID
     * @param req
     * @return
     */
    List<String> submitFormViews (String createView, String updateView, RvaMap req);

    /**
     * req.selection包括两行数据，则移动索引较大的行到另一行之上；如果包含一行数据，则和上一行数据交换索引值
     * @param viewId
     * @param req
     */
    void moveUp (String viewId, RvaMap req);

    /**
     * req.selection包括两行数据，则移动索引较小的行到另一行之下；如果仅包含一行数据，则和下一行数据交换索引值
     * @param viewId 列表视图ID
     * @param req
     */
    void moveDown (String viewId, RvaMap req);


    /**
     * 列表合计行数据
     * @param listId 列表视图ID
     * @param searchId 查询视图ID
     * @param req
     * @param wheres 条件，多个条件之间是【与】的关系，举例：xxx = 'abc'
     * @return
     */


    RvaMap<String, Object> selectSummariesData(String listId, String searchId, RvaMap req, String... wheres);

    void importExcel(String filePath, String createViewId, RvaMap rvaMap);

    void print(HttpServletResponse response, String beanName, RvaMap<String, Object> req);

    void hide(String viewId, RvaMap req);
}
