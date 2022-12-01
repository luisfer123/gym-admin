package com.gym.admin.data.enums;

import com.gym.admin.data.entities.AuthorityEntity;
import com.gym.admin.data.entities.UserEntity;

/**
 * {@linkplain ERole} defines the {@code User} roles. {@linkplain AuthorityEntity} is a wrapper for
 * a given {@linkplain ERole}. Each {@linkplain UserEntity} may have one or more 
 * {@linkplain AuthorityEntity}s associated, which each corresponds with one {@linkplain ERole} 
 * (Role). {@code ROLE_USER} is mandatory and all registered {@linkplain User}s must have
 * it.
 * 
 * @author Luis Fernando Martinez Oritz
 *
 */
public enum ERole {
	
	ROLE_USER ("Rol Usuario"),
	
	ROLE_MEMBER ("Rol Socio"),
	
	ROLE_COACH ("Rol Entrenador"),
	
	ROLE_RECEPTIONIST ("Rol Recepcionesta"),
	
	ROLE_ADMIN ("Rol Administrador")
	
	;
	
	private final String nameInSpanish;
	
	private ERole(String nameInSpanish) {
		this.nameInSpanish = nameInSpanish;
	}
	
	public String getNameInSpanish() {
		return this.nameInSpanish;
	}

}
