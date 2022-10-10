import {RvaUtils} from '@/api/rva/util'
import RvaFormTip from "@/components/rva/form/tip";

export default {
  components: {RvaFormTip},
  props: ['prop', 'value', 'formRef', 'formData', 'formItemProp', 'requestParams', 'info', 'viewData', 'hideLabel'],
  data() {
    return {
      showComponent: !this.prop.hide,
      rules: [],
      disabled: false,
      dictData: [],
      labelWidth: '',
    };
  },
  created() {
    let requiredIf = true
    let requiredIfStr = RvaUtils.parseValue(this.prop.data, 'requiredIf',);
    console.log('requiredIf str', requiredIfStr)
    if (requiredIfStr) {
      requiredIf = RvaUtils.parseJsValue(RvaUtils.parseValue(this.prop.data, 'requiredIf', true), {...this.formData, ...this.requestParams, ...this.viewData})
      console.log('requiredIf', this.prop.id, requiredIf)
    }

    RvaUtils.parseValidateRules(this.prop, this.rules,requiredIf);
    console.log('form input created ')
    this.refreshDictData()
    this.originalType = this.prop.type;
    this.disabled = this.info;
    this.afterCreated();
    this.afterCreated2();
    this.initLabelWidth();
  },
  methods: {
    refreshDictData() {
      if (['radio', 'checkbox', 'select', 'cascader'].indexOf(this.prop.type) >= 0) {
        RvaUtils.getDicts(this.prop.dictType, this, 'dictData', this.afterDict);
      }
    },
    refreshData(clear) {
    },
    setPropType(val) {
      if (val) {
        this.prop.type = val;
        this.$emit('rvaReloadView')
      }

    },
    setDictType(val) {
      if (val) {
        this.prop.dictType = val;
        this.$emit('rvaReloadView')
        this.refreshDictData()
      }
    },
    setValue(val) {
      this.$emit('input', val);
    },
    disable() {
      this.disabled = true;
      this.rerender()
    },
    enable() {
      this.disabled = false;
      this.rerender()
    },
    rerender() {
    },
    show() {
      this.rules = [];
      RvaUtils.parseValidateRules(this.prop, this.rules);
      this.showComponent = true;
      this.$emit('changeFormRowsPropData', 'show', this.prop)
    },
    hide() {
      for (let i = 0; i < this.rules.length; i++) {
        let rule = this.rules[i];
        if (rule.required == true) {
          this.rules.splice(i, 1);
          break;
        }
      }
      this.showComponent = false;
      this.$emit('changeFormRowsPropData', 'hide', this.prop)

    },
    afterCreated() {
    },
    afterCreated2() {
    },
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
    },
    setFunction(func) {
      eval('(' + func + ')')(this)
    },
  },
  computed: {
    readonly() {
      return this.prop.formReadonly == 'Y';
    },
    tip() {
      return RvaUtils.parseValue(this.prop.data, 'tip');
    }

  },
  watch: {
    'info': {
      handler(val, valOld) {
        this.disabled = val
      }
    },
  }
}
