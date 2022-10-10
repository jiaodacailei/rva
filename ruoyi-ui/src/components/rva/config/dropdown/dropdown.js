import {handleRvaButtonClick} from "@/api/rva/crud";

export default {
  components: {
    'rva-form': () => import('@/components/rva/form'),
    'rva-crud': () => import('@/components/rva/crud'),
    'rva-tcrud': () => import('@/components/rva/tcrud')
  },
  props: ['prop', 'command', 'executeCommand', 'requestParams'],
  created() {
  },
  data() {
    return {
      open: false,
      dialogAppIds: []
    }
  },
  methods: {
    closeView () {
      this.open = false
    },
    handleFormButtonClick(data) {
      handleRvaButtonClick(this, data);
    },
  },
  computed: {
    dialogAppId : function () {
      let firstChar = this.prop.id.substr(0, 1);
      if (firstChar == 'l') {// 列表视图
        return this.dialogAppIds[0];
      }
      if (firstChar == 's') {// 查询视图
        return this.dialogAppIds[2];
      }
      return this.dialogAppIds[1];// 表单视图
    }
  },
  watch: {
    executeCommand: function (val, oldVal) {
      if (this.command == this.executeCommand || this.command == this.executeCommand.name || this.command.indexOf(this.executeCommand.name) >= 0) {
        this.open = true;
      }
    }
  }
};
