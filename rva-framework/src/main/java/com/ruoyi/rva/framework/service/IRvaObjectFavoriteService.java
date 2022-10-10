package com.ruoyi.rva.framework.service;

import java.util.List;

import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObjectFavorite;

/**
 * 收藏Service接口
 *
 * @author jiaodacailei
 * @date 2021-12-01
 */
public interface IRvaObjectFavoriteService
{
    /**
     * 查询收藏
     *
     * @param id 收藏主键
     * @return 收藏
     */
    public RvaObjectFavorite selectRvaObjectFavoriteById(Long id);

    /**
     * 查询收藏列表
     *
     * @param rvaObjectFavorite 收藏
     * @return 收藏集合
     */
    public List<RvaObjectFavorite> selectRvaObjectFavoriteList(RvaObjectFavorite rvaObjectFavorite);

    /**
     * 新增收藏
     *
     * @param rvaObjectFavorite 收藏
     * @return 结果
     */
    public int insertRvaObjectFavorite(RvaObjectFavorite rvaObjectFavorite);

    /**
     * 修改收藏
     *
     * @param rvaObjectFavorite 收藏
     * @return 结果
     */
    public int updateRvaObjectFavorite(RvaObjectFavorite rvaObjectFavorite);

    /**
     * 批量删除收藏
     *
     * @param ids 需要删除的收藏主键集合
     * @return 结果
     */
    public int deleteRvaObjectFavoriteByIds(Long[] ids);

    /**
     * 删除收藏信息
     *
     * @param id 收藏主键
     * @return 结果
     */
    public int deleteRvaObjectFavoriteById(Long id);

    void toggle(RvaMap<String, Object> req);
}
