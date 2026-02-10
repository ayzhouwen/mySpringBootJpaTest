package com.zw;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zw.entity.TOrder;
import com.zw.entity.TOrderItem;
import com.zw.entity.User;
import com.zw.service.TOrderService;
import com.zw.util.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class ZwJpaTest {

    @LocalServerPort
    private int port;

    // 全局 ObjectMapper（用于反序列化泛型如 Page<User>）
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String baseUrl() {
        return "http://localhost:" + port + "/users";
    }
    @Autowired
    private TOrderService tOrderService;

    // === POST 创建用户 ===
    private User postUser(User user) {
        HttpResponse response = HttpRequest.post(baseUrl())
                .body(JSONUtil.toJsonStr(user))
                .contentType("application/json")
                .execute();

        assertThat(response.getStatus()).isEqualTo(200);
        return JSONUtil.toBean(response.body(), User.class);
    }

    // === GET 查询单个用户 ===
    private User getUserById(String id) {
        HttpResponse response = HttpRequest.get(baseUrl() + "/" + id).execute();
        if (response.getStatus() == 404) {
            return null;
        }
        assertThat(response.getStatus()).isEqualTo(200);
        return JSONUtil.toBean(response.body(), User.class);
    }

    // === GET 分页查询（支持泛型 Page<T>）===
    private <T> Page<T> getPage(String url, Class<T> elementType) throws Exception {
        HttpResponse response = HttpRequest.get(url).execute();
        assertThat(response.getStatus()).isEqualTo(200);

        return objectMapper.readValue(
                response.body(),
                objectMapper.getTypeFactory().constructParametricType(
                        org.springframework.data.domain.Page.class,
                        elementType
                )
        );
    }

    // === DELETE 删除用户 ===
    private void deleteUser(String id) {
        HttpResponse response = HttpRequest.delete(baseUrl() + "/" + id).execute();
        assertThat(response.getStatus()).isEqualTo(204); // Spring 默认返回 204 No Content
    }

    // === 测试用例开始 ===

    @Test
    @Order(1)
    void shouldCreateUser() {
        User input = new User();
        input.setUsername("张三");
        input.setEmail("zhangsan@test.com");

        User created = postUser(input);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getUsername()).isEqualTo("张三");
    }

    @Test
    @Order(2)
    void shouldGetUserById() {
        User user = new User();
        user.setUsername("李四");
        user.setEmail("lisi@test.com");
        User created = postUser(user);

        User found = getUserById(created.getId());

        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("李四");
    }

    @Test
    @Order(3)
    void shouldGetAllUsersWithPagination() throws Exception {
        User u1 = new User();
        u1.setUsername("王五");
        u1.setEmail("wangwu@test.com");
        postUser(u1);

        User u2 = new User();
        u2.setUsername("赵六");
        u2.setEmail("zhaoliu@test.com");
        postUser(u2);

        Page<User> page = getPage(baseUrl() + "?page=0&size=1", User.class);

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
    }

    @Test
    @Order(4)
    void shouldDeleteUser() {
        User user = new User();
        user.setUsername("待删");
        user.setEmail("delete@test.com");
        User created = postUser(user);

        deleteUser(created.getId());

        User notFound = getUserById(created.getId());
        assertThat(notFound).isNull();
    }

    @Test
    @Order(5)
    void shouldSearchByUsername() throws Exception {
        User u1 = new User();
        u1.setUsername("张小明");
        u1.setEmail("xiaoming@test.com");
        postUser(u1);

        User u2 = new User();
        u2.setUsername("张大伟");
        u2.setEmail("dawei@test.com");
        postUser(u2);

        User u3 = new User();
        u3.setUsername("李小红");
        u3.setEmail("xiaohong@test.com");
        postUser(u3);

        Page<User> page = getPage(baseUrl() + "/search?username=张&page=0&size=10", User.class);

        List<User> users = page.getContent();
        assertThat(users).hasSize(2);
        assertThat(users.stream().allMatch(u -> u.getUsername().contains("张"))).isTrue();
    }

    @Test
    void testCreateOrder() {
        // 1. 创建订单主表
        TOrder TOrder = new TOrder();
        TOrder.setOrderNo("ORD" + System.currentTimeMillis());
        TOrder.setUserId("user-123");
        TOrder.setStatus(0); // 待支付
        TOrder.setTotalAmount(new BigDecimal("449.97")); // 149.99 + 299.98
        TOrder.setActualAmount(new BigDecimal("449.97"));
        TOrder.setCurrency("CNY");
        TOrder.setShippingFee(BigDecimal.ZERO);
        TOrder.setDiscountAmount(BigDecimal.ZERO);
        TOrder.setConsigneeName("张三");
        TOrder.setConsigneePhone("13800138000");
        // 2. 创建第一个商品明细
        TOrderItem item1 = new TOrderItem();
        item1.setProductId("prod-001");
        item1.setSkuId("sku-red-xl");
        item1.setProductName("纯棉T恤");
        item1.setSkuDesc("红色 / XL");
        item1.setPrice(new BigDecimal("149.99"));
        item1.setQuantity(1);
        item1.setTotalPrice(new BigDecimal("149.99"));
        // 3. 创建第二个商品明细
        TOrderItem item2 = new TOrderItem();
        item2.setProductId("prod-002");
        item2.setSkuId("sku-blue-m");
        item2.setProductName("牛仔外套");
        item2.setSkuDesc("蓝色 / M");
        item2.setPrice(new BigDecimal("299.98"));
        item2.setQuantity(1);
        item2.setTotalPrice(new BigDecimal("299.98"));
        // 设置订单的明细列表
        TOrder.setItems(Arrays.asList(item1, item2));
        // 5. 保存订单（级联保存两个明细）
//        TOrder savedTOrder = tOrderService.createOrderAutoSaveItem(TOrder);
        TOrder savedTOrder = tOrderService.createOrderNoAutoSaveItem(TOrder);
    }

    /**
     * 注意 application.yml 的active要设置为 prod
     */
    @Test
    void testBatchInsert() {
        List<TOrder> list = new ArrayList<>();
        for (int i = 0; i <100 ; i++) {
            TOrder tOrder = new TOrder();
            tOrder.setOrderNo("ORD:" + IdUtil.getSnowflakeNextIdStr());
            tOrder.setUserId("user-123-test"+i);
            tOrder.setStatus(0); // 待支付
            tOrder.setTotalAmount(new BigDecimal(Convert.toStr(i))); // 149.99 + 299.98
            tOrder.setActualAmount(new BigDecimal(Convert.toStr(i)));
            tOrder.setCurrency("CNY");
            tOrder.setShippingFee(BigDecimal.ZERO);
            tOrder.setDiscountAmount(BigDecimal.ZERO);
            tOrder.setConsigneeName("张三"+ i);
            tOrder.setConsigneePhone("1380013800-"+i);
            list.add(tOrder);
        }
        log.info("外层调用批量写开始");
        long start = System.currentTimeMillis();
//        tOrderService.jpaBatchInsert1(list);
        tOrderService.springJdbcBatchInsert(list);
        long end = System.currentTimeMillis();
        long totalTimeMillis = end - start;
        double totalTimeSeconds = totalTimeMillis / 1000.0;
        double recordsPerSecond = list.size() / totalTimeSeconds;
        log.info(MyDateUtil.execTime("外层调用批量写结束-耗时",start));
        log.info("插入 {} 条数据，耗时 {} ms，平均每秒处理 {} 条", list.size(), totalTimeMillis, String.format("%.2f", recordsPerSecond));
//        tOrderService.jpaBatchInsert2(list);
    }


}