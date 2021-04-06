package com.clone.daangnclone.service;

import com.clone.daangnclone.dto.ProductRequestDto;
import com.clone.daangnclone.entity.Product;
import com.clone.daangnclone.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    public Product findProductDetail(String id) {
        return productRepository.findByDaangnProductId(id);
    }

    public List<Product> find_hot(String want){
        System.out.println(want);
        return productRepository.findByRegionContaining(want);
    }

    public void createProduct(ProductRequestDto requestDto){
        Product product = new Product(requestDto);
        productRepository.save(product);
    }

    public List<Product> searchProduct(String keyword) {
        List<Product> searchlist = new ArrayList<>();
        searchlist.addAll(productRepository.findByNicknameContaining(keyword));
        searchlist.addAll(productRepository.findByTitleContaining(keyword));
        searchlist.addAll(productRepository.findByContentsContaining(keyword));
        searchlist.addAll(productRepository.findByRegionContaining(keyword));

        searchlist = searchlist.parallelStream().distinct().collect(Collectors.toList());
        return searchlist;
    }

}

