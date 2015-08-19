package com.coumtri.account;

import javax.persistence.*;

import com.coumtri.home.form.ApplicationVersion;
import org.codehaus.jackson.annotate.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "account")
public class Account implements java.io.Serializable {

	public static final String FIND_BY_EMAIL = "Account.findByEmail";

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "EXTERNAL_ID", nullable = true)
	private String externalIdentifier;

	@Column(unique = true)
	private String email;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
	private Application application;
	
	@JsonIgnore
	private String password;

	private String role = "ROLE_USER";

    protected Account() {

	}
	public Account(String email, String password, String role) {
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public Account(String email, Long accountIdentifier, String appDirectId, ApplicationVersion applicationVersion) {
		this(email, appDirectId, applicationVersion);
        this.setId(accountIdentifier);
	}

    public Account(String email, String appDirectId, ApplicationVersion applicationVersion) {
		this(email, "password", "ROLE_USER");
        this.externalIdentifier = appDirectId;
        Application application = new Application(this);
        application.setVersion(applicationVersion);
        this.setApplication(application);
    }

    public Long getId() {
		return id;
	}

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public String getExternalIdentifier() {
		return externalIdentifier;
	}

	public void setExternalIdentifier(String externalIdentifier) {
		this.externalIdentifier = externalIdentifier;
	}
}
