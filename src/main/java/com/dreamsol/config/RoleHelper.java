package com.dreamsol.config;

import com.dreamsol.repositories.EndPointsMappingRepo;
import org.springframework.stereotype.Component;
import com.dreamsol.entities.EndpointMappings;
import com.dreamsol.entities.Role;
import com.dreamsol.repositories.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Component
public class RoleHelper
{
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private EndPointsMappingRepo endPointsMappingRepo;

    @Autowired
    private VendorEndpointsHelper vendorEndpointsHelper;

    private final Map<String,String[]> allRoleAndPermissionMap;

    public RoleHelper(Map<String,String[]> allRoleAndPermissionMap)
    {
        this.allRoleAndPermissionMap = allRoleAndPermissionMap;
        allRoleAndPermissionMap.put("ROLE_DEVELOPER",new String[]{"ACCESS_ALL"});
        allRoleAndPermissionMap.put("ROLE_ALL",new String[]{"ACCESS_ALL"});
    }
    public Map<String,String[]> getAllRoleAndPermissionMap()
    {
        List<Role> roles = roleRepo.findAll();
        for(Role role : roles)
        {
            String roleType = role.getRole();
            String[] endPoints = role.getEndPoints()
                    .stream()
                    .map(EndpointMappings::getEndPointKey)
                    .toList()
                    .toArray(new String[0]);
            allRoleAndPermissionMap.put("ROLE_"+roleType,endPoints);
        }
        return allRoleAndPermissionMap;
    }
}

