<script setup>
import { useStore } from "@/store/index.js";
import {continueToLendService, getMyBooksService, returnBookService} from "@/net/index.js";
import { ref, onMounted } from "vue";
import {ElMessage} from "element-plus";

const store = useStore();

// 为表格定义响应式数据
const tableData = ref([]);

// 获取已借书籍信息
const getMyBook = () => {
  console.log(store.reader.name);
  getMyBooksService(store.reader.name).then((res) => {
    tableData.value = res.data;
  });
};

onMounted(() => {
  // 当组件挂载时加载书籍信息
  getMyBook();
});

const dialogVisible = ref(false)

//续借函数
let bookId;
const continueToLend = ()=>{
  continueToLendService(bookId).then((res)=>{
    if(res.code === 200){
      ElMessage.success('成功续借');
      waitSync(1000);
      location.reload();
    }else if(res.code === 500){
      ElMessage.error('您没有续借次数了');
    }else {
      ElMessage.error('反生未知错误');
    }
  })
}

//续借功能按钮
const ContinueLendButton = (row)=>{
  bookId = row.bookId;

  dialogVisible.value = true;
}

const handleContinueLend = ()=>{
  console.log(bookId);
  continueToLend();
  dialogVisible.value = false;
}

const returnBook = (row)=>{
  let readerName = store.reader.name;
  let bookId = row.bookId;
  let operatorReturnName = '小吉';
  returnBookService(readerName,bookId,operatorReturnName).then((res) =>{
    if(res.data.mes === '没有逾期'){
      ElMessage.success('没有逾期，没有罚金');
    }else if(res.data.mes === '逾期了'){
      ElMessage.error('逾期了，有罚金：'+res.data.punishMoney);
    }
  })
}

const waitSync = (milliseconds) => {
  var start = new Date().getTime();
  var end = 0;
  while ((end - start) < milliseconds) {
    end = new Date().getTime();
  }
}
</script>

<template>
  <div class="table-container" style="background-color: dimgray;">
    <el-table :data="tableData" style="width: 100%; background-color: slategray;" :header-cell-style="{background:'slategray', color:'white'}">
      <el-table-column prop="dateBorrow" label="借书日期" width="180"/>
      <el-table-column prop="preReturnDay" label="应还日期" width="180" />
      <el-table-column prop="bookId" label="书本id" width="180"/>
      <el-table-column prop="bookName" label="书本名字" width="180"/>
      <el-table-column fixed="right" label="操作" width="120">
        <template #default="scope">
          <el-button
              link
              type="primary"
              size="small"
              @click.prevent="ContinueLendButton(scope.row)"
          >
            续借
          </el-button>
          <el-button
            link
            type="primary"
            size="small"
            @click.prevent="returnBook(scope.row)"
          >
            还书
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

<!--  续借对话框-->
  <el-dialog
      v-model="dialogVisible"
      title="续借对话框"
      width="30%"
      :before-close="handleClose"
  >
    <span>确定续借？</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleContinueLend()">
          确认
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.table-container {
  margin: 20px; /* 上下左右都添加10px的间距 */
}
</style>
