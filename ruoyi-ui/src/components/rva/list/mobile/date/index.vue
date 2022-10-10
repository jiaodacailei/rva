<template>
  <span>
    {{ dateValue }}
  </span>
</template>

<script>
import mobileComponent from "@/components/rva/list/mobile/mobileComponent";
var moment = require('moment');

export default {
  name: "RvaListMobileDate",
  mixins: [mobileComponent],
  computed: {
    dateValue() {
      return this.value ? moment(this.value).format('yyyy-MM-DD') : '';
    }
  }
}
</script>
