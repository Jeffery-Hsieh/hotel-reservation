<template>
  <div class="form-control">
    <button  @click.prevent="homepage">Home Page</button>
  </div>
</template>
<script>
  export default {
    methods: {
      homepage(){
        this.$router.push('/');
      }
    }
  }
</script>