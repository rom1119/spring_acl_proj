package com.example.demo.acl.model;

import com.example.demo.acl.validation.ValidPermission;

import javax.persistence.*;

@Entity
@Table(name = "acl_entry")
public class AclEntry {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name="acl_object_identity", nullable=false)
    private AclObjectIdentity objectIdentity;

    @Column(name = "ace_order")
    private int aceOrder;

    @ManyToOne
    @JoinColumn(name="sid", nullable=false)
    private AclSecurityID securityID;

    @ValidPermission
    @Column(name = "mask")
    private int mask;

    @Column(name = "granting")
    private boolean granting;


    @Column(name = "audit_success")
    private boolean auditSuccess;


    @Column(name = "audit_failure")
    private boolean auditFailure;

    public AclEntry() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AclObjectIdentity getObjectIdentity() {
        return objectIdentity;
    }

    public void setObjectIdentity(AclObjectIdentity objectIdentity) {
        this.objectIdentity = objectIdentity;
    }

    public int getAceOrder() {
        return aceOrder;
    }

    public void setAceOrder(int aceOrder) {
        this.aceOrder = aceOrder;
    }

    public AclSecurityID getSecurityID() {
        return securityID;
    }

    public void setSecurityID(AclSecurityID securityID) {
        this.securityID = securityID;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public boolean isGranting() {
        return granting;
    }

    public void setGranting(boolean granting) {
        this.granting = granting;
    }

    public boolean isAuditSuccess() {
        return auditSuccess;
    }

    public void setAuditSuccess(boolean auditSuccess) {
        this.auditSuccess = auditSuccess;
    }

    public boolean isAuditFailure() {
        return auditFailure;
    }

    public void setAuditFailure(boolean auditFailure) {
        this.auditFailure = auditFailure;
    }
}
