import {RvaUtils} from '@/api/rva/util'

export default {

  data() {
    return {
      rules: [],
      dictData: [],
      disabled: false,
      labelWidth: ''
    };
  },
  props: ['prop', 'value', 'formRef', 'formData','viewData'],
  created() {
    if (['radio', 'checkbox', 'select'].indexOf(this.prop.type) >= 0) {
      RvaUtils.getDicts(this.prop.dictType, this);
    }
    this.initLabelWidth();
  },
  methods: {
    initLabelWidth() {
      if (this.hideLabel) {
        this.labelWidth = '0px';
        return;
      }
      let width = this.getLabelWidth(this.prop) || this.getLabelWidth(this.viewData) || RvaUtils.getFormLabelWidth();
      console.log('initLabelWidth', width)
      if (width) {
        this.labelWidth = width + 'px';
      }
    },
    getLabelWidth(obj) {
      if (obj && obj.data) {
        return RvaUtils.parseValue(obj.data, 'labelWidth')
      }
    }
  },
  computed: {
    readonly() {
      return this.prop.formReadonly == 'Y';
    }
  },
  watch: {}
}
