<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <el-popover
        placement="bottom-start"
        width="460"
        trigger="click"
        @show="$refs['iconSelect'].reset()"
      >
        <IconSelect ref="iconSelect" @selected="selected"/>
        <el-input slot="reference" v-model="value2"   placeholder="点击选择图标" readonly>
          <svg-icon
            v-if="value2"
            slot="prefix"
            :icon-class="value2"
            class="el-input__icon"
            style="height: 32px;width: 16px;"
          />
          <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
        </el-input>
      </el-popover>
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>
  </div>
</template>

<script>

import formInput from "@/api/rva/form-input";
import {RvaUtils} from "@/api/rva/util";
import IconSelect from "@/components/IconSelect";


export default {
  mixins: [formInput],
  components: {IconSelect},
  data() {
    return {
      max: 20,
      value2: this.value
    }
  },
  methods: {
    afterCreated() {
      this.max = RvaUtils.parseInt(this.prop.formValueMax, 20);
    },
    selected(name) {
      this.value2 = name;
    this.$emit('input', name);

    },
  }
};
</script>
