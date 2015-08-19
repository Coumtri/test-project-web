package com.coumtri.config;

import com.coumtri.account.UserService;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {ApplicationConfig.class, JpaConfig.class, SecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {WebMvcConfig.class};
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        DelegatingFilterProxy securityFilterChain = new DelegatingFilterProxy("springSecurityFilterChain");
        // Source : http://stackoverflow.com/questions/4726824/how-to-log-http-request-body-in-spring-mvc
        com.github.isrsal.logging.LoggingFilter loggingFilter = new com.github.isrsal.logging.LoggingFilter();
        return new Filter[] {characterEncodingFilter, securityFilterChain, loggingFilter};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter("defaultHtmlEscape", "true");
        registration.setInitParameter("spring.profiles.active", "default");
    }

    // Source : http://stackoverflow.com/questions/18047539/how-can-implement-abstractannotationconfigdispatcherservletinitializer-for-java
    /*@Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {
        XmlWebApplicationContext root = new XmlWebApplicationContext();
        root.setConfigLocation("/WEB-INF/application-context.xml");
        servletContext.addListener(new ContextLoaderListener(root));
        //super.onStartup(servletContext);
    }*/

}