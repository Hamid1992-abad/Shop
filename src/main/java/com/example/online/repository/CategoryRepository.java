package com.example.online.repository;

import com.example.online.domainModel.Category;
import com.example.online.domainModel.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
