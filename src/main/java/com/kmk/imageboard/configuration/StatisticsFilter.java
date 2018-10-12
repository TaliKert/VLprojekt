package com.kmk.imageboard.configuration;

import com.kmk.imageboard.model.RequestData;
import com.kmk.imageboard.repository.StatisticRepository;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalTime;

@Component
public class StatisticsFilter implements Filter {

    @Autowired
    StatisticRepository statisticRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        chain.doFilter(request, response);
        HttpServletRequest servletRequest = (HttpServletRequest) request;

        String sessionId = WebUtils.getCookie(servletRequest, "JSESSIONID").getValue();

        if (statisticRepository.findBySessionId(sessionId) == null) {
            String header = servletRequest.getHeader("User-Agent");
            UserAgent userAgent = UserAgent.parseUserAgentString(header);

            String browserName = userAgent.getBrowser().getName();
            String osName = userAgent.getOperatingSystem().getName();
            LocalTime time = LocalTime.now();

            // For some reason, browsers send multiple requests at once
            // and the previous if statement doesn't have enough time to
            // query the database for existing Session ID's.
            // TODO: make it so the following would be more elegant
            try {
                statisticRepository.save(new RequestData(sessionId, browserName, osName, time));
            } catch (DataIntegrityViolationException e) { }
        }

    }

    @Override
    public void destroy() {

    }
}
