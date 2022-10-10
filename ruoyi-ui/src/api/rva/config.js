import {RvaUtils} from "@/api/rva/util";
import {handleRvaButtonClick} from "@/api/rva/crud";
import request from '@/utils/request'
import dropdown from '@/api/rva/dropdown'

export default {
  components: {
    'rva-form': () => import('@/components/rva/form'),
    'rva-crud': () => import('@/components/rva/crud'),
    'rva-tcrud': () => import('@/components/rva/tcrud')
  },
  mixins:[dropdown],
  props: {
    prop: {
      type: Object
    },
    showValue: {
      type: String,
      default: ''
    },
    hideLabel: {
      type: Boolean,
      default: false
    },
    labelClickable: {
      type: Boolean,
      default: false
    },
    requestParams : {
      type: Object,
      default: () => {return {}}
    }
  },
  computed: {
    nameStyle() {
      if (!this.showValue) {
        return
      }
      let fontColor = RvaUtils.parseValue(this.prop.data, 'fontColor', '#1890ff')
      return 'font-size: 12px; color:' + fontColor + ';'
    }
  },
  methods: {
    deleteProperty() {
      if (confirm('确定要删除该视图属性吗？')) {
        request({
          url: '/rva/metaapp/viewproperty/' + this.prop.id + '/delete',
          method: 'post'
        })
      }
    },
    deleteDict() {
      if (confirm('确定要删除该视图属性关联的数据字典吗？')) {
        request({
          url: '/rva/metaapp/viewproperty/' + this.prop.id + '/deleteDict',
          method: 'post'
        })
      }
    },
    deleteBlacklist() {
      if (confirm('确定要删除该视图属性关联的黑名单吗？')) {
        request({
          url: '/rva/metaapp/viewproperty/' + this.prop.id + '/deleteBlackList',
          method: 'post'
        })
      }
    },
    handleFormButtonClick(data) {
      handleRvaButtonClick(this, data);
    },
    getLabel() {
      return this.hideLabel ? '' : this.prop.name;
    }
  }
};
