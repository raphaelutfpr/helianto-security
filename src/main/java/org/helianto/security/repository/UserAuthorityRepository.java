package org.helianto.security.repository;

import java.io.Serializable;
import java.util.List;

import org.helianto.core.def.ActivityState;
import org.helianto.security.domain.UserAuthority;
import org.helianto.user.domain.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * User authority repository.
 * 
 * @author mauriciofernandesdecastro
 */
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Serializable> {
	
	/**
	 * Find by natural key.
	 * 
	 * @param userGroup
	 * @param serviceCode
	 */
	UserAuthority findByUserGroupAndServiceCode(UserGroup userGroup, String serviceCode);
	
	/**
	 * Find by natural key.
	 * 
	 * @param userGroupId
	 * @param serviceCode
	 */
	UserAuthority findByUserGroupIdAndServiceCode(int userGroupId, String serviceCode);
	
	/**
	 * List by userGroup.
	 * 
	 * @param userGroupId
	 */
	@Query("select new "
			+ "org.helianto.security.repository.UserAuthorityReadAdapter"
			+ "( userAuthority_.id "
			+ ", userAuthority_.userGroup.id "
			+ ", userAuthority_.serviceCode "
			+ ", userAuthority_.serviceExtension "
			+ ") "
			+ "from UserAuthority userAuthority_ "
			+ "where userAuthority_.userGroup.id = ?1 "
			+ "order by userAuthority_.serviceCode ASC ")
	List<UserAuthorityReadAdapter> findByUserGroupIdOrderByServiceCodeAsc(int userGroupId);

	/**
	 * Page by userGroup.
	 * 
	 * @param userGroupId
	 * @param authorityState
	 * @param page
	 */
	Page<UserAuthority> findByUserGroupIdAndAuthorityState(int userGroupId, ActivityState authorityState, Pageable page);
	
}
