package com.coumtri.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.coumtri.home.form.ApplicationVersion;
import com.coumtri.home.form.UserInformation;
import com.coumtri.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public class UserService implements IUserService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	private void saveAccount(String email, String password, String role, ApplicationVersion version) {
		Account account = new Account(email, passwordEncoder.encode(password), role);
		Application application = new Application(account);
		application.setVersion(version);
		account.setApplication(application);
		accountRepository.save(account);
	}
	
	@PostConstruct
	protected void initialize() {
		saveAccount("user", "demo", "ROLE_USER", ApplicationVersion.FULL);
		saveAccount("admin", "admin", "ROLE_ADMIN", ApplicationVersion.LIMITED);
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByEmail(username);
		if(account == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return createUser(account);
	}
	
	public void signin(Account account) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(account));
		accountRepository.save(account);
	}
	
	private Authentication authenticate(Account account) {
		return new UsernamePasswordAuthenticationToken(createUser(account), null, Collections.singleton(createAuthority(account)));		
	}
	
	private User createUser(Account account) {
		return new User(account.getEmail(), account.getPassword(), Collections.singleton(createAuthority(account)));
	}

	private GrantedAuthority createAuthority(Account account) {
		return new SimpleGrantedAuthority(account.getRole());
	}

	public List<UserInformation> showUserDetails() {
		List<UserInformation> users = new ArrayList<UserInformation>();
		List<Account> accounts = accountRepository.findAll();
		for (Account account : accounts) {
			users.add(new UserInformation(account.getEmail(), account.getApplication().getVersion()));
		}
		return users;
	}
}
