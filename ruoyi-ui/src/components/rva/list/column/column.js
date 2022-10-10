import RvaListConfig from "@/components/rva/list/config";

export default {
  props: ['prop', 'requestParams'],
  components: {
    "rva-list-config": RvaListConfig,
  },
  created() {
    console.log('this.prop.id', this.prop.id, this.requestParams)
  },
  methods: {
    formatValue(row, column, cellValue, index) {
      if (row[column.property + '__value']) {
        cellValue = row[column.property + '__value'];
      }
      return cellValue;
    },
  }
};
