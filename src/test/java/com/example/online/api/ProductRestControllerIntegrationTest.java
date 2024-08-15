package com.example.online.api;

import com.example.online.api.exeptions.CustomNotfoundExceptions;
import com.example.online.domainModel.Products;
import com.example.online.dto.ProductDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class ProductRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addProductsInDatabase() throws Exception {
        Products products=Products.builder()
                .name("SA1")
                .price(1000)
                .description("about SA1")
                .sellerName("DK")
                .imageName("SA1.jpg")
                .build();
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/products/addProducts")
                .content(new ObjectMapper().writeValueAsString(products))
                .contentType(MediaType.APPLICATION_JSON)
        );
        result
                .andExpect(status().isCreated())
                .andExpect(r->assertThat(r.getResponse().getStatus()).isEqualTo(201))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void getAllProductFromDatabase() throws Exception {
        ResultActions allProducts = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/allProducts")
           //
        );


        allProducts
                .andExpect(status().isOk())

                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void getProductByExistsId() throws Exception {
        int id=2;

       MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get
               ("/api/products/getProductById/{id}", id)).andReturn();

        ObjectMapper objectMapper=new ObjectMapper();
        Products products = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Products.class);
        assertThat(products.getId()).isEqualTo(id);
        assertThat(products).isInstanceOf(Products.class);
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);


    }
    @Test
    void ShouldThrowCustomNotFoundExceptionIfIdDoesNotExistsInDatabase() throws Exception {
        int id=200;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get
                ("/api/products/getProductById/{id}", id));

       assertThat(resultActions.andExpect(status().isNotFound()));
        resultActions
                .andExpect(r->assertThat(r.getResolvedException()).isInstanceOf(
                        CustomNotfoundExceptions.class))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void getProductsByCategoryId() throws Exception {
        int categoryId=1;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get
                ("/api/products/getProductsByCategoryId/{Categoryid}", categoryId));

       resultActions.andExpect(status().isOk());
        resultActions
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void getProductByExistsName() throws Exception {
        String name="lenovo 10";
        ResultActions findName = mockMvc.perform(MockMvcRequestBuilders.get
                        ("/api/products/getProductByName")
                .param("name", name)
        );
        findName
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(r->assertThat(r.getResponse().getContentAsString().equals("lenovo ")));


    }
    @Test
    void shouldThrowCustomNotFoundExceptionIfDoesNotNameInDatabase() throws Exception {
        String name="lenovo5";
        ResultActions findName = mockMvc.perform(MockMvcRequestBuilders.get
                        ("/api/products/getProductByName")
                .param("name", name)
        );
        findName
                .andExpect(status().isNotFound())
                .andExpect(r->assertThat(r.getResolvedException()).isInstanceOf(CustomNotfoundExceptions.class)
                        .hasMessage(String.format("product with name:%s not found",name))
                );


    }

    @Test
    void getProductsByPatternName() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get
                ("/api/products/getProductsByPatternName")).andReturn();

      /*  ObjectMapper objectMapper=new ObjectMapper();
        Products products = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Products.class);
        assertThat(products).isInstanceOf(Products.class);*/
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);

    }

    @Test
    void getProductsByPrice() throws Exception {
        Map<String,String>params=new HashMap<>();

        int minimum=500;
        int maximum=2000;
        params.put("min", String.valueOf(minimum));
        params.put("max", String.valueOf(maximum));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get
                ("/api/products/getProductsByPrice?min=500&max=2000")
        );
        resultActions
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void filterByNames() throws Exception {

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get
                ("/api/products/filterProductsByNamesList?namesList=samsung s10,lenovo 10")
        );

        resultActions

                .andExpect(result -> assertThat(result.getResponse().getStatus()).isEqualTo(200))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isNotNull());


    }

    @Test
    void getProductsWithTheMostViews() throws Exception {

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get
                ("/api/products/getProductsWithTheMostViews")
        );



        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


    }

    @Test
    void getProductsWithDiscountedPrice() throws Exception {
        int id=16;

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get
                ("/api/products/getProductsWithDiscountedPrice/{id}",id)
        );



        resultActions
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getStatus()).isEqualTo(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getStockProducts() throws Exception {

        int id=5;
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get
                ("/api/products/getStockProducts/{id}",id)
        );



        resultActions
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getStatus()).isEqualTo(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}