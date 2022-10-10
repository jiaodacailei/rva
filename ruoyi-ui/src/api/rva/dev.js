import {RvaUtils} from "@/api/rva/util";
export default {
  data() {
    return {
      devMode: RvaUtils.isDevMode(),
      showTimeout: RvaUtils.getDropdownShowTimeout()
    }
  },
}
