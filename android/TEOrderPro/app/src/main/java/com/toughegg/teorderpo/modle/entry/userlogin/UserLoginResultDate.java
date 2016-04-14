package com.toughegg.teorderpo.modle.entry.userlogin;

/**
 * Created by Andy on 15/11/27.
 */
public class UserLoginResultDate {
    private String id;
    private String restId;
    private String groupId;
    private String employeeId;
    private String username;
    private String email;
    private String phone;
    private int status;

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getRestId () {
        return restId;
    }

    public void setRestId (String restId) {
        this.restId = restId;
    }

    public String getGroupId () {
        return groupId;
    }

    public void setGroupId (String groupId) {
        this.groupId = groupId;
    }

    public String getEmployeeId () {
        return employeeId;
    }

    public void setEmployeeId (String employeeId) {
        this.employeeId = employeeId;
    }

    public String getUsername () {
        return username;
    }

    public void setUsername (String username) {
        this.username = username;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getPhone () {
        return phone;
    }

    public void setPhone (String phone) {
        this.phone = phone;
    }

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public String getString () {
        String result = "id:" + id + "\n restId:" + restId + "\n groupId:" + groupId + "\n employeeId:" + employeeId + "\n email:" + email + "\n " +
                "username:" + username + "\n phone:" + phone + "\n status:" + status;
        return result;
    }
}
