package com.bido.menu.service;

import com.bido.menu.dto.category.ProductCategoryDto;
import com.bido.menu.mapper.ReferenceMapper;
import com.bido.menu.repository.ProductCategoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductCategoryService {

    private final ProductCategoryRepository repository;
    private final ReferenceMapper mapper;

    public ProductCategoryService(ProductCategoryRepository repository, ReferenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ProductCategoryDto> findAllApproved() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "displayOrder", "name"))
                .stream()
                .filter(c -> c.getIsApproved() && c.getMergedInto() == null)
                .map(mapper::toCategoryDto)
                .toList();
    }
}
