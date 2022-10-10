import RvaCrud from "@/components/rva/crud";
import RvaForm from "@/components/rva/form";
import RvaList from "@/components/rva/list";
import RvaSearch from "@/components/rva/search";
import RvaTcrudConfig from "@/components/rva/tcrud/config";
import RvaTcrudContent from "@/components/rva/tcrud/content";

import request from '@/utils/request'
import {loadCrud} from "@/api/rva/crud";
import {RvaUtils} from "@/api/rva/util";
import handleButtonClick from "@/components/rva/handleButtonClick";
import queryContext from "@/components/rva/portal/queryContext";

export default {
  name: "rva-tcrud",
  mixins: [queryContext, handleButtonClick],
  components: {
    RvaCrud,
    RvaForm,
    RvaList,
    RvaSearch,
    RvaTcrudConfig,
    RvaTcrudContent,
  },
  props: ['appId', 'requestParams', 'showTitle'],
  data() {
    return {
      // 应用元数据
      appData: {},
      contents: [],
      treeCrudParams: {
        trigger: 0,
        treeCrudId: '',
        treeCrudNodeValue: ''
      },
      autoselect: true
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      loadCrud(this.appId).then(response => {
        this.appData = response.data;
        this.setQueryParams(this.appData, false, true);
        this.afterCreated();
      });
    },
    afterCreated() {
    },
    getParams(nodeData, selection) {
      let params = RvaUtils.clone(this.requestParams, {}, true);
      if (selection) {
        RvaUtils.clone({selection: [nodeData]}, params, true);
      } else {
        RvaUtils.clone(nodeData, params, true);
      }
      params.treeCrudNodeValue = nodeData ? nodeData.keyPropValue : '';
      params.treeCrudId = nodeData ? nodeData.treeCrudId : '';
      params.trigger = new Date().getTime();
      return params;
    },
    loadNode(node, resolve) {
      var url = RvaUtils.parseValue(this.appData.data, 'urlLoadNode', ['/rva/tcrud/', this.appId, '/load/node'].join(''));
      request({
        url,
        method: 'post',
        data: this.getParams(node.data, true)
      }).then(response => {
        console.log('loadNode - response', response);
        let result = resolve(response.rows);
        if (this.autoselect && response.rows && response.rows.length > 0) {
          // var me = this;
          this.$nextTick(() => {
            this.$refs.tcrudTree.setCurrentKey(response.rows[0].nodeKey);
            this.treeClick(response.rows[0]);
            this.autoselect = false;
          })
        }
        return result;
      });
    },
    loadContent(param) {
      var url = RvaUtils.parseValue(this.appData.data, 'urlLoadContent', ['/rva/tcrud/', this.appId, '/load/content'].join(''));
      request({
        url,
        method: 'post',
        data: this.getParams(param)
      }).then(response => {
        this.treeCrudParams = this.getParams(param);
        console.log('treeClick', param, this.treeCrudParams);
        this.contents = response.data;
        console.log('this.contents', this.contents);
      });
    },
    treeClick(nodeData, node) {
      // console.log('-----this.$refs.tcrudTree.getCurrentKey()', this.$refs.tcrudTree.getCurrentKey(), this.$refs.tcrudTree.getCurrentNode())

      this.loadContent(nodeData)
    },
    reloadView() {
      this.init();
    },
  },
  computed: {
    component() {
      return RvaUtils.parseValue(this.appData.data, 'component', 'tree');
    }
  }
};
