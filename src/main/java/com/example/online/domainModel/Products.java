package com.example.online.domainModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(uniqueConstraints =
        { @UniqueConstraint(name = "Unique_name", columnNames = {"name" })
        })
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
@NotBlank
    public String name;
   @Positive
    public double price;
    @NotBlank
    public String sellerName;
    @NotBlank
    public String description;
    @PositiveOrZero
    public double discountedPrice;
    @PositiveOrZero
    public int DiscountedPercentage;
    @PositiveOrZero
    public int view;
    @PositiveOrZero
    public int stock;
    @NotNull
    @NotBlank
    private String imageName;

    @ManyToMany(cascade = CascadeType.MERGE)
            @JoinTable(
                    name = "product_category",
                   joinColumns =@JoinColumn (name = "product_id"),
                    inverseJoinColumns = @JoinColumn(name = "category_id")
            )
    @ToString.Exclude

    @NotNull
    @Size(min = 1)
    List<Category>categoryList=new ArrayList<>();


    public void addCategory(Category category)
    {
        categoryList.add(category);
        category.getProducts().add(this);
    }
    public void removeCategory(int category) {
        categoryList.remove(category);

    }

   }
