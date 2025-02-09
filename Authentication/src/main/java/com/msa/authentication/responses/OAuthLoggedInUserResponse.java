package com.msa.authentication.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuthLoggedInUserResponse {
    private String email_id;
    private String user_name;
}
