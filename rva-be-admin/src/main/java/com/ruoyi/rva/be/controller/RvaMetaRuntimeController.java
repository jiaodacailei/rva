package com.ruoyi.rva.be.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaViewbutton;
import com.ruoyi.rva.framework.service.IRvaMetaRuntimeService;
import com.ruoyi.rva.framework.service.IRvaMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 应用场景下的元数据管理（属性、按钮label的下拉菜单）
 *
 * @author jiaodacailei
 * @date 2021-09-13
 */
@RestController
@RequestMapping("/rva/metaapp")
public class RvaMetaRuntimeController extends BaseController {
    @Autowired
    private IRvaMetaRuntimeService metaAppService;

    @Autowired
    private IRvaMetaService metaService;

    /**
     * 修改表objId的字段值
     *
     * @param objId
     * @param req
     * @return
     */
    @PostMapping("/object/{object}/update")
    public AjaxResult updateObject(@PathVariable("object") String objId, @RequestBody RvaMap<String, Object> req) {
        metaService.updateObject(objId, req);
        return AjaxResult.success();
    }

    @PostMapping("/viewproperty/{viewproperty}/delete")
    public AjaxResult deleteViewproperty(@PathVariable("viewproperty") String viewpropertyId) {
        metaService.deleteViewproperty(viewpropertyId);
        return AjaxResult.success();
    }

    @PostMapping("/viewproperty/createDict")
    public AjaxResult createDict(@RequestBody RvaMap<String, Object> req) {
        metaService.createViewpropertyDict(req);
        return AjaxResult.success();
    }

    @PostMapping("/viewproperty/updateDict")
    public AjaxResult updateDict(@RequestBody RvaMap<String, Object> req) {
        metaService.updateViewpropertyDict(req);
        return AjaxResult.success();
    }

    @PostMapping("/viewproperty/{viewproperty}/deleteDict")
    public AjaxResult deleteViewpropertyDict(@PathVariable("viewproperty") String viewpropertyId) {
        metaService.deleteViewpropertyDict(viewpropertyId);
        return AjaxResult.success();
    }

    @PostMapping("/viewproperty/deleteDict")
    public AjaxResult deleteDict(@RequestBody RvaMap<String, Object> req) {
        metaService.createViewpropertyDict(req);
        return AjaxResult.success();
    }

    @PostMapping("/viewproperty/addToBlackList")
    public AjaxResult addToBlackList(@RequestBody RvaMap<String, Object> req) {

        metaService.addToBlackList(req);
        return AjaxResult.success();
    }

    @PostMapping("/viewproperty/loadBlackList")
    public AjaxResult loadBlackList(@RequestBody RvaMap<String, Object> req) {
        return AjaxResult.success(metaService.loadBlackList(req));
    }

    @PostMapping("/viewproperty/{viewproperty}/deleteBlackList")
    public AjaxResult deleteBlackList(@PathVariable("viewproperty") String viewpropertyId) {
        metaService.deleteBlackList(viewpropertyId);
        return AjaxResult.success();
    }

    @PostMapping("/viewproperty/synchronize")
    public AjaxResult synchronize(@RequestBody RvaMap<String, Object> req) {
        metaService.synchronize(req.getList("c0_none_tongbupeizhi_shitu"), req.getList("c0_none_tongbupeizhi_shuxing"));
        return AjaxResult.success(metaService.loadBlackList(req));
    }

    /**
     * 删除按钮
     *
     * @param buttonId
     * @return
     */
    @PostMapping("/button/{button}/delete")
    public AjaxResult deleteButton(@PathVariable("button") String buttonId) {
        metaService.deleteViewbutton(buttonId);
        return AjaxResult.success();
    }

    @PostMapping("/button/{button}/addButtonToMenu")
    public AjaxResult addButtonToMenu(@PathVariable("button") String buttonId) {
        metaService.addButtonToMenu(buttonId);
        return AjaxResult.success();
    }

