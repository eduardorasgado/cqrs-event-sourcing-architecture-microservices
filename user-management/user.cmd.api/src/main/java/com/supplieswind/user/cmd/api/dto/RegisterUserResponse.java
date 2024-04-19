package com.supplieswind.user.cmd.api.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterUserResponse extends BaseResponse {
    private String id;

    @Builder
    public RegisterUserResponse(String id, String message) {
        super(message);
        this.id = id;
    }

    public RegisterUserResponse(String message) {
        super(message);
    }
}
