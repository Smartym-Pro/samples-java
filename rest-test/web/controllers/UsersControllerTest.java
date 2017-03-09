package pro.smartum.reptracker.gateway.web.controllers;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.matchers.StringContains;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.server.MockMvc;

import pro.smartum.reptracker.gateway.dao.entities.UserRole;
import pro.smartum.reptracker.gateway.security.SecurityUser;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.xmlConfigSetup;

/**
 * User: Sergey Valuy
 
 */
public class UsersControllerTest {

    @Ignore
    @Test
    public void testGetById() throws Exception {
        MockMvc mockMvc = xmlConfigSetup("classpath:spring/test-mvc-app-config.xml")
//                .configureWebAppRootDir("src/main/webapp", false)
                .build();

        TestSecurityUtils.authenticateUser(new SecurityUser(1, UserRole.ROLE_SUPER_PARTNER, UserRole.ROLE_PARTNER));

        mockMvc.perform(get("/users/getbyid").param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(StringContains.containsString("homer112")));
//                .andExpect(content().type(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.user.id").value("1"));
    }


}
