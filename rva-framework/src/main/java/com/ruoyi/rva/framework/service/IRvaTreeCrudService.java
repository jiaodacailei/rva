package com.ruoyi.rva.framework.service;

import com.ruoyi.rva.framework.domain.RvaAppitem;
import com.ruoyi.rva.framework.domain.RvaMap;

import java.util.List;

public interface IRvaTreeCrudService {

    /**
     * 树节点关联的视图或者应用ID，RvaAppItem.relatedAppId
     */
    String PARAM_TCRUD_ID = "treeCrudId";

    /**
     * 树节点对应数据的主键值
     */
    String PARAM_TCRUD_NODE_VALUE = "treeCrudNodeValue";

    /**
     * 树节点关联的RvaAppItem.id
     */
    String PARAM_APP_ITEM = "appItemId";

    /**
     * 树节点的nodeKey，必须在整个树中唯一：RvaAppItem.id + keyPropValue
     */
    String PARAM_NODE_KEY = "nodeKey";

    /**
     * 树节点关联的RvaAppItem.subType
     */
    String PARAM_APP_ITEM_SUB_TYPE = "appItemSubType";

    List<RvaMap<String, Object>> selectNodes(String appId, RvaMap req);

    List<RvaAppitem> selectContents(String appId, RvaMap req);
}
