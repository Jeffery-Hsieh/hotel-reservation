<template>
  <div id="app">
    <router-view name="nav"></router-view>
    <router-view></router-view>
    <router-view name='detail'></router-view>
    <router-view name='detail_1'></router-view>
  </div>
</template>

<script>
  export default {
    name: 'app',
  }
</script>
<style>
/* @import url('https://fonts.googleapis.com/css?family=Noto+Sans+TC:400,700&display=swap'); */
@import url('https://fonts.googleapis.com/css?family=Noto+Serif+TC:400,700&display=swap');
* {
  /* font-family: 'Noto Sans TC', sans-serif; */
  font-family: 'Noto Serif TC', serif;
  /* border: solid 1px #000; */
}
html, body {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
}
#app {
  width: 100%;
  height: 100%;
}
</style>
