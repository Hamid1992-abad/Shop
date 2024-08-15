package com.example.online.api;

import com.example.online.domainModel.Category;
import com.example.online.domainModel.Products;
import com.example.online.dto.CategoryDto;
import com.example.online.dto.ProductDto;
import com.example.online.service.CategoryService;
import com.example.online.service.ProductsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ch.qos.logback.classic.spi.ThrowableProxyVO.build;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/products")
public class ProductRestController {
    private final ProductsService productsService;
    private final CategoryService categoryService;


    @PostMapping(value = "addProducts")
    public ResponseEntity<Products> addProducts(@Valid @RequestBody ProductDto productDto)
    {

        Products products = productsService.addProducts(productDto);
        return new ResponseEntity<>(products, HttpStatus.CREATED);

    }
    @GetMapping(value = "allProducts")
    public ResponseEntity<List<Products>> getAllProduct()
    {
       List<Products> allProducts =  productsService.getAllProducts();


       return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

  @GetMapping(value = "getProductById/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable int id)
    {
       Products productById = productsService.getProductById(id);
        //productsService.updateViewProducts(id);

        return new ResponseEntity<>(productById, HttpStatus.OK);
    }
    @GetMapping(value = "getProductsByCategoryId/{Categoryid}")
    public ResponseEntity<Category> getProductsByCategoryId(@PathVariable int Categoryid)
    {
        Category categoryById = categoryService.getCategoryById(Categoryid);
        categoryById.getProducts().size();

        return new ResponseEntity<>(categoryById, HttpStatus.OK);
    }

    @GetMapping(value = "getProductByName")
    public ResponseEntity<Products> getProductByName(@RequestParam String name)
    {
        Products productByName = productsService.getProductByName(name);
        return new ResponseEntity<>(productByName, HttpStatus.OK);
    }
    @GetMapping(value = "getProductsByPatternName")
    public ResponseEntity <List<Products>>  getProductsByPatternName()
    {
        List<Products> byNameLike = productsService.findByNameLike();

        return new ResponseEntity<>(byNameLike,HttpStatus.OK);
    }
    @GetMapping("getProductsByPrice")
    public ResponseEntity<List<Products>> getProductsByPrice(@RequestParam Map<String,String> allParameters)
    {
        List<Products> getProductsByPrice = productsService.filterByPrice(allParameters);
        return new ResponseEntity<>(getProductsByPrice,HttpStatus.OK);
    }
    @GetMapping("filterProductsByNamesList")
    public ResponseEntity<List<Products>> filterByNames(@RequestParam List<String>namesList)
    {
        List<Products> filteredByNames = productsService.filterByNames(namesList);

      return new ResponseEntity<>(filteredByNames,HttpStatus.OK);
    }

    @GetMapping(value = "getProductsWithTheMostViews")
    public ResponseEntity <List<Products>>  getProductsWithTheMostViews()
    {
        List<Products> productsByMaxView = productsService.getProductsByMaxView();
        return new ResponseEntity<>(productsByMaxView,HttpStatus.OK);
    }


    @GetMapping(value = "getProductsWithDiscountedPrice/{id}")
    public ResponseEntity<Products> getProductsWithDiscountedPrice(@PathVariable int id)
    {

         Products productById = productsService.getProductById(id);
               if (productById.getName().startsWith("lenovo")){
            double price = productById.getPrice();
            System.out.println(price);
           double discountedPrice=  price * 0.8;
            System.out.println(discountedPrice);
            productsService.updatePrice( discountedPrice,id);
                   productsService.discountedPercentage(20,id);
            }
            if (productById.getName().startsWith("samsung")){
                double price = productById.getPrice();
                System.out.println(price);
                double discountedPrice=  price * 0.7;
                System.out.println(discountedPrice);
                productsService.updatePrice( discountedPrice,id);
                productsService.discountedPercentage(30,id);

            }
        return new ResponseEntity<>(productById, HttpStatus.OK);
    }

    @GetMapping(value = "getStockProducts/{id}")
    public ResponseEntity <Products>  getStockProducts(@PathVariable int id)
    {
        Products stockProducts = productsService.getStockProducts(id);

        return new ResponseEntity<>(stockProducts, HttpStatus.OK);
    }

}
