package com.ruoyi.rva.framework.service.impl;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.service.IRvaObjectFavoriteService;
import com.ruoyi.rva.framework.service.IRvaSystemService;
import com.ruoyi.rva.framework.service.IRvaViewService;
import com.ruoyi.rva.framework.util.RvaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaObjectFavoriteMapper;
import com.ruoyi.rva.framework.domain.RvaObjectFavorite;
import org.springframework.util.ObjectUtils;

/**
 * 收藏Service业务层处理
 *
 * @author jiaodacailei
 * @date 2021-12-01
 */
@Service
public class RvaObjectFavoriteServiceImpl implements IRvaObjectFavoriteService
{
    @Autowired
    private RvaObjectFavoriteMapper rvaObjectFavoriteMapper;

    @Autowired
    private IRvaViewService rvaViewService;

    @Autowired
    private IRvaSystemService rvaSystemService;

    /**
     * 查询收藏
     *
     * @param id 收藏主键
     * @return 收藏
     */
    @Override
    public RvaObjectFavorite selectRvaObjectFavoriteById(Long id)
    {
        return rvaObjectFavoriteMapper.selectRvaObjectFavoriteById(id);
    }

    /**
     * 查询收藏列表
     *
     * @param rvaObjectFavorite 收藏
     * @return 收藏
     */
    @Override
    public List<RvaObjectFavorite> selectRvaObjectFavoriteList(RvaObjectFavorite rvaObjectFavorite)
    {
        return rvaObjectFavoriteMapper.selectRvaObjectFavoriteList(rvaObjectFavorite);
    }

    /**
     * 新增收藏
     *
     * @param rvaObjectFavorite 收藏
     * @return 结果
     */
    @Override
    public int insertRvaObjectFavorite(RvaObjectFavorite rvaObjectFavorite)
    {
        rvaObjectFavorite.setCreateTime(DateUtils.getNowDate());
        return rvaObjectFavoriteMapper.insertRvaObjectFavorite(rvaObjectFavorite);
    }

    /**
     * 修改收藏
     *
     * @param rvaObjectFavorite 收藏
     * @return 结果
     */
    @Override
    public int updateRvaObjectFavorite(RvaObjectFavorite rvaObjectFavorite)
    {
        rvaObjectFavorite.setUpdateTime(DateUtils.getNowDate());
        return rvaObjectFavoriteMapper.updateRvaObjectFavorite(rvaObjectFavorite);
    }

    /**
     * 批量删除收藏
     *
     * @param ids 需要删除的收藏主键
     * @return 结果
     */
    @Override
    public int deleteRvaObjectFavoriteByIds(Long[] ids)
    {
        return rvaObjectFavoriteMapper.deleteRvaObjectFavoriteByIds(ids);
    }

    /**
     * 删除收藏信息
     *
     * @param id 收藏主键
     * @return 结果
     */
    @Override
    public int deleteRvaObjectFavoriteById(Long id)
    {
        return rvaObjectFavoriteMapper.deleteRvaObjectFavoriteById(id);
    }


    /**
     * button.params.type
     *
     * @param req
     */
    @Override
    public void toggle(RvaMap<String, Object> req) {
        List<Map> selection = req.getList("selection");
        String keyPropValue = new RvaMap<>(selection.get(0)).getString("keyPropValue");
        RvaMap button = req.getMap("button");
        String viewId = button.getString("viewId");

        RvaMap params = button.getMap("params");
        if (ObjectUtils.isEmpty(params) || ObjectUtils.isEmpty(params.getString("type"))) {
            RvaUtils.throwCallException("请指明类型！");
        }
        String type = params.getString("type");
        RvaView rvaView = rvaViewService.selectRvaViewById(viewId, new RvaMap<>());
        String objId = rvaView.getObjId();
        SysUser loginUser = rvaSystemService.getLoginUser();
        Long userId = loginUser.getUserId();
        RvaObjectFavorite rvaObjectFavorite = new RvaObjectFavorite();
        rvaObjectFavorite.setObjId(objId);
        rvaObjectFavorite.setObjIdValue(keyPropValue);
        rvaObjectFavorite.setUserId(userId);
        rvaObjectFavorite.setType(type);
        List<RvaObjectFavorite> rvaObjectFavorites = rvaObjectFavoriteMapper.selectRvaObjectFavoriteList(rvaObjectFavorite);
        if (ObjectUtils.isEmpty(rvaObjectFavorites)) {
            rvaObjectFavoriteMapper.insertRvaObjectFavorite(rvaObjectFavorite);
        } else {
            Long id = rvaObjectFavorites.get(0).getId();
            rvaObjectFavoriteMapper.deleteRvaObjectFavoriteById(id);
        }
    }
}
