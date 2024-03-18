import request from '@/utils/request.js'

//注册
export const registerService = (registerData) =>{
    return request.post('readers/addReader',registerData,{
        headers: {
            'Content-Type': 'application/json',
        },
    });
}
//登录
export const loginService = (registerData) =>{
    return request.post('readers/login',registerData,{
        headers: {
            'Content-Type': 'application/json',
        },
    });
}
//获取角色信息
export const getReaderService = (readerName) =>{
    return request.get('readers/findReaderByName',{
        params: {
            readerName: readerName
        }
    });
}

//保存用户基本信息
export const updateReaderBaseFormService = (baseForm) =>{
    return request.put('readers/updateReader',baseForm,{
        headers: {
            'Content-Type': 'application/json',
        },
    });
}

//查询所有图书信息
export const getAllBooksService = () =>{
    return request.get('books/selectAllBooks',{});
}

//通过图书名查询图书信息
export const getBooksByNameService = (bookName)=>{
    return request.get('books/findBookByName',{
        params:{
            bookName:bookName
        }
    })
}

//修改图书信息
export const updateBookService = (updateData) =>{
    return request.put('books/update',updateData,{
        headers:{
            'Content-Type': 'application/json',
        },
    });
}

//删除图书信息
export const deleteBookService = (id) =>{
    return request.delete('books/deleteById',{
        params:{
            id:id
        }
    })
}

//增加图书信息
export const addBookService = (addBookData)=>{
    return request.post('books/insertBook',addBookData,{
        headers:{
            'Content-Type': 'application/json',
        },
    });
}

// 读者借书操作
export const lendBookService = (readerName, bookId, operatorBorrowName) => {
    return request.post('readers/lendBook', null, {
        params: {
            readerName: readerName,
            bookId: bookId,
            operatorBorrowName: operatorBorrowName
        }
    });
};

//查询所有图书操作员
export const getAllBookOperatorsService = ()=>{
    return request.get('readers/getAllBookOperator')
}

//查询读者所借书籍
export const getMyBooksService = (readerName) =>{
    return request.get('readers/getMyBooks',{
        params:{
            readerName:readerName
        }
    })
}

//图书续借
export const continueToLendService = (bookId) =>{
    return request.put('borrow/updatePreReturnDay', null, {
        params: {
            bookId: bookId
        }
    });
}

//查询所有读者和图书管理员
export const getAllLowLeveReaderService = () =>{
    return request.get('readers/findLowLevelReader')
}

//删除角色
export const deleteReaderByIdService = (readerId) =>{
    return request.delete('readers/deleteReaderById/'+readerId)
}

//更新角色2
export const updateReader2Service = (addForm) =>{
    return request.put('readers/updateReader2',addForm,{
        headers:{
            'Content-Type': 'application/json',
        },
    });
}

//创建角色
export const addReaderService = (addFrom) =>{
    return request.post('readers/addReader',addFrom,{
        headers:{
            'Content-Type': 'application/json',
        }
    })
}

//改密码
export const changePasswordService = (passwordForm) =>{
    return request.post('readers/changePassword',passwordForm,{
        headers:{
            'Content-Type': 'application/json',
        }
    })
}

//还书
export const returnBookService = (readerName,bookId,operatorReturnName) =>{
    return request.post('readers/returnBook',null,{
        params:{
            readerName:readerName,
            bookId:bookId,
            operatorReturnName:operatorReturnName
        }
    })

}