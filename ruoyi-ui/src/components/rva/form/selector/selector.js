import {RvaUtils} from "@/api/rva/util";
import request from "@/utils/request";
import de from "element-ui/src/locale/lang/de";

export default {
  data() {
    return {
      options: [],
      loading: false,
      allowCreate: false
    }
  },
  methods: {
    refreshData(clear) {
      if (RvaUtils.isNotEmpty(clear) && clear != "@@clear" && clear != "@@first") {
        this.$emit('input', clear.toString());
        this.$nextTick(() => {
          this.afterCreated();
        })
        return;
      }
      if (clear == "@@first") {
        this.setValue();
      }

      let first = clear == "@@first"

      this.searchSelectOptions('#$@$#', false, first);
    },
    afterCreated() {
      if (RvaUtils.isEmpty(this.value)) {
        if (RvaUtils.parseValue(this.prop.data, 'selectorInitOptions', 'N') == 'Y') {
          this.searchSelectOptions('#$@$#');
        }
        return;
      }
      // 显示遮罩层
      this.loading = true;
      this.initOptions(this.getUrl('formInputorData', 'getByIds'), {
        ids: this.prop.formSelectorSingle == 'N' ? this.value.join(",") : this.value
      }, () => {
        if (this.prop.formReadonly == 'Y') {
          this.loading = false;
          return;
        }
        if (RvaUtils.parseValue(this.prop.data, 'selectorInitOptions', 'N') == 'Y') {
          this.searchSelectOptions('#$@$#', true);
        }
      });
      this.getAllowCreate();
    },
    initOptions(url, params, fn, append, first) {
      if (!url) {
        this.$modal.msgError('参数配置错误，请检查：formRelatedCrud/formInputorData/formInputorSearch');
        return;
      }

      if (!append && !this.multiple) {
        this.options = [];
      }
      RvaUtils.clone(this.requestParams, params, true);
      RvaUtils.clone(this.formData, params, true);
      params.appIds = this.getAppIds();
      params.pageSize = RvaUtils.parseValue(this.prop.data, 'selectorPageSize', 50);
      params.loadWhere = RvaUtils.parseValue(this.prop.data, 'relatedCrudWhere');
      params.loadWhereAppend = RvaUtils.parseValue(this.prop.data, 'relatedAppend', 'N');
      request({
        url,
        method: 'post',
        data: params
      }).then(response => {
        var selectorOptionLabel = RvaUtils.parseValue(this.prop.data, 'selectorOptionLabel');
        var selectorOptionValue = RvaUtils.parseValue(this.prop.data, 'selectorOptionValue');
        for (var i in response.rows) {
          let row = response.rows[i];
          row.value = row.selectorValue;
          row.label = row.selectorLabel;
          if (selectorOptionLabel) {
            row.label = RvaUtils.parseJsValue(selectorOptionLabel, row);
          }
          if (selectorOptionValue) {
            row.value = RvaUtils.parseJsValue(selectorOptionValue, row);
          }
        }
        var selectAllowCreate = RvaUtils.parseValue(this.prop.data, 'selectAllowCreate');
        // debugger
        if (response.rows.length == 0 && selectAllowCreate == 'Y' && params.searchContent != '') {
          if (this.options.filter(option => option.value == params.searchContent).length == 0) {
            // this.options.push({
            //   value: params.searchContent,
            //   label: params.searchContent + '-新建'
            // })
            if (this.prop.formSelectorSingle == 'Y') {
              this.change(params.searchContent);
            } else {

              if (this.value && this.value.length) {
                this.value.push(params.searchContent)
                this.change(this.value);
              } else {
                this.change([params.searchContent]);
              }

            }
          }
        } else if (append) {
          if (response.rows) {
            response.rows.forEach(row => {
              if (this.options.find(option => option.value == row.value)) {
                return;
              }
              this.options.push(row);
            });
          }
        } else {
          let options = response.rows ? response.rows : [];
          if (this.multiple) {
            if (this.value && this.value.length) {
              this.value.forEach(val => {
                if (!options.find(option => option.value == val)) {
                  let opt = this.options.find(option => option.value == val);
                  if (opt) {
                    options.unshift(opt)
                  }
                }
              })
            }
          }
          this.options = options;

          if (!this.multiple && RvaUtils.isNotEmpty(this.value)) {
            this.prop.option = this.options.find(o => o.value == this.value);
            console.log('this.prop.option', this.prop.option, this.prop.id);
            this.$emit('rva-trigger', this.prop.id)
          }
        }
        if (selectAllowCreate == 'Y' && !this.containValue(params.searchContent)) {
          this.options.push({
            value: params.searchContent,
            label: params.searchContent
          })
        }
        // debugger
        // if (RvaUtils.isNotEmpty(this.value)) {
        //   if (this.value instanceof Array) {
        //     let vals = this.value.filter(this.containValue)
        //     if (vals.length < this.value.length) {
        //       this.$emit('input', vals);
        //     }
        //   } else if (!this.containValue(this.value)) {
        //     this.$emit('input', undefined);
        //   }
        // }

        if (RvaUtils.isNotEmpty(this.value)) {
          if (this.value instanceof Array) {
            let vals = this.value.filter(this.containValue)
            if (vals.length < this.value.length) {
              this.$emit('input', vals);
            }
          } else if (!this.containValue(this.value)) {
            this.$emit('input', undefined);
          }
        } else if (first && this.options && this.options.length) {
          this.$emit('input', this.options[0].value);
        }
        if (fn) {
          fn();
        }
        // 隐藏遮罩层
        this.loading = false;
      }).catch(err => {
        console.log(url, err);
        // 隐藏遮罩层
        this.loading = false;
      });
    },
    containValue(val) {
      let contain = false;
      for (let i = 0; i < this.options.length; i++) {
        let option = this.options[i];
        if (option.value == val) {
          contain = true;
        }
      }
      return contain;
    },
    searchSelectOptions(query, append, first) {
      if (query === '') {
        return;
      }
      // 显示遮罩层
      this.loading = true;
      if (query == '#$@$#') {
        query = '';
      }
      this.initOptions(this.getUrl('formInputorSearch', 'search'), {
        searchContent: query
      }, undefined, append, first);
    },
    getAppIds() {
      if (RvaUtils.isNotEmpty(this.prop.formRelatedCrud)) {
        return this.prop.formRelatedCrud.split(',');
      }
      return [];
    },
    getUrl(key, suffix) {
      var url = this.prop[key];
      if (url) {
        return url;
      }
      var appIds = this.getAppIds();
      if (appIds.length > 0) {
        return ['/rva/crud/', appIds[0], '/', suffix].join('');
      }
    },
    getAllowCreate() {
      this.allowCreate = RvaUtils.parseValue(this.prop.data, 'selectAllowCreate', 'N') == 'Y'
    },
    cancalReadOnly(onOff) {
      if (RvaUtils.isPC()) {
        return;
      }
      this.$nextTick(() => {
        if (!onOff) {
          const Selects = this.$refs
          // 如果只有1个下拉框，这段就足够了---start
          if (Selects.agentSelect) {
            const input = Selects.agentSelect.$el.querySelector('.el-input__inner')
            input.removeAttribute('readonly')
          }
          // 如果只有1个下拉框，这段就足够了---end
          // 如果有多个，就加多几个，代码可以优化，我懒了
          if (Selects.agent2Select) {
            const appinput = Selects.appSelect.$el.querySelector('.el-input__inner')
            appinput.removeAttribute('readonly')
          }
          if (Selects.agent3Select) {
            const gameinput = Selects.gameSelect.$el.querySelector('.el-input__inner')
            gameinput.removeAttribute('readonly')
          }
        }
      });
    }
  },
  watch: {
    options: function (val) {
      // debugger
      if (val && val.length > 1 && val[0].value == val[1].value) {
        val.unshift()
      }
      console.log('this.options', this.options);
    }
  },
  computed: {
    optionEditable() {
      if (RvaUtils.isEmpty(this.value)) {
        return false;
      }
      return RvaUtils.parseValue(this.prop.data, 'selectorOptionEditable', 'N') == 'Y'
    },
    multiple() {
      return this.prop.formSelectorSingle == 'N'
    }
  }
}
