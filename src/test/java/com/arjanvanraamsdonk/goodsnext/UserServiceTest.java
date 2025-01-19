package com.arjanvanraamsdonk.goodsnext;

import com.arjanvanraamsdonk.goodsnext.dtos.UserDto;
import com.arjanvanraamsdonk.goodsnext.exceptions.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.User;
import com.arjanvanraamsdonk.goodsnext.repositories.UserRepository;
import com.arjanvanraamsdonk.goodsnext.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        userService = new UserService(userRepository, null);
    }

    @Test
    void testGetUserById_userExists_returnsUser() {

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));


        UserDto result = userService.getUserById(1L);


        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_userNotFound_throwsException() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(RecordNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteUser_userExists_returnsTrue() {

        when(userRepository.existsById(1L)).thenReturn(true);


        boolean result = userService.deleteUser(1L);


        assertTrue(result);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_userNotFound_returnsFalse() {

        when(userRepository.existsById(1L)).thenReturn(false);


        boolean result = userService.deleteUser(1L);


        assertFalse(result);
        verify(userRepository, never()).deleteById(anyLong());
    }
}
