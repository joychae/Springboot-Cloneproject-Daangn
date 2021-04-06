package com.clone.daangnclone.repository;

import com.clone.daangnclone.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByDaangnProductId(String id);
    List<Product> findByNicknameContaining(String keyword);
    List<Product> findByTitleContaining(String keyword);
    List<Product> findByContentsContaining(String keyword);
    List<Product> findByRegionContaining(String keyword);
}
