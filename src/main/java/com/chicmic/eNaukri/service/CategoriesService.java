package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.Categories;
import com.chicmic.eNaukri.repo.CategoriesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesService {
    private final CategoriesRepo categoriesRepo;
    public Categories findById(Long id) {
        return categoriesRepo.findById(id).get();
    }
    public List<Categories> searchCategories(String query){
        return categoriesRepo.findCategoriesByName(query);
    }
}
