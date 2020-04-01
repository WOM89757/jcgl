//package com.wm.jcgl;
//
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.wm.sys.controller.LoginfoController;
//import com.wm.sys.entity.Loginfo;
//import com.wm.sys.service.LoginfoService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.web.context.WebApplicationContext;
//
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class ControllerUnitTest {
//
//    MockMvc mockMvc;
//
//    @Autowired
//    protected WebApplicationContext wac;
//
//    @Autowired
//    LoginfoController Controller;
//
//    @MockBean
//    LoginfoService Service;
//
//    /**
//     * List of samples entity
//     */
//    private List<Loginfo> entity;
//
//    @Before
//    public void setup() throws Exception {
//        this.mockMvc = standaloneSetup(this.Controller).build();// Standalone context
//        // mockMvc = MockMvcBuilders.webAppContextSetup(wac)
//        // .build();
//        Loginfo Loginfo1 = Loginfo.builder()
//                .title("Hokuto no ken")
//                .description("The year is 199X. The Earth has been devastated by nuclear war...")
//                .build();
//        Loginfo Loginfo2 = Loginfo.builder()
//                .title("Yumekui Kenbun")
//                .description("For those who suffer nightmares, help awaits at the Ginseikan Tea House, where patrons can order much more than just Darjeeling. Hiruko is a special kind of a private investigator. He's a dream eater....")
//                .build();
//        entity = new ArrayList<>();
//        entity.add(Loginfo1);
//        entity.add(Loginfo2);
//    }
//
//    @Test
//    public void testSearchSync() throws Exception {
//        // Mocking service
//        when(Service.getentityByTitle(any(String.class))).thenReturn(entity);
//        mockMvc.perform(get("/Loginfo/sync/ken").contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].title", is("Hokuto no ken")))
//                .andExpect(jsonPath("$[1].title", is("Yumekui Kenbun")));
//    }
//
//    @Test
//    public void testSearchASync() throws Exception {
//        // Mocking service
//        when(Service.getentityByTitle(any(String.class))).thenReturn(entity);
//        MvcResult result = mockMvc.perform(get("/Loginfo/async/ken").contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(request().asyncStarted())
//                .andDo(print())
//                // .andExpect(status().is2xxSuccessful()).andReturn();
//                .andReturn();
//        // result.getRequest().getAsyncContext().setTimeout(10000);
//        mockMvc.perform(asyncDispatch(result))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].title", is("Hokuto no ken")));
//    }
//}