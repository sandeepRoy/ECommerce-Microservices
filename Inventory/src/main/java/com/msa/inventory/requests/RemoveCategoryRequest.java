package com.msa.inventory.requests;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveCategoryRequest {
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private Integer category_id;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String category_name;
}
