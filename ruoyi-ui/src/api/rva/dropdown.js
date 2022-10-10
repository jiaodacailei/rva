import {RvaUtils} from "@/api/rva/util";
import dev from '@/api/rva/dev'

export default {
  components: {
  },
  mixins:[dev],
  data() {
    return {
      command: false,
    }
  },
  methods: {
    executeCommand(command) {
      if (!command) {
        return;
      }
      this.command = command;
      console.log('command', command);
      if (RvaUtils.isObject(command)) {
        if (this[command.name]) {
          eval('this.' + command.name + '(command)');
        }
        return;
      }
      console.log('this.' + command + '()');
      if (this[command]) {
        eval('this.' + command + '()');
      }
    },
    visibleChange(visible) {
      if (visible) {
        this.command = false;
      }
    },
  }
};
