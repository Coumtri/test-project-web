package com.coumtri.account;

import com.coumtri.home.form.ApplicationVersion;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Julien on 2015-08-13.
 */
@Entity
@Table(name = "application_detail")
public class Application implements java.io.Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "APPLICATION_SUSPENDED", nullable = true)
    private boolean suspended;

    @Column(name = "APPLICATION_ASSIGNED", nullable = false)
    private boolean assigned;

    @Column(name = "INCOMING_INVOICE", nullable = true)
    private boolean incomingInvoice;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Account account;

    @Column(name = "APPLICATION_VERSION", nullable = false)
    private String version;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_CREATED", nullable = false, length = 10)
    private Date created;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_UPDATED", nullable = true, length = 10)
    private Date updated;

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }

    public Application() {
    }

    public Application(Account account) {
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public ApplicationVersion getVersion() {
        return ApplicationVersion.valueOf(version);
    }

    public void setVersion(ApplicationVersion version) {
        this.version = version.name();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public boolean isIncomingInvoice() {
        return incomingInvoice;
    }

    public void setIncomingInvoice(boolean incomingInvoice) {
        this.incomingInvoice = incomingInvoice;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
}
