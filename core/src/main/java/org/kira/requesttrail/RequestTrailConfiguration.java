package org.kira.requesttrail;

import org.kira.requesttrail.aspect.RequestTrailAspect;
import org.kira.requesttrail.aspect.RequestTrailAspectCondition;
import org.kira.requesttrail.filter.RequestTrailFilter;
import org.kira.requesttrail.header.TrailHeaderStrategy;
import org.kira.requesttrail.interceptor.RequestTrailInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.*;
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @license Request Trail v1.0
 * Copyright(c) 2016, Prasad K. Thotakura
 * License: MIT
 * https://opensource.org/licenses/MIT
 */
@Configuration
@EnableAspectJAutoProxy

@ComponentScan(basePackages = "org.kira.requesttrail")
public class RequestTrailConfiguration
{

    private static final Logger LOGGER= LoggerFactory.getLogger(RequestTrailConfiguration.class);

    @PostConstruct
    public void logIntializationMsg(){
        LOGGER.info("Initializing Request Trail configuration");
    }

    @Bean
    public RequestTrailPropertyHolder propertyHolder()
    {
        return new RequestTrailPropertyHolder();
    }

    @Bean(name = "requestTrailHeaderStrategy")
    public TrailHeaderStrategy requestTrailHeaderStrategy()
    {
        TrailHeaderStrategy headerStrategy = new TrailHeaderStrategy();
        headerStrategy.setCorrelationHeader(propertyHolder().getProperty(RequestTrailPropertyHolder.REQUEST_TRAIL_CORRELATION_HEADER));
        headerStrategy.setCallStackHeader(propertyHolder().getProperty(RequestTrailPropertyHolder.REQUEST_TRAIL_APP_CALL_STACK_HEADER));
        headerStrategy.setHeaderStrategy(propertyHolder().getProperty(RequestTrailPropertyHolder.REQUEST_TRAIL_HEADER_STRATEGY));
        return headerStrategy;
    }

    @Bean(name = "kiraRestTemplate")
    public RestTemplate restTemplate()
    {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> msgConverters = restTemplate.getMessageConverters();
        msgConverters.add(new StringHttpMessageConverter());
        msgConverters.add(new MappingJackson2HttpMessageConverter());
        msgConverters.add(new MappingJackson2XmlHttpMessageConverter());
        msgConverters.add(new ByteArrayHttpMessageConverter());
        msgConverters.add(new ResourceHttpMessageConverter());
        msgConverters.add(new FormHttpMessageConverter());
        msgConverters.add(new AtomFeedHttpMessageConverter());
        if (!Boolean.valueOf(propertyHolder().getProperty(RequestTrailPropertyHolder.REQUEST_TRAIL_ASPECT_ENABLE)))
        {
            restTemplate.getInterceptors().add(new RequestTrailInterceptor());
        }
        return restTemplate;
    }

    @Bean(name = "requestTrailFilter")
    public RequestTrailFilter requestTrailFilter()
    {
        RequestTrailFilter requestTrackerFilter = new RequestTrailFilter();
        requestTrackerFilter.setHeaderStrategy(requestTrailHeaderStrategy());
        return requestTrackerFilter;
    }

    @Bean(name = "requestTrailAspect")
    @Conditional(RequestTrailAspectCondition.class)
    public RequestTrailAspect correlationAspect()
    {
        return new RequestTrailAspect();
    }

}
