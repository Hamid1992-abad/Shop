package com.example.online.service;

import com.example.online.api.exeptions.CustomNotfoundExceptions;
import com.example.online.domainModel.Category;
import com.example.online.domainModel.Products;
import com.example.online.dto.ProductDto;
import com.example.online.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;


    public static String UploadDir="\\src\\main\\resources\\static\\productImages";

    @Transactional
    public Products addProducts(ProductDto productDto)
    {
        Products products= Products.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .sellerName(productDto.getSellerName())
                .imageName(productDto.getImageName())
                 .build();
     String image= products.getImageName();
        if (!image.isEmpty()&&image!=null) {
            String name = UUID.randomUUID() + image;
            Path uploadPath = Path.of(UploadDir);
            uploadPath = uploadPath.resolve(name);
            products.setImageName(String.valueOf(uploadPath));
        }
        List<Category>categories=new ArrayList<>();

       Category category=new Category();
     category.setId(1);
       category.setName("Laptop");
       categories.add(category);

         products.setCategoryList(categories);



        Products saveProducts = productsRepository.save(products);
        return saveProducts;
    }
    public List<Products> getAllProducts() {

        List<Products> productsList = productsRepository.findAll();

        return  productsList;
    }

    public  Products getProductById(int id)
    {
        Optional<Products> productsOptional = productsRepository.findById(id);
        return productsOptional.orElseThrow(()->new CustomNotfoundExceptions(String.format("product with id:%s not found",id)));
    }
    public  Products getProductByName(String name)

    {
        Optional<Products> optionalName = productsRepository.findByName(name);
        return optionalName.orElseThrow(()->new CustomNotfoundExceptions(String.format("product with name:%s not found",name)));
    }

    public List<Products>filterByPrice(Map<String,String> allParameters)
    {
        int minimum=allParameters.containsKey("min")? Integer.parseInt(allParameters.get("min")) :0;
        int maximum=allParameters.containsKey("max")? Integer.parseInt(allParameters.get("max")) :Integer.MAX_VALUE;

        return productsRepository.findByPriceGreaterThanEqualAndPriceLessThanEqual(minimum,maximum);
    }
    public List<Products> filterByNames(List<String> namesList) {
        return productsRepository.findByNameIsIn(namesList);

    }
    @Transactional
    public void updateViewProducts( int id){
        int views=0;


        Products viewProducts = getProductById(id);


            views= viewProducts.getView();
            views++;

        int updatedView = productsRepository.updateView(views, id);
    }
    @Transactional
    public void updatePrice(double discount, int id){
        double updatedPrice = productsRepository.discountedPrice(discount, id);
       }

    @Transactional
    public void discountedPercentage(int discountedPercentage , int id){

        productsRepository.DiscountedPercentage(discountedPercentage,id);
    }

    @Transactional
    public List<Products> getProductsByMaxView() {

        List<Integer> view = productsRepository.findView();


        view.sort(Comparator.reverseOrder());
        view.stream().forEach(System.out::println);
        List<Integer> collect = view.stream().limit(10).collect(Collectors.toList());

        int size = collect.size();
        int first=0;
        int second=0;
        int i=1;
  for (int c: collect) {

                if (i==1){
                    first=c;
                  }
      if (i==size){
          second=c;

      }
      i++;
}


        List<Products> byViewBetween = productsRepository.findByViewBetween(second,first);


        return byViewBetween;
}
    @Transactional
    public List<Products> findByNameLike() {

        List<Products> lenovo = productsRepository.findByNameLike("%sam%");

        return lenovo;

    }
       @Transactional
    public Products getStockProducts(int id)
    {
       int stock=0;
       int controlStock=0;
        Products productById = getProductById(id);
        stock= productById.getStock();
        System.out.println(stock);
     if (stock>0){
        controlStock=--stock;
     }

        System.out.println(controlStock);
        int updatedStock = productsRepository.updateStock(controlStock, id);

        return productById;

    }

  }