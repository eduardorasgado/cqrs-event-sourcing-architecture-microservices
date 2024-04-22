package com.supplieswind.user.query.api.handlers;

import com.supplieswind.user.query.api.dto.UserLookupResponse;
import com.supplieswind.user.query.api.queries.FindAllUsersQuery;
import com.supplieswind.user.query.api.queries.FindUserByIdQuery;
import com.supplieswind.user.query.api.queries.SearchUsersQuery;

public interface UserQueryHandler {
    UserLookupResponse on(FindUserByIdQuery query);
    UserLookupResponse on(FindAllUsersQuery query);
    UserLookupResponse on(SearchUsersQuery query);
}
