package com.ruoyi.rva.fe.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.rva.framework.domain.RvaBaseEntity;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.util.RvaUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 列项对象 rva_fe_list_item
 * 
 * @author jiaodacailei
 * @date 2021-12-01
 */
public class RvaFeListItem extends RvaBaseEntity implements Comparable<RvaFeListItem>
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 位置（title-标题 excerpt-摘要 extras-额外字段 avatar-图片 buttons-操作按钮 time-时间文本 badge-badge文本 right-右侧文本） */
    @Excel(name = "位置", readConverterExp = "t=itle-标题,e=xcerpt-摘要,e=xtras-额外字段,a=vatar-图片,b=uttons-操作按钮,t=ime-时间文本,b=adge-badge文本,r=ight-右侧文本")
    private String position;

    /** 列表ID，rva_fe_list.id */
    @Excel(name = "列表ID，rva_fe_list.id")
    private String listId;

    /** 属性ID，rva_property.id */
    @Excel(name = "属性ID，rva_property.id")
    private String propId;

    /** 图标 */
    @Excel(name = "图标")
    private String icon;

    /** 排序 */
    @Excel(name = "排序")
    private Integer idx;

    /** 文本内容，为velocity模板，解析后的内容用于显示，如果为空，则显示name的值 */
    @Excel(name = "文本内容，为velocity模板，解析后的内容用于显示，如果为空，则显示name的值")
    private String text;

    /** 排序索引 */
    @Excel(name = "排序索引")
    private Integer orderIndex;

    /** 排序类型，DESC/ASC */
    @Excel(name = "排序类型，DESC/ASC")
    private String orderType;

    /** 分组值，用于对列表项进行静态分组，关联rva_fe_list.group_dict */
    @Excel(name = "分组值，用于对列表项进行静态分组，关联rva_fe_list.group_dict")
    private String groupValue;

    /** 位置（dialog ajax navigateTo switchTab redirectTo reLaunch toogleFavorite delete share） */
    @Excel(name = "位置", readConverterExp = "d=ialog,a=jax,n=avigateTo,s=witchTab,r=edirectTo,r=eLaunch,t=oogleFavorite,d=elete,s=hare")
    private String actionType;

    /** 动作参数，JSON格式，action_type=dialog时，参数中应该包含viewId，指明关联的表单视图；action_type=ajax/navigateTo/switchTab/redirectTo/reLaunch时，参数中应该包含url */
    @Excel(name = "动作参数，JSON格式，action_type=dialog时，参数中应该包含viewId，指明关联的表单视图；action_type=ajax/navigateTo/switchTab/redirectTo/reLaunch时，参数中应该包含url")
    private String actionParams;

    /** 数据字典 */
    @Excel(name = "数据字典")
    private String dict;

    /** 小数位 */
    @Excel(name = "小数位")
    private Integer numberScale;

    /** 显示箭头 */
    @Excel(name = "显示箭头")
    private String showArrow;

    /** Switch地址，有值时显示Switch控件，点击ajax请求 */
    @Excel(name = "Switch地址，有值时显示Switch控件，点击ajax请求")
    private String switchUrl;

    /** 其他数据 */
    @Excel(name = "其他数据")
    private String data;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setPosition(String position)
    {
        this.position = position;
    }

    public String getPosition()
    {
        return position;
    }
    public void setListId(String listId) 
    {
        this.listId = listId;
    }

    public String getListId() 
    {
        return listId;
    }
    public void setPropId(String propId) 
    {
        this.propId = propId;
    }

    public String getPropId() 
    {
        return propId;
    }
    public void setIcon(String icon) 
    {
        this.icon = icon;
    }

    public String getIcon() 
    {
        return icon;
    }
    public void setIdx(Integer idx) 
    {
        this.idx = idx;
    }

    public Integer getIdx() 
    {
        return idx;
    }
    public void setText(String text) 
    {
        this.text = text;
    }

    public String getText() 
    {
        return text;
    }
    public void setOrderIndex(Integer orderIndex) 
    {
        this.orderIndex = orderIndex;
    }

    public Integer getOrderIndex() 
    {
        return orderIndex;
    }
    public void setOrderType(String orderType) 
    {
        this.orderType = orderType;
    }

    public String getOrderType() 
    {
        return orderType;
    }
    public void setGroupValue(String groupValue) 
    {
        this.groupValue = groupValue;
    }

    public String getGroupValue() 
    {
        return groupValue;
    }
    public void setActionType(String actionType) 
    {
        this.actionType = actionType;
    }

    public String getActionType() 
    {
        return actionType;
    }
    public void setActionParams(String actionParams) 
    {
        this.actionParams = actionParams;
    }

    public String getActionParams() 
    {
        return actionParams;
    }
    public void setDict(String dict) 
    {
        this.dict = dict;
    }

    public String getDict() 
    {
        return dict;
    }
    public void setNumberScale(Integer numberScale) 
    {
        this.numberScale = numberScale;
    }

    public Integer getNumberScale() 
    {
        return numberScale;
    }
    public void setShowArrow(String showArrow) 
    {
        this.showArrow = showArrow;
    }

    public String getShowArrow() 
    {
        return showArrow;
    }
    public void setSwitchUrl(String switchUrl) 
    {
        this.switchUrl = switchUrl;
    }

    public String getSwitchUrl() 
    {
        return switchUrl;
    }
    public void setData(String data) 
    {
        this.data = data;
    }

    public String getData() 
    {
        return data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("position", getPosition())
            .append("listId", getListId())
            .append("propId", getPropId())
            .append("icon", getIcon())
            .append("idx", getIdx())
            .append("text", getText())
            .append("orderIndex", getOrderIndex())
            .append("orderType", getOrderType())
            .append("groupValue", getGroupValue())
            .append("actionType", getActionType())
            .append("actionParams", getActionParams())
            .append("dict", getDict())
            .append("numberScale", getNumberScale())
            .append("showArrow", getShowArrow())
            .append("switchUrl", getSwitchUrl())
            .append("data", getData())
            .toString();
    }

    public final static String POSITION_TITLE = "title";

    public final static String POSITION_EXCERPT = "excerpt";

    public final static String POSITION_AVATAR = "avatar";

    public final static String POSITION_EXTRAS = "extras";

    public final static String POSITION_BUTTONS = "buttons";

    public final static String POSITION_CONTENT = "content";

    public String getRelatedView () {
        return RvaUtils.parseMap(this.actionParams).getString("viewId");
    }

    public void setRelatedView (String viewId) {
        this.actionParams = RvaUtils.setJsonProperty(this.actionParams, "viewId", viewId);
    }

    public String getRelationId() {
        return getJsonPropertyString("relationId");
    }

    public void setRelationId(String relationId) {
        setJsonProperty("relationId", relationId);
    }

    public String getPropSubId() {
        return getJsonPropertyString("propSubId");
    }

    public void setPropSubId(String propSubId) {
        setJsonProperty("propSubId", propSubId);
    }

    public String getType() {
        return getJsonPropertyString("type");
    }

    public void setType(String type) {
        setJsonProperty("type", type);
    }

    @Override
    public int compareTo(RvaFeListItem o) {
        return this.getIdx() - o.getIdx();
    }
}
