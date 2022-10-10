<template>
  <div class="app-container home">
    <template v-if="appData">
      <rva-portal :appId="appId" v-if="appData.type == 'portal'">
      </rva-portal>
      <rva-tcrud :appId="appId" v-else-if="appData.type == 'tcrud'">
      </rva-tcrud>
    </template>
    <div class="empty-page" v-else>

      <img src="@/assets/images/empty.png">
      <!--      <div class="pic-404">-->
      <!--        -->
      <!--        <img class="pic-404__parent" src="@/assets/404_images/404.png" alt="404">-->
      <!--        <img class="pic-404__child left" src="@/assets/404_images/404_cloud.png" alt="404">-->
      <!--        <img class="pic-404__child mid" src="@/assets/404_images/404_cloud.png" alt="404">-->
      <!--        <img class="pic-404__child right" src="@/assets/404_images/404_cloud.png" alt="404">-->
      <!--      </div>-->
      <h3>欢迎来到 <span style="margin: 0 10px; color: #1c84c6">{{ title }}</span> 后台</h3>

    </div>

  </div>
</template>

<script>

import RvaPortal from "@/components/rva/portal";
import RvaTcrud from "@/components/rva/tcrud";
import {listCrud, loadCrud} from "@/api/rva/crud";
import {getSysTitle} from "@/utils/ruoyi";


export default {
  name: "Index",
  data() {
    return {
      type: "tcrud",
      appId: "",
      roleId: 0,
      appData: false
    };
  },
  computed: {
    title() {
      return getSysTitle()
    }
  },
  components: {
    RvaPortal, RvaTcrud
  },
  mounted() {
    this.roleId = this.$store.state.user.roleIds[0]
    this.init();

  },
  methods: {
    init() {
      listCrud('/rva/view/u1_sys_role/load/update', {roleId: this.roleId}).then(response => {
        if (response.data.formData['u1_sys_role_yingyong']) {
          this.appId = response.data.formData['u1_sys_role_yingyong']
          loadCrud(this.appId).then(res => {
            this.appData = res.data;
            console.log(' this.appData=============', this.appData)
          });
        }
      })
    },

  },
};
</script>

<style scoped lang="scss">
.home {
  .empty-page {
    display: flex;
    -ms-flex-wrap: wrap;
    flex-wrap: wrap;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    -ms-flex-line-pack: center;
    align-content: center;
    align-items: flex-start;
    flex-direction: column;
  }

  .pic-404 {
    position: relative;
    float: left;
    width: 600px;
    overflow: hidden;
    margin-top: 200px;

    &__parent {
      width: 100%;
    }

    &__child {
      position: absolute;

      &.left {
        width: 80px;
        top: 17px;
        left: 220px;
        opacity: 0;
        animation-name: cloudLeft;
        animation-duration: 2s;
        animation-timing-function: linear;
        animation-fill-mode: forwards;
        animation-delay: 1s;
      }

      &.mid {
        width: 46px;
        top: 10px;
        left: 420px;
        opacity: 0;
        animation-name: cloudMid;
        animation-duration: 2s;
        animation-timing-function: linear;
        animation-fill-mode: forwards;
        animation-delay: 1.2s;
      }

      &.right {
        width: 62px;
        top: 100px;
        left: 500px;
        opacity: 0;
        animation-name: cloudRight;
        animation-duration: 2s;
        animation-timing-function: linear;
        animation-fill-mode: forwards;
        animation-delay: 1s;
      }

      @keyframes cloudLeft {
        0% {
          top: 17px;
          left: 220px;
          opacity: 0;
        }
        20% {
          top: 33px;
          left: 188px;
          opacity: 1;
        }
        80% {
          top: 81px;
          left: 92px;
          opacity: 1;
        }
        100% {
          top: 97px;
          left: 60px;
          opacity: 0;
        }
      }
      @keyframes cloudMid {
        0% {
          top: 10px;
          left: 420px;
          opacity: 0;
        }
        20% {
          top: 40px;
          left: 360px;
          opacity: 1;
        }
        70% {
          top: 130px;
          left: 180px;
          opacity: 1;
        }
        100% {
          top: 160px;
          left: 120px;
          opacity: 0;
        }
      }
      @keyframes cloudRight {
        0% {
          top: 100px;
          left: 500px;
          opacity: 0;
        }
        20% {
          top: 120px;
          left: 460px;
          opacity: 1;
        }
        80% {
          top: 180px;
          left: 340px;
          opacity: 1;
        }
        100% {
          top: 200px;
          left: 300px;
          opacity: 0;
        }
      }
    }
  }


}
</style>

