package com.dreamsol.controllers;

import com.dreamsol.dto.RoleDto;
import com.dreamsol.services.imp.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("role")
public class RoleController
{
    @Autowired
    private RoleServiceImpl roleService;

    @PostMapping("add")
    public ResponseEntity<?> addRole(@RequestBody RoleDto roleDto)
    {
        return roleService.addRole(roleDto);
    }

    @GetMapping("get-all")
    public ResponseEntity<List<RoleDto>> fetchAllRole()
    {
        return roleService.getAllRoles();
    }

    @DeleteMapping("delete/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable Integer roleId)
    {
        return roleService.deleteRole(roleId);
    }

    @PutMapping("update/{roleId}")
    public ResponseEntity<?> updateRole(@RequestBody RoleDto roleDto,
                                        @PathVariable Integer roleId)
    {
        return roleService.updateRole(roleDto,roleId);
    }



}
