package com.example.online.repository;

import com.example.online.domainModel.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;


public interface ProductsRepository extends JpaRepository<Products,Integer> {
     Optional<Products> findByName(String name);
    List<Products> findByNameLike(String pattern);
    List<Products> findByPriceGreaterThanEqualAndPriceLessThanEqual(int min, int max);
    List<Products>findByNameIsIn(List<String>namesList);

    @Query("update  Products p set p.view=:view where p.id=:id")
    @Modifying

    int updateView( int view, int id);
    @Query("update  Products p set p.discountedPrice=:discount where p.id=:id")
    @Modifying

   int discountedPrice(double discount, int id);

    @Query("update  Products p set p.DiscountedPercentage=:DiscountedPercentage where p.id=:id")
    @Modifying

  int DiscountedPercentage(int DiscountedPercentage, int id);

    @Query("update  Products p set p.stock=:Stock where p.id=:id")
    @Modifying

    int updateStock(int Stock, int id);

    @Query("SELECT p.view from Products p ")
    List<Integer> findView();
    List<Products>findByViewBetween(int a,int b);



}
