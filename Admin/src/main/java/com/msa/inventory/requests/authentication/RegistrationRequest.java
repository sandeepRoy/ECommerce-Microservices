package com.msa.inventory.requests.authentication;

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
public class RegistrationRequest {
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String firstname;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String lastname;

    @NotNull
    private String email;
    @NotNull
    private String password;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String role;
}
