package com.dreamsol.config;

import com.dreamsol.entities.EndpointMappings;
import com.dreamsol.entities.Permission;
import com.dreamsol.entities.Role;
import com.dreamsol.repositories.PermissionRepo;
import com.dreamsol.repositories.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RoleAndPermissionHelper {
    @Autowired
    private PermissionRepo permissionRepo;

    @Autowired
    private RoleRepo roleRepo;

//    @Autowired
//    private EndPointsMappingRepo endPointsMappingRepo;

//    @Autowired
//    private VendorEndpointsHelper vendorEndpointsHelper;

    private final Map<String, String[]> allRoleAndPermissionMap;

    public RoleAndPermissionHelper(Map<String, String[]> allRoleAndPermissionMap) {
        this.allRoleAndPermissionMap = allRoleAndPermissionMap;
    }

    public Map<String, String[]> getAllRoleAndPermissionMap() {
        List<Role> roles = roleRepo.findAll();
        List<Permission> permissions = permissionRepo.findAll();
        for (Role role : roles) {
            String roleType = role.getRole();
            String[] endPoints = role.getEndPoints()
                    .stream()
                    .map(EndpointMappings::getEndPointKey)
                    .toList()
                    .toArray(new String[0]);
            allRoleAndPermissionMap.put(roleType, endPoints);
        }
        for (Permission permission : permissions) {
            String permissionType = permission.getPermission();
            String[] endPoints = permission.getEndPoints()
                    .stream()
                    .map(EndpointMappings::getEndPointKey)
                    .toList()
                    .toArray(new String[0]);
            allRoleAndPermissionMap.put(permissionType, endPoints);
        }
        System.out.println();
        return allRoleAndPermissionMap;

    }
}