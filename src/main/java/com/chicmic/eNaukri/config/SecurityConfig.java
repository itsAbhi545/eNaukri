package com.chicmic.eNaukri.config;

import com.chicmic.eNaukri.filter.CustomAuthenticationFilter;
import com.chicmic.eNaukri.filter.CustomAuthorizationFilter;
import com.chicmic.eNaukri.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.chicmic.eNaukri.ENaukriApplication.passwordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {

    private final UserDetailsService userDetailsService;
    @Autowired
    private final UserServiceImpl userService;
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

    //Filter objects
        CustomAuthenticationFilter authenticationFilter=
                new CustomAuthenticationFilter(userService,authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)));
        authenticationFilter.setFilterProcessesUrl("/api/login");
//        CustomAuthenticationFilter authenticationFilter1=
//                new CustomAuthenticationFilter(userService,authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)));
//        authenticationFilter.setFilterProcessesUrl("/company-login");

    //csrf+session
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    //permits
        http.authorizeHttpRequests().requestMatchers("/user/**","/company/**").hasAnyAuthority("USER");
        http.authorizeHttpRequests().anyRequest().permitAll();

    //adding filters
        http.addFilterBefore(new CustomAuthorizationFilter(userService), UsernamePasswordAuthenticationFilter.class);
        http.addFilter(authenticationFilter);

    //building
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
