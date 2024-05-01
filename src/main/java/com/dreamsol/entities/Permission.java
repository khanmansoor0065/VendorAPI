package com.dreamsol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Permission
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String permission;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "permission_endpoints", joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "endPointKey"))
    private List<EndpointMappings> endPoints;
}
