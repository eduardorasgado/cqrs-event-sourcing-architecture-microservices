package com.supplieswind.user.core.events;

import com.supplieswind.user.core.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdatedEvent {
    private String id;
    private User user;
}
