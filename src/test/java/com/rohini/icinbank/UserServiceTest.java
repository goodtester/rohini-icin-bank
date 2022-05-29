package com.rohini.icinbank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.rohini.icinbank.domain.model.User;
import com.rohini.icinbank.repository.UserRepository;
import com.rohini.icinbank.service.UserService;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserService.class)
public class UserServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private String username;

    @MockBean
    UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        username = EASY_RANDOM.nextObject(String.class); 
    }

    @Test
    void getSuccess_Test() {
        User user = new User();

        when(userRepository.findByUsername(username))
            .thenReturn(user);

        var result = userService.loadUserByUsername(username);

        assertEquals(user, result);
    }

    @Test
    void getThrow_Test() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(username);
        });
    }
}
