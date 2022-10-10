<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" @click="editOption" :label-clickable="optionEditable" :requestParams="requestParams"></rva-form-config>
      </template>
      <el-select
        style="width: 100%"
        :value="value"
        :multiple="multiple"
        filterable
        clearable
        remote
        default-first-option
        :allow-create="allowCreate"
        :disabled="disabled || prop.formReadonly == 'Y'"
        reserve-keyword
        :placeholder="'请输入' + prop.name"
        :remote-method="searchSelectOptions"
        @change="change"
        ref="agentSelect"
        @hook:mounted="cancalReadOnly"
        @visible-change="cancalReadOnly"
        :loading="loading">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>
    <el-dialog :title="prop.name" :visible.sync="openDialog" v-if="openDialog" :width="dialogWidth" append-to-body>
      <template v-if="multiple">
        <rva-listeditor :appId="prop.formRelatedCrud" :requestParams="dialogFormParams"
                  @rva-button-click="handleFormButtonClick" @rva-form-width="dialogWidth = $event + 'px'"
        ></rva-listeditor>
      </template>
      <template v-else>
        <rva-form v-if="dialogViewId" :viewId="dialogViewId" :requestParams="dialogFormParams"
                  @rva-button-click="handleFormButtonClick" @rva-form-width="dialogWidth = $event + 'px'"
        ></rva-form>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import formInput from "@/api/rva/form-input";
import selector from "@/components/rva/form/selector/selector";
import {handleRvaButtonClick, loadCrud} from '@/api/rva/crud';

export default {
  mixins: [formInput, selector],
  data() {
    return {
      openDialog: false,
      dialogWidth: '900px',
      dialogFormParams: {},
      dialogViewId: false
    }
  },
  components: {
    'rva-form': () => import('@/components/rva/form'),
    'rva-listeditor': () => import('@/components/rva/crud/listeditor'),
  },
  methods: {
    handleFormButtonClick (data) {
      handleRvaButtonClick(this, data);
    },
    editOption() {
      this.dialogFormParams = {
        ...this.requestParams,
        ...this.formData,
        selectedOptionValue: this.value
      }
      if (!this.multiple) {
        loadCrud(this.prop.formRelatedCrud).then(res => {
          this.dialogFormParams = {
            ...this.requestParams,
            ...this.formData,
            selectedOptionValue: this.value,
            selection: [{keyPropValue: this.value}]
          }
          this.dialogViewId = res.data.updateId;
          this.openDialog = true;
        })
      } else {
        this.dialogFormParams.selectedOptionValue = (this.value && this.value.length > 0) ? "'" + this.value.join("','") + "'" : ''
        this.openDialog = true;
      }
    },
    change(value) {
      // debugger
      let results = this.options.filter(option => {
        if (this.prop.formSelectorSingle == 'N') {
          if (value && value.length && value.slice(-1)[0] == option.value) {
            return true;
          }
        } else {
          return option.value == value
        }
      })
      if (results.length == 0) {
        if (this.prop.formSelectorSingle == 'N') {
          if (value != '') {
            this.options.push({
              value: value.slice(-1)[0],
              label: value.slice(-1)[0]
            });
          }
        } else {
          if (value != '') {
            this.options.push({
              value,
              label: value
            });
          }
        }

      }
      console.log('selector-change', value);
      if (this.prop.formSelectorSingle == 'Y') {
        this.prop.option = this.options.find(o => o.value == value);
      }
      this.$emit('input', value)
    },
  }
}
</script>
