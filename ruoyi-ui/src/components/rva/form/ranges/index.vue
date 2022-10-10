<template>
  <div class="rva-form-ranges" @mouseup="sliderMouseUp" @mousemove="sliderMouseMove" @mouseleave="sliderMouseOut"
       @mousedown.stop.prevent="sliderMouseDown">
    <el-form-item :label="prop.name" :prop="formItemProp ? formItemProp : prop.id" :rules="rules"
                  v-show="showComponent" :label-width="labelWidth">
      <template slot="label">
        <rva-form-config :prop="prop" :requestParams="requestParams"></rva-form-config>
      </template>
      <div class="block">
        <el-row>
          <el-col :span="20">
            <div role="slider" :aria-valuemin="min" :aria-valuemax="max" aria-orientation="horizontal" class="el-slider"
                 :aria-label="`slider between ${min} and ${max}`">
              <div class="el-slider__runway" ref="sliderRunway">
                <template v-for="(range, rangeIndex) in ranges">
                  <el-tooltip class="item" effect="dark" :content="`${range[0]}-${range[1]}`" placement="top">
                    <div class="el-slider__bar" :style="{width: `${getRangePercent(range)}%`, left: `${getRangeMinPercent(range)}%`}"></div>
                  </el-tooltip>
                  <el-tooltip class="item" effect="dark" :content="`${range[0]}`" placement="top-start" :manual="true"
                              :value="showTooltip(true, rangeIndex)"
                  >
                    <div tabindex="0" class="el-slider__button-wrapper" :style="{left: `${getRangeMinPercent(range)}%`}"
                         @mousedown="sliderButtonMouseDown(rangeIndex, true, $event)">
                        <div class="el-tooltip el-slider__button" tabindex="0"></div>
                    </div>
                  </el-tooltip>
                  <el-tooltip class="item" effect="dark" :content="`${range[1]}`" placement="top-start" :manual="true"
                              :value="showTooltip(false, rangeIndex)" :offset="currentRangeX - currentRangeXStart"
                  >
                    <div tabindex="0" class="el-slider__button-wrapper" :style="{left: `${getRangeMaxPercent(range)}%`}"
                         @mousedown="sliderButtonMouseDown(rangeIndex, false, $event)">
                        <div class="el-tooltip el-slider__button" tabindex="0"></div>
                    </div>
                  </el-tooltip>
                </template>
                <div>
                  <div v-for="mark in rangeMarks" class="el-slider__stop el-slider__marks-stop"
                       :style="{left: `${getPercent(mark.value)}%`}"></div>
                </div>
                <div class="el-slider__marks">
                  <div v-for="(mark, markIndex) in rangeMarks" :key="mark.value" class="el-slider__marks-text"
                       :style="{width: `${rangeMarks.length == (markIndex + 1) ? '30px' : '' }`, left: `${getPercent(mark.value)}%`}">
                    {{ mark.label }}
                  </div>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="4">
            <el-dropdown @command="createRange">
              <span class="el-dropdown-link">
                <span style="margin-top: 12px; margin-left: 12px" class="text-success">添加</span>
                <i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-for="(option, optionIndex) in rangeCreateOptions"
                                  :key="optionIndex" :command="optionIndex" icon="el-icon-plus"
                >{{ option.label }}</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <span v-if="ranges.length > 0" style="margin-top: 12px; margin-left: 12px" class="text-danger" @click="removeRange">删除</span>
          </el-col>
        </el-row>
      </div>
      <template v-if="tip">
        <rva-form-tip :tip="tip"/>
      </template>
    </el-form-item>
  </div>
</template>

<script>

import formInput from "@/api/rva/form-input";
import {RvaUtils} from "@/api/rva/util";

