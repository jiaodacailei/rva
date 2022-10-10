import {RvaUtils} from "@/api/rva/util";

export default {
  data() {
    return {
      appId : '',
      requestParams : {trigger: 0}
    };
  },
  created() {
    console.log('this.$route', this.$route)
    if (this.$route.params && this.$route.params.appId) {
      this.appId = this.$route.params.appId;
    } else {
      let strs = this.$route.path.split('/');
      this.appId = strs[strs.length - 1];
    }
    let index = this.appId.indexOf(',');
    if (index > 0) {
      this.appId.substring(index + 1).split('&').forEach(param => {
        let keyValue = param.trim().split('=');
        this.requestParams[keyValue[0]] = keyValue.length > 1 ? keyValue[1] : 'Y';
      });
      this.appId = this.appId.substring(0, index);
    }
    if (sessionStorage.getItem(this.$route.path)) {
      this.requestParams['rvaAppParams'] = JSON.parse(sessionStorage.getItem(this.$route.path));
      this.requestParams.trigger ++;
    }
    console.log('initApp', this.appId, this.requestParams);
    this.afterCreated()
  },
  methods: {
    afterCreated() {}
  }
};