    /**
     * 在按钮所属的列表视图上，增加该列表及其关联的表单视图中，所有数据字典的修改按钮
     *
     * @param buttonId
     * @return
     */
    @PostMapping("/button/{button}/createListDictButtons")
    public AjaxResult createListDictButtons(@PathVariable("button") String buttonId) {
        metaService.createListDictButtons(buttonId);
        return AjaxResult.success();
    }

    /**
     * 创建crud‘新建’按钮，并克隆‘新建’视图与之关联
     *
     * @param buttonId 触发本操作的按钮id
     * @param cascade  创建的按钮和视图是否级联
     * @return
     */
    @PostMapping("/button/{button}/createCrudCreateButton")
    public AjaxResult createCrudCreateButton(@PathVariable("button") String buttonId, @RequestParam(defaultValue = "true") Boolean cascade) {
        metaAppService.createCrudCreateButton(buttonId, cascade);
        return AjaxResult.success();
    }

    /**
     * 创建列表上的删除按钮
     *
     * @param buttonId 触发本操作的按钮id
     * @return
     */
    @PostMapping("/button/{button}/createTopDeleteButton")
    public AjaxResult createTopDeleteButton(@PathVariable("button") String buttonId) {
        metaAppService.createTopDeleteButton(buttonId);
        return AjaxResult.success();
    }

    /**
     * 创建crud‘修改’按钮，并克隆‘修改’视图与之关联
     *
     * @param buttonId 触发本操作的按钮id
     * @param cascade  创建的按钮和视图是否级联
     * @return
     */
    @PostMapping("/button/{button}/createCrudUpdateButton")
    public AjaxResult createCrudUpdateButton(@PathVariable("button") String buttonId, @RequestParam(defaultValue = "true") Boolean cascade) {
        metaAppService.createCrudUpdateButton(buttonId, cascade);
        return AjaxResult.success();
    }

    /**
     * 创建列表行上的删除按钮
     *
     * @param buttonId 触发本操作的按钮id
     * @return
     */
    @PostMapping("/button/{button}/createInnerDeleteButton")
    public AjaxResult createInnerDeleteButton(@PathVariable("button") String buttonId) {
        metaAppService.createInnerDeleteButton(buttonId);
        return AjaxResult.success();
    }

    /**
     * 创建crud‘克隆’按钮，并克隆‘新建’视图与之关联
     *
     * @param buttonId 触发本操作的按钮id
     * @param cascade  创建的按钮和视图是否级联
     * @return
     */
    @PostMapping("/button/{button}/createCrudCloneButton")
    public AjaxResult createCrudCloneButton(@PathVariable("button") String buttonId, @RequestParam(defaultValue = "true") Boolean cascade) {
        metaAppService.createCrudCloneButton(buttonId, cascade);
        return AjaxResult.success();
    }

    /**
     * 创建列表上的上移按钮
     *
     * @param buttonId 触发本操作的按钮id
     * @return
     */
    @PostMapping("/button/{button}/createMoveUpButton")
    public AjaxResult createMoveUpButton(@PathVariable("button") String buttonId) {
        metaAppService.createMoveUpButton(buttonId);
        return AjaxResult.success();
    }

    /**
     * 创建列表上的下移按钮
     *
     * @param buttonId 触发本操作的按钮id
     * @return
     */
    @PostMapping("/button/{button}/createMoveDownButton")
    public AjaxResult createMoveDownButton(@PathVariable("button") String buttonId) {
        metaAppService.createMoveDownButton(buttonId);
        return AjaxResult.success();
    }

    /**
     * 创建表单上的提交‘新建’按钮
     *
     * @param buttonId 触发本操作的按钮id
     * @return
     */
    @PostMapping("/button/{button}/createSubmitCreateButton")
    public AjaxResult createSubmitCreateButton(@PathVariable("button") String buttonId) {
        metaAppService.createSubmitCreateButton(buttonId);
        return AjaxResult.success();
    }

