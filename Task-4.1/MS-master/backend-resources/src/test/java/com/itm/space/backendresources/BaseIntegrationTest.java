package com.itm.space.backendresources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.itm.space.backendresources.api.request.UserRequest;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.ws.rs.core.Response;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@SpringBootTest
@AutoConfigureMockMvc
public class BaseIntegrationTest {

    private final ObjectWriter contentWriter = new ObjectMapper()
            .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
            .writer()
            .withDefaultPrettyPrinter();

    @Autowired
    protected MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Keycloak keycloak;
    @Value("${keycloak.realm}")
    private String realm;

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateEndpoint() throws Exception {
        UserRequest userRequest = new UserRequest("asdf",
                "asdf@mail.com", "asdf", "asdf", "asdf");
        String userRequestJson = objectMapper.writeValueAsString(userRequest);
        
        RealmResource realmResource = mock(RealmResource.class);
        UsersResource usersResource = mock(UsersResource.class);
        Response response = mock(Response.class);

        when(response.getStatusInfo()).thenReturn(Response.Status.CREATED);

        when(keycloak.realm(realm)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testGetByIdEndpoint() throws Exception {
        UUID id = UUID.fromString("bf66f1a7-6bc4-498e-847a-b3154afb00e0");
        mvc.perform(get("/api/users/{id}", id))
                        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testHelloEndpoint() throws Exception {
        mvc.perform(get("/api/users/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("user"));

    }

    protected MockHttpServletRequestBuilder requestToJson(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder
                .contentType(APPLICATION_JSON);
    }

    protected MockHttpServletRequestBuilder requestWithContent(MockHttpServletRequestBuilder requestBuilder,
                                                               Object content) throws JsonProcessingException {
        return requestToJson(requestBuilder).content(contentWriter.writeValueAsString(content));
    }
}
