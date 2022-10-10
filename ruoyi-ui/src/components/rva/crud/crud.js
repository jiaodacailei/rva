import RvaSearchView from "@/components/rva/search";

import {handleRvaButtonClick, loadCrud} from "@/api/rva/crud";
import queryContext from "@/components/rva/portal/queryContext";
import dev from "@/api/rva/dev"

export default {
  name: "RvaCrud",
  mixins: [queryContext, dev],
  components: {
    "rva-search": RvaSearchView,
  },
  props: ['selector', 'appId', 'requestParams', 'showTitle', 'appMeta'],
  data() {
    return {
      // 应用元数据
      appData: {},
      // 遮罩层
      loading: true,
    };
  },
  created() {
    this.loadAppData();
  },
  methods: {
    /** 查询模块列表 */
    loadAppData() {

      if (this.appId) {
        this.loading = true;
        loadCrud(this.appId).then(response => {
          this.appData = response.data;
          this.$emit('rvaAppData', this.appData);
          sessionStorage.setItem('rvaDevMode', this.appData.devMode);
          console.log('loadAppData success', this.appData);
          this.setQueryParams(this.appData)
          this.metaLoaded();
          this.loading = false;
        });
      } else {
        this.appData = this.appMeta;
        this.setQueryParams(this.appData)
        this.metaLoaded();
      }
    },
    getRequestParams() {
      if (this.requestParams) {
        this.requestParams.appId = this.appId;
        return this.requestParams;
      }
      return {
        appId: this.appId
      }
    },
    metaLoaded() {

    },
    handleButtonClick(data) {
      handleRvaButtonClick(this, data);
    },
  }
};
