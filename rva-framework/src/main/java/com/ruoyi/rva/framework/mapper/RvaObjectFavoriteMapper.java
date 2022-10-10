package com.ruoyi.rva.framework.mapper;

import java.util.List;
import com.ruoyi.rva.framework.domain.RvaObjectFavorite;

/**
 * 收藏Mapper接口
 * 
 * @author jiaodacailei
 * @date 2021-12-01
 */
public interface RvaObjectFavoriteMapper 
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
     * 删除收藏
     * 
     * @param id 收藏主键
     * @return 结果
     */
    public int deleteRvaObjectFavoriteById(Long id);

    /**
     * 批量删除收藏
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRvaObjectFavoriteByIds(Long[] ids);
}
