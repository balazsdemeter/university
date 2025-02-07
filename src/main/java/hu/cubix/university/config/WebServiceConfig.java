package hu.cubix.university.config;

import hu.cubix.university.xmlws.TimeTableXmlWs;
import jakarta.xml.ws.Endpoint;
import lombok.RequiredArgsConstructor;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.annotation.Secured;

@Configuration
@RequiredArgsConstructor
public class WebServiceConfig {

    private final Bus bus;
    private final TimeTableXmlWs timeTableXmlWs;

    @Secured("admin")
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, timeTableXmlWs);
        endpoint.publish("/studentTimeTableInfo");

        return endpoint;
    }
}