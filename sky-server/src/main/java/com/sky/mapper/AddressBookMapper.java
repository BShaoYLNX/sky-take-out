package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    //新增地址
    void insert(AddressBook addressBook);

    //查询当前用户所有地址
    List<AddressBook> listByUserId(@Param("userId") Long userId);

    //查询当前用户的默认地址
    AddressBook getDefaultByUserId(@Param("userId") Long userId);

    //修改地址
    void update(AddressBook addressBook);

    //按用户id批量更新
    void updateByUserId(AddressBook addressBook);

    //根据ID删除地址
    void deleteById(@Param("id") Long id);

    //根据ID查询地址
    AddressBook getById(@Param("id") Long id);
}