/* Copyright 2005 I Serv Consultoria Empresarial Ltda.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.helianto.security.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.helianto.security.domain.IdentitySecret;
import org.helianto.user.repository.UserReadAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Models core user information retrieved by UserDetailsService as an adapter class
 * to {@link org.helianto.user.domain.User}.
 * 
 * <p>
 * A new <code>UserDetailsAdapter</code> may be created from a single
 * {@link org.helianto.user.domain.User} and the correspondent credential to be expected 
 * during authentication. A new <code>UserDetailsAdapter</code> may also be created from
 * a group with no credential specified, where the authentication is then considered 
 * to be anonymous.
 * </p>
 * 
 * @author Mauricio Fernandes de Castro
 */
public class UserDetailsAdapter
	implements
      Serializable
    , UserDetails
    , UserAuthentication {

    static final Logger logger = LoggerFactory.getLogger(UserDetailsAdapter.class);
    
	private static final long serialVersionUID = 1L;
	
	private UserReadAdapter userReadAdapter;

	private IdentitySecret identitySecret;
    
    private List<GrantedAuthority> authorities = new ArrayList<>();

    /**
     * Constructor
     */
    public UserDetailsAdapter() {
    	super();
    }
    
    /**
     * Constructor.
     * 
     * @param userReadAdapter
     */
    public UserDetailsAdapter(UserReadAdapter userReadAdapter) {
        this();
        this.userReadAdapter = userReadAdapter;
    }
    
    /**
     * Constructor.
     * 
     * @param userReadAdapter
     * @param identitySecurity
     */
    public UserDetailsAdapter(UserReadAdapter userReadAdapter, IdentitySecret identitySecurity) {
        this(userReadAdapter);
        this.identitySecret = identitySecurity;
    }
    
    /**
     * Context id.
     */
    public int getContextId() {
		return userReadAdapter.getContextId();
	}
    
    /**
     * Entity id.
     */
    public int getEntityId() {
		return userReadAdapter.getEntityId();
	}
    
    /**
     * Identity id.
     */
    public int getIdentityId() {
		return userReadAdapter.getIdentityId();
	}
    
    /**
     * User id.
     */
    public int getUserId() {
		return userReadAdapter.getUserId();
	}
    
    public boolean isAccountNonExpired() {
    	return userReadAdapter.isAccountNonExpired();
    }

    public boolean isAccountNonLocked() {
    	return userReadAdapter.isAccountNonLocked();
    }
    
    public boolean isCredentialsNonExpired() {
    	// delegate to the application
        return true;
    }

    public boolean isEnabled() {
    	return isAccountNonLocked();
    }

    public String getPassword() {
    	if (identitySecret!=null) {
    		return identitySecret.getIdentitySecret();
    	}
        return "";
     }

    public String getUsername() {
        return userReadAdapter.getUserName();
    }
    
    public List<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

    
    @Override
    public Set<String> getAuthoritySet() {
        if (authorities!=null) {
            return AuthorityUtils.authorityListToSet(authorities);
        }
        return new HashSet<>();
    }
    
    @Override
    public Locale getUserLocale() {
    	// TODO get the actual user locale
    	return Locale.getDefault();
    }
    
    /**
     * Convenience to retrieve user details from context.
     */
    public static UserDetailsAdapter getUserDetailsFromContext() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (authentication!=null) {
    		return (UserDetailsAdapter) authentication.getPrincipal();
    	}
    	return null;
    }
    
}
