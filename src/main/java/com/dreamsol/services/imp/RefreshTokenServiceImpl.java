package com.dreamsol.services.imp;

import com.dreamsol.entities.RefreshToken;
import com.dreamsol.entities.Vendor;
import com.dreamsol.repositories.RefreshTokenRepo;
import com.dreamsol.repositories.VendorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl {

    @Autowired
    private VendorRepo vendorRepo;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    public  long refreshTokenValidity=5*60*60*10000;

    public RefreshToken createRefreshToken(String username) {
        Vendor vendor = vendorRepo.findByEmail(username);
        RefreshToken refreshToken1=vendor.getRefreshToken();

        String refreshTokenValue = UUID.randomUUID().toString();

        Instant expiryTime = Instant.now().plusMillis(refreshTokenValidity);

        if(refreshToken1==null) {
            refreshToken1 = RefreshToken.builder()
                    .refreshToken(refreshTokenValue)
                    .expiry(expiryTime)
                    .vendor(vendor)
                    .build();
        }else {
            refreshToken1.setExpiry(expiryTime);
        }
        vendor.setRefreshToken(refreshToken1);
        refreshTokenRepo.save(refreshToken1);

        return refreshToken1;
    }

    public RefreshToken verifyRefreshToken(String refreshToken) throws Exception {
        RefreshToken refreshToken1=refreshTokenRepo.findByRefreshToken(refreshToken).orElseThrow(()-> new RuntimeException("Given Token does not exists "));
        if(refreshToken1.getExpiry().compareTo(Instant.now())<0){
            refreshTokenRepo.delete(refreshToken1);
            throw new Exception("Refresh Token expired!!");
        }else {
            return refreshToken1;
        }


    }


}
