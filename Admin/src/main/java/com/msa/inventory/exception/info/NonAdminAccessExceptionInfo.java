package com.msa.inventory.exception.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NonAdminAccessExceptionInfo {
    private String message;
    private String code;
}
