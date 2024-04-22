package com.dreamsol.services.imp;

import com.dreamsol.dto.ApiResponse;
import com.dreamsol.dto.RoleDto;
import com.dreamsol.entities.EndpointMappings;
import com.dreamsol.entities.Role;
import com.dreamsol.config.VendorEndpointsHelper;
import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.repositories.RoleRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl
{
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private VendorEndpointsHelper endpointsHelper;

    public ResponseEntity<?> addRole(RoleDto roleDto)
    {
        Role Dbrole=roleRepo.findByRole(roleDto.getRole());
        if (Objects.isNull(Dbrole))
        {
            Map<String,String> endPoints=endpointsHelper.getVendorEndpoints();
            Role role=new Role();
            BeanUtils.copyProperties(roleDto,role);
            role.setEndPoints(
                    roleDto.getEndPoints()
                            .stream()
                            .map((endPointKey)->new EndpointMappings(endPointKey,endPoints.get(endPointKey)))
                            .toList()
            );
            roleRepo.save(role);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("New Role '" + roleDto.getRole() + "' created successfully!", true));
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(roleDto.getRole() + " Role already exist!", false));
        }

    }

    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<Role> roleList = roleRepo.findAll();
        List<RoleDto> roleDtoList = roleList.stream().map(this::roleToRoleDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(roleDtoList);
    }

    private RoleDto roleToRoleDto(Role role) {
        RoleDto roleDto = new RoleDto();
        BeanUtils.copyProperties(role, roleDto);
        roleDto.setEndPoints(
                role.getEndPoints().stream()
                        .map(EndpointMappings::getEndPointKey)
                        .collect(Collectors.toList())
        );

        return roleDto;
    }

    public ResponseEntity<?> deleteRole(Integer roleId) {
        Role role=roleRepo.findById(roleId).orElseThrow(()-> new ResourceNotFoundException("Role", "Id", roleId));
        roleRepo.deleteById(roleId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Role deleted Successfully", true), HttpStatus.OK);
    }

    public ResponseEntity<?> updateRole(RoleDto roleDto, Integer roleId) {
        Map<String,String> endPoints=endpointsHelper.getVendorEndpoints();
        Role Dbrole=roleRepo.findById(roleId).orElseThrow(()-> new ResourceNotFoundException("Role", "Id", roleId));
        Role role=new Role();
        BeanUtils.copyProperties(roleDto,role);
        role.setEndPoints(
                roleDto.getEndPoints()
                        .stream()
                        .map((endPointKey)->new EndpointMappings(endPointKey,endPoints.get(endPointKey)))
                        .toList()
        );
        roleRepo.save(role);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Role " + roleDto.getRole() + " updated successfully!", true));
    }
}
