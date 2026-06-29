package com.bido.menu.service;

import com.bido.menu.dto.DietaryTagDto;
import com.bido.menu.mapper.ReferenceMapper;
import com.bido.menu.repository.DietaryTagRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DietaryTagService {

    private final DietaryTagRepository repository;
    private final ReferenceMapper mapper;

    public DietaryTagService(DietaryTagRepository repository, ReferenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<DietaryTagDto> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream()
                .map(mapper::toDietaryTagDto)
                .toList();
    }
}
