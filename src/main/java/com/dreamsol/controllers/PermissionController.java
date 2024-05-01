package com.dreamsol.controllers;

import com.dreamsol.dto.PermissionDto;
import com.dreamsol.services.imp.PermissionServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("permission")
public class PermissionController
{
    @Autowired
    private PermissionServiceImpl permissionService;

    @PostMapping("add")
    public ResponseEntity<?> addPermission(@Valid @RequestBody PermissionDto permissionDto)
    {
        return permissionService.addPermission(permissionDto);
    }

    @GetMapping("get-all")
    public ResponseEntity<List<PermissionDto>> fetchAllPermission()
    {
        return permissionService.getAllPermission();
    }

    @DeleteMapping("delete/{permissionId}")
    public ResponseEntity<?> deletePermission(@PathVariable Integer permissionId)
    {
        return permissionService.deletePermission(permissionId);
    }

    @PutMapping("update/{roleId}")
    public ResponseEntity<?> updatePermission(@RequestBody PermissionDto permissionDto,
                                        @PathVariable Integer permissionId)
    {
        return permissionService.updatePermission(permissionDto,permissionId);
    }

}
