package com.example.online.service;

import com.example.online.domainModel.Products;
import com.example.online.dto.ProductDto;
import com.example.online.repository.ProductsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import java.util.*;
import java.util.stream.Collectors;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


import static org.mockito.BDDMockito.then;

class ProductsServiceTest {
    @Captor
    private ArgumentCaptor<Products> productArgumentCaptor;

    private ProductsService productsService;
    @Mock
    private ProductsRepository productsRepositoryMock;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        productsService=new ProductsService(productsRepositoryMock);
    }

    @Test
    void addProducts() {
        ProductDto productDto=ProductDto.builder()
                .id(1)
                .name("SA1")
                .price(1000)
                .description("about SA1")
                .sellerName("DK")
               .imageName("SA1.jpg")
                .build();

        given(productsRepositoryMock.findById(1)).willReturn(Optional.empty());
        Products products = productsService.addProducts(productDto);

        //then(productsRepositoryMock).should().save(products);
        then(productsRepositoryMock).should().save(productArgumentCaptor.capture());
        ArgumentCaptor<Products> productsArgumentCaptor = productArgumentCaptor;
        productsArgumentCaptor.getValue();
        assertThat(productsArgumentCaptor.getValue().getName()).isEqualTo("SA1");






    }

    @Test
    void getAllProducts() {
        Products firstProduct=Products.builder()
                .id(1)
                .name("SA1")
                .price(1000)
                .description("about SA1")
                .sellerName("DK")
                .imageName("SA1.jpg")
                .build();
        Products secondProduct=Products.builder()
                .id(2)
                .name("SA2")
                .price(2000)
                .description("about SA2")
                .sellerName("DK")
                .imageName("SA2.jpg")
                .build();
        List<Products>productsList=new ArrayList<>();
        productsList.add(firstProduct);
        productsList.add(secondProduct);

        BDDMockito.BDDMyOngoingStubbing<List<Products>> getAllProducts =
                given(productsRepositoryMock.findAll()).willReturn(productsList);
        productsService.getAllProducts();

        assertThat(getAllProducts.equals(productsList));
    }

    @Test
    void getProductByIdIfTheIdExistsInTheDatabase() {
        Products firstProduct=Products.builder()
                .id(1)
                .name("SA1")
                .price(1000)
                .description("about SA1")
                .sellerName("DK")
                .imageName("SA1.jpg")
                .build();

                given(productsRepositoryMock.findById(1)).willReturn(Optional.ofNullable(firstProduct));
                productsService.getProductById(1);

        assertThat(productsRepositoryMock.findById(1).equals(firstProduct));
    }
    @Test
    void ThrowRunTimeExceptionIfTheIdNotExistsInTheDatabase() {
        Products firstProduct=Products.builder()
                .id(1)
                .name("SA1")
                .price(1000)
                .description("about SA1")
                .sellerName("DK")
                .imageName("SA1.jpg")
                .build();

        given(productsRepositoryMock.findById(1)).willReturn(Optional.ofNullable(firstProduct));
        ThrowingCallable throwingCallable= ()-> productsService.getProductById(2);

       assertThatThrownBy(throwingCallable).isInstanceOf(RuntimeException.class).
               hasMessage(String.format("product with id:%s not found",2));

 }



    @Test
    void getProductByNameIfTheNameExistsInTheDatabase() {
        Products firstProduct=Products.builder()
                .id(1)
                .name("SA1")
                .price(1000)
                .description("about SA1")
                .sellerName("DK")
                .imageName("SA1.jpg")
                .build();


                given(productsRepositoryMock.findByName("SA1")).willReturn(Optional.of(firstProduct));
                productsService.getProductByName("SA1");


        assertThat(productsRepositoryMock.findByName("SA1").equals(firstProduct));

        }
    @Test
    void ThrowRunTimeExceptionIfTheNameNotExistsInTheDatabase() {
        Products firstProduct=Products.builder()
                .id(1)
                .name("SA1")
                .price(1000)
                .description("about SA1")
                .sellerName("DK")
                .imageName("SA1.jpg")
                .build();


        given(productsRepositoryMock.findByName("SA1")).willReturn(Optional.of(firstProduct));

        ThrowingCallable throwingCallable= ()-> productsService.getProductByName("SA2");

        assertThatThrownBy(throwingCallable).isInstanceOf(RuntimeException.class);
    }

    @Test
    void filterByPrice() {
        Products firstProduct=Products.builder()
                .id(1)
                .name("SA1")
                .price(1000)
                .description("about SA1")
                .sellerName("DK")
                .imageName("SA1.jpg")
                .build();
        Products secondProduct=Products.builder()
                .id(2)
                .name("SA2")
                .price(2000)
                .description("about SA2")
                .sellerName("DK")
                .imageName("SA2.jpg")
                .build();
        Products thirdProduct=Products.builder()
                .id(1)
                .name("SA3")
                .price(3000)
                .description("about SA3")
                .sellerName("DK")
                .imageName("SA3.jpg")
                .build();
        Products fourthProduct=Products.builder()
                .id(1)
                .name("SA4")
                .price(4000)
                .description("about SA4")
                .sellerName("DK")
                .imageName("SA4.jpg")
                .build();
        List<Products>productsList=new ArrayList<>();
        productsList.add(firstProduct);
        productsList.add(secondProduct);
        productsList.add(thirdProduct);
        productsList.add(fourthProduct);

        given(productsRepositoryMock.findAll()).willReturn(productsList);
        Map<String,String>filterParameters=new HashMap<>();
        filterParameters.put("min","800");
        filterParameters.put("max","5000");

        List<Products> productsList1 = productsService.filterByPrice(filterParameters);


        List<Double> doubleList = productsList.stream().map(a -> a.getPrice()).collect(Collectors.toList());

        List<Double> doubleList1 = productsList1.stream().map(a -> a.getPrice()).collect(Collectors.toList());

        assertThat(doubleList1).asList().isSubsetOf(doubleList);
    }

    @Test
    void filterByNames() {
        Products firstProduct=Products.builder()
                .id(1)
                .name("SA1")
                .price(1000)
                .description("about SA1")
                .sellerName("DK")
                .imageName("SA1.jpg")
                .build();
        Products secondProduct=Products.builder()
                .id(2)
                .name("SA2")
                .price(2000)
                .description("about SA2")
                .sellerName("DK")
                .imageName("SA2.jpg")
                .build();
        Products thirdProduct=Products.builder()
                .id(3)
                .name("SA3")
                .price(3000)
                .description("about SA3")
                .sellerName("DK")
                .imageName("SA3.jpg")
                .build();
        Products fourthProduct=Products.builder()
                .id(4)
                .name("SA4")
                .price(4000)
                .description("about SA4")
                .sellerName("DK")
                .imageName("SA4.jpg")
                .build();
        List<Products>productsList=new ArrayList<>();
        productsList.add(firstProduct);
        productsList.add(secondProduct);
        productsList.add(thirdProduct);
        productsList.add(fourthProduct);


        given(productsRepositoryMock.findAll()).willReturn(productsList);

        List<String> namesList=new ArrayList<>();
        namesList.add("SA1");
        namesList.add("SA2");
        namesList.add("SA3");

        productsService.filterByNames(namesList);
        List<String> stringList = productsList.stream().map(a -> a.getName()).collect(Collectors.toList());

        assertThat(namesList).asList().isSubsetOf(stringList);
    }

    @Test
    void updateViewProducts() {
        Products firstProduct=Products.builder()
                .id(1)
                .name("SA1")
                .price(1000)
                .description("about SA1")
                .sellerName("DK")
                .imageName("SA1.jpg")
                .build();
        given(productsRepositoryMock.findById(1)).willReturn(Optional.of(firstProduct));
        productsService.updateViewProducts(1);
    }

    @Test
    void updatePrice() {

        Products firstProduct=Products.builder()
                .id(1)
                .name("SA1")
                .price(1000)
                .description("about SA1")
                .sellerName("DK")
                .imageName("SA1.jpg")
                .build();

        given(productsRepositoryMock.findById(1)).willReturn(Optional.of(firstProduct));
        double discountedPrice=  firstProduct.getPrice() * 0.8;
        System.out.println(discountedPrice);
        productsService.updatePrice(discountedPrice,1);
          }

    @Test
    void discountedPercentage() {

        Products firstProduct=Products.builder()
                .id(1)
                .name("SA1")
                .price(1000)
                .description("about SA1")
                .sellerName("DK")
                .imageName("SA1.jpg")
                .build();

        given(productsRepositoryMock.findById(1)).willReturn(Optional.of(firstProduct));
        int discountedPercentage=20;
        productsService.discountedPercentage(discountedPercentage,firstProduct.getId());
    }

    @Test
    void getProductsByMaxView() {


        Products firstProduct=Products.builder()
                .id(1)
                .name("SA1")
                .price(1000)
                .description("about SA1")
                .sellerName("DK")
                .imageName("SA1.jpg")
                .view(10)
                .build();
        Products secondProduct=Products.builder()
                .id(2)
                .name("SA2")
                .price(2000)
                .description("about SA2")
                .sellerName("DK")
                .imageName("SA2.jpg")
                .view(20)
                .build();
        Products thirdProduct=Products.builder()
                .id(3)
                .name("SA3")
                .price(3000)
                .description("about SA3")
                .sellerName("DK")
                .imageName("SA3.jpg")
                .view(30)
                .build();
        Products fourthProduct=Products.builder()
                .id(4)
                .name("SA4")
                .price(4000)
                .description("about SA4")
                .sellerName("DK")
                .imageName("SA4.jpg")
                .view(40)
                .build();
        List<Products>productsList=new ArrayList<>();
        productsList.add(firstProduct);
        productsList.add(secondProduct);
        productsList.add(thirdProduct);
        productsList.add(fourthProduct);

        given(productsRepositoryMock.findAll()).willReturn(productsList);
        List<Integer> integerList = productsRepositoryMock.findAll().stream().map(a -> a.getView()).collect(Collectors.toList());

        given(productsRepositoryMock.findView()).willReturn(integerList);

        productsService.getProductsByMaxView();

        assertThat(integerList).asList().isSubsetOf(productsRepositoryMock.findView());
    }

    @Test
    void findByNameLike() {


        Products firstProduct=Products.builder()
                .id(1)
                .name("samsung1")
                .price(1000)
                .description("about firstLenovo")
                .sellerName("DK")
                .imageName("firstLenovo.jpg")
                .view(10)
                .build();
        Products secondProduct=Products.builder()
                .id(2)
                .name("samsung2")
                .price(2000)
                .description("about secondLenovo")
                .sellerName("DK")
                .imageName("secondLenovo.jpg")
                .view(20)
                .build();
        Products thirdProduct=Products.builder()
                .id(3)
                .name("samsung3")
                .price(3000)
                .description("about thirdLenovo")
                .sellerName("DK")
                .imageName("thirdLenovo.jpg")
                .view(30)
                .build();
        Products fourthProduct=Products.builder()
                .id(4)
                .name("samsung4")
                .price(4000)
                .description("about fourthLenovo")
                .sellerName("DK")
                .imageName("fourthLenovo.jpg")
                .view(40)
                .build();
        List<Products>productsList=new ArrayList<>();
        productsList.add(firstProduct);
        productsList.add(secondProduct);
        productsList.add(thirdProduct);
        productsList.add(fourthProduct);

        given(productsRepositoryMock.findAll()).willReturn(productsList);
        List<String> ListLenovo = productsRepositoryMock.findAll().stream().map(a -> a.getName()).collect(Collectors.toList());

        given(productsRepositoryMock.findByNameLike("samsung")).willReturn(productsList);


        List<Products> byNameLike = productsService.findByNameLike();
        //List<String> stringLenovo = byNameLike.stream().map(a -> a.getName()).collect(Collectors.toList());

        assertThat(productsList.size()).isEqualTo(ListLenovo.size());


    }

    @Test
    void getStockProducts() {

        Products fourthProduct=Products.builder()
                .id(4)
                .name("fourthLenovo")
                .price(4000)
                .description("about fourthLenovo")
                .sellerName("DK")
                .imageName("fourthLenovo.jpg")
                .stock(5)
                .build();
        given(productsRepositoryMock.findById(4)).willReturn(Optional.of(fourthProduct));
        Optional<Integer> first = productsRepositoryMock.findById(4).stream().map(a -> a.getStock()).findFirst();
        System.out.println(first);

        Products stockProducts = productsService.getStockProducts(fourthProduct.getId());

        assertThat(stockProducts.getStock()).isEqualTo(fourthProduct.getStock());
    }

}