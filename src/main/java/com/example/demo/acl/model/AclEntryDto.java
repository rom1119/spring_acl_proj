package com.example.demo.acl.model;

import com.example.demo.acl.validation.ValidPermission;
import com.example.demo.main.validation.group.Created;
import com.example.demo.main.validation.group.Edited;

import javax.validation.constraints.NotNull;

public class AclEntryDto
{
    private Long id;

//    private AclObjectIdentity objectIdentity;

    private int aceOrder;

    @NotNull(groups = {Created.class})
    private AclSecurityID securityID;

    @ValidPermission(groups = {Edited.class})
    private int mask;

    private boolean granting;

    private boolean auditSuccess;

    private boolean auditFailure;

    public AclEntryDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
