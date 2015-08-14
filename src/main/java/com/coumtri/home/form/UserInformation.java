package com.coumtri.home.form;

/**
 * Created by Julien on 2015-08-13.
 */
public class UserInformation {
    private String username;
    private ApplicationVersion version;

    public UserInformation(String username, ApplicationVersion version) {
        this.username = username;
        this.version = version;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ApplicationVersion getVersion() {
        return version;
    }

    public void setVersion(ApplicationVersion version) {
        this.version = version;
    }
}
