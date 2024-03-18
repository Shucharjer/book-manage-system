<script setup>

import {Search} from "@element-plus/icons-vue";
import {
  addReaderService,
  deleteReaderByIdService,
  getAllLowLeveReaderService,
  getReaderService, updateReader2Service,
} from "@/net/index.js";
import {onMounted, ref} from "vue";
import {ElMessage} from "element-plus";
import {useStore} from "@/store/index.js";
import router from "@/router/index.js";

const store = useStore()

const tableData = ref([]);


//查询所有读者和图书管理员
const getAllLowLeveReader = () =>{
  getAllLowLeveReaderService().then((res) =>{
    tableData.value = res.data;
  })
}

const inputReaderName = ref('')
//查询角色信息
const findReaderByName = () => {
  console.log(inputReaderName.value);

  if(inputReaderName.value !== ''){
    getReaderService(inputReaderName.value).then((res) => {
      const dataArray = Array.isArray(res.data) ? res.data : [res.data];
      console.log(dataArray); // 输出数组结构
      tableData.value = dataArray;
    });
  }else {
    getAllLowLeveReader();
  }


};

const dialogVisible_Delete = ref(false)
let readerId;
const handleDelete = (row) =>{
  readerId = row.id;
  dialogVisible_Delete.value = true
}

const deleteReaderById = () =>{
  deleteReaderByIdService(readerId).then((res) =>{
    if(res.code === 200){
      ElMessage.success('删除成功');
    }else {
      ElMessage.error('删除失败');
    }
  })
}

const deleteReader = () =>{
  deleteReaderById();
  dialogVisible_Delete.value = false;
}

const dialogVisible_Edit = ref(false)

const addForm = ref({})

const handleEdit = (row)=>{
  //读者信息赋值到表单addForm里
  addForm.value = {
    id: row.id,
    name: row.name,
    sex: row.sex,
    type: row.type,
    dept: row.dept,
    phone: row.phone,
    email: row.email,
    registerTime: row.registerTime,
    photo: row.photo,
    status: row.status,
    borrowNum: row.borrowNum,
    password: row.password,
    role: row.role,
  };

  dialogVisible_Edit.value = true;
}
const updateReaderInfo = () =>{
  console.log(addForm.value);
  updateReader2Service(addForm.value).then((res) =>{
    if(res.code === 200){
      ElMessage.success('修改成功');
    }else{
      ElMessage.error('出现了问题');
    }
  })
  dialogVisible_Edit.value = false;
}

const dialogVisible_ADD = ref(false)

const addReaderForm = ref({})

const handleADD = ()=>{
  dialogVisible_ADD.value = true;
}

const addReader = () =>{
  addReaderService(addReaderForm.value).then((res) =>{
    if(res.code === 200){
      ElMessage.success('添加角色成功');
    }else {
      ElMessage.error('添加角色失败');
    }
  });

  dialogVisible_ADD.value = false;
}

onMounted(() =>{

  //查询读者权限，权限为管理员（2或以上）才能进入
  if(store.reader.role >= 2){
    getAllLowLeveReader();
  }else {
    ElMessage.error('没有权限')
    router.push('/index/user')
  }


})


</script>

<template>
  <div style="margin-top: 50px">
    <div  style="margin: 20px 920px 20px 20px;display: flex;align-items: center">
      <el-input
          v-model="inputReaderName"
          placeholder="请输入角色名"
          class="input-with-select"
          maxlength="10"
          style="flex: 1; margin-right: 30px"

      >
        <template #prepend>
          <el-button :icon="Search" @click="findReaderByName()"/>
        </template>
      </el-input>
      <el-button type="primary" @click="handleADD()">增加角色</el-button>

    </div>

  </div>
  <div style="margin-top: 20px">
    <el-scrollbar>
      <el-table :data="tableData" :cell-style="{background:'slategray'}" :header-cell-style="{background:'slategray', color:'white'}" style="background-color: slategray;">
        <el-table-column prop="id" label="读者编号" width="140" />
        <el-table-column label="姓名" width="180" style="text-align: center">
          <template #default="scope">
            <el-popover effect="light" trigger="hover" placement="top" width="auto">
              <template #default>
                <div>
                  <el-image style="width: 100px; height: 100px"
                            :src="scope.row.photo"
                  ></el-image>
                </div>
                <div>id: {{ scope.row.id }}</div>
                <div>姓名: {{scope.row.name}}</div>
                <div>性别：{{scope.row.sex}}</div>
                <div>读者类别：{{scope.row.type}}</div>
                <div>读者单位：{{scope.row.dept}}</div>
                <div>电话:{{scope.row.phone}}</div>
                <div>邮箱:{{scope.row.email}}</div>
                <div>注册时间:{{scope.row.registerTime}}</div>
                <div>借书证状态:{{scope.row.status}}</div>
                <div>借书数目:{{scope.row.borrowNum}}</div>
                <div>密码:{{scope.row.password}}</div>
              </template>
              <template #reference>
                <el-tag>{{scope.row.name}}</el-tag>
              </template>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="借书证状态" width="180" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button
                size="small"
                @click="handleEdit(scope.row)"
            >编辑信息</el-button>
            <el-button
                size="small"
                type="danger"
                @click="handleDelete(scope.row)"
            >删除角色</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>
  </div>

