package com.dreamsol.repositories;

import com.dreamsol.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepo extends JpaRepository<Permission, Integer>
{
    Permission findByPermission(String permission);
}
