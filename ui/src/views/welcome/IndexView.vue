<script setup>
import {onMounted, ref} from "vue";
import {useStore} from "@/store/index.js";
import {getReaderService} from "@/net/index.js";
import {
  Avatar,
  Back,
  ChatLineSquare,
  Collection,
  Files,
  Key,
  Management,
  Notebook,
  User
} from "@element-plus/icons-vue";
import router from "@/router/index.js";


const store = useStore()
onMounted(async () => {
  try {
    const readerName = localStorage.getItem("readerName")
    const response = await getReaderService(readerName);
    store.reader = response.data;
    console.log(store.reader);


  } catch (error) {
    console.error('Error fetching user data:', error);
  }
});

const logout = ()=>{
  console.log('logout');
  localStorage.removeItem("readerName");
  router.push('/');
}

const goToMyBook = () => {
  console.log('Clicked on "我借的图书"');
  router.push({ name: 'my-book' });
};

const readerPhoto = store.reader.photo;

</script>

<template>
  <div class = "main-content"  element-loading-text="正在加载数据，请稍等" style="background-color: dimgray;">
    <el-container style="height: 100%; background-color: dimgray;" >
      <el-header class="main-content-header">
        <div style="flex: 1" class="user-info">
          <div  class="profile">
            <div>{{store.reader.name}}</div>
            <div>{{store.reader.email}}</div>
          </div>
          <el-dropdown>
            
            <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" style="background-color: dimgray;"/>
            
             <template #dropdown>
              <el-dropdown-item @click="logout()" style="background-color: dimgray;">
                <el-icon ><Back /></el-icon>
                退出登录
              </el-dropdown-item>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-scrollbar style="height: calc(100vh - 55px)">
            <el-menu
                router
                :default-active="$route.path"
                style="height: calc(100vh - 55px); background-color: dimgray;">
              <el-sub-menu index="1">
                <template #title style="background-color: dimgray;">
                  <el-icon style="background-color: dimgray;"><User /></el-icon>
                  <span><b>用户</b></span>
                </template>
                <el-menu-item index="/index/user" style="background-color: dimgray;">
                  <el-icon><Avatar /></el-icon>
                  <template #title >
                    用户信息
                  </template>
                </el-menu-item>
                <el-menu-item @click="goToMyBook()"  index="/index/my-book" style="background-color: dimgray;">
                  <el-icon><Files /></el-icon>
                  <template #title>
                    我借的图书
                  </template>
                </el-menu-item>
              </el-sub-menu>
              <el-sub-menu index="2">
                <template #title>
                  <el-icon><Notebook /></el-icon>
                  <span><b>图书</b></span>
                </template>
                <el-menu-item index="/index/book" style="background-color: dimgray;">
                  <el-icon><Management /></el-icon>
                  <template #title>
                    图书大全
                  </template>
                </el-menu-item>
              </el-sub-menu>
              <el-sub-menu index="3">
                <template #title>
                  <el-icon><Key /></el-icon>
                  <span><b>管理界面</b></span>
                </template>
                <el-menu-item index="/index/management" style="background-color: dimgray;">
                  <el-icon><Avatar /></el-icon>
                  <template #title>
                    人员管理
                  </template>
                </el-menu-item>
              </el-sub-menu>
            </el-menu>
          </el-scrollbar>
        </el-aside>
        <el-main class="main-content-page" style="background-color: dimgray;">
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.main-content-page{
  padding: 0;
  background-color: #f7f8fa;
}

.main-content{
  height: 100vh;
  width: 100vw;
}

.main-content-header{
  border-bottom: solid 1px ;
  height: 55px;
  display: flex;
  align-items: center;
  box-sizing: border-box;
}

.logo{
  height: 32px;
}

.user-info{
  display: flex;
  justify-content: flex-end;
  align-items: center;


  .el-avatar:hover{
    cursor: pointer;
  }

}

.profile{
  text-align: right;
  margin-right: 20px;

  :first-child{
    font-size: 18px;
    font-weight: bold;
    line-height: 20px;
  }

  :last-child{
    font-size: 10px;
    color: grey;
  }
}
</style>