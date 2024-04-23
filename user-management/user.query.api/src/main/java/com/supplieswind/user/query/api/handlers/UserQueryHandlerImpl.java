package com.supplieswind.user.query.api.handlers;

import com.supplieswind.user.core.models.User;
import com.supplieswind.user.query.api.dto.UserLookupResponse;
import com.supplieswind.user.query.api.queries.FindAllUsersQuery;
import com.supplieswind.user.query.api.queries.FindUserByIdQuery;
import com.supplieswind.user.query.api.queries.SearchUsersQuery;
import com.supplieswind.user.query.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserQueryHandlerImpl implements UserQueryHandler{
    private final UserRepository repository;

    @QueryHandler
    @Override
    public UserLookupResponse on(FindUserByIdQuery query) {
        User user = repository
                .findById(query.getId())
                .orElseThrow(NoSuchElementException::new);

        return new UserLookupResponse(List.of(user));
    }

    @QueryHandler
    @Override
    public UserLookupResponse on(FindAllUsersQuery query) {
        return new UserLookupResponse(repository.findAll());
    }

    @QueryHandler
    @Override
    public UserLookupResponse on(SearchUsersQuery query) {
        return new UserLookupResponse(repository.findAll(query.getFilter()));
    }
}
