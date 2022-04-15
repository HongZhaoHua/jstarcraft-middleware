package com.jstarcraft.cloud.registration.redis;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.stereotype.Component;

@Component
public class RedisRegistration implements Registration {

    @Value("${server.port}")
    private Integer port;

    @Value("${spring.application.name}")
    private String applicationName;

    private String host;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public String getServiceId() {
        return applicationName;
    }

    @Override
    public String getHost() {
        try {
            if (host == null)
                return getLocalHostLANAddress().getHostAddress();
            else
                return host;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public Map<String, String> getMetadata() {
        return null;
    }

    // TODO 此方法考虑迁移到core
    private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration networks = NetworkInterface.getNetworkInterfaces(); networks.hasMoreElements();) {
                NetworkInterface network = (NetworkInterface) networks.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration addresses = network.getInetAddresses(); addresses.hasMoreElements();) {
                    InetAddress address = (InetAddress) addresses.nextElement();
                    if (!address.isLoopbackAddress()) {// 排除loopback类型地址
                        if (address.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return address;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现,先记录候选地址
                            candidateAddress = address;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception exception) {
            UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: ");
            unknownHostException.initCause(exception);
            throw unknownHostException;
        }
    }

}
