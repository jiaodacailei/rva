<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <el-checkbox-group :value="values" @input="changeValues">
        <el-checkbox
          v-for="option in options"
          :key="option.value"
          :label="option.value"
          :disabled="disabled || prop.formReadonly == 'Y'"
        >{{ option.label }}
        </el-checkbox>
      </el-checkbox-group>
      <el-button size="small" plain class="el-icon-plus" type="primary" @click="addObject" v-if="!disabled && prop.formReadonly != 'Y'"></el-button>
      <rva-crud-selector v-if="open" :open.sync="open" :appIds="getAppIds()"
                         :singleSelect="prop.formSelectorSingle == 'Y'" :requestParams="getSelectorRequestParams()"
                         @rvaConfirmListSelection="handleSelection"
      ></rva-crud-selector>
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>
  </div>
</template>

<script>
import formInput from "@/api/rva/form-input";
import selector from "@/components/rva/form/selector/selector";
import RvaCrudSelector from "@/components/rva/crud/selector";
import {RvaUtils} from "@/api/rva/util";

export default {
  components: {RvaCrudSelector},
  mixins: [formInput, selector],
  data() {
    return {
      open: false,
      values : []
    }
  },
  methods: {
    afterCreated2() {
      if (!RvaUtils.isEmpty(this.value)) {
        if (this.value instanceof Array) {
          this.values = this.value;
        } else {
          this.values = [this.value];
        }
      }
    },
    addObject() {
      this.open = true;
    },
    handleSelection(selection) {
      selection = selection.filter(sel => {
        return this.values.indexOf(sel.value) < 0
      })
      if (selection.length == 0) {
        return;
      }
      if (this.prop.formSelectorSingle == 'Y') {// 单选
        this.options = [selection[0]];
        this.values = [selection[0].value];
      } else {
        this.options = this.options.concat(selection);
        this.values = this.values.concat(selection.map(sel => sel.value))
      }
      console.log('handleSelection(selection)', this.options, this.values)
      this.changeValues()
    },
    changeValues(vals) {
      console.log('objectselector', vals, this.values);
      if (vals) {
        this.values = vals;
      }
      if (this.prop.formSelectorSingle == 'Y') {// 单选
        if (this.values.length > 0) {
          let lastVal = this.values[this.values.length - 1]
          this.values = [lastVal];
          this.$emit('input', lastVal)
        } else {
          this.values = [];
          this.$emit('input', '')
        }
      } else {
        this.$emit('input', this.values)
      }
      console.log('objectselector-end', vals, this.values);
    },
    getSelectorRequestParams() {
      return {
        ...this.requestParams,
        ...this.formData,
        loadWhere: RvaUtils.parseValue(this.prop.data, 'relatedCrudWhere')
      }
    }
  }
}
</script>
