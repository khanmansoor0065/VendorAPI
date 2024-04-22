package com.dreamsol.config;

import com.dreamsol.security.JwtAuthenticationEntryPoint;
import com.dreamsol.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.*;

@Configuration
public class SecurityConfig
{
    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Autowired
    private JwtAuthenticationFilter filter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VendorEndpointsHelper vendorEndpointsHelper;

    @Autowired
    private RoleHelper roleAndPermissionHelper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth->
                                auth.requestMatchers("/auth/**","/swagger-ui/**","/v3/api-docs/**","/role/**")
                                       .permitAll().requestMatchers("/vendor/add").permitAll()
                                        .requestMatchers("/vendor/**").authenticated())
                .exceptionHandling(ex->ex.authenticationEntryPoint(point))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public void updateSecurityConfig()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Map<String, String[]> allRoleAndPermissionMap = roleAndPermissionHelper.getAllRoleAndPermissionMap();
            Map<String,String> endpointMappings = vendorEndpointsHelper.getVendorEndpoints();
            Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();
            Set<String> authorityTypes = new HashSet<>();
            Set<String> patternsList = new HashSet<>();

            for (GrantedAuthority authority : userAuthorities) {
                authorityTypes.add(authority.getAuthority());
                String[] endpoints = allRoleAndPermissionMap.getOrDefault(authority.getAuthority(), new String[0]);
                patternsList.addAll(Arrays.stream(endpoints)
                        .map(endpointMappings::get)
                        .toList());
            }

            httpSecurity.authorizeHttpRequests(auth -> auth
                    .requestMatchers(patternsList.toArray(new String[0]))
                    .hasAnyAuthority(authorityTypes.toArray(new String[0]))
            );
        }
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }
}