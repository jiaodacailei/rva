package com.ruoyi.rva.fe.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.rva.framework.service.IRvaSystemService;
import com.ruoyi.system.domain.vo.MetaVo;
import com.ruoyi.system.domain.vo.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rva.fe.mapper.RvaUniappPermissionMapper;
import com.ruoyi.rva.fe.domain.RvaUniappPermission;
import com.ruoyi.rva.fe.service.IRvaUniappPermissionService;
import org.springframework.util.ObjectUtils;

/**
 * 移动应用权限Service业务层处理
 *
 * @author jiaodacailei
 * @date 2022-05-12
 */
@Service
public class RvaUniappPermissionServiceImpl implements IRvaUniappPermissionService {
    @Autowired
    private RvaUniappPermissionMapper rvaUniappPermissionMapper;

    /**
     * 查询移动应用权限
     *
     * @param id 移动应用权限主键
     * @return 移动应用权限
     */
    @Override
    public RvaUniappPermission selectRvaUniappPermissionById(String id) {
        return rvaUniappPermissionMapper.selectRvaUniappPermissionById(id);
    }

    /**
     * 查询移动应用权限列表
     *
     * @param rvaUniappPermission 移动应用权限
     * @return 移动应用权限
     */
    @Override
    public List<RvaUniappPermission> selectRvaUniappPermissionList(RvaUniappPermission rvaUniappPermission) {
        return rvaUniappPermissionMapper.selectRvaUniappPermissionList(rvaUniappPermission);
    }

    /**
     * 新增移动应用权限
     *
     * @param rvaUniappPermission 移动应用权限
     * @return 结果
     */
    @Override
    public int insertRvaUniappPermission(RvaUniappPermission rvaUniappPermission) {
        rvaUniappPermission.setCreateTime(DateUtils.getNowDate());
        return rvaUniappPermissionMapper.insertRvaUniappPermission(rvaUniappPermission);
    }

    /**
     * 修改移动应用权限
     *
     * @param rvaUniappPermission 移动应用权限
     * @return 结果
     */
    @Override
    public int updateRvaUniappPermission(RvaUniappPermission rvaUniappPermission) {
        rvaUniappPermission.setUpdateTime(DateUtils.getNowDate());
        return rvaUniappPermissionMapper.updateRvaUniappPermission(rvaUniappPermission);
    }

    /**
     * 批量删除移动应用权限
     *
     * @param ids 需要删除的移动应用权限主键
     * @return 结果
     */
    @Override
    public int deleteRvaUniappPermissionByIds(String[] ids) {
        return rvaUniappPermissionMapper.deleteRvaUniappPermissionByIds(ids);
    }

    /**
     * 删除移动应用权限信息
     *
     * @param id 移动应用权限主键
     * @return 结果
     */
    @Override
    public int deleteRvaUniappPermissionById(String id) {
        return rvaUniappPermissionMapper.deleteRvaUniappPermissionById(id);
    }

    @Override
    public List<RvaUniappPermission> selectMenuTreeByUserId(Long userId, String uniAppId) {
        List<RvaUniappPermission> list = new ArrayList<>();
        if (SecurityUtils.isAdmin(userId)) {
            RvaUniappPermission rvaUniappPermission = new RvaUniappPermission();
            rvaUniappPermission.setStatus("0");
            rvaUniappPermission.setVisible("0");
            list = rvaUniappPermissionMapper.selectRvaUniappPermissionList(rvaUniappPermission);
        } else {
            list = rvaUniappPermissionMapper.selectList(userId, uniAppId);
        }
        String parentId = "0";
        return this.getChildPerms(list, parentId);
    }

    @Override
    public List<RvaUniappPermission> getChildPerms(List<RvaUniappPermission> list, String parentId) {

        List<RvaUniappPermission> returnList = new ArrayList<RvaUniappPermission>();
        for (Iterator<RvaUniappPermission> iterator = list.iterator(); iterator.hasNext(); ) {
            RvaUniappPermission t = (RvaUniappPermission) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (ObjectUtils.isEmpty(t.getParentId()) || t.getParentId().equals(parentId)) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;

    }

    @Override
    public List<RouterVo> buildMenus(List<RvaUniappPermission> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
//        for (RvaUniappPermission menu : menus) {
//            RouterVo router = new RouterVo();
//            router.setHidden("1".equals(menu.getVisible()));
//            router.setName(getRouteName(menu));
//            router.setPath(getRouterPath(menu));
//            router.setComponent(getComponent(menu));
//            router.setQuery(menu.getQuery());
//            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
//            List<SysMenu> cMenus = menu.getChildren();
//            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
//                router.setAlwaysShow(true);
//                router.setRedirect("noRedirect");
//                router.setChildren(buildMenus(cMenus));
//            } else if (isMenuFrame(menu)) {
//                router.setMeta(null);
//                List<RouterVo> childrenList = new ArrayList<RouterVo>();
//                RouterVo children = new RouterVo();
//                children.setPath(menu.getPath());
//                children.setComponent(menu.getComponent());
//                children.setName(StringUtils.capitalize(menu.getPath()));
//                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
//                children.setQuery(menu.getQuery());
//                childrenList.add(children);
//                router.setChildren(childrenList);
//            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
//                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
//                router.setPath("/inner");
//                List<RouterVo> childrenList = new ArrayList<RouterVo>();
//                RouterVo children = new RouterVo();
//                String routerPath = StringUtils.replaceEach(menu.getPath(), new String[]{Constants.HTTP, Constants.HTTPS}, new String[]{"", ""});
//                children.setPath(routerPath);
//                children.setComponent(UserConstants.INNER_LINK);
//                children.setName(StringUtils.capitalize(routerPath));
//                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
//                childrenList.add(children);
//                router.setChildren(childrenList);
//            }
//            routers.add(router);
//        }
        return routers;
    }

    private void recursionFn(List<RvaUniappPermission> list, RvaUniappPermission t) {
        // 得到子节点列表
        List<RvaUniappPermission> childList = getChildList(list, t);
        t.setChildren(childList);
        for (RvaUniappPermission tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }

    }

    /**
     * 得到子节点列表
     */
    private List<RvaUniappPermission> getChildList(List<RvaUniappPermission> list, RvaUniappPermission t) {
        List<RvaUniappPermission> tlist = new ArrayList<RvaUniappPermission>();
        Iterator<RvaUniappPermission> it = list.iterator();
        while (it.hasNext()) {
            RvaUniappPermission n = (RvaUniappPermission) it.next();
            if (t.getId().equals(n.getParentId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<RvaUniappPermission> list, RvaUniappPermission t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

}
