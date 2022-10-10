import {handleRvaButtonClick} from "@/api/rva/crud";
export default {
  methods: {
    handleButtonClick(data) {
      handleRvaButtonClick(this, data);
    },
  },
};
