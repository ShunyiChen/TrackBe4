package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "ftp_servers")
public class FTPServer {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** FTP服务器名称 */
    private String name;
    /** FTP服务器登录名 */
    private String userName;
    /** FTP服务器登录密码 */
    private String password;
    /** FTP服务器主机地址 */
    private String host;
    /** FTP服务器登录端口 */
    private int port;
    /** 连接模式 */
    private String mode;
    /** 用户主目录 */
    private String userHome;

    public FTPServer() {}

    public FTPServer(String name, String userName, String password, String host, int port, String mode, String userHome) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
        this.mode = mode;
        this.userHome = userHome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUserHome() {
        return userHome;
    }

    public void setUserHome(String userHome) {
        this.userHome = userHome;
    }

    @Override
    public String toString() {
        return "FTPServer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", mode='" + mode + '\'' +
                ", userHome='" + userHome + '\'' +
                '}';
    }
}
