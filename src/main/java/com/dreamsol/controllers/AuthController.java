package com.dreamsol.controllers;

import com.dreamsol.config.CustomUserDetails;
import com.dreamsol.config.VendorEndpointsHelper;
import com.dreamsol.dto.ApiResponse;
import com.dreamsol.entities.*;
import com.dreamsol.repositories.EndPointsMappingRepo;
import com.dreamsol.security.JwtHelper;
import com.dreamsol.services.imp.RefreshTokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;

    @Autowired
    private VendorEndpointsHelper endpointsHelper;

    @Autowired
    private EndPointsMappingRepo endPointsMappingRepo;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

        this.doAuthenticate(request.getUsername(), request.getPassword());


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.helper.generateToken(userDetails);

        RefreshToken refreshToken=this.refreshTokenService.createRefreshToken(userDetails.getUsername());

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .refreshToken(refreshToken.getRefreshToken())
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public JwtResponse refreshJwtToken(@RequestBody RefreshTokenRequest request) throws Exception {
        RefreshToken refreshToken=refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        Vendor vendor=refreshToken.getVendor();
        CustomUserDetails userDetails = new CustomUserDetails(vendor);
        String token=this.helper.generateToken(userDetails);
        return JwtResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .jwtToken(token)
                .username(vendor.getEmail())
                .build();
    }


    @PutMapping("/update-endpoints")
    public ResponseEntity<?> updateEndpoints()
    {
        for (Map.Entry<String, String> entry : endpointsHelper.getVendorEndpoints().entrySet()) {
            String endpointKey = entry.getKey();
            String endpointLink = entry.getValue();

            EndpointMappings endpointMapping = new EndpointMappings();
            endpointMapping.setEndPointKey(endpointKey);
            endpointMapping.setEndPointLink(endpointLink);

            endPointsMappingRepo.save(endpointMapping);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(" Endpoints updated successfully",true));
    }


    private void doAuthenticate(String username, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            manager.authenticate(authentication);


        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }


}

