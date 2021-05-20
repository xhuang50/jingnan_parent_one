package com.jn.web.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * @Author yaxiongliu
 **/
@Configuration
public class TomcatConfig {
    //自定义SpringBoot嵌入式Tomcat
    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                //配置安全约束
                SecurityConstraint constraint = new SecurityConstraint();
                constraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                //拦截所有请求
                collection.addPattern("/*");
                constraint.addCollection(collection);
                context.addConstraint(constraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(http11NioConnector());
        return tomcat;
    }
    //配置keepAlive
    @Bean
    public Connector http11NioConnector() {
        Connector connector=new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol nioProtocol = (Http11NioProtocol) connector.getProtocolHandler();
        //等待队列最多允许1000个线程在队列中等待
        nioProtocol.setAcceptCount(1000);
        // 设置最大线程数
        nioProtocol.setMaxThreads(1000);
        // 设置最大连接数
        nioProtocol.setMaxConnections(20000);
        //定制化keepalivetimeout,设置30秒内没有请求则服务端自动断开keepalive链接
        nioProtocol.setKeepAliveTimeout(30000);
        //当客户端发送超过10000个请求则自动断开keepalive链接
        nioProtocol.setMaxKeepAliveRequests(10000);
        // 请求方式
        connector.setScheme("http");
        connector.setPort(9002);                    //自定义的
        connector.setRedirectPort(8443);
        return connector;
    }
    //配置连接器nio2
    @Bean
    public Connector http11Nio2Connector() {
        Connector connector=new Connector("org.apache.coyote.http11.Http11Nio2Protocol");
        Http11Nio2Protocol nio2Protocol = (Http11Nio2Protocol) connector.getProtocolHandler();
        //等待队列最多允许1000个线程在队列中等待
        nio2Protocol.setAcceptCount(1000);
        // 设置最大线程数
        nio2Protocol.setMaxThreads(1000);
        // 设置最大连接数
        nio2Protocol.setMaxConnections(20000);
        //定制化keepalivetimeout,设置30秒内没有请求则服务端自动断开keepalive链接
        nio2Protocol.setKeepAliveTimeout(30000);
        //当客户端发送超过10000个请求则自动断开keepalive链接
        nio2Protocol.setMaxKeepAliveRequests(10000);
        // 请求方式
        connector.setScheme("http");
        connector.setPort(9002);                    //自定义的
        connector.setRedirectPort(8443);
        return connector;
    }
}
