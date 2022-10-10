<template>
  <el-col>
    <el-row>
      <span :class="item.icon" :style="{['font-size']: '120%', color}">{{ item.value }}</span>
    </el-row>
    <el-tooltip effect="dark" :content="tip" placement="bottom">
      <span :style="{['align-content']: 'center', color}" @click.stop="editKpiItem()">{{item.name}}</span>
    </el-tooltip>
    <el-dialog :visible.sync="open" width="900px" append-to-body v-if="open">
      <rva-form viewId="u0_rva_kpi_item" :requestParams="{selection: [{keyPropValue: item.id}]}"
                @rva-button-click="handleButtonClick"></rva-form>
    </el-dialog>
  </el-col>
</template>

<script>
import RvaForm from "@/components/rva/form";
import dev from "@/api/rva/dev";
import {RvaUtils} from "@/api/rva/util";
import handleButtonClick from "@/components/rva/handleButtonClick";

export default {
  components: {
    RvaForm
  },
  mixins: [handleButtonClick, dev],
  props: ['appData', 'item'],
  data() {
    return {
      open: false
    }
  },
  methods: {
    editKpiItem() {
      if (this.devMode) {
        this.open = true;
      }
    }
  },
  computed: {
    color() {
      let colorType = RvaUtils.parseValue(this.item.data, 'color')
      if (!colorType || colorType == 'N') {
        colorType = RvaUtils.parseValue(this.appData.data, 'color', 'primary')
      }
      return RvaUtils.getColor(colorType)
    },
    tip() {
      let tip = RvaUtils.parseValue(this.item.data, 'tip')
      if (!tip) {
        return this.item.name;
      }
      return tip;
    }
  },
};
</script>
