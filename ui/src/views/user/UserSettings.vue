<script setup>
import Card from "@/components/Card.vue";
import {Lock, Message, Refresh, Select, User} from "@element-plus/icons-vue";
import {useStore} from "@/store/index.js";
import {onMounted, reactive, ref, toRefs} from "vue";
import {changePasswordService, updateReaderBaseFormService} from "@/net/index.js";
import {ElMessage} from "element-plus";

const store = useStore()


const baseFormRef = ref()
const emailFormRef = ref()
const passwordRef = ref()


const baseForm = reactive({
  id: 0,
  name: '',
  sex: '',
  type: 0,
  dept: '',
  phone: '',
  email: '',
  registerTime:'',
  photo:'',
  status:'',
  borrowNum: 0,
  role: 0,
})

const passwordForm = ref({
  readerName: '',
  oldPassword: '',
  newPassword: ''
});

const passwordRules = {
  password:[
    {min:1,message:'输入的密码不能为空',trigger:['blur','change']}
  ]
}

const emailForm = reactive({
  email:''
})

const rules = {
  name: [
    {min:1 ,max:20,message:'用户名长度需要在1-20之间',trigger: ['blur','change']}
  ],
  email:[
    {required: true,message:'请输入邮件地址',trigger:'blur'},
    {type:'email',message: '请输入合法的电子邮件地址',trigger: ['blur','change']}
  ]
}

//将提前加载好的用户信息保存到表单里
onMounted(() =>{
  baseForm.id = store.reader.id;
  baseForm.name = store.reader.name;
  baseForm.sex = store.reader.sex;
  baseForm.type = store.reader.type;
  baseForm.dept = store.reader.dept;
  baseForm.phone = store.reader.phone;
  baseForm.email = store.reader.email;
  baseForm.registerTime = store.reader.registerTime;
  baseForm.photo = store.reader.photo;
  baseForm.status = store.reader.status;
  baseForm.borrowNum = store.reader.borrowNum;
  baseForm.role = store.reader.role;
  emailForm.email = store.reader.email;

  console.log(store.reader);
  console.log(baseForm);
})

//保存读者基本信息
const updateBaseForm = () => {
  console.log(baseForm);
  updateReaderBaseFormService(baseForm)
      .then((res) => {
        if (res.code === 200) {
          ElMessage.success('用户基本信息更改成功');
        } else {
          ElMessage.error('用户基本信息修改失败');
        }
      })
      .catch((error) => {
        console.error('请求失败：', error);
      });
}


//保存邮箱
const updateEmailForm = ()=>{
  baseForm.email = emailForm.email;
  updateReaderBaseFormService(baseForm)
      .then((res) => {
        if (res.code === 200) {
          ElMessage.success('用户基本信息更改成功');
        } else {
          ElMessage.error('用户基本信息修改失败');
        }
      })
      .catch((error) => {
        console.error('请求失败：', error);
      });
}

const updateReaderPassword = () => {
  passwordForm.value.readerName = store.reader.name;
  const formData = passwordForm.value; // 获取原始值
  console.log(formData);

  changePasswordService(formData).then((res) => {
    if (res.code === 200) {
      ElMessage.success('密码修改成功');
    } else if (res.code === -1) {
      ElMessage.error('原密码输入错误');
    } else {
      ElMessage.error('发生未知错误');
    }
  });
};

</script>

<template>
  <div style="display: flex; background-color: dimgray;">
    <div class="settings-left">
      <card :icon="User" title="账号信息设置" desc="在这里编辑您的个人信息" style="background-color:slategrey;">
        <el-form :model="baseForm" :rules="rules" ref="baseFormRef" label-position="top" style="margin:0 10px 10px 10px">
          <el-form-item label="读者姓名" prop="name">
            <el-input   v-model="baseForm.name" />
          </el-form-item>
          <el-form-item label="性别">
            <div>{{baseForm.sex}}</div>
          </el-form-item>
          <el-form-item label="单位名称" prop="dept">
            <el-input v-model="baseForm.dept" />
          </el-form-item>
          <el-form-item label="电话号码" prop="phone">
            <el-input v-model="baseForm.phone" maxlength="11"/>
          </el-form-item>
          <el-form-item label="读者照片(url地址)" prop="photo">
            <el-input v-model="baseForm.photo" />
          </el-form-item>
          <el-form-item label="已借书数量" prop="borrowNum">
            <el-input v-model="baseForm.borrowNum" disabled/>
          </el-form-item>
          <div>
            <el-button :icon="Select" type="success" @click="updateBaseForm()">保存用户信息</el-button>
          </div>
        </el-form>
      </card>
      <card style="margin-top: 10px; background-color: slategray;" :icon="Message" title="电子邮件设置" desc="您可以在这里修改电子邮件">
        <el-form :model="emailForm" :rules="rules" ref="emailFormRef"  label-position="top" style="margin: 0 10px 10px 10px">
          <el-form-item label="电子邮件" prop="email">
            <el-input v-model="emailForm.email" />
          </el-form-item>
          <el-form-item>
            <el-row style="width: 100%" :gutter="10">
              <div>
                <el-button :icon="Refresh" type="success" @click="updateEmailForm()">保存</el-button>
              </div>
              <el-col :span="18" style="color: grey">
                请确保电子邮件填写正确!
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </card>
      <card style="margin-top: 10px; background-color: slategray;" :icon="Lock" title="修改密码" desc="您可以在这里修改密码">
        <el-form :model="passwordForm.value" :rules="passwordRules" ref="passwordRef" label-position="top" style="margin: 0 10px 10px 10px">
          <el-form-item label="旧密码" prop="oldPassword">
            <el-input v-model="passwordForm.oldPassword" />
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="passwordForm.newPassword" />
          </el-form-item>
          <el-form-item>
            <el-row style="width: 100%" :gutter="10">
              <div>
                <el-button :icon="Refresh" type="success" @click="updateReaderPassword()">保存</el-button>
              </div>
              <el-col :span="18" style="color: grey">
                请确保密码填写正确!
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </card>
    </div>
    <div class="settings-right">
      <div style="position: sticky;top: 20px">
        <card style="background-color: slategray;">
          <div style="text-align: center;padding: 5px 15px 0 15px">
            <div>
              <el-avatar :size="70" fit="cover"
                         src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
              <div style="font-weight: bold; color: aqua;">你好,{{store.reader.name}}!</div>
            </div>
            <el-divider style="margin: 10px 0"/>
            <div style="font-size: 14px;color: azure;padding:10px">
              欢迎来到图书管理系统
            </div>
          </div>
        </card>
        <card style="margin-top: 10px;font-size: 14px; background-color: slategray;">
          <div style="color: azure;">借书证注册时间：{{store.reader.registerTime}}</div>
          <div style="color: aqua;text-align: center">学生证默认有效期为1年，请注意有效期</div>
        </card>
      </div>

    </div>
  </div>
</template>

<style scoped>
.settings-left{
  flex: 1;
  margin: 20px;
}

.settings-right{
  width: 300px;
  margin: 20px 30px 20px 0px;
}
</style>