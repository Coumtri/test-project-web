package com.coumtri.home.form;

/**
 * Created by Julien on 2015-08-13.
 */
public class UserInformation {
    private String username;
    private ApplicationVersion version;
    private boolean assigned;

    public UserInformation(String username, ApplicationVersion version, boolean assigned) {
        this.username = username;
        this.version = version;
        this.assigned = assigned;
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

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
}
