package skyline.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-3-13
 * Time: 上午11:01
 * To change this template use File | Settings | File Templates.
 */
public class PlatformHelper {
    private static Logger logger = LoggerFactory.getLogger(PlatformHelper.class);

    public static List<String> getLocalIpList() throws SocketException {
        Enumeration netInterfaces = null;
        List<String> ipList = new ArrayList<String>();
        netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) netInterfaces.nextElement();
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address) {
                    ipList.add(ip.getHostAddress());
                }
            }
        }
        return ipList;
    }

    /**
     * 判断是否是生产环境
     */
    public static boolean isProductionServerIp() throws IOException {
        //String configedIp = PropertyManager.getProperty("production.server.ip");
        String configedIp = getRealtimeProjectConfigProperty("production.server.ip");
        if (StringUtils.isEmpty(configedIp)) {
            return false;
        }
        List<String> ipList = getLocalIpList();
        if (ipList.isEmpty()) {
            return false;
        }
        for (String ip : ipList) {
            if (ip.equals(configedIp)) {
                return true;
            }
        }
        return false;
    }

    public static String getRealtimeProjectConfigProperty(String property) throws IOException {
        InputStream inputStream = PlatformHelper.class.getClassLoader().getResourceAsStream("prjcfg.properties");
        Properties p = new Properties();
        p.load(inputStream);
        return p.getProperty(property);
    }
}
