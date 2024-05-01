package com.dreamsol.security;

import com.dreamsol.entities.Permission;
import com.dreamsol.entities.Role;
import com.dreamsol.entities.Vendor;
import com.dreamsol.repositories.VendorRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtHelper {

    @Autowired
    private VendorRepo vendorRepo;

    @Value("${jwtToken.validity}")
    public String tokenTime;

    public static final long JWT_TOKEN_VALIDITY = 1000;

    private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Vendor vendor=vendorRepo.findByEmail(userDetails.getUsername());
        Map<String, Object> payload = new HashMap<>();
        payload.put("Id",vendor.getId());
        payload.put("Name",vendor.getName());
        payload.put("Email",vendor.getEmail());
        payload.put("Mobile",vendor.getMob());
        payload.put("Vendor Type",vendor.getVendorType().getTypeName());
        payload.put("Role",vendor.getRoles().stream().map(Role::getRole).collect(Collectors.toList()));
        payload.put("Permission",vendor.getPermissions().stream().map(Permission::getPermission).collect(Collectors.toList()));
        return doGenerateToken(payload, userDetails.getUsername());
    }


    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * Integer.parseInt(tokenTime)))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}