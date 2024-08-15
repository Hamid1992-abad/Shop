package com.example.online.api;

import com.example.online.domainModel.Category;
import com.example.online.domainModel.Products;
import com.example.online.dto.CategoryDto;
import com.example.online.dto.ProductDto;
import com.example.online.service.CategoryService;
import com.example.online.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
public class AdminRestController {
    private final ProductsService productsService;
    private final CategoryService categoryService;

    @PostMapping("addCategory")
    public ResponseEntity<Category> addCategory(@RequestBody CategoryDto categoryDto)
    {
        Category category = categoryService.addCategory(categoryDto);


        return new ResponseEntity<>(category, HttpStatus.CREATED);

    }

    @GetMapping("getCategory/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable int categoryId)
    {
        Category categoryById = categoryService.getCategoryById(categoryId);


        return new ResponseEntity<>(categoryById, HttpStatus.OK);

    }
    @GetMapping("getAllCategory")
    public ResponseEntity<List<Category>> getAllCategory()
    {
        List<Category> allCategory = categoryService.getAllCategory();

        return new ResponseEntity<>(allCategory, HttpStatus.OK);

    }
   /* @GetMapping("deleteCategory/{categoryId}")
    public ResponseEntity deleteCategoryById(@PathVariable int categoryId)
    {
        Products products=new Products();
        CategoryService deleteCategory = categoryService;
        deleteCategory.removeCategoryById(categoryId);
        products.removeCategory();
        return new ResponseEntity<>(deleteCategory, HttpStatus.OK);

    }*/

      }