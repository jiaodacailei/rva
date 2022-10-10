package com.ruoyi.rva.framework.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.rva.framework.domain.RvaApp;
import com.ruoyi.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.framework.mapper.RvaAppMapper;
import com.ruoyi.rva.framework.service.IRvaAppService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 应用Service业务层处理
 * 
 * @author jiaodacailei
 * @date 2021-07-27
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaAppServiceImpl implements IRvaAppService 
{
    private final RvaAppMapper rvaAppMapper;

    private final ISysConfigService configService;

    /**
     * 查询应用
     * 
     * @param id 应用ID
     * @return 应用
     */
    @Override
    public Object selectRvaAppById(String id)
    {
        String[] ids = id.split(",");
        if (ids.length == 1) {
            return rvaAppMapper.selectRvaAppById(id);
        }
        List<RvaApp> apps = new ArrayList<>();
        for (String appId : ids) {
            RvaApp app = rvaAppMapper.selectRvaAppById(appId);
            apps.add(app);
        }
        return apps;
    }

    /**
     * 查询应用列表
     * 
     * @param rvaApp 应用
     * @return 应用
     */
    @Override
    public List<RvaApp> selectRvaAppList(RvaApp rvaApp)
    {
        return rvaAppMapper.selectRvaAppList(rvaApp);
    }

    /**
     * 新增应用
     * 
     * @param rvaApp 应用
     * @return 结果
     */
    @Override
    public int insertRvaApp(RvaApp rvaApp)
    {
        return rvaAppMapper.insertRvaApp(rvaApp);
    }

    /**
     * 修改应用
     * 
     * @param rvaApp 应用
     * @return 结果
     */
    @Override
    public int updateRvaApp(RvaApp rvaApp)
    {
        return rvaAppMapper.updateRvaApp(rvaApp);
    }

    /**
     * 批量删除应用
     * 
     * @param ids 需要删除的应用ID
     * @return 结果
     */
    @Override
    public int deleteRvaAppByIds(String[] ids)
    {
        return rvaAppMapper.deleteRvaAppByIds(ids);
    }

    /**
     * 删除应用信息
     * 
     * @param id 应用ID
     * @return 结果
     */
    @Override
    public int deleteRvaAppById(String id)
    {
        return rvaAppMapper.deleteRvaAppById(id);
    }
}
