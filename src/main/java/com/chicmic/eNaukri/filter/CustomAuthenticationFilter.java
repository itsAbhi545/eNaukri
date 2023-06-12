package com.chicmic.eNaukri.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.shaded.org.eclipse.jetty.util.ajax.JSON;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.AlgorithmConstraints;
import java.security.AlgorithmParameters;
import java.util.*;

import static com.chicmic.eNaukri.config.SecurityConstants.EXPIRATION_TIME;
import static com.chicmic.eNaukri.config.SecurityConstants.SECRET;
import static org.apache.hadoop.util.VersionInfo.getUser;
//import static org.apache.zookeeper.server.util.AuthUtil.getUser;


@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private UserServiceImpl userService;


    public CustomAuthenticationFilter(UserServiceImpl userService, AuthenticationManager authenticationManager) {
        this.userService=userService;
        this.authenticationManager=authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    // sending username, password to authentication manager
        String username=request.getParameter("username");
        String password=request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username,password);
        return authenticationManager.authenticate(authenticationToken);

    }

    @Override
    protected void successfulAuthentication
            (HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

    // after successful login
        Collection<Authority> authorities=new ArrayList<>();
        authorities.add(new Authority("USER"));

        Users loggedInUser=userService.getUserByEmail(authResult.getName());
        String token = JWT.create()
                .withSubject(((User) authResult.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET.getBytes()));
        UserToken userToken=UserToken.builder().userr(loggedInUser).token(token).build();

    // saving uuid & updating cookies
        userService.saveUUID(userToken);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(loggedInUser,null,authorities);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//        String body = ((User) authResult.getPrincipal()).getUsername() + " " + token;
//
//        response.getWriter().write(body);
//        response.getWriter().flush();
        Map<String,String> hashMap=new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("email",request.getParameter("username"));
        new ObjectMapper().writeValue(response.getOutputStream(),hashMap);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        Map<String, Object> error = new HashMap<>();
        error.put("message", failed.getLocalizedMessage());
        if(failed instanceof UsernameNotFoundException){
            error.put("message","Bad Credentials");
        }
        error.put("code", 401);
        error.put("path", request.getServletPath());
        error.put("timestamp", System.currentTimeMillis());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        new ObjectMapper().writeValue(response.getOutputStream(),error);
    }
}
