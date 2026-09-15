package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    //新增地址
    void add(AddressBook addressBook);

    //查询当前用户所有地址
    List<AddressBook> list();

    //查询默认地址
    AddressBook getDefault();

    //修改地址
    void update(AddressBook addressBook);

    //删除地址
    void delete(Long id);

    //根据ID查询地址
    AddressBook getById(Long id);

    //设置默认地址
    void setDefault(AddressBook addressBook);
}