package com.assignment.demo.it.controller;

import com.assignment.demo.controller.AccountController;
import com.assignment.demo.domain.Account;
import com.assignment.demo.domain.User;
import com.assignment.demo.exception.NoContentException;
import com.assignment.demo.exception.NotFoundException;
import com.assignment.demo.it.AbstractControllerIT;
import com.assignment.demo.it.AbstractIT;
import com.assignment.demo.service.AccountService;
import com.assignment.demo.service.UserService;
import com.assignment.demo.vo.request.AccountRequest;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerIT extends AbstractControllerIT {

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAll() throws Exception {
        var name = "Account test";
        var customerId = UUID.randomUUID();
        var account = new Account();
        account.setName(name);
        account.setId(customerId);

        var accounts = Collections.singletonList(account);

        when(accountService.getAllAccounts()).thenReturn(accounts);

        mockMvc.perform(get("/v1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is(name)))
                .andExpect(jsonPath("$[0].balance", Matchers.is(0)))
                .andExpect(jsonPath("$[0].id", Matchers.is(customerId.toString())));
    }

    @Test
    public void testFindAllEmptyLst() throws Exception {
        when(accountService.getAllAccounts()).thenThrow(NoContentException.class);

        mockMvc.perform(get("/v1/accounts"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFindById() throws Exception {
        var name = "Account test";
        var id = UUID.randomUUID();
        var account = new Account();
        account.setName(name);
        account.setId(id);

        when(accountService.getAccountById(id)).thenReturn(account);

        mockMvc.perform(get(String.format("/v1/accounts/%s", id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(name)))
                .andExpect(jsonPath("$.balance", Matchers.is(0)))
                .andExpect(jsonPath("$.id", Matchers.is(id.toString())));
    }

    @Test
    public void testFindByIdNotFound() throws Exception {
        var id = UUID.randomUUID();
        when(accountService.getAccountById(id)).thenThrow(NotFoundException.class);

        mockMvc.perform(get(String.format("/v1/accounts/%s", id)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", Matchers.is("NOT_FOUND")));
    }

    @Test
    public void testFindByCustomerId() throws Exception {
        var customerId = UUID.randomUUID();
        var user = new User();
        user.setCustomerId(customerId);

        var name = "Account test";
        var account = new Account();
        account.setName(name);
        account.setCustomer(user);

        var accounts = Collections.singletonList(account);

        when(accountService.getAllAccountsByCustomerId(customerId)).thenReturn(accounts);

        mockMvc.perform(get(String.format("/v1/accounts/customers/%s", customerId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is(name)))
                .andExpect(jsonPath("$[0].balance", Matchers.is(0)))
                .andExpect(jsonPath("$[0].customer.customerId", Matchers.is(customerId.toString())));
    }

    @Test
    public void testFindByCustomerIdNotFound() throws Exception {
        var id = UUID.randomUUID();
        when(accountService.getAllAccountsByCustomerId(id)).thenThrow(NotFoundException.class);

        mockMvc.perform(get(String.format("/v1/accounts/customers/%s", id)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", Matchers.is("NOT_FOUND")));
    }

    @Test
    public void testCreateAccount() throws Exception {
        var customerId = UUID.randomUUID();
        var customer = new User();
        customer.setId(customerId);

        var accountRequest = new AccountRequest();
        accountRequest.setCustomerId(customerId);
        accountRequest.setName("Name");

        when(userService.getUserByCustomerId(customerId)).thenReturn(customer);

        mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper(accountRequest)))
                .andExpect(status().isCreated());
    }
}
