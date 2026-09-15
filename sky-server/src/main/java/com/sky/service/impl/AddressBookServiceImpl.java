package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public void add(AddressBook addressBook) {
        // 设置当前用户id
        addressBook.setUserId(BaseContext.getCurrentId());
        // 新增地址默认非默认地址
        addressBook.setIsDefault(0);
        addressBookMapper.insert(addressBook);
    }

    @Override
    public List<AddressBook> list() {
        Long userId = BaseContext.getCurrentId();
        return addressBookMapper.listByUserId(userId);
    }

    @Override
    public AddressBook getDefault() {
        Long userId = BaseContext.getCurrentId();
        return addressBookMapper.getDefaultByUserId(userId);
    }

    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    @Override
    public void delete(Long id) {
        addressBookMapper.deleteById(id);
    }

    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }

    /**
     * 设置默认地址：先把该用户所有地址的 is_default 置 0，再把目标地址置 1
     */
    @Override
    @Transactional
    public void setDefault(AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();

        // 将该用户所有地址的 is_default 置 0
        AddressBook reset = new AddressBook();
        reset.setUserId(userId);
        reset.setIsDefault(0);
        addressBookMapper.updateByUserId(reset);

        // 将指定地址设置为默认地址
        AddressBook target = new AddressBook();
        target.setId(addressBook.getId());
        target.setIsDefault(1);
        addressBookMapper.update(target);
    }
}