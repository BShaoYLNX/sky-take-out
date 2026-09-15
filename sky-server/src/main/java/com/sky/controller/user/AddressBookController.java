package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userAddressBookController")
@RequestMapping("/user/addressBook")
@Api(tags = "用户端-地址簿接口")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     */
    @PostMapping
    @ApiOperation("新增地址")
    public Result add(@RequestBody AddressBook addressBook) {
        log.info("新增地址：{}", addressBook);
        addressBookService.add(addressBook);
        return Result.success();
    }

    /**
     * 查询当前登录用户的所有地址
     */
    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址")
    public Result<List<AddressBook>> list() {
        log.info("查询当前用户的所有地址");
        List<AddressBook> list = addressBookService.list();
        return Result.success(list);
    }

    /**
     * 查询默认地址
     */
    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefault() {
        log.info("查询默认地址");
        AddressBook addressBook = addressBookService.getDefault();
        return Result.success(addressBook);
    }

    /**
     * 根据ID修改地址
     */
    @PutMapping
    @ApiOperation("根据ID修改地址")
    public Result update(@RequestBody AddressBook addressBook) {
        log.info("修改地址：{}", addressBook);
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 根据ID删除地址
     */
    @DeleteMapping
    @ApiOperation("根据ID删除地址")
    public Result delete(@RequestParam Long id) {
        log.info("删除地址，id={}", id);
        addressBookService.delete(id);
        return Result.success();
    }

    /**
     * 根据ID查询地址
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        log.info("查询地址，id={}", id);
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 设置默认地址
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        log.info("设置默认地址：{}", addressBook);
        addressBookService.setDefault(addressBook);
        return Result.success();
    }
}