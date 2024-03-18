<script setup>
import {reactive, ref} from "vue";
import {Lock, User} from "@element-plus/icons-vue";


const isRegister = ref(false)

const registerData = ref({
  name: '',
  password: ''
})

const rules = {
  name: [
    {required:true, message: '请输入用户名',trigger:'blur'}
  ],
  password: [
    {required:true ,message:'请输入密码',trigger:'blur'}
  ]
}

//注册函数
import {registerService} from '@/net/index.js'
import {ElMessage} from "element-plus";
const register = async ()=>{
  let result = await registerService(registerData.value);
  if(result.code == 200){
    //成功
    ElMessage.success('注册成功');
    console.log(registerData.value.name);
    console.log(registerData.value.password);
  }else {
    ElMessage.error('注册失败');
  }
}

//登录函数
import {loginService} from "@/net/index.js";
import router from "@/router/index.js";

const login = async ()=>{
  let res = await loginService(registerData.value);
  if(res.code == 200){
    ElMessage('登陆成功');
    localStorage.setItem("readerName",registerData.value.name);
    await router.push('/index');
  }else {
    ElMessage.error('登陆失败');

  }
}
</script>

<template>
  <div style="text-align: center;margin: 0 20px; background-color: dimgray;">
    <div style="margin-top: 150px">
      <div style="font-size: 25px;font-weight: bold">登录</div>
    </div>
    <div style="margin-top: 50px">
      <el-form :model="registerData" :rules="rules"   ref="form">
        <el-form-item prop="name">
          <el-input v-model="registerData.name" maxlength="10" type="text" placeholder="读者姓名">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="registerData.password" type="password" maxlength="15" placeholder="密码">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
    </div>
    <div style="margin-top: 40px">
      <el-button style="width: 270px" type="success" @click="login()">立即登录</el-button>
    </div>
    <el-divider>
      <span style="font-size: 13px;color: grey">没有账号</span>
    </el-divider>
    <div>
      <el-button style="width: 270px" type="warning" plain @click="register()">立即注册</el-button>
    </div>
  </div>
</template>

<style scoped>

</style>