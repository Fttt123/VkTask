package ru.javabegin.BookStore.Controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.javabegin.BookStore.entity.Deal;
import ru.javabegin.BookStore.entity.MainServer;
import ru.javabegin.BookStore.entity.account;
import ru.javabegin.BookStore.entity.books;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MainController.class)
class MainControllerTest {
    @MockBean
    private MainServer mainServer;
    @Autowired
    private MockMvc mockMvcmvc;

    @Test
    void books() throws Exception {
        when(mainServer.getBooks()).thenReturn(Collections.singletonList(new books(4, "book")));
        this.mockMvcmvc
                .perform(MockMvcRequestBuilders
                        .get("/market")
                        .header("name", "book"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)));
    }

    @Test
    void account() throws Exception {
        when(mainServer.getAccount()).thenReturn((new account(2000)));
        this.mockMvcmvc
                .perform(MockMvcRequestBuilders
                        .get("/account")
                        .header("money", 2000))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)));
    }

    @Test
    void messageMarketDeal() throws Exception {
        this.mockMvcmvc
                .perform(MockMvcRequestBuilders
                        .post("/market/deal")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"id\":\"0\", \"amount\":\"3\"}")
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}