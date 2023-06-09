package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface UsersRepositoryWithBagRelationships {
    Optional<Users> fetchBagRelationships(Optional<Users> users);

    List<Users> fetchBagRelationships(List<Users> users);

    Page<Users> fetchBagRelationships(Page<Users> users);
}
