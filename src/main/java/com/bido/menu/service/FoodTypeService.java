package com.bido.menu.service;

import com.bido.menu.dto.FoodTypeDto;
import com.bido.menu.mapper.ReferenceMapper;
import com.bido.menu.repository.FoodTypeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FoodTypeService {

    private final FoodTypeRepository repository;
    private final ReferenceMapper mapper;

    public FoodTypeService(FoodTypeRepository repository, ReferenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<FoodTypeDto> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "displayOrder", "name"))
                .stream()
                .map(mapper::toFoodTypeDto)
                .toList();
    }
}
