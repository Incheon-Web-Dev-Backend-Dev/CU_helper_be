package com.example.cu_helper_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private Long consumerPrice;
    private Boolean isEvent;
    private Short eventType;
    private Long quantity;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdAt;
    private String image;
    private String description;
    private Long barcode;
}
