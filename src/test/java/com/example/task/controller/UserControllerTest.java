package com.example.task.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.example.task.entities.Account;
import com.example.task.entities.User;
import com.example.task.service.UserService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

public class UserControllerTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTransferMoney() {
        Long currentUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = BigDecimal.valueOf(100.00);

        User currentUser = new User();
        Account account = new Account();
        account.setBalance(amount);
        account.setInitialBalance(amount);
        currentUser.setAccount(account);
        currentUser.setId(currentUserId);

        when(userService.getCurrentUser()).thenReturn(currentUser);
        doNothing().when(userService).transferMoney(currentUserId, toUserId, amount);

        ResponseEntity<String> response = userController.transferMoney(toUserId, amount);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("Transfer successful", response.getBody());

        verify(userService, times(1)).transferMoney(currentUserId, toUserId, amount);
    }

}