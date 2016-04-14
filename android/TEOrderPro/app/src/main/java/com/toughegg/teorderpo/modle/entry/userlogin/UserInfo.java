package com.toughegg.teorderpo.modle.entry.userlogin;

/**
 * Created by toughegg on 15/8/25.
 */
public class UserInfo {
    private String id;// 网络返回ID
    private String restCode;
    private String employeeId;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestCode() {
        return restCode;
    }

    public void setRestCode(String restCode) {
        this.restCode = restCode;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString () {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", restCode='" + restCode + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
