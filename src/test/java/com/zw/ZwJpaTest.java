package com.zw;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zw.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZwJpaTest {

    @LocalServerPort
    private int port;

    // 全局 ObjectMapper（用于反序列化泛型如 Page<User>）
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String baseUrl() {
        return "http://localhost:" + port + "/users";
    }

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
}