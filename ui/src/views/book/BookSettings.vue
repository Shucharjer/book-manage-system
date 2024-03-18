<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { getAllBooksService ,getBooksByNameService,updateBookService,deleteBookService,addBookService,
  lendBookService,getAllBookOperatorsService} from "@/net/index.js";
import { ElMessage, ElMessageBox } from "element-plus";
import {Search, Timer} from "@element-plus/icons-vue";
import {useStore} from "@/store/index.js";


const store = useStore()

const bookData = reactive({
  id: 0,
  code: "",
  bookName: "",
  author: "",
  publisher: "",
  publishDay: "",
  isbnId: "",
  categoryId: "",
  languageId: 0,
  pages: 0,
  price: 0,
  dateIn: "",
  brief: "",
  bookCover: "",
  status: "",
});

const tableData = reactive([]);


// 将语言代号转换为实际语言
const mapLanguageIdToText = (languageId) => {
  switch (languageId) {
    case 0:
      return "中文";
    case 1:
      return "英文";
    case 2:
      return "日文";
    case 3:
      return "俄文";
    case 4:
      return "德文";
    case 5:
      return "法文";
    default:
      return "未知";
  }
};

//获取全部图书
const getAllBooks = () => {
  getAllBooksService().then((res) => {
    if (res.code === 200) {
      console.log(res.data);
      // 将查询到的图书数据存储到tableData
      tableData.splice(0, tableData.length, ...res.data);
    } else {
      ElMessage.error("查询图书失败");
    }
  });
};

//修改图书信息
const updateBook = () =>{
  updateBookService(editform).then((res) =>{
    if(res.code === 200){
      ElMessage.success('修改成功');
    }else{
      ElMessage.error('出现了错误');
    }
  })
};

onMounted(() => {
  getAllBooks();
  getAllBookOperators();
});

const dialogVisible_Edit = ref(false);

// 定义一个用于编辑的表单数据
const editform = reactive({});

//编辑图书确认
const handleEdit = (row) => {
  //由图书序号名称查询图书信息
  getBooksByNameService(row.bookName).then((res) =>{
    if(res.code === 200){
      // 打开编辑弹窗时，将当前行的数据赋值给 editform
      editform.id = res.data.id;
      editform.code = res.data.code;
      editform.bookName = res.data.bookName;
      editform.author = res.data.author;
      editform.publisher = res.data.publisher;
      editform.publishDay = res.data.publishDay;
      editform.isbnId = res.data.isbnId;
      editform.categoryId = res.data.categoryId;
      editform.languageId = res.data.languageId;
      editform.pages = res.data.pages;
      editform.price = res.data.price;
      editform.dateIn = res.data.dateIn;
      editform.brief = res.data.brief;
      //TODO 图书封面链接待解决
      editform.bookCover = res.data.bookCover;
      editform.status = res.data.status;

      dialogVisible_Edit.value = true;
    }else{
      ElMessage.error('查询错误....')
    }
  })

};

const handleClose = (done: () => void) => {
  ElMessageBox.confirm("你确定关闭弹窗?")
      .then(() => {
        done();
      })
      .catch(() => {
        // catch error
      });
};

const dialogVisible_Delete = ref(false);

//编辑图书弹窗确认
const dialogVisible_Edit_Confirm = () =>{
  dialogVisible_Edit.value = false;
  updateBook();
  location.reload();
};


let bookIdToDelete; // 声明一个变量来存储要删除的 bookId

const handleDelete = (row) => {
  // 在单击删除按钮时设置 bookIdToDelete
  bookIdToDelete = row.id;
  dialogVisible_Delete.value = true;
}

const confirmDelete = () => {
  // 在 deleteBookService 函数中使用 bookIdToDelete
  deleteBookService(bookIdToDelete)
      .then((res) => {
        if (res.code === 200) {
          ElMessage.success('删除成功');
          getAllBooks(); // 在成功删除后刷新书籍列表
        } else {
          ElMessage.error('删除失败');
        }
      })
      .finally(() => {
        dialogVisible_Delete.value = false;
      });
}

