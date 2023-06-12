package com.chicmic.eNaukri.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.model.Authority;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.*;
import static com.chicmic.eNaukri.config.SecurityConstants.SECRET;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private UserServiceImpl userService;

    public  CustomAuthorizationFilter(UserServiceImpl userService){
        this.userService=userService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(request.getServletPath());

        if(request.getServletPath().contains("/user/")||request.getServletPath().contains("/company/")){
            String token=request.getHeader("Authorization").substring(7);
            if(token==null || userService.findUserFromUUID(token)==null){
                Map<String,String> error=new HashMap<>();
                error.put("error_message","Please provide valid token");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
            else if(token!=null) {
                String user = JWT.require(Algorithm.HMAC256(SECRET.getBytes()))
                        .build()
                        .verify(token)
                        .getSubject();
                System.out.println(user+"/jefgegfyugeryfg");
                Users temp= userService.getUserByEmail(user);
                Collection<Authority> authorities=new ArrayList<>();
                authorities.add(new Authority("USER"));

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                        new UsernamePasswordAuthenticationToken(temp,null,authorities);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                filterChain.doFilter(request,response);
            }
        }
        else filterChain.doFilter(request,response);
    }
}
