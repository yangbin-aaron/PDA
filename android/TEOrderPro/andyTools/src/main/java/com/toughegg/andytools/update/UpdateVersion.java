package com.toughegg.andytools.update;

/**
 * Created by Andy on 16/3/12.
 */
public class UpdateVersion {
    private String version;// 版本名300015
    private String versionShort;// 版本号3.0.0.15
    private String installUrl;// 下载地址
    private String changelog;// 版本日志
    private String updated_at;// 时间
    private String name;// name

    public String getUpdated_at () {
        return updated_at;
    }

    public void setUpdated_at (String updated_at) {
        this.updated_at = updated_at;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionShort() {
        return versionShort;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    @Override
    public String toString () {
        return "UpdateVersion{" +
                "version='" + version + '\'' +
                ", versionShort='" + versionShort + '\'' +
                ", installUrl='" + installUrl + '\'' +
                ", changelog='" + changelog + '\'' +
                '}';
    }
}
