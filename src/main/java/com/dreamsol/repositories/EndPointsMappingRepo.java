package com.dreamsol.repositories;

import com.dreamsol.entities.EndpointMappings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EndPointsMappingRepo extends JpaRepository<EndpointMappings,String> {
}
