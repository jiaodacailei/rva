<template>
  <div style="height: 90%;width: 100%">
    <video ref="videoPlayer" class="video-js" :class="full?'vjs-big-play-centered vjs-fluid':''"></video>
  </div>
</template>

<script>
import videojs from 'video.js';

export default {
  name: 'VideoPlayer',
  props: {
    autoplay: {
      type: Boolean,
      default: true
    },
    controls: {
      type: Boolean,
      default: true
    },
    src: {
      type: String,
      default: ''
    },

    height: {
      type: Number,
      default: 200,
    },
    full: {
      type: Boolean,
      default: false
    },
    width: {
      type: Number,
      default: 200,
    }
  },
  data() {
    return {
      player: null
    }
  }
  ,
  mounted() {
    this.init()
  },
  methods: {
    init() {
      let videoOptions = {
        autoplay: this.autoplay,
        controls: this.controls,
        height: this.height,
        width: this.width,
        sources: [
          {
            src: this.src
          }
        ],
      }
      this.player = videojs(this.$refs.videoPlayer, videoOptions, () => {
        this.player.log('onPlayerReady', this);
      });
    }
  },
  beforeDestroy() {
    if (this.player) {
      this.player.dispose();
    }
  }
}
</script>
<style>
@import '~video.js/dist/video-js.css'
/*.video-js{*/
/*  width: 100%;*/
/*}*/

</style>
