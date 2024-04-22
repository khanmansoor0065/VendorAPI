package com.dreamsol.repositories;

import com.dreamsol.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer>
{
    Role findByRole(String roleName);

}
