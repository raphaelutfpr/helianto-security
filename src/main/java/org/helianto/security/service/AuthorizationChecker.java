package org.helianto.security.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.helianto.security.internal.UserDetailsAdapter;
import org.helianto.security.repository.UserAuthorityReadAdapter;
import org.helianto.security.repository.UserAuthorityRepository;
import org.helianto.user.domain.UserGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * Service to update authorization.
 * 
 * @author mauriciofernandesdecastro
 */
@Service
public class AuthorizationChecker {

    private static Logger logger = LoggerFactory.getLogger(AuthorizationChecker.class);

	@Inject
    protected UserAuthorityRepository userAuthorityRepository;
    
	/**
	 * Updates authorities for the given user.
	 * 
	 * @param userDetailsAdapter
	 * @param parentGroups
	 */
	public UserDetailsAdapter updateAuthorities(UserDetailsAdapter userDetailsAdapter, List<UserGroup> parentGroups) {
		List<UserAuthorityReadAdapter> adapterList = userAuthorityRepository.findByUserGroupIdOrderByServiceCodeAsc(parentGroups);
        List<String> roleNames = UserAuthorityReadAdapter.getRoleNames(adapterList, userDetailsAdapter.getIdentityId());
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String roleName: roleNames) {
            authorities.add(new SimpleGrantedAuthority(roleName));
            logger.info("Granted authority: {}.", roleName);
        }
        userDetailsAdapter.setAuthorities(authorities);
        return userDetailsAdapter;
	}
	
}
