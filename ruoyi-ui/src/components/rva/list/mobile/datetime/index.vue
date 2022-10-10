<template>
  <span style="font-size: x-small; color: darkgray">
    {{ dateValue }}
  </span>
</template>

<script>
import mobileComponent from "@/components/rva/list/mobile/mobileComponent";
import {formatTime} from "@/utils/index"

var moment = require('moment');

export default {
  name: "RvaListMobileDate",
  mixins: [mobileComponent],
  computed: {
    dateValue() {
      return this.value ? formatTime(new Date(this.value).getTime()) : '';
    }
  }
}
</script>
