import request from '@/utils/request'
import {handleRvaButtonClick} from '@/api/rva/crud'
import {download} from '@/utils/request'
import {getFileUrl} from "@/utils/ruoyi";
import {RvaUtils} from '@/api/rva/util'

export default {
  props: ['viewButtonData', 'selection', 'disabled', 'formData', 'formRef', 'requestParams','viewData'],
  components: {
    'rva-form': () => import('@/components/rva/form'),
    'rva-crud': () => import('@/components/rva/crud'),
    'rva-tcrud': () => import('@/components/rva/tcrud'),
    'rva-crud-selector': () => import('@/components/rva/crud/selector')
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      open: false,
      dialogFormParams: false,
      dialogAppParams: {trigger: 0},
      dialogWidth: '80%',
      dialogSelector: false,
      dialogSelectorSingle: false,
      show: false,
      actionTitle: ''
    };
  },
  created() {
    this.ifShow();
    this.dialogSelector = (RvaUtils.parseValue(this.viewButtonData.data, 'dialogSelector', 'N') == 'Y');
    this.dialogSelectorSingle = (RvaUtils.parseValue(this.viewButtonData.data, 'dialogSelectorSingle', 'N') == 'Y');
    // console.log('this.dialogSelector', this.viewButtonData.name, this.dialogSelector, this.viewButtonData);
  },
  methods: {
    getPermissions() {
      if (RvaUtils.isDevMode()) {
        return []
      }
      return [this.viewButtonData.id]
    },
    getParams() {
      var params = {...this.viewButtonData.params, ...this.requestParams};
      if (this.selection) {
        params.selection = this.selection
      }
      params.button = this.viewButtonData;
      let title = RvaUtils.parseValue(this.viewButtonData.data, 'actionTitle')
      if (title) {
        this.actionTitle = RvaUtils.parseJsValue(title, params);
      } else {
        this.actionTitle = this.viewButtonData.name
      }
      // 设置关联视图actionDialogViewId[或者应用actionDialogAppId]的loadWhere
      params.loadWhere = RvaUtils.parseValue(this.viewButtonData.data, 'relatedLoadWhere');
      params.loadWhereAppend = RvaUtils.parseValue(this.viewButtonData.data, 'relatedAppend', 'N');
      if (this.viewButtonData.params) {
        for (var key in this.viewButtonData.params) {
          let val = this.viewButtonData.params[key];
          if (RvaUtils.isEmpty(val)) {
            continue;
          }
          val = RvaUtils.parseJsValue(val, params);
          params[key] = val;
        }
      }
      return params;
    },
    setDialogWidth(width) {
      this.dialogWidth = width + 'px';
    },
    /** 处理按钮点击事件 */
    handleButtonClick() {
      console.log('handleButtonClick....', this.viewButtonData);
      // 显示按钮遮罩层，不允许再次点击
      this.loading = true;
      // 增加参数
      var params = this.getParams();
      if (this.viewButtonData.type == 'js') {
        if (this.viewButtonData.action == 'reload') {
          location.reload();
        } else if (this.viewButtonData.action == 'download') {
          download(this.viewButtonData.actionUrl, params, this.viewData.name + '.xlsx')
        } else if (this.viewButtonData.action == 'open') {
          var fileUrl = getFileUrl(this.viewButtonData.actionUrl, process.env.VUE_APP_BASE_API, params);
          window.open(fileUrl)
        } else {
          this.$emit('rva-button-click', [{action: this.viewButtonData.action}]);
        }
        this.loading = false;
        return;
      }
      if (this.viewButtonData.action == 'tab') {
        let path = this.viewButtonData.actionUrl || `/rva/${this.viewButtonData.type}/${this.viewButtonData.actionDialogViewId || this.viewButtonData.actionDialogAppId},_keyValue=${this.selection && this.selection[0].keyPropValue}`;
        this.$router.push({path});
        sessionStorage.setItem(path + ".title", this.actionTitle);
        sessionStorage.setItem(path, JSON.stringify(params));
        // 隐藏按钮遮罩层，允许再次点击
        this.loading = false;
        return;
      }
      if (this.viewButtonData.type == 'ajax') {
        console.log('ajax', this.formData, params, this.formRef, this.$refs);
        if (RvaUtils.parseValue(this.viewButtonData.data, 'validateForm') == 'N' || !this.formRef) {// 不校验表单
          this.ajax(params);
          return;
        }
        this.formRef.validate((valid, object) => {
          if (valid) {
            this.ajax(params);
          } else {
            console.log('validateForm error!!', object);
            // 隐藏按钮遮罩层，允许再次点击
            this.loading = false;
            return false;
          }
        });
        return;
      }
      if (this.viewButtonData.type == 'form') {
        // 显示对话框
        console.log('button.request...');
        this.dialogFormParams = params;
        this.open = true;
        // 隐藏按钮遮罩层，允许再次点击
        this.loading = false;
        return;
      }
      // 显示对话框
      this.open = true;
      this.dialogAppParams.rvaAppParams = params;
      this.dialogAppParams.trigger++;
      // 隐藏按钮遮罩层，允许再次点击
      this.loading = false;
    },
    ajax(params) {
      RvaUtils.clone(this.formData, params);
      RvaUtils.clone(this.dialogAppParams, params);
      // /rva/view/c1_rva_module/submit/sql
      let url = this.viewButtonData.actionUrl;
      if (url && url.indexOf('/') < 0) {
        url = `/rva/viewbutton/${this.viewButtonData.id}/submit/sql`
      }
      request({
        url,
        method: 'post',
        data: params
      }).then(response => {
        this.$modal.msgSuccess("操作成功！");
        var actions = [];
        var closeView = true;
        if (this.viewButtonData.actionSuccess) {
          this.viewButtonData.actionSuccess.split(',').forEach(function (action) {
            if (action == 'keepView') {
              closeView = false;
            } else {
              actions.push({
                action,
                request: params,
                response
              });
            }
          });
        }
        if (closeView) {
          actions.unshift({
            action: 'closeView'
          });
        }
        this.$emit('rva-button-click', actions);
        console.log('rva-button-click', actions)
        // 隐藏按钮遮罩层，允许再次点击
        this.loading = false;
      }).catch(err => {
        console.log(this.viewButtonData.actionUrl, err);
        // 隐藏按钮遮罩层，允许再次点击
        this.loading = false;
      });
    },
    closeView(action) {// handleRvaButtonClick中会调用
      this.open = false;
    },
    handleFormButtonClick(data) {
      handleRvaButtonClick(this, data);
    },
    handleButtonConfigClick(data) {
      handleRvaButtonClick(this, data);
    },
    ifShow() {
      // console.log ('this.viewButtonData.showIf', this.viewButtonData.showIf);
      if (this.viewButtonData.position == 'inner') {
        this.show = true;
      } else {
        if (RvaUtils.isEmpty(this.viewButtonData.showIf)) {
          this.show = true;
        } else {
          try {
            this.show = eval('(' + this.viewButtonData.showIf + ')');
          } catch (e) {
            this.show = true;
          }
        }
      }
      // console.log ('this.show', this.viewButtonData.id, this.show);
    },
    getConfirmMessage() {
      return this.viewButtonData.confirmMessage;
    },
    handleSelectorSelection(selection) {
      console.log('handleSelectorSelection', selection)
      this.ajax({selectorSelection: selection});
    }
  },
  watch: {
    requestParams: {
      handler() {
        this.ifShow();
      },
      deep: true
    },
    selection: {
      handler() {
        this.ifShow();
      },
      deep: true
    }
  }
}