    /**
     * 创建表单上的提交‘修改’按钮
     *
     * @param buttonId 触发本操作的按钮id
     * @return
     */
    @PostMapping("/button/{button}/createSubmitUpdateButton")
    public AjaxResult createSubmitUpdateButton(@PathVariable("button") String buttonId) {
        metaAppService.createSubmitUpdateButton(buttonId);
        return AjaxResult.success();
    }

    /**
     * 创建表单上的重置按钮
     *
     * @param buttonId 触发本操作的按钮id
     * @return
     */
    @PostMapping("/button/{button}/createResetButton")
    public AjaxResult createResetButton(@PathVariable("button") String buttonId) {
        metaAppService.createResetButton(buttonId);
        return AjaxResult.success();
    }

    /**
     * 创建表单上的取消按钮
     *
     * @param buttonId 触发本操作的按钮id
     * @return
     */
    @PostMapping("/button/{button}/createCancelButton")
    public AjaxResult createCancelButton(@PathVariable("button") String buttonId) {
        metaAppService.createCancelButton(buttonId);
        return AjaxResult.success();
    }

    /**
     * rva-button-config:选择表单建按钮
     *
     * @return
     */
    @PostMapping("/button/selectForm")
    public AjaxResult selectForm(@RequestBody Map<String, Object> req) {
        RvaViewbutton selectedButton = RvaViewbutton.create(req.get("rvaAppParams"));
        metaAppService.createButtonBySelectForm(new RvaMap(req).getString("c0_none_xuanzebiaodanjiananniu_biaodanshitu"), selectedButton);
        return AjaxResult.success();
    }

    /**
     * rva-button-config:模板新建-导入
     *
     * @return
     */
    @PostMapping("/button/selectImportForm")
    public AjaxResult createImportButtonBySelectForm(@RequestBody Map<String, Object> req) {
        RvaViewbutton selectedButton = RvaViewbutton.create(req.get("rvaAppParams"));
        metaAppService.createImportButtonBySelectForm(new RvaMap(req).getString("c0_none_daoruxuanzebiaodan_xinjianbiaodan"), selectedButton);
        return AjaxResult.success();
    }


    /**
     * 创建crud‘导出’按钮
     *
     * @param buttonId 触发本操作的按钮id
     * @return
     */
    @PostMapping("/button/{button}/createExportButton")
    public AjaxResult createExportButton(@PathVariable("button") String buttonId) {
        metaAppService.createExportButton(buttonId);
        return AjaxResult.success();
    }


    /**
     * rva-button-config:选择crud建按钮
     *
     * @return
     */
    @PostMapping("/button/selectCrud")
    public AjaxResult selectCrud(@RequestBody Map<String, Object> req) {
        RvaViewbutton selectedButton = RvaViewbutton.create(req.get("rvaAppParams"));
        metaAppService.createButtonBySelectCrud(new RvaMap(req).getString("c0_none_xuanzecrudjiananniu_crud"), selectedButton);
        return AjaxResult.success();
    }

    /**
     * rva-button-config:新建tcrud建按钮
     *
     * @return
     */
    @PostMapping("/button/createTcrud")
    public AjaxResult createTcrud(@RequestBody Map<String, Object> req) {
        RvaMap map = new RvaMap(req);
        RvaViewbutton selectedButton = RvaViewbutton.create(req.get("rvaAppParams"));
        metaAppService.createButtonByCreateTcrud(map.getList("cc0_none_content"), map.getList("cc0_none_nav"), selectedButton);
        return AjaxResult.success();
    }

    /**
     * rva-button-config:选择tcrud建按钮
     *
     * @return
     */
    @PostMapping("/button/selectTcrud")
    public AjaxResult selectTcrud(@RequestBody Map<String, Object> req) {
        RvaViewbutton selectedButton = RvaViewbutton.create(req.get("rvaAppParams"));
        metaAppService.createButtonBySelectTcrud(new RvaMap<>(req).getString("cc1_none_crud"), selectedButton);
        return AjaxResult.success();
    }
}
