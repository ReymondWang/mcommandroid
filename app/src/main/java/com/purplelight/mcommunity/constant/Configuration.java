package com.purplelight.mcommunity.constant;

/**
 * 参数配置类
 * Created by wangyn on 16/4/10.
 */
public class Configuration {

    public class Server{
        public static final String WEB = "http://139.196.186.85:8080/mcm/";

        public static final String IMAGE = "http://139.196.186.85:8888/";
    }

    public class Fastdfs{
        public static final int CONNECT_TIMEOUT = 2;
        public static final int NETWORK_TIMEOUT = 30;
        public static final String CHARSET = "ISO8859-1";
        public static final int TRACKER_HTTP_PORT = 8888;
        public static final boolean ANTI_STEAL_TOKEN = false;
        public static final String SECRET_KEY = "FastDFS1234567890";
        public static final String TRACKER_SERVER = "139.196.186.85:22122";
    }

    public class Fragment{
        public static final String HOME = "1";
        public static final String WORK = "2";
        public static final String PROFILE = "3";
    }

}
