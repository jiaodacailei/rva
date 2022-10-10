<template>
  <div class="chart" :id="id"></div>
</template>
<script>
import * as echarts from 'echarts';

export default {
  props: ["option"],
  data() {
    return {
      myChart: null,
    }
  },
  methods: {
    initCharts() {
      let {width, height} = this.option;
      const myChart = echarts.init(document.getElementById(this.id), null, {
        width,
        height: height? height: 300
      })
      myChart.setOption(this.option, true)
      this.myChart = myChart
    },
    getId() {
      let time = new Date().getTime()
      let num = Math.floor(Math.random() * 101)
      return time + "-" + num;
    }


  },
  beforeDestroy() {
    if (this.myChart) {
      this.myChart.clear()
    }
  },
  created() {
    this.id = this.getId();
  },
  mounted() {
    this.initCharts()
    window.addEventListener('resize', _ => {
      if (this.myChart) {
        this.myChart.resize()
      }
    })

  },
  watch: {
    option: {
      handler(n, o) {
        this.initCharts()
      }
    }
  }
}
</script>
<style>
.chart {
  width: 100%;
  height: 100%;
}
</style>
