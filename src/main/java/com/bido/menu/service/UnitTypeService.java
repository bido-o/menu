package com.bido.menu.service;

import com.bido.menu.dto.UnitTypeDto;
import com.bido.menu.mapper.ReferenceMapper;
import com.bido.menu.repository.UnitTypeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UnitTypeService {

    private final UnitTypeRepository repository;
    private final ReferenceMapper mapper;

    public UnitTypeService(UnitTypeRepository repository, ReferenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<UnitTypeDto> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream()
                .map(mapper::toUnitTypeDto)
                .toList();
    }
}
