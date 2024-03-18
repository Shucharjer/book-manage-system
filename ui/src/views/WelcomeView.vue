<script setup>
// Your script setup code
</script>

<template>
  <div class="centered-container" style="background-color: dimgray;">
    <el-row class="login-page">
      <el-col :span="12" class="bg"></el-col>
      <router-view/>
      <el-col :span="9" :offset="3" class="form">
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.centered-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  

}

.login-page {
  position: relative;
  height: 100%; /* Ensure the container takes the full height */
  background-color: dimgray;


  /* Overlay to make the image lighter */

}

/* ... rest of your styles ... */
</style>
