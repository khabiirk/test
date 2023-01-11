package com.timeservice.authorizationserver;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;

/**
 * Controller that extracts user details, based on the passed oauth2 token.
 * <p>
 * Sample access: curl "http://127.0.0.1:8084/api/username?access_token=...="
 */
@RestController
public class IdentifyUserController {

    /**
     * Resolves the token to the list of groups, the owner adheres to.
     */
    @GetMapping(value = "/api/userroles")
    public Collection<SimpleGrantedAuthority> currentUserRole() {

        return (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }


    /**
     * Resolves the token to the owner's username
     *
     * @param principal
     * @return
     */
    @GetMapping(value = "/api/username")
    public String currentUserName(Principal principal) {
        return principal.getName();
    }
}
