package com.chicmic.eNaukri.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.model.Authority;
import com.chicmic.eNaukri.model.UserRole;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.service.UserServiceImpl;
import com.chicmic.eNaukri.service.UsersService;
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

    private UsersService usersService;

    public  CustomAuthorizationFilter(UsersService usersService){
        this.usersService=usersService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("\u001B[35m" + request.getServletPath() + "\u001B[0m");

        if(request.getServletPath().contains("/user/")||(request.getServletPath().contains("/company/")
                && !request.getServletPath().contains("/company/signup") && !request.getServletPath().contains("/company/search"))

        ){
            String token=request.getHeader("Authorization");
            if(token==null || token.substring(7).isEmpty() || usersService.findUserFromUUID(token.substring(7))==null){
                Map<String,String> error=new HashMap<>();
                error.put("error_message","Please provide valid token");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
            else if(token!=null) {
                token = token.substring(7);
                String user = JWT.require(Algorithm.HMAC256(SECRET.getBytes()))
                        .build()
                        .verify(token)
                        .getSubject();
                System.out.println("\u001B[35m" + user + "\u001B[0m");
                System.out.println("/jefgegfyugeryfg");
                Users temp= usersService.getUserByEmail(user);
                List<UserRole> userRoleList = usersService.findRolesByUser(temp);
                Collection<Authority> authorities=new ArrayList<>();
                for (UserRole role : userRoleList) {
                    authorities.add(new Authority(role.getRoleId().getRoleName()));
                }
                for(Authority authority : authorities) {
                    System.out.println("\u001B[33m" + authority.getAuthority() + "\u001B[0m");
                }
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                        new UsernamePasswordAuthenticationToken(temp.getEmail(),null,authorities);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                filterChain.doFilter(request,response);
            }
        }
        else {
            System.out.println("savhvsfajvfsaabjnbsfa");
            filterChain.doFilter(request,response);
        }
    }
}
