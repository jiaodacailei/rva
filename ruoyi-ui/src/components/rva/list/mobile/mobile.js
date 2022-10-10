import RvaListInnerButton from "@/components/rva/list/button/inner";
import RvaListMobileTextfield from "@/components/rva/list/mobile/textfield";
import RvaListMobileDictionary from "@/components/rva/list/mobile/dictionary";
import RvaListMobileDate from "@/components/rva/list/mobile/date";
import RvaListMobileDatetime from "@/components/rva/list/mobile/datetime";
import RvaListMobileImage from "@/components/rva/list/mobile/image";
import RvaListMobileVideo from "@/components/rva/list/mobile/video";

import {handleRvaButtonClick} from "@/api/rva/crud";
import {RvaUtils} from "@/api/rva/util";

export default {
  name: "RvaListMobile",
  props: ['viewData', 'listData', 'requestParams', 'orderByList', 'loading', 'selector', 'selectLimits', 'summariesData'],
  components: {
    RvaListInnerButton,
    RvaListMobileTextfield,
    RvaListMobileDictionary,
    RvaListMobileDate,
    RvaListMobileDatetime,
    RvaListMobileImage,
    RvaListMobileVideo
  },
  data() {
    return {
      // 选中数组
      selection: [],
      activeItem: '',
      drawer: false
    };
  },
  computed: {
    showSummary() {
      return RvaUtils.isNotEmpty(this.summariesData);
    },
    shortProperties() {
      return this.viewData.properties.filter((p, index) => index < 5 && p.type != 'hidden' && p.type != 'button')
    }
  },
  methods: {
    getRowTitle(row) {
      let prop = this.viewData.properties.find(p => p.type != 'hidden');
      return `${prop.name}: ${row[prop.id]}`
    },
    showDetail(row) {
      this.selection = [row];
      this.drawer = true;
    },
    handleSelectionChange(row, selected) {
      if (selected == undefined) {
        selected = !row.selected
        row.selected = selected
      }
      if (selected) {
        this.selection.push(row)
        let limits = this.selectLimits || 99999999;
        // limits = 1;
        if (this.selection.length > limits) {
          for (let i = this.selection.length - limits - 1; i >= 0; i--) {
            this.selection[i].selected = false;
            this.selection.splice(i, 1);
          }
        }
      } else {
        for (let i = 0; i < this.selection.length; i++) {
          if (this.selection[i] == row) {
            this.selection.splice(i, 1);
          }
        }
      }
      console.log('this.selection', this.selection)
      this.$emit('rvaListSelection', this.selection)
    },
    getButtonProp() {
      return this.viewData.properties.find(prop => prop.type == 'button');
    },
    getPropButtonSize() {
      let p = this.getButtonProp()
      let size = 0;
      for (var i in this.listData) {
        let val = this.listData[i][p.id]
        if (val) {
          let len = this.listData[i][p.id].length;
          if (len > size) {
            size = len;
          }
        } else {
          console.log('getPropButtonSize-undefined', p.id);
        }
      }
      if (size > 4) {
        size = 4;
      }
      return size;
    },
    getPropButtonColSpan(row) {
      let size = this.getPropButtons(row).length;
      if (size > 4) {
        size = 4;
      }
      return 24 / size;
    },
    getPropButtons(row) {
      let p = this.getButtonProp()
      return row[p.id];
    },
    loadList(action) {// handleRvaButtonClick中会调用
      this.$emit('rvaListReload');
    },
    /** 处理按钮点击事件 */
    handleButtonClick(data) {
      handleRvaButtonClick(this, data);
    },
    /**
     * 合计行数据
     * @param param
     */
    getSummaries(param) {
      let array = [];
      //选择框
      array.push("");
      let summariesData = this.summariesData;
      this.viewData.properties.forEach((p) => {
        let viewPropertyId = p.id;
        array.push(summariesData[viewPropertyId])
      });
      return array;
    },
    getPropComponent(prop) {
      return prop.type;
    }
  },
  watch: {
    listData: {
      handler() {
        if (this.listData && this.listData.length && this.activeItem == '') {
          this.activeItem = this.listData[0].keyPropValue
        }
      },
      deep: true
    }
  }
};
