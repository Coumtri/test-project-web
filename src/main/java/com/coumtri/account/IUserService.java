package com.coumtri.account;

import com.coumtri.home.form.ApplicationVersion;
import com.coumtri.home.form.UserInformation;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * Created by Julien on 2015-08-16.
 */
public interface IUserService extends UserDetailsService {
    Long signin(Account account);

    List<UserInformation> showUserDetails();

    Account changeApplicationVersion(String accountIdentifier, ApplicationVersion applicationVersion);

    Long removeAccount(String accountIdentifier);

    Long suspendAccount(String accountIdentifier);

    Long unsuspendAccount(String accountIdentifier);

    Long addIncomingInvoice(String accountIdentifier);

    Long removeIncomingInvoice(String accountIdentifier);

    Long changeAssignment(String accountIdentifier, boolean newAssignementValue);

    Account findAccount(String accountIdentifier);
}
