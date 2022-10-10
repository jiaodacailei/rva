<template>
  <dict-tag :options="options" :value="row[prop.id]"/>
</template>
<script>

import mobileComponent from "@/components/rva/list/mobile/mobileComponent";
import {RvaUtils} from "@/api/rva/util";

export default {
  name: "RvaListMobileDictionary",
  mixins: [mobileComponent],
  data() {
    return {
      options: []
    }
  },
  created() {
    this.getDicts();
  },
  methods: {
    getDicts() {
      if (this.prop.dictType) {
        RvaUtils.getDicts(this.prop.dictType, this, 'options', () => {
          this.options = this.options.map(function (e) {
            return {
              value: e.dictValue,
              label: e.dictLabel,
              raw: {
                listClass: e.listClass,
                cssClass: e.cssClass
              }
            };
          })
        })
      }
    }
  }
}
</script>
