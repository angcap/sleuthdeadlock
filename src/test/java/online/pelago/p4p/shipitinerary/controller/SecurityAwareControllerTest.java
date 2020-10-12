package online.pelago.p4p.shipitinerary.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public abstract class SecurityAwareControllerTest {
    public static final String NOT_ADMIN_USER = "testuser";
    public static final String ADMIN_USER = "john.doe";

    @MockBean
    OpaqueTokenIntrospector opaqueTokenIntrospector;

    @Autowired
    public MockMvc mockMvc;

    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),		
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	

    protected ObjectMapper mapper;
    public String content;

    @PostConstruct
    public void setup() throws Exception {
        mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        doAnswer(invocationOnMock -> {
            String username = invocationOnMock.getArgument(0, String.class);
            return new DefaultOAuth2AuthenticatedPrincipal(username,
                    Collections.singletonMap("", ""),
                    Collections.singleton(new SimpleGrantedAuthority("TEST_ROLE")));
            

        }).when(opaqueTokenIntrospector).introspect(anyString());
    }

    public MockHttpServletRequestBuilder setPostPutRequest(MockHttpServletRequestBuilder request, String username, String content) {
        return authorized(request, username)
                .content(content)
                .accept(MediaType.APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder setPostPutRequest(MockHttpServletRequestBuilder request, String username) {
        return authorized(request, username)
                .content(content)
                .accept(MediaType.APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder authorized(MockHttpServletRequestBuilder request, String username) {
        return request
                .header("Authorization", "Bearer "+username);
    }
    public MockHttpServletRequestBuilder authorized(MockHttpServletRequestBuilder request) {
        return authorized(request, UUID.randomUUID().toString());
    }
}
