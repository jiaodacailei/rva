package com.ruoyi.rva.activiti.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.rva.activiti.mapper.RvaProcessMapper;
import com.ruoyi.rva.framework.domain.RvaBaseEntity;
import com.ruoyi.rva.framework.util.RvaUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 流程任务定义对象 rva_process_task
 *
 * @author jiaodacailei
 * @date 2022-03-22
 */
public class RvaProcessTask extends RvaBaseEntity implements Comparable<RvaProcessTask> {

    private static final long serialVersionUID = 1L;

    /**
     * refuseEnd的选项，打回到初始节点
     */
    private static final String REFUSED_FIRST = "F";

    /**
     * refuseEnd的选项，打回到上一节点
     */
    private static final String REFUSED_PREVIOUS = "P";

    /**
     * refuseEnd的选项，打回到结束
     */
    private static final String REFUSED_END = "E";

    /**
     * refuseEnd的选项，打回到当前节点
     */
    private static final String REFUSED_CURRENT = "C";

    /**
     * refuseEnd的选项，打回到某个节点
     */
    private static final String REFUSED_OTHER = "O";

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
     * 标识
     */
    @Excel(name = "标识")
    private String key;

    /**
     * 是否决策
     */
    @Excel(name = "是否决策")
    private String decision;

    /**
     * 打回结束
     */
    @Excel(name = "打回结束")
    private String refuseEnd;

    /**
     * 流程定义，rva_process.id
     */
    @Excel(name = "流程定义，rva_process.id")
    private String processId;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Integer idx;

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

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getDecision() {
        return decision;
    }

    public void setRefuseEnd(String refuseEnd) {
        this.refuseEnd = refuseEnd;
    }

    public String getRefuseEnd() {
        return refuseEnd;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("key", getKey())
                .append("decision", getDecision())
                .append("refuseEnd", getRefuseEnd())
                .append("processId", getProcessId())
                .append("idx", getIdx())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("data", getData())
                .toString();
    }

    @Override
    public int compareTo(RvaProcessTask o) {
        return this.getIdx() - o.getIdx();
    }

    public String getKeyUnderScoreCase() {
        return StringUtils.toUnderScoreCase(this.key);
    }

    public void setViewId(String viewId) {
        setJsonProperty("viewId", viewId);
    }

    public String getViewId() {
        return getJsonPropertyString("viewId");
    }

    /**
     * 获取决策属性ID
     *
     * @return
     */
    public String getReviewResultProperty() {
        String viewId = getViewId();
        if (viewId == null) {
            return null;
        }
        return viewId + "_" + getKeyUnderScoreCase() + "_jg";
    }

    @JsonIgnore
    private RvaProcess getProcess() {
        return RvaUtils.getBean(RvaProcessMapper.class).get().selectRvaProcessById(this.processId);
    }

    @JsonIgnore
    public RvaProcessTask getRefusedOtherTask() {
        if (REFUSED_OTHER.endsWith(refuseEnd)) {
            return getProcess().getTasks().stream().filter(task -> task.getId().equals(getJsonPropertyString("refusedBackId"))).findFirst().get();
        }
        return null;
    }

    /**
     * 初始横坐标
     */
    private final static int x = 30;

    /**
     * 初始纵坐标
     */
    private final static int y = 100;

    // 事件顶部纵坐标
    private final static int eventY = 1 + y;
    // 事件宽度
    private final static int eventWidth = 42;
    private final static int eventHeight = 42;
    private final static int taskWidth = 106;
    private final static int taskHeight = 56;
    private final static int gateWayWidth = 40;
    private final static int gateWayHeight = 40;
    private final static int flowWidth = 60;
    private final static int flowY = y + eventWidth / 2;
    private final static int taskY = y + eventWidth / 2 - taskHeight / 2;
    private final static int gateWayY = y + eventWidth / 2 - gateWayHeight / 2;
    private final static int flowRefuseVerticalMargin = 20;

    @JsonIgnore
    public int getRefusedOtherTaskX() {
        List<RvaProcessTask> tasks = getProcess().getTasks();
        int taskX = x + eventWidth + flowWidth + taskWidth/2;
        for (int i = 0; i < tasks.size(); i++) {
            RvaProcessTask task = (RvaProcessTask) tasks.get(i);
            if (task == getRefusedOtherTask()) {
                break;
            }
            taskX += flowWidth + taskWidth;
            if ("Y".equals(task.getDecision())) {
                taskX += flowWidth + gateWayWidth;
            }
        }
        return taskX;
    }
}
