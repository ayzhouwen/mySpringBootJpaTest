package com.zw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
/**
 * JPA自动用户接口
 */
@Configuration
public class UserIDAuditor implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
//        Long userId = (Long)ContextHolder.get("userId");
//        return Optional.ofNullable(userId);
        //没有安全框架，先暂时写死
        return Optional.of("测试用户Id");
    }
}
