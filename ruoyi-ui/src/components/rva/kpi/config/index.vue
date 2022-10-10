<template>
  <component :is="devMode ? 'el-link' : 'rva-span'">
    <rva-portal-config :appData="appData" :portalData="requestParams.portal" @click="click"></rva-portal-config>
    <el-dialog :visible.sync="open" width="900px" append-to-body v-if="open">
      <rva-form viewId="u0_rva_kpi" :requestParams="{selection: [{keyPropValue: appData.id}]}"
                @rva-button-click="handleButtonClick"></rva-form>
    </el-dialog>
  </component>
</template>

<script>
import RvaSpan from "@/components/rva/span"
import RvaForm from "@/components/rva/form"
import RvaPortalConfig from "@/components/rva/portal/config"
import handleButtonClick from "@/components/rva/handleButtonClick";
import dev from "@/api/rva/dev"
import RvaPortal from "@/components/rva/portal";
export default {
  name: "RvaKpiConfig",
  props: ['appData', 'requestParams'],
  mixins: [handleButtonClick, dev],
  components: {
    RvaPortal,
    RvaSpan, RvaForm, RvaPortalConfig
  },
  data() {
    return {
      open: false,
    }
  },
  methods: {
    click() {
      if (this.devMode) {
        this.open = true;
      }
    }
  },
}
</script>

<style scoped>

</style>
