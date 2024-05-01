package com.dreamsol.services.imp;

import com.dreamsol.config.VendorEndpointsHelper;
import com.dreamsol.dto.ApiResponse;
import com.dreamsol.dto.PermissionDto;
import com.dreamsol.entities.EndpointMappings;
import com.dreamsol.entities.Permission;
import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.repositories.PermissionRepo;
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
public class PermissionServiceImpl
{
    @Autowired
    private PermissionRepo permissionRepo;

    @Autowired
    private VendorEndpointsHelper endpointsHelper;

    public ResponseEntity<?> addPermission(PermissionDto permissionDto)
    {
        Permission DbPermission=permissionRepo.findByPermission(permissionDto.getPermission());
        if (Objects.isNull(DbPermission))
            {
                Map<String,String> endPoints=endpointsHelper.getVendorEndpoints();
                Permission permission=new Permission();
                BeanUtils.copyProperties(permissionDto,permission);
                permission.setEndPoints(
                        permissionDto.getEndPoints()
                                .stream()
                                .map((endPointKey)->new EndpointMappings(endPointKey,endPoints.get(endPointKey)))
                                .toList()
                );
                permissionRepo.save(permission);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Permission '" + permissionDto.getPermission() + "' created successfully!", true));
            } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(permissionDto.getPermission() + " Permission already exist!", false));
        }
    }

    public ResponseEntity<List<PermissionDto>> getAllPermission()
    {
        List<Permission> permissionList = permissionRepo.findAll();
        List<PermissionDto> permissionDtoList = permissionList.stream().map(this::permissionToPermissionDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(permissionDtoList);
    }
    private PermissionDto permissionToPermissionDto(Permission permission) {
        PermissionDto permissionDto = new PermissionDto();
        BeanUtils.copyProperties(permission, permissionDto);
        permissionDto.setEndPoints(
                permission.getEndPoints().stream()
                        .map(EndpointMappings::getEndPointKey)
                        .collect(Collectors.toList())
        );

        return permissionDto;
    }

    public ResponseEntity<?> deletePermission(Integer permissionId)
    {
        Permission permission=permissionRepo.findById(permissionId).orElseThrow(()-> new ResourceNotFoundException("Permission", "Id", permissionId));
        permissionRepo.deleteById(permissionId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Permission deleted Successfully", true), HttpStatus.OK);
    }

    public ResponseEntity<?> updatePermission(PermissionDto permissionDto, Integer permissionId) {
        Map<String,String> endPoints=endpointsHelper.getVendorEndpoints();
        Permission Dbpermission=permissionRepo.findById(permissionId).orElseThrow(()-> new ResourceNotFoundException("Permission", "Id", permissionId));
        Permission permission=new Permission();
        BeanUtils.copyProperties(permissionDto,permission);
        permission.setEndPoints(
                permissionDto.getEndPoints()
                        .stream()
                        .map((endPointKey)->new EndpointMappings(endPointKey,endPoints.get(endPointKey)))
                        .toList()
        );
        permissionRepo.save(permission);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Permission " + permissionDto.getPermission() + " updated successfully!", true));
    }
}
