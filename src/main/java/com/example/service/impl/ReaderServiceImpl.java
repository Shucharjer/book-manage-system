package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.BookMapper;
import com.example.mapper.BorrowMapper;
import com.example.mapper.ReaderMapper;
import com.example.mapper.ReaderTypeMapper;
import com.example.pojo.dto.Book;
import com.example.pojo.dto.Borrow;
import com.example.pojo.dto.Reader;
import com.example.pojo.dto.ReaderType;
import com.example.pojo.vo.BookVO;
import com.example.pojo.vo.PasswordVO;
import com.example.service.ReaderService;
import com.example.utils.Md5Util;
import com.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReaderServiceImpl extends ServiceImpl<ReaderMapper, Reader> implements ReaderService {

    @Autowired
    private ReaderMapper readerMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BorrowMapper borrowMapper;

    @Autowired
    private ReaderTypeMapper readerTypeMapper;

    @Override
    public Reader findReaderById(Integer id) {
        QueryWrapper<Reader> wrapper = new QueryWrapper<>();
        wrapper.eq("rdID",id);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public void deleteReaderById(Integer id) {
        LambdaQueryWrapper<Reader> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Reader::getId,id);
        readerMapper.delete(lambdaQueryWrapper);
    }

    @Override
    public void addReader(Reader reader) {
        //密码加密
        String md5String = Md5Util.getMD5String(reader.getPassword());
        reader.setPassword(md5String);
        readerMapper.insert(reader);
    }

    @Override
    public void updateReader(Reader reader) {
        LambdaUpdateWrapper<Reader> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper
                .eq(Reader::getId, reader.getId())
                .set(Reader::getName, reader.getName())
                .set(Reader::getSex, reader.getSex())
                .set(Reader::getType, reader.getType())
                .set(Reader::getDept, reader.getDept())
                .set(Reader::getPhone, reader.getPhone())
                .set(Reader::getEmail, reader.getEmail())
                .set(Reader::getPhoto, reader.getPhoto())
                .set(Reader::getStatus, reader.getStatus())
                .set(Reader::getBorrowNum, reader.getBorrowNum())
                .set(Reader::getRole, reader.getRole());

        readerMapper.update(null, lambdaUpdateWrapper);
    }

    @Override
    public boolean login(String readerName, String password) {
        //将密码转化为加密后的密码
        String md5Password = Md5Util.getMD5String(password);

        //账号密码比对
        LambdaQueryWrapper<Reader> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(Reader::getName,readerName)
                .eq(Reader::getPassword,md5Password);
        Reader reader = readerMapper.selectOne(lambdaQueryWrapper);

        return reader!=null;
    }

    @Override
    public void lendBook(String readerName,Integer bookId, String operatorBorrowName) {

        //reader.rdBorrowQty +1
        LambdaUpdateWrapper<Reader> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(Reader::getName,readerName)
                .setSql("rdBorrowQty = rdBorrowQty+1");

        readerMapper.update(null,updateWrapper);

        //book.bkStatus = ‘借出'
        LambdaUpdateWrapper<Book> updateWrapper1 = new LambdaUpdateWrapper<>();
        updateWrapper1
                .eq(Book::getId,bookId)
                .setSql("bkStatus = '借出'");

        bookMapper.update(null,updateWrapper1);

        //insert(tb_borrow)
        //读者序号
        LambdaQueryWrapper<Reader> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Reader::getName,readerName);
        Reader selectedReader = readerMapper.selectOne(lambdaQueryWrapper);
        Integer readerId = selectedReader.getId();

        //通过读者序号和图书序号去添加数据in tb_borrow
        borrowMapper.add(readerId,bookId,operatorBorrowName);


    }

    @Override
    public void returnBook(String readerName,Integer bookId, String operatorReturnName) {
        //判断是否逾期
        LambdaQueryWrapper<Borrow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Borrow::getBookId,bookId);
        Date preReturnDay = borrowMapper.selectOne(lambdaQueryWrapper).getPreReturnDay();
        Date current = new Date();

        if( (current.compareTo(preReturnDay) ) > 0){
            //逾期了
            //1.获取罚款率

            LambdaQueryWrapper<Reader> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(Reader::getName,readerName);
            Reader reader = readerMapper.selectOne(lambdaQueryWrapper1);
            Integer readerType = reader.getType();

            LambdaQueryWrapper<ReaderType> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper2.eq(ReaderType::getType,readerType);
            ReaderType readerType1 = readerTypeMapper.selectOne(lambdaQueryWrapper2);
            Double punishRate = readerType1.getPunishRate();

            //2.TB_Reader : 	rdBorrowQty = rdBorrowQty - 1  根据读者姓名
            LambdaUpdateWrapper<Reader> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper
                    .eq(Reader::getName,readerName)
                    .setSql("rdBorrowQty = rdBorrowQty - 1");
            readerMapper.update(null,lambdaUpdateWrapper);

            /**
             * 3.
             *             TB_Book：bkDateIn = now(),bkStatus = '在馆',
              */


            LambdaUpdateWrapper<Book> lambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper1
                    .eq(Book::getId,bookId)
                    .setSql("bkDateIn = now(),bkStatus = '在馆'");
            bookMapper.update(null,lambdaUpdateWrapper1);

            //4.修改tb_borrow
            //获取原先的数据
            LambdaQueryWrapper<Borrow> lambdaQueryWrapper3 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper3.eq(Borrow::getBookId,bookId);
            Borrow borrowDetails = borrowMapper.selectOne(lambdaQueryWrapper3);

            //计算超期天数
            Date preReturnDay1 = borrowDetails.getPreReturnDay();
            Date currentTime = new Date();
            LocalDate localDate1 = preReturnDay1.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate localDate2 = currentTime.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            Double overDay = (double) ChronoUnit.DAYS.between(localDate1, localDate2);

            //计算罚款
            Double overMoney = overDay * punishRate;

            //改表
            LambdaUpdateWrapper<Borrow> lambdaUpdateWrapper2 = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper2
                    .eq(Borrow::getBookId,bookId)
                    .set(Borrow::getActReturnDay,currentTime)
                    .set(Borrow::getOverDays,overDay)
                    .set(Borrow::getOverMoney,overMoney)
                    .set(Borrow::getPunishMoney,overMoney)
                    .set(Borrow::getIsReturn,1)
                    .set(Borrow::getReturnOperationPersonName,operatorReturnName);
            borrowMapper.update(null,lambdaUpdateWrapper2);

        }else {
            //没逾期
            //1.修改TB_Reader
            LambdaUpdateWrapper<Reader> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper
                    .eq(Reader::getName,readerName)
                    .setSql("rdBorrowQty = rdBorrowQty - 1");
            readerMapper.update(null,lambdaUpdateWrapper);

            //2.修改TB_Book
            LambdaUpdateWrapper<Book> lambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper1
                    .eq(Book::getId,bookId)
                    .setSql("bkStatus = '在馆',bkDateIn = now() ");
            bookMapper.update(null,lambdaUpdateWrapper1);

            //3.修改TB_Borrow
            LambdaUpdateWrapper<Borrow> lambdaUpdateWrapper2 = new LambdaUpdateWrapper<>();
            Date currentTime = new Date();
            lambdaUpdateWrapper2
                    .eq(Borrow::getBookId,bookId)
                    .set(Borrow::getActReturnDay,currentTime)
                    .set(Borrow::getIsReturn,1)
                    .set(Borrow::getReturnOperationPersonName,operatorReturnName);
            borrowMapper.update(null,lambdaUpdateWrapper2);

        }
    }

    @Override
    public Reader findReaderByName(String readerName) {
        LambdaQueryWrapper<Reader> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Reader::getName,readerName);
        return readerMapper.selectOne(lambdaQueryWrapper);

    }

    @Override
    public List<Reader> getAllBookOperator() {
        LambdaQueryWrapper<Reader> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Reader::getRole,1);
        return readerMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BookVO> getMyBooks(String readerName) {
        LambdaQueryWrapper<Reader> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //有姓名查询id
        lambdaQueryWrapper.eq(Reader::getName,readerName);
        Integer id = readerMapper.selectOne(lambdaQueryWrapper).getId();

        //在borrow表里由id查询所借的所有书本id;所有书本借书日期，应还日期。
        LambdaQueryWrapper<Borrow> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Borrow::getReaderId,id).eq(Borrow::getIsReturn,0);
        List<Borrow> borrows = borrowMapper.selectList(lambdaQueryWrapper1);

        List<Integer> bookIds = borrows.stream()
                .map(Borrow::getBookId)
                .collect(Collectors.toList());

        List<Date> dateBorrowList = borrows.stream()
                .map(Borrow::getLendDay)
                .collect(Collectors.toList());

        List<Date> preReturnDayList = borrows.stream()
                .map(Borrow::getPreReturnDay)
                .collect(Collectors.toList());

        //使用这些bookId去book表查询对应书本的全部信息
        LambdaQueryWrapper<Book> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        List<Book> bookList = new ArrayList<>();
        bookIds.forEach(bookId -> {
            lambdaQueryWrapper2.eq(Book::getId,bookId);
            bookList.add(bookMapper.selectOne(lambdaQueryWrapper2));
        });

        //遍历dateBorrowList，preReturnDayList，bookList 将其封装为BookVO
        List<BookVO> bookVOList = new ArrayList<>();

        for (int i = 0; i < dateBorrowList.size(); i++) {
            Date dateBorrow = dateBorrowList.get(i);
            Date preReturnDay = preReturnDayList.get(i);
            Book book = bookList.get(i);

            BookVO bookVO = new BookVO(dateBorrow, preReturnDay, book.getId(),book.getBookName());
            bookVOList.add(bookVO);
        }

        return bookVOList;
    }

    @Override
    public List<Reader> findLowLevelReader() {
        LambdaQueryWrapper<Reader> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Reader::getRole, 0, 1);
        return readerMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void updateReader2(Reader reader) {
        LambdaUpdateWrapper<Reader> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper
                .eq(Reader::getId,reader.getId())
                .set(Reader::getName,reader.getName())
                .set(Reader::getSex,reader.getSex())
                .set(Reader::getType,reader.getType())
                .set(Reader::getDept,reader.getDept())
                .set(Reader::getPhone,reader.getPhone())
                .set(Reader::getEmail,reader.getEmail())
                .set(Reader::getRegisterTime,reader.getRegisterTime())
                .set(Reader::getPhoto,reader.getPhoto())
                .set(Reader::getStatus,reader.getStatus())
                .set(Reader::getBorrowNum,reader.getBorrowNum())
                .set(Reader::getPassword,reader.getPassword())
                .set(Reader::getRole,reader.getRole());

        readerMapper.update(null,lambdaUpdateWrapper);
    }

    @Override
    public Integer changePassword(PasswordVO passwordVO) {
        String readerName = passwordVO.getReaderName();
        String oldPassword = passwordVO.getOldPassword();
        String newPasword = passwordVO.getNewPassword();

        //比较用户输入的旧密码是否一致
        //查询原正确的旧密码(md5)
        LambdaQueryWrapper<Reader> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Reader::getName,readerName);
        String password = readerMapper.selectOne(lambdaQueryWrapper).getPassword();
        //比较
        boolean b = Md5Util.checkPassword(oldPassword, password);

        if(b){
            //改密码
            String md5String = Md5Util.getMD5String(newPasword);

            LambdaUpdateWrapper<Reader> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper
                    .eq(Reader::getName,readerName)
                    .set(Reader::getPassword,md5String);
            readerMapper.update(null,lambdaUpdateWrapper);
            return 1;
        }else {
            //旧密码输入错误
            return -1;
        }

    }


}
