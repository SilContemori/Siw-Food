package it.uniroma3.siw.model;

//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToOne;
//
//@Entity
//public class Credentials {
//
//	public static final String DEFAULT_ROLE = "DEFAULT";
//	public static final String ADMIN_ROLE = "ADMIN";
//	public static final String CUOCO_ROLE = "CUOCO";
//	
//	@Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//	private String username;
//	private String password;
//	private String role;
//
//	@OneToOne(cascade = CascadeType.ALL)
//	private User user;
//	
//	@OneToOne(cascade = CascadeType.ALL)
//	private Cuoco cuoco;
//	
//	
//	public Cuoco getCuoco() {
//		return cuoco;
//	}
//
//	public void setCuoco(Cuoco cuoco) {
//		this.cuoco = cuoco;
//		this.setRole(CUOCO_ROLE);
//	}
//
//	public String getUsername() {
//		return username;
//	}
//	
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//		this.setRole(DEFAULT_ROLE);
//	}
//	
//	public void setUsername(String username) {
//		this.username = username;
//	}
//	
//	public String getPassword() {
//		return password;
//	}
//	
//	public void setPassword(String password) {
//		this.password = password;
//	}
//	
//	public String getRole() {
//		return role;
//	}
//	
//	public void setRole(String role) {
//		this.role = role;
//	}
//
//}
//

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Credentials {

	public static final String DEFAULT_ROLE = "DEFAULT";
	public static final String ADMIN_ROLE = "ADMIN";
	public static final String CUOCO_ROLE = "CUOCO";
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String username;
	private String password;
	private String role;

	@OneToOne(cascade = CascadeType.ALL)
	private User user;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Cuoco cuoco;
	
	public String getUsername() {
		return username;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		this.setRole(DEFAULT_ROLE);
	}
	
	public Cuoco getCuoco() {
		return cuoco;
	}

	public void setCuoco(Cuoco cuoco) {
		this.cuoco = cuoco;
		this.setRole(CUOCO_ROLE);
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}

}
