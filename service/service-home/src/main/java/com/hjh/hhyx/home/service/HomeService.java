package com.hjh.hhyx.home.service;

import java.util.Map;

/**
 * @author 韩
 * @version 1.0
 */
public interface HomeService {

    //首页数据
    Map<String, Object> home(Long userId);
}