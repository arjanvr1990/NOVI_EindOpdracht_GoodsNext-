package com.arjanvanraamsdonk.goodsnext;

import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoDto;
import com.arjanvanraamsdonk.goodsnext.dtos.UserDto;
import com.arjanvanraamsdonk.goodsnext.dtos.UserInputDto;
import com.arjanvanraamsdonk.goodsnext.models.ContactInfo;
import com.arjanvanraamsdonk.goodsnext.models.User;
import com.arjanvanraamsdonk.goodsnext.repositories.UserRepository;
import com.arjanvanraamsdonk.goodsnext.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void testCreateUser_savesUserCorrectly() {
        UserInputDto inputDto = new UserInputDto();
        inputDto.setUsername("newuser");
        inputDto.setPassword("password");
        inputDto.setAuthorities(List.of("ROLE_USER"));
        ContactInfoDto contactInfoDto = new ContactInfoDto("test@test.com", "City", "1234AB", "Street 1", "123456789");
        inputDto.setContactInfo(contactInfoDto);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("newuser");
        savedUser.setRoles(Set.of("ROLE_USER"));

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.createUser(inputDto);

        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        assertEquals(Set.of("ROLE_USER"), result.getRoles());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    public UserDto createUser(UserInputDto userInputDto) {
        User user = new User();
        user.setUsername(userInputDto.getUsername());
        user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        user.setRoles(userInputDto.getAuthorities().stream().collect(Collectors.toSet()));

        if (userInputDto.getContactInfo() != null) {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setEmail(userInputDto.getContactInfo().getEmail());
            contactInfo.setCity(userInputDto.getContactInfo().getCity());
            contactInfo.setPostalCode(userInputDto.getContactInfo().getPostalCode());
            contactInfo.setAddress(userInputDto.getContactInfo().getAddress());
            contactInfo.setPhoneNumber(userInputDto.getContactInfo().getPhoneNumber());
            user.setContactInfo(contactInfo);
        }

        User savedUser = userRepository.save(user);

        return new UserDto(savedUser.getId(), savedUser.getUsername(), null, savedUser.getRoles());
    }



    @Test
    void testUpdateUser_updatesUserCorrectly() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("olduser");
        existingUser.setRoles(Set.of("ROLE_ADMIN"));

        UserInputDto inputDto = new UserInputDto();
        inputDto.setUsername("updateduser");
        inputDto.setPassword("newpassword");
        inputDto.setAuthorities(List.of("ROLE_USER"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");

        userService.updateUser(1L, inputDto);

        assertEquals("updateduser", existingUser.getUsername());
        assertEquals("encodedNewPassword", existingUser.getPassword());
        assertEquals(Set.of("ROLE_USER"), existingUser.getRoles());
        verify(passwordEncoder, times(1)).encode("newpassword");
        verify(userRepository, times(1)).save(existingUser);
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
