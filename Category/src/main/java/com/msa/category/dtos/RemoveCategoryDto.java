package com.msa.category.dtos;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.AnyKeyJavaClass;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoveCategoryDto {
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private Integer category_id;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String category_name;
}
