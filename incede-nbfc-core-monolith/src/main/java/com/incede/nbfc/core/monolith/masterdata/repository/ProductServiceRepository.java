package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.ProductService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductServiceRepository extends JpaRepository<ProductService, Integer> {

}
