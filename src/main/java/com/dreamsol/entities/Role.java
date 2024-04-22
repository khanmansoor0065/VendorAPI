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
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_endpoints", joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "endPointKey"))
    private List<EndpointMappings> endPoints;
}