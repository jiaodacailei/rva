<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
import {loadInitStoreData} from "@/api/rva/init";
import {getSysTitle, getSysLogo} from "@/utils/ruoyi"

export default {
  name: 'App',
  created() {
    loadInitStoreData();
  },
  metaInfo() {
    return {
      title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
      titleTemplate: title => {
        let sysName = getSysTitle()
        return title ? `${title} - ${sysName}` : sysName
      },
      link: [{                 // set link
        rel: 'icon',
        href: getSysLogo()
      }]

    }
  }
}
</script>
<style lang="scss">
//解决ElementUI el-select在移动端需要点击两次才能弹出下拉与选中
.el-scrollbar {
  > .el-scrollbar__bar {
    opacity: 1 !important;
  }
}
</style>
