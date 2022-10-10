<template>
  <el-card>
    <el-collapse v-model="activeNames" @change="loadLogs">
      <el-collapse-item name="diagram">
        <template slot="title">
          <el-button class="el-icon-caret-right" type="primary" size="mini" plain>   流程图</el-button>
        </template>
        <el-card>
          <iframe :src="imageUrl" style="height: 300px;width: 100%" fit="contain"></iframe>
        </el-card>
      </el-collapse-item>
      <el-collapse-item name="relatedForm" v-if="relatedUpdateViewId">
        <template slot="title">
          <el-button class="el-icon-caret-right" type="info" size="mini" plain>{{' ' + relatedUpdateViewName}}</el-button>
        </template>
        <el-card>
          <rva-form :view-id="relatedUpdateViewId" :request-params="{bizKey, appId}" :info="true" @rva-meta="relatedUpdateViewName = $event.name"></rva-form>
        </el-card>
      </el-collapse-item>
      <el-collapse-item name="currentTask" v-if="currentTask.url">
        <template slot="title">
          <el-button class="el-icon-caret-right" type="success" size="mini" plain> 当前任务</el-button>
          <el-divider direction="vertical" ></el-divider>
          <el-tag type="info" style="margin-left: 5px">{{ currentTask.name }}</el-tag>
        </template>
        <el-card>
          <rva-form :view-id="currentTask.url" :request-params="requestParams"></rva-form>
        </el-card>
      </el-collapse-item>
      <el-collapse-item name="log">
        <template slot="title">
          <el-button class="el-icon-caret-right" type="info" size="mini" plain> 流程日志</el-button>
        </template>
        <el-timeline v-if="logs">
          <el-timeline-item v-for="(log, index) in logs" :timestamp="log.name + '  ' + log.time" placement="top" :key="index">
            <el-card>
              <rva-form :view-id="log.url" :request-params="requestParams" :info="true"></rva-form>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-collapse-item>
    </el-collapse>
  </el-card>
</template>

<script>
import RvaForm from "@/components/rva/form";

import {getTaskViewData, getTaskLogData} from "@/api/rva/activiti";
import {RvaUtils} from "@/api/rva/util";

export default {
  name: "RvaActiviti",
  components: {
    RvaForm
  },
  props:['appId', 'requestParams'],
  data() {
    return {
      bizKey: '',
      activeNames:['currentTask'],
      // 当前任务数据
      currentTask: {},
      logs: false,
      imageUrl: false,
      relatedUpdateViewId: false,
      relatedUpdateViewName: '关联数据'
    };
  },
  created() {
    this.loadAppData();
  },
  methods: {
    /** 查询模块列表 */
    loadAppData() {
      this.bizKey = this.requestParams.rvaAppParams.bizKey;
      if (!this.bizKey) {
        this.bizKey = this.requestParams.rvaAppParams.selection[0].keyPropValue;
      }
      this.imageUrl = `${process.env.VUE_APP_BASE_API}/rva/activiti/${this.appId}/load/image?bizKey=${this.bizKey}`;

      getTaskViewData(this.appId, this.bizKey).then(response => {
        this.currentTask = response.data;
        this.relatedUpdateViewId = RvaUtils.parseValue(response.data.app.data, 'relatedUpdateForm', false);
        if (this.relatedUpdateViewId) {
          this.activeNames.push('relatedForm')
        }
      });
    },
    loadLogs () {
      if (this.activeNames.indexOf('log') < 0) {
        return;
      }
      if (this.logs) {
        return;
      }
      getTaskLogData(this.appId, this.bizKey).then(response => {
        this.logs = response.data;
      });
    }
  }
};
</script>
