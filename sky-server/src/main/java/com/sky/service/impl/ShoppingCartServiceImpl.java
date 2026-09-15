package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();
        return shoppingCartMapper.listByUserId(userId);
    }

    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();

        // 查询当前用户的购物车列表
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.listByUserId(userId);

        // 遍历查找是否已存在相同商品
        ShoppingCart shoppingcart = null;
        for (ShoppingCart cart : shoppingCartList) {
            // 如果添加的是套餐，只比较套餐ID
            if (shoppingCartDTO.getSetmealId() != null) {
                if (cart.getSetmealId() != null
                        && cart.getSetmealId().equals(shoppingCartDTO.getSetmealId())) {
                    shoppingcart = cart;
                    break;
                }
            }
            // 如果添加的是菜品，比较菜品ID和口味
            else if (shoppingCartDTO.getDishId() != null) {
                if (cart.getDishId() != null
                        && cart.getDishId().equals(shoppingCartDTO.getDishId())) {
                    // 口味比较
                    String cartFlavor = cart.getDishFlavor();
                    String dtoFlavor = shoppingCartDTO.getDishFlavor();
                    boolean sameFlavor = (cartFlavor == null && dtoFlavor == null)
                            || (cartFlavor != null && cartFlavor.equals(dtoFlavor));
                    if (sameFlavor) {
                        shoppingcart = cart;
                        break;
                    }
                }
            }
        }

        // 已存在则数量+1，否则新增记录
        if (shoppingcart != null) {
            shoppingcart.setNumber(shoppingcart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(shoppingcart);
        } else {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
            shoppingCart.setUserId(userId);
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            // 补全商品名称、图片、单价
            if (shoppingCartDTO.getDishId() != null) {
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else if (shoppingCartDTO.getSetmealId() != null) {
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }

            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    /**
     * 删除一个商品
     * @param shoppingCartDTO
     */
    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();

        // 查询当前用户的购物车列表
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.listByUserId(userId);

        // 遍历查找匹配的商品
        ShoppingCart target = null;
        for (ShoppingCart cart : shoppingCartList) {
            // 比较套餐ID
            if (shoppingCartDTO.getSetmealId() != null) {
                if (cart.getSetmealId() != null
                        && cart.getSetmealId().equals(shoppingCartDTO.getSetmealId())) {
                    target = cart;
                    break;
                }
            }
            // 比较菜品ID和口味
            else if (shoppingCartDTO.getDishId() != null) {
                if (cart.getDishId() != null
                        && cart.getDishId().equals(shoppingCartDTO.getDishId())) {
                    String cartFlavor = cart.getDishFlavor();
                    String dtoFlavor = shoppingCartDTO.getDishFlavor();
                    boolean sameFlavor = (cartFlavor == null && dtoFlavor == null)
                            || (cartFlavor != null && cartFlavor.equals(dtoFlavor));
                    if (sameFlavor) {
                        target = cart;
                        break;
                    }
                }
            }
        }

        // 找不到就直接返回
        if (target == null) {
            return;
        }

        // 数量判断：>1 则-1，=1 则删除整条记录
        if (target.getNumber() > 1) {
            target.setNumber(target.getNumber() - 1);
            shoppingCartMapper.updateNumberById(target);
        } else {
            shoppingCartMapper.deleteById(target.getId());
        }
    }
}