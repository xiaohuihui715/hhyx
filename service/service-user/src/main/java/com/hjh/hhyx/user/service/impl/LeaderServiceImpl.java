package com.hjh.hhyx.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjh.hhyx.model.user.Leader;
import com.hjh.hhyx.user.mapper.LeaderMapper;
import com.hjh.hhyx.user.service.LeaderService;
import org.springframework.stereotype.Service;

/**
 * @author 韩
 * @version 1.0
 */
@Service
public class LeaderServiceImpl extends ServiceImpl<LeaderMapper, Leader> implements LeaderService {
}
