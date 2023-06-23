package com.hjh.hhyx.search.repository;

import com.hjh.hhyx.model.search.SkuEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 韩
 * @version 1.0
 */
@Repository
public interface SkuSearchRepository extends ElasticsearchRepository<SkuEs, Long> {
    //降序查询爆款商品前十条数据
    //findByOrderByHotScoreDesc
    Page<SkuEs> findByOrderByHotScoreDesc(Pageable page);

    Page<SkuEs> findByCategoryIdAndWareId(Long categoryId, Long wareId, Pageable pageable);

    Page<SkuEs> findByKeywordAndWareId(String keyword, Long wareId, Pageable pageable);
}
