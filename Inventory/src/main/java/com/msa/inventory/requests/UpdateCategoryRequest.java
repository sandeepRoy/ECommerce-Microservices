package com.msa.inventory.requests;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryRequest {
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private Integer category_id;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String category_old_name;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String category_new_name;
}
