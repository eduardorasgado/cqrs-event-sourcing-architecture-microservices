package com.supplieswind.user.query.api.repositories;

import com.supplieswind.user.core.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
