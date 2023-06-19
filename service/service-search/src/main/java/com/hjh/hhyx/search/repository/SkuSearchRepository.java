package com.hjh.hhyx.search.repository;

import com.hjh.hhyx.model.search.SkuEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 韩
 * @version 1.0
 */
@Repository
public interface SkuSearchRepository extends ElasticsearchRepository<SkuEs, Long> {
}
