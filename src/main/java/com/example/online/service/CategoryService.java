package com.example.online.service;

import com.example.online.api.exeptions.CustomNotfoundExceptions;
import com.example.online.domainModel.Category;
import com.example.online.dto.CategoryDto;
import com.example.online.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category addCategory(CategoryDto categoryDto) {
        Category category= Category.builder()
                .name(categoryDto.nameCategory)
                .build();
        Category saveCategory = categoryRepository.save(category);
        return saveCategory;

    }
    public Category getCategoryById(int categoryId) {

        Optional<Category> getCategoryById = categoryRepository.findById(categoryId);
        return getCategoryById.orElseThrow(()->new CustomNotfoundExceptions("product with this categoryId  not found"));

    }
    public List<Category> getAllCategory() {

        List<Category> categories = categoryRepository.findAll();
        return categories;

    }

}