<!--  删除对话框-->
  <el-dialog :cell-style="{background:'slategray'}" :header-cell-style="{background:'slategray', color:'white'}" style="background-color: slategray;"
      v-model="dialogVisible_Delete"
      title="Tips"
      width="30%"
      :before-close="handleClose"
  >
    <span style="color: red">确认删除吗</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible_Delete = false">取消删除</el-button>
        <el-button type="primary" @click="deleteReader()">
          确认删除
        </el-button>
      </span>
    </template>
  </el-dialog>

<!--  编辑读者信息对话框-->
  <el-dialog :cell-style="{background:'slategray'}" :header-cell-style="{background:'slategray', color:'white'}" style="background-color: slategray;"
      v-model="dialogVisible_Edit"
      title="编辑对话框"
      width="30%"
      :before-close="handleClose"
  >
    <div>
      <el-form :model="addForm" label-width="120px">
        <el-form-item label="读者序号">
          <el-input v-model="addForm.id" disabled />
        </el-form-item>
        <el-form-item label="姓名" required>
          <el-input v-model="addForm.name" />
        </el-form-item>
        <el-form-item label="性别" required>
          <el-input v-model="addForm.sex" />
        </el-form-item>
        <el-form-item label="读者类别" required>
          <el-input v-model="addForm.type" />
        </el-form-item>
        <el-form-item label="读者单位" required>
          <el-input v-model="addForm.dept" />
        </el-form-item>
        <el-form-item label="读者电话">
          <el-input v-model="addForm.phone" />
        </el-form-item>
        <el-form-item label="邮箱" required>
          <el-input v-model="addForm.email" />
        </el-form-item>
        <el-form-item label="登记时间" required>
          <el-input v-model="addForm.registerTime" />
        </el-form-item>
        <el-form-item label="照片" required>
          <el-input v-model="addForm.photo" />
        </el-form-item>
        <el-form-item label="借书证状态" required>
          <el-input v-model="addForm.status" />
        </el-form-item>
        <el-form-item label="借书数量" required>
          <el-input v-model="addForm.borrowNum" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="addForm.password" />
        </el-form-item>
        <el-form-item label="角色" required>
          <el-input v-model="addForm.role" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible_Edit = false">取消编辑</el-button>
        <el-button type="primary" @click="updateReaderInfo()">
          确认编辑
        </el-button>
      </span>
    </template>
  </el-dialog>

<!--  添加角色对话框-->
  <el-dialog
      v-model="dialogVisible_ADD"
      title="添加角色对话框"
      width="30%"
      :before-close="handleClose"
  >
    <div>
      <el-form :model="addReaderForm" label-width="120px">
        <el-form-item label="读者序号">
          <el-input v-model="addReaderForm.id" disabled />
        </el-form-item>
        <el-form-item label="姓名" required>
          <el-input v-model="addReaderForm.name" />
        </el-form-item>
        <el-form-item label="性别" required>
          <el-input v-model="addReaderForm.sex" />
        </el-form-item>
        <el-form-item label="读者类别" required>
          <el-input v-model="addReaderForm.type" />
        </el-form-item>
        <el-form-item label="读者单位" required>
          <el-input v-model="addReaderForm.dept" />
        </el-form-item>
        <el-form-item label="读者电话" required>
          <el-input v-model="addReaderForm.phone" />
        </el-form-item>
        <el-form-item label="邮箱" required>
          <el-input v-model="addReaderForm.email" />
        </el-form-item>
        <el-form-item label="登记时间" >
          <el-input v-model="addReaderForm.registerTime" disabled/>
        </el-form-item>
        <el-form-item label="照片" required>
          <el-input v-model="addReaderForm.photo" />
        </el-form-item>
        <el-form-item label="借书证状态" required>
          <el-input v-model="addReaderForm.status" />
        </el-form-item>
        <el-form-item label="借书数量" >
          <el-input v-model="addReaderForm.borrowNum" />
        </el-form-item>
        <el-form-item label="密码" required>
          <el-input v-model="addReaderForm.password" />
        </el-form-item>
        <el-form-item label="角色" required>
          <el-input v-model="addReaderForm.role" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible_ADD = false">取消</el-button>
        <el-button type="primary" @click="addReader()">
          添加
        </el-button>
      </span>
    </template>
  </el-dialog>

</template>

<style scoped>

</style>