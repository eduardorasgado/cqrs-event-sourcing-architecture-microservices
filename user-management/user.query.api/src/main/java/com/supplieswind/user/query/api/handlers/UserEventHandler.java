package com.supplieswind.user.query.api.handlers;

import com.supplieswind.user.core.events.UserRegisteredEvent;
import com.supplieswind.user.core.events.UserRemovedEvent;
import com.supplieswind.user.core.events.UserUpdatedEvent;

public interface UserEventHandler {
    void on(UserRegisteredEvent event);
    void on(UserUpdatedEvent event);
    void on(UserRemovedEvent event);
}
