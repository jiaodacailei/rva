<template>
  <div>
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <template v-if="value && value.length">
        <el-row v-for="index in value.length" :key="index">
          <el-col :span="20">
            <el-input
              :placeholder="'请输入' + prop.name"
              clearable
              type="textarea"
              size="small"
              :value="value[index - 1]"
              :rows="rows"
              :disabled="disabled"
              @input="handleInput($event, index)"
              :maxlength="parseInt(prop.formValueMax) == NaN ? 20 : parseInt(prop.formValueMax)"
              :readonly="prop.formReadonly == 'Y'"
            />
          </el-col>
          <el-col :span="4" style="padding-left: 10px">
            <el-button size="mini" plain class="el-icon-plus" type="text" @click.stop="handleAdd(index)"></el-button>
            <el-button size="mini" plain class="el-icon-minus" type="text" @click.stop="handleDelete(index)"></el-button>
          </el-col>
        </el-row>
      </template>
      <el-button v-else size="mini" type="text" @click.stop="handleAdd(0)"
                 class="el-icon-plus"> 新增
      </el-button>
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>
  </div>
</template>

<script>

import {RvaUtils} from "@/api/rva/util";
import formInput from "@/api/rva/form-input";

export default {
  mixins: [formInput],
  data() {
    return {
      rows: 2,
    };
  },
  methods: {
    afterCreated() {
      this.rows = RvaUtils.parseValue(this.prop.data, 'textareaRows', 2);
    },
    handleAdd() {
      if (this.value) {
        this.value.push('')
      } else {
        this.$emit('input', [''])
      }
    },
    handleDelete(index) {
      this.value.splice(index - 1, 1)
      this.$emit('input', this.value)
    },
    handleInput(val, index) {
      this.value[index - 1] = val;
      this.value.push('')
      this.value.pop()
      this.$emit('input', this.value)
      console.log(val, index, this.value)
    }
  }
};
</script>
