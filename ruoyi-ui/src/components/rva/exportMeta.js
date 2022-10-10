export default {
  data() {
    return {
      openExport: false,
      exportId: '',
      exportType: ''
    }
  },
  methods: {
    exportMeta(cmd) {
      this.exportId = cmd.exportId;
      this.exportType = cmd.exportType;
      this.openExport = true;
    }
  },
  computed: {
    exportViewId() {
      if (['crud', 'tcrud', 'portal'].indexOf(this.exportType) >= 0) {
        return 'c0_none_yuanshujujiaoben';
      }
      return 'c0_none_shituyuanshujujiaoben';
    }
  }
};