const inputBookName = ref('')
const findBookByName = () => {

  getBooksByNameService(inputBookName.value)
      .then((res) => {
        //用户是否在输入框输入东西了
        if(inputBookName.value ===''){
          getAllBooks();
        }
        else {
          if (res.data !== null) {
            ElMessage.success('查询成功');
            // 将查询到的图书信息存储到新对象中
            const foundBook = {
              id: res.data.id,
              code: res.data.code,
              bookName: res.data.bookName,
              author: res.data.author,
              publisher: res.data.publisher,
              publishDay: res.data.publishDay,
              isbnId: res.data.isbnId,
              categoryId: res.data.categoryId,
              languageId: res.data.languageId,
              pages: res.data.pages,
              price: res.data.price,
              dateIn: res.data.dateIn,
              brief: res.data.brief,
              bookCover: res.data.bookCover,
              status: res.data.status
            };
            // 清空原有数据并将新对象添加到 tableData 数组中
            tableData.splice(0, tableData.length, foundBook);
          } else {
            ElMessage.error('不存在该书籍');
          }
        }
      })
      .catch((error) => {
        console.error(error);
        ElMessage.error('查询出错');
      });
};

//增加图书
const dialogVisible_Add = ref(false)
const addForm = reactive({})
const handleAdd = ()=>{
  console.log('123')
  console.log(addForm);
  //将添加图书的表单数据发送到后端
  addBookService(addForm).then((res) =>{
    if(res.code === 200){
      ElMessage.success('添加图书成功');
      //重置表单
      Object.keys(addForm).forEach((key) => {
        addForm[key] = '';
      location.reload();
      });
    }else{
      ElMessage.error('添加图书失败');
    }
  });

  dialogVisible_Add.value = false;
}

