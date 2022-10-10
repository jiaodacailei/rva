package com.ruoyi.rva.framework.service.impl;

import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.mapper.*;
import com.ruoyi.rva.framework.service.IRvaDocService;
import com.ruoyi.rva.framework.util.RvaConstants;
import com.ruoyi.rva.framework.util.RvaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaDocServiceImpl implements IRvaDocService {

    private final RvaObjectMapper objectMapper;

    private final RvaViewMapper viewMapper;

    private final RvaAppMapper appMapper;

    @Override
    public String getViewpropertyDoc(RvaViewproperty viewproperty) {
        RvaView view = viewMapper.selectRvaViewById(viewproperty.getViewId());
        RvaObject object = RvaObject.NONE.equals(view.getObjId()) ? null : objectMapper.selectRvaObjectById(view.getObjId());
        List<String> strings = new ArrayList<>();
        // strings.add(viewproperty.getName());
        RvaRelation relation = viewproperty.getRelation();
        RvaObject relatedObj = relation == null ? null : relation.getRelatedObj();
        RvaProperty property = null;
        if (relation == null) {
            property = RvaUtils.isEmpty(viewproperty.getPropId()) ? null : object.getProperty(viewproperty.getPropId());
        } else {
            property = RvaUtils.isEmpty(viewproperty.getPropId()) ? null : relatedObj.getProperty(viewproperty.getPropId());
        }

        if (property == null) {
            if (relation == null) {
                strings.add("【异常】");
            } else {
                strings.add("表示和" + relatedObj.getName() + "的关系数据");
            }
        } else {
            if (relation == null) {
                strings.add(getObjectPropertyDoc(object, property));
            } else {
                strings.add("属于" + relatedObj.getName() + "的属性");
                strings.add(getObjectPropertyDoc(relatedObj, property));
            }
        }
        if (RvaUtils.isNotEmpty(viewproperty.getFormInitValue())) {
            strings.add("默认加载值：" + formatDefaultValue(viewproperty.getFormInitValue()));
        }
        if (RvaUtils.isNotEmpty(viewproperty.getFormSubmitValue())) {
            strings.add("默认提交值：" + formatDefaultValue(viewproperty.getFormSubmitValue()));
        }
        if ("Y".equals(viewproperty.getFormRequired())) {
            strings.add("是必填项");
        }
        List<SysDictData> dictDataList = viewproperty.getDictDataList();
        if (dictDataList.size() > 0) {
            String dictNames = "";
            for (SysDictData sysDictData : dictDataList) {
                if (!"".equals(dictNames)) {
                    dictNames += "/";
                }
                dictNames += sysDictData.getDictLabel();
            }
            strings.add("包含选项（" + dictNames + "）");
        }
        return viewproperty.getName() + "：" + RvaUtils.join(strings, "，");
    }

    private String formatDefaultValue (String val) {
        if (RvaConstants.EXPRESSION_NOW.equals(val)) {
            return "当前时间";
        }
        if (RvaConstants.EXPRESSION_LOGIN_USER.equals(val)) {
            return "当前登录用户";
        }
        return val;
    }

    private String getCnType (String type) {
        if (Arrays.asList(RvaProperty.TYPE_INTEGER, RvaProperty.TYPE_BIGINT, RvaProperty.TYPE_SMALLINT).contains(type)) {
            return "整数";
        }
        if (Arrays.asList(RvaProperty.TYPE_VARCHAR, RvaProperty.TYPE_TEXT).contains(type)) {
            return "文本";
        }
        if (RvaProperty.TYPE_DATE.equals(type)) {
            return "日期";
        }
        if (RvaProperty.TYPE_DATETIME.equals(type)) {
            return "日期时间";
        }
        return "数字";
    }

    private String getObjectPropertyDoc(RvaObject object, RvaProperty prop) {
        String metaType = prop.getType();
        String doc = "";
        String M_DEFAULT = prop.getDefaultValue();
        // column
        if (Arrays.asList(RvaProperty.TYPE_VARCHAR, RvaProperty.TYPE_NUMERIC).contains(metaType)) {
            doc += getCnType(metaType) + "(" + prop.getTypeDetail() + ")";
        } else {
            doc += getCnType(metaType);
        }
        // null
//        if ("Y".equals(prop.getRequired())) {
//            doc += "，必填项";
//        }
        // default
//        if (RvaUtils.isNotEmpty(M_DEFAULT)) {
//            if (M_DEFAULT.startsWith("~")) {
//                doc += String.format("，默认值 %s", M_DEFAULT.substring(1));
//            } else {
//                doc += String.format("，默认值 '%s'", M_DEFAULT);
//            }
//        }
        // 自增长
        if (object.isPrimaryKey(prop.getName())) {
            doc += "，主键";
            if (Arrays.asList(RvaProperty.TYPE_INTEGER, RvaProperty.TYPE_SMALLINT, RvaProperty.TYPE_BIGINT).contains(metaType)) {
                doc += "N".equals(prop.getIdGenType()) ? "" : "，自增长";
            }
        }

        return doc;
    }

    @Override
    public RvaApp getApp(String appId) {
        return appMapper.selectRvaAppById(appId);
    }

    @Override
    public RvaView getView(String viewId) {
        return viewMapper.selectRvaViewById(viewId);
    }

    @Override
    public List<RvaViewproperty> getHiddensWithoutPK(RvaView view) {
        RvaObject object = RvaObject.NONE.equals(view.getObjId()) ? null : objectMapper.selectRvaObjectById(view.getObjId());
        List<RvaViewproperty> hiddens = view.getHiddens();
        Iterator<RvaViewproperty> iterator = hiddens.iterator();
        while (iterator.hasNext()) {
            RvaViewproperty next = iterator.next();
            String propId = next.getPropId();
            if (RvaUtils.isEmpty(propId)) {
                continue;
            }
            RvaProperty property = object.getProperty(propId);
            if (property == null) {
                continue;
            }
            if (object.isPrimaryKey(property.getName())) {
                iterator.remove();
                break;
            }
        }
        return hiddens;
    }
}
