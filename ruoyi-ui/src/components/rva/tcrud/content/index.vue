<template>
  <div>
    <template v-if="item.relatedAppType == 'form'">
      <component :is="'rva-' + item.relatedAppType" :requestParams="{...treeCrudParams, tcrudItem: item}"
                 :viewId="item.relatedAppId" @rva-button-click="handleButtonClick" :showTitle="true"
      ></component>
    </template>
    <template v-else-if="item.relatedAppType == 'list' || item.relatedAppType == 'search'">
      <component :is="'rva-' + item.relatedAppType" :requestParams="{...treeCrudParams, tcrudItem: item}" :queryContext="queryContext"
                 :viewId="item.relatedAppId" :queryParams="queryParams" :showTitle="true"
      ></component>
    </template>
    <template v-else>
      <component :is="'rva-' + item.relatedAppType" :requestParams="{...treeCrudParams, tcrudItem: item}"
                 :appId="item.relatedAppId" :queryContext="queryContext" :showTitle="true"
      ></component>
    </template>
  </div>
</template>

<script>
import RvaCrud from "@/components/rva/crud";
import RvaTcrud from "@/components/rva/tcrud";
import RvaForm from "@/components/rva/form";
import RvaList from "@/components/rva/list";
import RvaSearch from "@/components/rva/search";
import RvaKpi from "@/components/rva/kpi";
import RvaChart from "@/components/rva/chart";
import handleButtonClick from "@/components/rva/handleButtonClick";

export default {
  name: "rva-tcrud-content",
  props: ['item', 'treeCrudParams', 'queryContext'],
  mixins: [handleButtonClick],
  created() {
    console.log('rva-tcrud-content created...')
  },
  components: {
    RvaCrud,
    RvaForm,
    RvaList,
    RvaSearch,
    RvaTcrud,
    RvaChart,
    RvaKpi
  },
  data() {
    return {
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        // 显示搜索条件
        showSearch: true,
        trigger: 0
      }
    };
  },
};
</script>
