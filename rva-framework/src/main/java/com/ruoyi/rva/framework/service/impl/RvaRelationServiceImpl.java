package com.ruoyi.rva.framework.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ruoyi.rva.framework.domain.*;
import com.ruoyi.rva.framework.mapper.*;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import com.ruoyi.rva.framework.service.IRvaPropertyService;
import com.ruoyi.rva.framework.service.IRvaViewpropertyService;
import com.ruoyi.rva.framework.util.RvaPinyinUtils;
import com.ruoyi.rva.framework.util.RvaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.service.IRvaRelationService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 关系Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class RvaRelationServiceImpl implements IRvaRelationService 
{

    private final RvaPropertyMapper rvaPropertyMapper;

    private final RvaRelationMapper rvaRelationMapper;

    private  final RvaRelationitemMapper rvaRelationItemMapper;

    private final RvaObjectMapper rvaObjectMapper;

    private final RvaSchemaMapper rvaSchemaMapper;

    private final RvaDataMapper dataMapper;

    private final IRvaMetaService rvaMetaService;

    private final IRvaPropertyService propertyService;

    private final IRvaViewpropertyService viewpropertyService;

    @Override
    public void quickCreate(RvaMap<String, Object> req) {
        String selfObjId = req.getString("c0_none_kuaisuxinjian_benfangduixiang");
        String selfPropId = req.getString("c0_none_kuaisuxinjian_benfangshuxing");
        String relObjId = req.getString("c0_none_kuaisuxinjian_guanlianduixiang");
        String relPropId = req.getString("c0_none_kuaisuxinjian_guanlianshuxing");
        String type = req.getString("c0_none_kuaisuxinjian_leixing");
        String name = req.getString("c0_none_kuaisuxinjian_name");
        RvaObject selfObj = rvaObjectMapper.selectRvaObjectById(selfObjId);
        RvaObject relObj = rvaObjectMapper.selectRvaObjectById(relObjId);
        RvaProperty selfFk = null;
        if (selfPropId == null) {
            if (RvaRelation.TYPE_M21.equals(type)) {
                // 创建多对一外键字段，此处会执行DDL，但不要执行DML
                selfFk = this.createFkProp(selfObj, relObj, relPropId);
                selfPropId = selfFk.getId();
            } else {
                selfPropId = selfObj.getKeyProperty().getId();
            }
        }
        RvaProperty relFk = null;
        if (relPropId == null) {
            if (RvaRelation.TYPE_12M.equals(type)) {
                // 创建一对多外键字段，此处会执行DDL，但不要执行DML
                relFk = this.createFkProp(relObj, selfObj, selfPropId);
                relPropId = relFk.getId();
            } else {
                relPropId = relObj.getKeyProperty().getId();
            }
        }
        // 如果是多对多，需要创建关系对象，此处会执行DDL，但不要执行DML
        RvaObject relationObj = null;
        if (RvaRelation.TYPE_M2M.equals(type)) {
            relationObj = createRelationObject(selfObj, relObj);
        }
        // 等上述DDL执行成功之后，再统一执行必须的DML。因为DDL的执行，会中断事务，为尽量保持数据的一致性，应该先执行DDL，再执行DML
        if (selfFk != null) {
            rvaPropertyMapper.insertRvaProperty(selfFk);
        }
        if (relFk != null) {
            rvaPropertyMapper.insertRvaProperty(relFk);
        }
        // 插入关系
        RvaRelation r = new RvaRelation();
        r.setId(createId(selfObjId, relObjId, type));
        r.setIdx(0);
        r.setName(name);
        r.setObjId(selfObjId);
        r.setRelatedName(selfObj.getName());
        r.setType(type);
        if (relationObj != null) {
            r.setRelationObjId(relationObj.getId());
        }
        rvaRelationMapper.insertRvaRelation(r);
        // 插入关系项
        RvaRelationitem ri = new RvaRelationitem();
        ri.setId(r.getId() + "_0");
        ri.setIdx(0);
        ri.setPropId(selfPropId);
        ri.setRelatedPropId(relPropId);
        ri.setRelatedObjId(relObjId);
        ri.setRelationId(r.getId());
        if (relationObj != null) {
            ri.setRelationPropId(relationObj.getProperties().get(0).getId());
            ri.setRelationInversePropId(relationObj.getProperties().get(1).getId());
        }
        rvaRelationItemMapper.insertRvaRelationitem(ri);
    }

    private RvaProperty createFkProp(RvaObject selfObj, RvaObject relObj, String relPropId) {
        RvaProperty relProp = null;
        if (RvaUtils.isEmpty(relPropId)) {
            relProp = relObj.getKeyProperty();
        } else {
            relProp = relObj.getProperty(relPropId);
            if (relProp == null) {
                RvaUtils.throwQueryException("关联属性", "关联属性ID=" + relPropId);
            }
        }
        RvaProperty fk = new RvaProperty();
        fk.setName(RvaPinyinUtils.getPinyinLower(relObj.getName()) + "_" + RvaUtils.generateKey32(4));
        fk.setId(selfObj.getId() + "_" + fk.getName());
        fk.setType(relProp.getType());
        fk.setTypeDetailAndNumberScale(relProp.getTypeDetail());
        fk.setObjId(selfObj.getId());
        fk.setValueMax(relProp.getValueMax());
        fk.setValueMin(relProp.getValueMin());
        fk.setDescription(relObj.getName());
        String createSql = RvaProperty.getCreateSql(fk, selfObj);
        log.info("createSelfFkProp:\n" + createSql);
        rvaSchemaMapper.execute(createSql);
        return fk;
    }

    private RvaObject createRelationObject (RvaObject selfObj, RvaObject relObj) {
        RvaProperty p1 = getRelationProperty(selfObj);
        RvaProperty p2 = getRelationProperty(relObj);
        String suffix = RvaUtils.generateKey32(4);
        String table = String.format("%s_%s_%s", selfObj.getId(), relObj.getNo(), suffix);
        String sql = String.format("create table %s (%s, %s, primary key(%s, %s)) comment = '%s_%s'", table, RvaProperty.getSqlFieldClause(p1, false), RvaProperty.getSqlFieldClause(p2, false), p1.getName(), p2.getName(), selfObj.getName(), relObj.getName());
        log.info("createRelationObject:\n" + sql);
        rvaSchemaMapper.execute(sql);
        RvaObject relationObj = rvaMetaService.createObject(table);
        Collections.sort(relationObj.getProperties(), new Comparator<RvaProperty>() {
            @Override
            public int compare(RvaProperty o1, RvaProperty o2) {
                return o1.getName().equals(p1.getName()) ? -1 : 1;
            }
        });

        return relationObj;
    }

    private RvaProperty getRelationProperty(RvaObject selfObj) {
        RvaProperty p1 = new RvaProperty();
        p1.setDescription(selfObj.getName());
        p1.setName(RvaPinyinUtils.getPinyinLower(selfObj.getName()) + "_" + RvaUtils.generateKey32(4));
        p1.setType(selfObj.getKeyProperty().getType());
        p1.setTypeDetailAndNumberScale(selfObj.getKeyProperty().getTypeDetail());
        return p1;
    }

    private static final String SUFFIX_INVERSE = "_inverse";

    @Override
    public void reverse(String relationId) {
        RvaRelation relation = selectRvaRelationById(relationId);
        String inverseId = relationId + SUFFIX_INVERSE;
        if (relationId.endsWith(SUFFIX_INVERSE)) {
            inverseId = relationId.substring(0, relationId.indexOf(SUFFIX_INVERSE));
        }
        RvaRelation relationInverse = selectRvaRelationById(inverseId);
        if (relationInverse != null) {
            RvaUtils.throwExistsException("逆向关系");
        }
        RvaRelation r = new RvaRelation();
        r.setId(inverseId);
        r.setIdx(0);
        r.setName(relation.getRelatedName());
        r.setObjId(relation.getRelatedObj().getId());
        r.setRelatedName(relation.getName());
        String type = relation.getType();
        if (relation.isM21()) {
            type = RvaRelation.TYPE_12M;
        } else if (relation.is12M()) {
            type = RvaRelation.TYPE_M21;
        } else if (relation.isM2M()) {
            type = RvaRelation.TYPE_M2M;
        }
        r.setType(type);
        r.setRelationObjId(relation.getRelationObjId());
        rvaRelationMapper.insertRvaRelation(r);
        // 插入关系项
        RvaRelationitem ri = new RvaRelationitem();
        ri.setId(r.getId() + "_0");
        ri.setIdx(0);
        ri.setPropId(relation.getRelatedProp().getId());
        ri.setRelatedPropId(relation.getProp().getId());
        ri.setRelatedObjId(relation.getObjId());
        ri.setRelationId(r.getId());
        if (RvaUtils.isNotEmpty(relation.getRelationObjId())) {
            ri.setRelationPropId(relation.getRelationInverseProp().getId());
            ri.setRelationInversePropId(relation.getRelationProp().getId());
        }
        rvaRelationItemMapper.insertRvaRelationitem(ri);
    }

    /**
     * 查询关系
     * 
     * @param id 关系ID
     * @return 关系
     */
    @Override
    public RvaRelation selectRvaRelationById(String id)
    {
        return rvaRelationMapper.selectRvaRelationById(id);
    }

    /**
     * 查询关系列表
     * 
     * @param rvaRelation 关系
     * @return 关系
     */
    @Override
    public List<RvaRelation> selectRvaRelationList(RvaRelation rvaRelation)
    {
        return rvaRelationMapper.selectRvaRelationList(rvaRelation);
    }

    /**
     * 新增关系
     * 
     * @param rvaRelation 关系
     * @return 结果
     */
    @Override
    public int insertRvaRelation(RvaRelation rvaRelation)
    {
        return rvaRelationMapper.insertRvaRelation(rvaRelation);
    }

    /**
     * 修改关系
     * 
     * @param rvaRelation 关系
     * @return 结果
     */
    @Override
    public int updateRvaRelation(RvaRelation rvaRelation)
    {
        return rvaRelationMapper.updateRvaRelation(rvaRelation);
    }

    /**
     * 批量删除关系
     * 
     * @param ids 需要删除的关系ID
     * @return 结果
     */
    @Override
    public int deleteRvaRelationByIds(String[] ids)
    {
        return rvaRelationMapper.deleteRvaRelationByIds(ids);
    }

    /**
     * 删除关系信息
     * 
     * @param id 关系ID
     * @return 结果
     */
    @Override
    public int deleteRvaRelationById(String id)
    {
        return rvaRelationMapper.deleteRvaRelationById(id);
    }

    @Override
    public RvaRelation createM21(String selfObjId, String relObjId) {
        RvaObject selfObj = rvaObjectMapper.selectRvaObjectById(selfObjId);
        RvaObject relObj = rvaObjectMapper.selectRvaObjectById(relObjId);
        // 创建外键
        RvaProperty fkProp = createFkProp(selfObj, relObj, null);
        rvaPropertyMapper.insertRvaProperty(fkProp);
        // 插入关系
        RvaRelation r = new RvaRelation();
        r.setId(createId(selfObjId, relObjId, RvaRelation.TYPE_M21));
        r.setIdx(0);
        r.setName(relObj.getName());
        r.setObjId(selfObjId);
        r.setRelatedName(selfObj.getName());
        r.setType(RvaRelation.TYPE_M21);
        rvaRelationMapper.insertRvaRelation(r);
        // 插入关系项
        RvaRelationitem ri = new RvaRelationitem();
        ri.setId(r.getId() + "_0");
        ri.setIdx(0);
        ri.setPropId(fkProp.getId());
        ri.setRelatedPropId(relObj.getKeyProperty().getId());
        ri.setRelatedObjId(relObjId);
        ri.setRelationId(r.getId());
        rvaRelationItemMapper.insertRvaRelationitem(ri);
        return r;
    }

    @Override
    public void delete(String id) {
        RvaRelation relation = rvaRelationMapper.selectRvaRelationById(id);
        if (relation.isM21()) {
            relation.getRelatedObj().getRelations().forEach(r -> {
                if (r.is12M() && r.getRelatedProp().getId().equals(relation.getProp().getId())) {
                    deleteWithViewProperties(r.getId());
                }
            });
            propertyService.drop(relation.getProp().getId());
        } else if (relation.is12M()) {
            relation.getRelatedObj().getRelations().forEach(r -> {
                if (r.isM21() && r.getProp().getId().equals(relation.getRelatedProp().getId())) {
                    deleteWithViewProperties(r.getId());
                }
            });
            propertyService.drop(relation.getRelatedProp().getId());
        } else if (relation.isM2M()) {
            relation.getRelatedObj().getRelations().forEach(r -> {
                if (r.isM2M() && r.getRelationObjId().equals(relation.getRelationObjId())) {
                    deleteWithViewProperties(r.getId());
                }
            });
            rvaMetaService.deleteAllObjectMeta(relation.getRelationObjId());
            String dropTableSql = RvaObject.getDropTableSql(relation.getRelationObjId());
            rvaSchemaMapper.execute(dropTableSql);
        }
        deleteWithViewProperties(id);
    }

    private void deleteWithViewProperties(String id) {
        viewpropertyService.deleteWithTriggersByRelationId(id);
        dataMapper.deleteWhereMap("rva_relation", new RvaMap<>("id", id));
        dataMapper.deleteWhereMap("rva_relationitem", new RvaMap<>("relation_id", id));
    }
}
