package com.zw.util;

import cn.hutool.core.collection.CollUtil;
import com.zw.entity.dto.MyPage;
import org.springframework.data.domain.Page;

public class MyPageUtil {
    public static <T> MyPage<T> getMyPage(Page<T> jpaPage ) {
        MyPage<T> myPage = new MyPage<>();
        if (jpaPage != null&& CollUtil.isNotEmpty(jpaPage.getContent())){
            myPage.setList(jpaPage.getContent());
            myPage.setTotal(jpaPage.getTotalElements());
            myPage.setPageNum(jpaPage.getNumber()+1);
            myPage.setPageSize(jpaPage.getSize());
        }
        return myPage;
    }
}
