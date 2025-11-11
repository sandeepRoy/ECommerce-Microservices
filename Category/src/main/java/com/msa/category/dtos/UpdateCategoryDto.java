package com.msa.category.dtos;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCategoryDto {
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private Integer category_id;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String category_name_old;

    @NotNull
    private String category_name_new;
}
