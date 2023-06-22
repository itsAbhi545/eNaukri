package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.Categories;
import com.chicmic.eNaukri.repo.CategoriesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriesService {
    private final CategoriesRepo categoriesRepo;
    public Categories findById(Long id) {
        return categoriesRepo.findById(id).get();
    }
}