export default {
  mixins: [formInput],
  props: ['marks', 'createOptions'],
  data() {
    return {
      max: 100,
      min: 0,
      step: 1,
      currentRangeIndex: -1,
      currentRangeMin: true,
      currentRangeX: -1,
      currentRangeXStart: 0,
      ranges: [],
      rangeMarks: [],
      rangeCreateOptions: [],
      showMaxTooltip: false,
      showMinTooltip: false
    }
  },
  methods: {
    afterCreated() {
      this.max = RvaUtils.parseInt(this.prop.formValueMax, 100);
      this.min = RvaUtils.parseInt(this.prop.formValueMin, 0);
      // this.$emit("input", [[50, 100], [150, 200]]);
      if (RvaUtils.isNotEmpty(this.value)) {
        this.ranges = JSON.parse(this.value);
      }
      // 初始化rangeMarks，如果用户没有指定，则自行构造
      if (this.marks && this.marks.length > 0) {
        this.rangeMarks = RvaUtils.cloneDeep(this.marks);
      } else {
        let num = Math.round((this.max - this.min) / this.step);
        this.rangeMarks = [];
        let segs = 4;
        let segNum = Math.round(num / segs);
        console.log('num, segNum', num, segNum)
        for (let i = 0; i < segs; i++) {
          let val = this.min + i * segNum * this.step;
          this.rangeMarks.push({value: val, label: `${val}`});
        }
        this.rangeMarks.push({value: this.max, label: `${this.max}`});
      }
      // 初始化rangeCreateOptions，如果用户没有指定，则自行构造
      if (this.createOptions && this.createOptions.length > 0) {
        this.rangeCreateOptions = RvaUtils.cloneDeep(this.createOptions);
      } else {
        this.rangeCreateOptions = [];
        for (let i = 0; i < this.rangeMarks.length - 1; i++) {
          let range = [this.rangeMarks[i].value, this.rangeMarks[i + 1].value];
          this.rangeCreateOptions.push({range, label: JSON.stringify(range)});
        }
      }
    },
    getRangeMinPercent(range) {
      return this.getPercent(range[0]);
    },
    getRangeMaxPercent(range) {
      return this.getPercent(range[1]);
    },
    getRangePercent(range) {
      return this.getPercent(range[1] - range[0]);
    },
    getPercent(value) {
      return value / (this.max - this.min) * 100;
    },
    sliderButtonMouseDown(rangeIndex, min, e) {
      this.currentRangeIndex = rangeIndex;
      this.currentRangeMin = min;
      this.currentRangeX = e.screenX;
      this.currentRangeXStart = e.screenX;
      console.log('sliderButtonMouseDown', e)
    },
    sliderMouseMove(e) {
      if (this.currentRangeIndex == -1) {
        return;
      }
      let range = this.ranges[this.currentRangeIndex];
      let offsetValue = (e.screenX - this.currentRangeX) / this.$refs.sliderRunway.clientWidth * (this.max - this.min)
      offsetValue = Math.ceil(offsetValue / this.step) * this.step;
      if (this.currentRangeMin) {
        range[0] += offsetValue;
      } else {
        range[1] += offsetValue;
      }
      if (range[0] > range[1]) {
        let tmp = range[0];
        range[0] = range[1]
        range[1] = tmp;
        this.currentRangeMin = !this.currentRangeMin
      }
      if (range[0] < this.min) {
        range[0] = this.min;
      }
      if (range[1] > this.max) {
        range[1] = this.max;
      }
      this.currentRangeX = e.screenX
      // 触发重画slider
      this.ranges.push([])
      this.ranges.pop()
      console.log('sliderMouseMove', this.currentRangeX - this.currentRangeXStart)
    },
    sliderMouseUp(e) {
      this.sliderMouseOut(e)
    },
    sliderMouseDown(e) {
      console.log('sliderMouseDown', e);
    },
    sliderMouseOut(e) {
      this.currentRangeIndex = -1
      this.currentRangeX = -1
      this.currentRangeXStart = 0;
      this.$emit("input", JSON.stringify(this.ranges))
      console.log('sliderMouseOut...', e.target.className)
    },
    createRange(optionIndex) {
      let option = this.rangeCreateOptions[optionIndex];
      for (let i = 0; i < this.ranges.length; i++) {
        let range = this.ranges[i]
        if (range[0] == option.range[0] && range[1] == option.range[1]) {
          this.$message.warning('该区间已经存在！');
          return;
        }
      }
      this.ranges.push(RvaUtils.cloneDeep(option.range));
    },
    removeRange() {
      this.$confirm('本操作将删除最后添加的区间，并且不可恢复，请再次确认！').then(() => {
        this.ranges.pop();
      }).catch(() => {});
    },
    showTooltip(min, rangeIndex) {
      return this.currentRangeIndex == rangeIndex && this.currentRangeMin == min
    }
  }
};
</script>
<style scoped lang="scss">
.rva-form-ranges {
  height: 65px;
}
</style>
