<template>
  <component :is="dev ? 'el-link' : 'rva-span'">
    <rva-portal-config :appData="appData" :portalData="requestParams.portal" @click="click"></rva-portal-config>
  </component>
</template>

<script>
import RvaSpan from "@/components/rva/span"
import RvaPortalConfig from "@/components/rva/portal/config"
import {RvaUtils} from "@/api/rva/util";

export default {
  name: "index",
  props: ['appData', 'requestParams'],
  components: {
    RvaSpan, RvaPortalConfig
  },
  methods: {
    click() {
      if (this.dev) {
        let path = '/rva/chart/setting/' + this.appData.id
        sessionStorage.setItem(path, JSON.stringify({selection: [{keyPropValue: this.appData.id}]}));
        this.$router.push({
          path
        })
        sessionStorage.setItem(path + ".title", '设置' + this.appData.name);
      }
    }
  },
  computed: {
    dev() {
      return RvaUtils.isDevMode()
    }
  }
}
</script>

<style scoped>

</style>
