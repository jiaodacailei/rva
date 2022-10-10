export default {
  props: ['prop', 'row', 'requestParams'],
  methods: {
  },
  computed: {
    value() {
      let val = this.row[this.prop.id + '__value']
      if (val == undefined) {
        val = this.row[this.prop.id]
      }
      return val;
    }
  }
}
