package edu.arizona.rice.krad.web.filter;

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class UserLoginFilter extends org.kuali.rice.krad.web.filter.UserLoginFilter {
    private static final String COOKIE_IS_SET_SECURE = "cookie.is.set.secure";
    private static final String COOKIE_IS_SET_HTTP_ONLY = "cookie.is.set.http.only";


    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }


    private void doFilter(HttpServletRequest request, HttpServletResponse response,
                          FilterChain chain) throws IOException, ServletException {
        try {
            establishUserSession(request);
            establishSessionCookie(request, response);
            establishBackdoorUser(request);

            addToMDC(request);

            chain.doFilter(request, response);
        } finally {
            removeFromMDC();
        }
    }


    private void establishSessionCookie(HttpServletRequest request, HttpServletResponse response) {
        String kualiSessionId = getKualiSessionId(request.getCookies());
        if (kualiSessionId == null) {
            kualiSessionId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(KRADConstants.KUALI_SESSION_ID, kualiSessionId);
            cookie.setHttpOnly(isSetHttpOnly());
            cookie.setSecure(isSetSecure());
            response.addCookie(cookie);
        }
        KRADUtils.getUserSessionFromRequest(request).setKualiSessionId(kualiSessionId);
    }


    private boolean isSetSecure() {
        return getKualiConfigurationService().getPropertyValueAsBoolean(COOKIE_IS_SET_SECURE);
    }


    private boolean isSetHttpOnly() {
        return getKualiConfigurationService().getPropertyValueAsBoolean(COOKIE_IS_SET_HTTP_ONLY);
    }


    private ConfigurationService getKualiConfigurationService() {
        return CoreApiServiceLocator.getKualiConfigurationService();
    }
}
