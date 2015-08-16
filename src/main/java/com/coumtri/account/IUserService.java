package com.coumtri.account;

import com.coumtri.home.form.UserInformation;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * Created by Julien on 2015-08-16.
 */
public interface IUserService extends UserDetailsService {
    void signin(Account account);

    List<UserInformation> showUserDetails();
}
