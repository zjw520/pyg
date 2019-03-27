package com.pinyougou.user.service.impl;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.util.HttpClientUtils;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.OrderItem;
import com.pinyougou.pojo.Seller;
import com.pinyougou.pojo.User;

import com.pinyougou.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * UserServiceImpl 服务接口实现类
 *
 * @version 1.0
 * @date 2019-02-27 16:23:07
 */

@Service(interfaceName = "com.pinyougou.service.UserService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Value("${sms.url}")
    private String smsUrl;

    @Value("${sms.signName}")
    private String signName;

    @Value("${sms.templateCode}")
    private String templateCode;

    /**
     * 添加方法
     */
    public void save(User user) {
        try {
            user.setCreated(new Date());
            user.setUpdated(user.getCreated());
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            userMapper.insertSelective(user);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 修改方法
     */
    public void update(User user) {
        try {
            userMapper.updateByPrimaryKeySelective(user);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 根据主键id删除
     */
    public void delete(Serializable id) {
        try {
            userMapper.deleteByPrimaryKey(id);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 批量删除
     */
    public void deleteAll(Serializable[] ids) {
        try {
            // 创建示范对象
            Example example = new Example(User.class);
            // 创建条件对象
            Example.Criteria criteria = example.createCriteria();
            // 创建In条件
            criteria.andIn("id", Arrays.asList(ids));
            // 根据示范对象删除
            userMapper.deleteByExample(example);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 根据主键id查询
     */
    public User findOne(Serializable id) {
        try {
            return userMapper.selectByPrimaryKey(id);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 查询全部
     */
    public List<User> findAll() {
        try {
            return userMapper.selectAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 多条件分页查询
     */
    public List<User> findByPage(User user, int page, int rows) {
        try {
            PageInfo<User> pageInfo = PageHelper.startPage(page, rows)
                    .doSelectPageInfo(new ISelect() {
                        @Override
                        public void doSelect() {
                            userMapper.selectAll();
                        }
                    });
            return pageInfo.getList();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean sendCode(String phone) {
        try {
            String code = UUID.randomUUID().toString().replaceAll("-", "").replaceAll("[a-z|A-Z]", "").substring(0, 6);
            HttpClientUtils httpClientUtils = new HttpClientUtils(false);
            Map<String, String> param = new HashMap<>();
            param.put("phone", phone);
            param.put("signName", signName);
            param.put("templateCode", templateCode);
            param.put("templateParam", "{\"code\":\"" + code + "\"}");
            String content = httpClientUtils.sendPost(smsUrl, param);

            Map<String, Object> resMap = JSON.parseObject(content, Map.class);
            redisTemplate.boundValueOps(phone).set(code, 90, TimeUnit.SECONDS);
            return (boolean) resMap.get("success");

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean checkSmsCode(String phone, String code) {
        String sysCode = redisTemplate.boundValueOps(phone).get();
        return StringUtils.isNoneBlank(sysCode) && sysCode.equals(code);
    }

    /**
     * 根据用户名查询对应的数据
     * @param username
     * @return User
     */
    @Override
    public User findUser(String username) {
        User user = new User();
        user.setUsername(username);
        User user2 = userMapper.selectOne(user);

        return user2;
    }

    /**
     * 保存添加用户
     * @param user
     */
    @Override
    public void updateUser(User user) {
        try {
            userMapper.updateByPrimaryKeySelective(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean tianjia(User user) {
        try{
            //修改日期ete());
            //密码加密
//            user.setPassword(user.getPassword());
            Example example=new Example(User.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("username",user.getUsername());///where username ='acc'
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));//and password="1234567'
            userMapper.updateByExampleSelective(user,example);
//            userMapper.updateByPrimaryKeySelective(user);
            //修改
            return true;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean updphone(User user) {
        try{
            Example example=new Example(User.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("username",user.getUsername());
            user.setPhone(user.getPhone());
            userMapper.updateByExampleSelective(user,example);
            return true;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Map<String, Object>> showOrder(String userId) {
        try {
            List<Map<String,Object>>  list = new ArrayList<>();
            Example example = new Example(Order.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", userId);
            //获取订单集合
            List<Order> orders = orderMapper.selectByExample(example);

            if (orders!=null && orders.size()>0){
                for (Order order : orders) {//订单遍历
                    Map<String,Object> map = new HashMap<>();
                    Long orderId = order.getOrderId();
                    //id超过long类型的取值范围,转换成字符串
                    String orderIdStr = orderId.toString();
                    map.put("orderId", orderIdStr);
                    map.put("status",order.getStatus() );
                    Date createTime = order.getCreateTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //日期转换成字符串类型
                    String date = simpleDateFormat.format(createTime);
                    map.put("createTime", date);
                    //获取店铺名称
                    String sellerId = order.getSellerId();
                    Seller seller = sellerMapper.selectSellerBySellerId(sellerId);
                    String nickName = seller.getNickName();
                    map.put("sellerId",nickName );
                    //根据orderId 查询orderItem表
                    List<OrderItem> orderItems = orderItemMapper.findOrderItemS(orderId);
                    //创建List<Map<string,object>>,封装orderItem的属性值,以及对应商品的规格值(根据itemId查询item表)
                    List<Map<String,Object>> itemList = new ArrayList<>();
                    double pay = 0;
                    for (OrderItem orderItem : orderItems) {//遍历订单详情
                        Map<String,Object> itemMap = new HashMap<>();
                        double price = orderItem.getPrice().doubleValue();
                        itemMap.put("price", price);
                        double num = orderItem.getNum().doubleValue();
                        itemMap.put("num", orderItem.getNum());
                        pay+=num*price;
                        itemMap.put("picPath",orderItem.getPicPath() );
                        itemMap.put("title",orderItem.getTitle() );
                        Long itemId = orderItem.getItemId();
                        //根据itemId获取规格:
                        String spec = itemMapper.findSpcByItemId(itemId);
                        Map specMap = JSON.parseObject(spec, Map.class);
                        String specString = specMap.toString();
                        String specStr = specString.replace("{", "").replace("}", "").replace("=", ":");
                        itemMap.put("spec",specStr );
                        itemList.add(itemMap);
                    }
                    //pay 订单实际支付总金额
                    order.setPayment(new BigDecimal(pay));
                    map.put("payment",order.getPayment() );
                    map.put("orderItems", itemList);
                    list.add(map);
                }
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User showtable(String username) {
        try {
            User user=new User();
            user.setUsername(username);
            return userMapper.selectOne(user);
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public boolean sendCodee(String phone) {
        try {
            String code = UUID.randomUUID().toString().replaceAll("-", "").replaceAll("[a-z|A-Z]", "").substring(0, 6);
            HttpClientUtils httpClientUtils = new HttpClientUtils(false);
            Map<String, String> param = new HashMap<>();
            param.put("phone", phone);
            param.put("signName", signName);
            param.put("templateCode", templateCode);
            param.put("templateParam", "{\"code\":\"" + code + "\"}");
            String content = httpClientUtils.sendPost(smsUrl, param);

            Map<String, Object> resMap = JSON.parseObject(content, Map.class);
            redisTemplate.boundValueOps(phone).set(code, 90, TimeUnit.SECONDS);
            return (boolean) resMap.get("success");

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    };


}