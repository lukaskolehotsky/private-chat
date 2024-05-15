package com.bench.privatechat.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {

    private String username;

    private String email;

}