const bookOperatorsData = reactive({
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

const bookOperators = ref([]);

//查询所有图书管理员
const getAllBookOperators = () => {
  getAllBookOperatorsService().then((res) => {
    bookOperators.value = res.data.map((operator) => operator.name);
  });
};

//读者借书
const dialogVisible_LendBook = ref(false)
let bookId;
const bookOperatorName = ref('')
const LendBook = (row) =>{
  if(row.status !== '在馆'){
    ElMessage.error('书本已借出');
  }else {
    dialogVisible_LendBook.value = true;
    bookId = row.id;
  }

}

const handleLendBook = () => {
  console.log(store.reader.name, bookId,bookOperatorName.value)
  // 使用所选图书数据和操作员数据执行借书操作
  lendBookService(store.reader.name, bookId,bookOperatorName.value)
      .then((res) => {
        if (res.code === 200) {
          ElMessage.success('借书成功');
          // 您可能需要刷新图书列表或执行其他操作
        } else {
          ElMessage.error('借书失败');
        }
      })
      .finally(() => {
        // 关闭借书弹窗并刷新
        dialogVisible_LendBook.value = false;
        location.reload();
      });
};

const waitSync = (milliseconds) => {
  var start = new Date().getTime();
  var end = 0;
  while ((end - start) < milliseconds) {
    end = new Date().getTime();
  }
}



</script>

<template>
  <div style="margin-top: 50px">
    <div  style="margin: 20px 920px 20px 20px;display: flex;align-items: center">
      <el-input
          v-model="inputBookName"
          placeholder="请输入图书名"
          class="input-with-select"
          maxlength="10"
          style="flex: 1; margin-right: 30px"

      >
        <template #prepend>
          <el-button :icon="Search" @click="findBookByName()"/>
        </template>
      </el-input>
      <el-button type="primary" @click="dialogVisible_Add = true" v-if="store.reader.role !== 0">增加图书</el-button>

    </div>

  </div>
  <div style="margin-top: 20px">
    <el-scrollbar>
      <el-table :data="tableData" :cell-style="{background:'slategray'}" :header-cell-style="{background:'slategray', color:'white'}" style="background-color: slategray;">
        <el-table-column prop="id" label="图书序号" width="140" />
        <el-table-column label="书名" width="180" style="text-align: center">
          <template #default="scope">
            <el-popover effect="light" trigger="hover" placement="top" width="auto">
              <template #default>
                <div>
                  <el-image style="width: 100px; height: 100px"
                            :src="scope.row.bookCover"
                  ></el-image>
                </div>
                <div>id: {{ scope.row.id }}</div>
                <div>ISBN书号: {{scope.row.code}}</div>
                <div>分类号：{{scope.row.categoryId}}</div>
                <div>页数：{{scope.row.pages}}</div>
                <div>价格：{{scope.row.price}}</div>
                <div>入馆时间:{{scope.row.dateIn}}</div>
                <div>简介:{{scope.row.brief}}</div>
              </template>
              <template #reference>
                <el-tag>{{ scope.row.bookName }}</el-tag>
              </template>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column prop="author" label="作者" width="180" />
        <el-table-column prop="publisher" label="出版社" width="200" />
        <el-table-column prop="languageId" label="语言" width="100">
          <template v-slot="{ row }">
            {{ mapLanguageIdToText(row.languageId) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="是否在馆" width="140" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button
                size="small"
                @click="handleEdit(scope.row)"
                v-if="store.reader.role !== 0"
            >编辑</el-button>
            <el-button
                size="small"
                type="danger"
                @click="handleDelete(scope.row)"
                v-if="store.reader.role !== 0"
            >删除</el-button>
            <el-button
                size="small"
                type="success"
                @click="LendBook(scope.row)"
            >借书</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>
  </div>

<!--  编辑框弹窗-->
  <el-dialog :cell-style="{background:'slategray'}" :header-cell-style="{background:'slategray', color:'white'}" style="background-color: slategray;"
      v-model="dialogVisible_Edit"
      title="书本编辑"
      width="30%"
      :before-close="handleClose_Edit"
  >
    <el-form :model="editform" label-width="120px">
      <el-form-item label="图书序号">
        <el-input v-model="editform.id" disabled />
      </el-form-item>
      <el-form-item label="图书条码号">
        <el-input v-model="editform.code" />
      </el-form-item>
      <el-form-item label="书名">
        <el-input v-model="editform.bookName" />
      </el-form-item>
      <el-form-item label="作者">
        <el-input v-model="editform.author" />
      </el-form-item>
      <el-form-item label="出版社">
        <el-input v-model="editform.publisher" />
      </el-form-item>
      <el-form-item label="出版日期">
        <el-input v-model="editform.publishDay" />
      </el-form-item>
      <el-form-item label="ISBN书号">
        <el-input v-model="editform.isbnId" />
      </el-form-item>
      <el-form-item label="分类号">
        <el-input v-model="editform.categoryId" />
      </el-form-item>
      <el-form-item label="语言">
        <el-input v-model="editform.languageId" />
      </el-form-item>
      <el-form-item label="页数">
        <el-input v-model="editform.pages" />
      </el-form-item>
      <el-form-item label="价格">
        <el-input v-model="editform.price" />
      </el-form-item>
      <el-form-item label="入馆日期">
        <el-input v-model="editform.dateIn" />
      </el-form-item>
      <el-form-item label="内容简介">
        <el-input v-model="editform.brief" />
      </el-form-item>
      <el-form-item label="图书封面照片">
        <el-input v-model="editform.bookCover" />
      </el-form-item>
      <el-form-item label="图书状态">
        <el-input v-model="editform.status" />
      </el-form-item>

    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible_Edit = false">取消</el-button>
        <el-button type="primary" @click="dialogVisible_Edit_Confirm()" >
          确认
        </el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 删除书本弹窗 -->
  <el-dialog v-model="dialogVisible_Delete" title="书本删除" width="30%" :before-close="handleClose" :cell-style="{background:'slategray'}" :header-cell-style="{background:'slategray', color:'white'}" style="background-color: slategray;">
    <span>确认要删除这本书吗？</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible_Delete = false">取消</el-button>
        <el-button type="primary" @click="confirmDelete()">确认删除</el-button>
      </span>
    </template>
  </el-dialog>

<!--增加图书弹窗-->
  <el-dialog
      v-model="dialogVisible_Add"
      title="添加图书"
      width="30%"
      :before-close="handleClose"
      :cell-style="{background:'slategray'}"
      :header-cell-style="{background:'slategray', color:'white'}"
      style="background-color: slategray;"
  >
    <div>
      <el-form :model="addForm" label-width="120px">
        <el-form-item label="图书序号">
          <el-input v-model="addForm.id" disabled />
        </el-form-item>
        <el-form-item label="图书条码号" required>
          <el-input v-model="addForm.code" />
        </el-form-item>
        <el-form-item label="书名" required>
          <el-input v-model="addForm.bookName" />
        </el-form-item>
        <el-form-item label="作者" required>
          <el-input v-model="addForm.author" />
        </el-form-item>
        <el-form-item label="出版社" required>
          <el-input v-model="addForm.publisher" />
        </el-form-item>
        <el-form-item label="出版日期">
          <el-input v-model="addForm.publishDay" />
        </el-form-item>
        <el-form-item label="ISBN书号" required>
          <el-input v-model="addForm.isbnId" />
        </el-form-item>
        <el-form-item label="分类号" required>
          <el-input v-model="addForm.categoryId" />
        </el-form-item>
        <el-form-item label="语言" required>
          <el-select v-model="addForm.languageId">
            <el-option label="中文" value="0"/>
            <el-option label="英文" value="1"/>
            <el-option label="日文" value="2"/>
            <el-option label="俄文" value="3"/>
            <el-option label="德文" value="4"/>
            <el-option label="法文" value="5"/>
          </el-select>
        </el-form-item>
        <el-form-item label="页数" required>
          <el-input v-model="addForm.pages" />
        </el-form-item>
        <el-form-item label="价格" required>
          <el-input v-model="addForm.price" />
        </el-form-item>
        <el-form-item label="入馆日期">
          <el-input v-model="addForm.dateIn" />
        </el-form-item>
        <el-form-item label="内容简介" required>
          <el-input v-model="addForm.brief" />
        </el-form-item>
        <el-form-item label="图书封面照片" required>
          <el-input v-model="addForm.bookCover" />
        </el-form-item>
        <el-form-item label="图书状态" required>
          <el-select v-model="addForm.status">
            <el-option label="在馆" value="在馆"/>
            <el-option label="借出" value="借出"/>
            <el-option label="遗失" value="遗失"/>
            <el-option label="变卖" value="变卖"/>
            <el-option label="销毁" value="销毁"/>
          </el-select>
        </el-form-item>

      </el-form>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible_Add = false">取消</el-button>
        <el-button type="primary" @click="handleAdd()">
          确认添加
        </el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 借书弹窗 -->
  <el-dialog 
      v-model="dialogVisible_LendBook"
      title="借书对话框"
      width="30%"
      :before-close="handleClose"
      :cell-style="{background:'slategray'}"
      :header-cell-style="{background:'slategray', color:'white'}"
      style="background-color: slategray;"
  >
    <div>
      <el-form-item label="选择图书管理员" required>
        <el-select v-model="bookOperatorName">
          <el-option
              v-for="operator in bookOperators"
              :key="operator"
              :label="operator"
              :value="operator"
          />
        </el-select>
      </el-form-item>
    </div>
    <template #footer>
    <span class="dialog-footer">
      <el-button @click="dialogVisible_LendBook = false">取消借书</el-button>
      <el-button type="primary" @click="handleLendBook()">
        确认借书
      </el-button>
    </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>