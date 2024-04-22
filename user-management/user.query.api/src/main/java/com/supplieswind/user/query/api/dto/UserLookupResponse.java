package com.supplieswind.user.query.api.dto;

import com.supplieswind.user.core.dto.BaseResponse;
import com.supplieswind.user.core.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserLookupResponse extends BaseResponse {
    private List<User> users;

    public UserLookupResponse(List<User> users) {
        this(null, users);
    }

    public UserLookupResponse(String message, List<User> users) {
        super(message);
        this.users = new ArrayList<>();
        this.users.addAll(users);
    }

    @Builder
    public UserLookupResponse(String message, User user) {
        super(message);

        users = new ArrayList<>();
        users.add(user);
    }

    public UserLookupResponse(String message) {
        super(message);
        users = new ArrayList<>();
    }
}
