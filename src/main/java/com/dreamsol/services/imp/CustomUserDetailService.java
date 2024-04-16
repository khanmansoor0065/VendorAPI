package com.dreamsol.services.imp;

import com.dreamsol.config.CustomUserDetails;
import com.dreamsol.entities.Vendor;
import com.dreamsol.exceptions.VendorNotFoundException;
import com.dreamsol.repositories.VendorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private VendorRepo vendorRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Vendor vendor = vendorRepo.findByEmail(username);
        if (vendor == null) {
            try {
                throw new VendorNotFoundException("Vendor not found with username: " + username);
            } catch (VendorNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return new CustomUserDetails(vendor);
    }
}
