package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "vfs_clients")
public class VFSClient {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 客户端名称 */
    private String name;
    /** 客户端编码 */
    private String code;
    /** 连接用户名 */
    private String userName;
    /** 连接密码 */
    private String password;
    /** 连接主机地址 */
    private String host;
    /** 连接端口号 */
    private int port;
    /** 用户主目录 */
    private String userHome;
    /** 文件传输协议 */
    private String protocol;
    /** 客户端容量 */
    @OneToOne
    @JoinColumn(name = "capacity_id")
    private VFSCapacity capacity;

    public VFSClient() {
    }

    public VFSClient(String name, String code, String userName, String password, String host, int port, String userHome, String protocol, VFSCapacity capacity) {
        this.name = name;
        this.code = code;
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
        this.userHome = userHome;
        this.protocol = protocol;
        this.capacity = capacity;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getUserHome() {
        return userHome;
    }

    public void setUserHome(String userHome) {
        this.userHome = userHome;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public VFSCapacity getCapacity() {
        return capacity;
    }

    public void setCapacity(VFSCapacity capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "VFSClient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", userHome='" + userHome + '\'' +
                ", protocol='" + protocol + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
