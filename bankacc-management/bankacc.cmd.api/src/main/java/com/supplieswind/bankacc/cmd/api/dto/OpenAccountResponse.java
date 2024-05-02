package com.supplieswind.bankacc.cmd.api.dto;

import com.supplieswind.bankacc.core.dto.BaseResponse;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OpenAccountResponse extends BaseResponse {
    private String id;

    @Builder
    public OpenAccountResponse(String id, String message) {
        super(message);
        this.id = id;
    }

    public OpenAccountResponse(String id) {
        this.id = id;
    }
}
