package com.ruoyi.rva.framework.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.rva.framework.domain.RvaLog;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.mapper.RvaDataMapper;
import com.ruoyi.rva.framework.mapper.RvaLogMapper;
import com.ruoyi.rva.framework.mapper.RvaViewMapper;
import com.ruoyi.rva.framework.service.IRvaLogService;
import com.ruoyi.rva.framework.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 日志Service业务层处理
 *
 * @author jiaodacailei
 * @date 2022-03-20
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class RvaLogServiceImpl implements IRvaLogService
{
    private final RvaLogMapper logMapper;

    private final RvaDataMapper dataMapper;

    /**
     * 查询日志
     *
     * @param id 日志主键
     * @return 日志
     */
    @Override
    public RvaLog selectRvaLogById(Long id)
    {
        return logMapper.selectRvaLogById(id);
    }

    /**
     * 查询日志列表
     *
     * @param rvaLog 日志
     * @return 日志
     */
    @Override
    public List<RvaLog> selectRvaLogList(RvaLog rvaLog)
    {
        return logMapper.selectRvaLogList(rvaLog);
    }

    @Override
    public List<RvaLog> selectRvaLogList(String keyValue, RvaView view, RvaMap<String, Object> formData, RvaMap<String, Object> req) {
        String logTable = view.getLogTable();
        if ("N".equals(logTable)) {
            RvaUtils.throwRequiredException("日志表");
        }
        RvaMap velocityContext = getVelocityContext(keyValue, formData, view).rvaPutAll(req);
        String logWhere = parseValue(view.getLogWhere(), velocityContext, String.format("key_value = '%s' and obj_id = '%s'", "${formData.keyPropValue}", view.getObjId()));
        RvaLog rvaLog = new RvaLog();
        rvaLog.setLogWhere(logWhere);
        rvaLog.setTable(logTable);
        rvaLog.setObjId(view.getObjId());
        return selectRvaLogList(rvaLog);
    }

    /**
     * 新增日志
     *
     * @param rvaLog 日志
     * @return 结果
     */
    @Override
    public int insertRvaLog(RvaLog rvaLog)
    {
        rvaLog.setCreateTime(DateUtils.getNowDate());
        return logMapper.insertRvaLog(rvaLog);
    }

    private final RvaViewMapper rvaViewMapper;


    private final RvaVelocityUtils velocityUtils;

    private String KEY_VALUE_EXPRESSION = String.format("${%s}", RvaConstants.PROP_KEY_VALUE);

    @Override
    public void insertRvaLog(String viewId, String keyPropValue, RvaMap req) {
        RvaView view = rvaViewMapper.selectRvaViewById(viewId);
        String logTable = view.getLogTable();
        if ("N".equals(logTable)) {
            return;
        }
        RvaLog log = new RvaLog();
        log.setTable(logTable);
        log.setViewId(viewId);
        log.setObjId(view.getObjId());
        RvaMap<String, Object> formData = new RvaMap<>();
        view.getProperties().forEach(p -> {
            formData.put(p.getId(), req.get(p.getId()));
        });
        log.setFormData(RvaJsonUtils.writeAsString(formData));
        log.setViewData(RvaJsonUtils.writeAsString(view));
        log.setCreateUpdateInfo();
        RvaMap velocityContext = getVelocityContext(keyPropValue, req, view).rvaPut("createTime", RvaDateUtils.formatDate(log.getCreateTime(), "yyyy-MM-dd HH:mm"));
        String name = parseValue(view.getLogName(), velocityContext, String.format("${%s.userName}于${createTime}${viewData.name}", RvaConstants.LOGIN_USER));
        log.setName(name);
        String keyValue = parseValue(view.getLogKeyValue(), velocityContext, KEY_VALUE_EXPRESSION);
        log.setKeyValue(keyValue);
        String fkValue = parseValue(view.getLogFkValue(), velocityContext, null);
        log.setFkValue(fkValue);
        logMapper.insertRvaLog(log);
    }

    private RvaMap getVelocityContext(String keyPropValue, RvaMap req, RvaView view) {
        return new RvaMap(req).rvaPut("viewData", view).rvaPut("formData", req)
                .rvaPut(RvaConstants.PROP_KEY_VALUE, keyPropValue);
    }

    private String parseValue (String expression, RvaMap velocityContext, String defaultVal) {
        if (RvaUtils.isEmpty(expression)) {
            expression = defaultVal;
        }
        if (RvaUtils.isEmpty(expression)) {
            return null;
        }
        return velocityUtils.parseWithLoginUser(expression, velocityContext);
    }

    /**
     * 修改日志
     *
     * @param rvaLog 日志
     * @return 结果
     */
    @Override
    public int updateRvaLog(RvaLog rvaLog)
    {
        return logMapper.updateRvaLog(rvaLog);
    }

    /**
     * 批量删除日志
     *
     * @param ids 需要删除的日志主键
     * @return 结果
     */
    @Override
    public int deleteRvaLogByIds(Long[] ids)
    {
        return logMapper.deleteRvaLogByIds(ids);
    }

    /**
     * 删除日志信息
     *
     * @param id 日志主键
     * @return 结果
     */
    @Override
    public int deleteRvaLog(Long id)
    {
        return logMapper.deleteRvaLogById(id);
    }

    @Override
    public int deleteRvaLog(String objId, String keyValue, String logTable) {
        if ("N".equals(logTable) || RvaUtils.isEmpty(logTable)) {
            return 0;
        }
        return dataMapper.deleteWhereMap("Y".equals(logTable) ? "rva_log" : logTable, new RvaMap<String, Object>("key_value", keyValue).rvaPut("obj_id", objId));
    }
}
