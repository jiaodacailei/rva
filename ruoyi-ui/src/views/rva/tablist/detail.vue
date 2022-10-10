<template>
  <div class="app-container">
    <rva-detail :id="id"></rva-detail>
  </div>
</template>

<script>
import RvaDetail from "@/components/rva/detail";
export default {
  name: "RvaTabListDetail",
  data() {
    return {
      id: '',
    }
  },
  components: {
    "rva-detail": RvaDetail
  },
  created() {
    let path = this.$route.path;
    let params = JSON.parse(sessionStorage.getItem(path));
    this.id = params.selection[0].keyPropValue
    this.requestParams = this.$route.query

  }
};
</script>
