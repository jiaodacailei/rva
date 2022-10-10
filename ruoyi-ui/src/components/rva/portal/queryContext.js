import {RvaUtils} from "@/api/rva/util";

export default {
  props: ['queryContext'],
  data() {
    return {
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        // 显示搜索条件
        showSearch: RvaUtils.isPC(),
        trigger: 0
      }
    };
  },
  methods: {
    setQueryParams(context, force, create) {
      let queryContext = this.queryContext
      if (!queryContext && create) {
        queryContext = {
          pageNum: 1,
          pageSize: 10,
          // 显示搜索条件
          showSearch: RvaUtils.isPC(),
          trigger: 0
        }
      }
      if (queryContext && (force || RvaUtils.parseValue(context.data, "shareQueryContext", 'N') == 'Y')) {
        this.queryParams = queryContext
        console.log("shareQueryContext", context)
      }
    },
  }
};
