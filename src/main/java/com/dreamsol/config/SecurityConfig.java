package com.dreamsol.config;

import com.dreamsol.security.JwtAuthenticationEntryPoint;
import com.dreamsol.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.*;

@Configuration
@EnableWebSecurity
@EnableWebMvc
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
    private AccessDeniedHandler accessDeniedHelper;

    @Autowired
    private VendorEndpointsHelper vendorEndpointsHelper;

    @Autowired
    private RoleAndPermissionHelper roleAndPermissionHelper;

    private HttpSecurity httpSecurity;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        this.httpSecurity=http;
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth->
                                auth.requestMatchers(
                                                "/role/**",
                                                "/permission/**",
                                                "/auth/**",
                                                "/swagger-ui/**",
                                                "/v3/api-docs/**")
                                       .permitAll());
                http.exceptionHandling(ex->ex.authenticationEntryPoint(point).accessDeniedHandler(accessDeniedHelper))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public void updateSecurity() throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            List<String> authList=authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            Map<String, String[]> allRoleAndPermissionMap = roleAndPermissionHelper.getAllRoleAndPermissionMap();
            Map<String,String> endpointMappings = vendorEndpointsHelper.getVendorEndpoints();
            List<String> roleUrls = new ArrayList<String>();
            Set<String> keys = allRoleAndPermissionMap.keySet();
            for (String key : keys) {
                if (key.equalsIgnoreCase(authList.get(1))) {
                    List<String> urlkey = List.of(allRoleAndPermissionMap.get(key));
                    for (String url : urlkey) {
                        Set<String> urlMapKeys = endpointMappings.keySet();
                        for (String urlMapKey : urlMapKeys) {
                            if (urlMapKey.equalsIgnoreCase(url)) {
                                roleUrls.add(endpointMappings.get(urlMapKey));
                            }
                        }
                    }
                }
            }
            List<String> permissionUrls = new ArrayList<String>();
            for (String key : keys) {
                if (key.equalsIgnoreCase(authList.get(0))) {
                    List<String> urlkey = List.of(allRoleAndPermissionMap.get(key));
                    for (String url : urlkey) {
                        Set<String> urlMapKeys = endpointMappings.keySet();
                        for (String urlMapKey : urlMapKeys) {
                            if (urlMapKey.equalsIgnoreCase(url)) {
                                permissionUrls.add(endpointMappings.get(urlMapKey));
                            }
                        }
                    }
                }
            }

            List<String> authorizedurls = permissionUrls.stream().filter(roleUrls::contains)
                    .toList();
            String[] URLS = new String[authorizedurls.size()];
            for (int x = 0; x < authorizedurls.size(); x++) {
                URLS[x] = authorizedurls.get(x);

    }
            Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();

            Set<String> authorityTypes = new HashSet<>();
            Set<String> patternsList = new HashSet<>();

            for (GrantedAuthority authority : userAuthorities)
            {
                authorityTypes.add(authority.getAuthority());
                String[] endpoints = allRoleAndPermissionMap.getOrDefault(authority.getAuthority(), new String[0]);
                patternsList.addAll(Arrays.stream(endpoints)
                        .map(endpointMappings::get)
                        .toList());
            }
            this.httpSecurity. authorizeHttpRequests(auth -> auth
                    .requestMatchers(URLS)
                    .hasAnyAuthority(authorityTypes.toArray(new String[0]))
            );
            System.out.println(Arrays.stream(URLS).toList());
            System.out.println(Arrays.toString(authorityTypes.toArray(new String[0])));
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