package com.arjanvanraamsdonk.goodsnext.unity;

import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoDto;
import com.arjanvanraamsdonk.goodsnext.dtos.UserDto;
import com.arjanvanraamsdonk.goodsnext.dtos.UserInputDto;
import com.arjanvanraamsdonk.goodsnext.exceptions.RecordNotFoundException;
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

    private UserInputDto createUserInputDto() {
        ContactInfoDto contactInfoDto = new ContactInfoDto(
                "email@example.com", "City", "1234AB", "Street 1", "0612345678"
        );
        UserInputDto inputDto = new UserInputDto();
        inputDto.setUsername("testuser");
        inputDto.setPassword("password");
        inputDto.setAuthorities(List.of("ROLE_USER"));
        inputDto.setContactInfo(contactInfoDto);
        return inputDto;
    }

    private User createUser() {
        ContactInfo contactInfo = new ContactInfo(
                "email@example.com", "City", "1234AB", "Street 1", "0612345678"
        );
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of("ROLE_USER"));
        user.setContactInfo(contactInfo);
        return user;
    }

    @Test
    void testCreateUser_savesUserCorrectly() {
        UserInputDto inputDto = createUserInputDto();
        User savedUser = createUser();

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.createUser(inputDto);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals(Set.of("ROLE_USER"), result.getRoles());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_missingContactInfo_savesUser() {
        UserInputDto inputDto = createUserInputDto();
        inputDto.setContactInfo(null);
        User savedUser = createUser();
        savedUser.setContactInfo(null);

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.createUser(inputDto);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals(Set.of("ROLE_USER"), result.getRoles());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_nullInput_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(null)
        );

        assertEquals("Input data for creating user cannot be null", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }


    @Test
    void testGetUserById_userExists_returnsUser() {
        User user = createUser();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllUsers_usersExist_returnsListOfUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setRoles(Set.of("ROLE_USER"));

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setRoles(Set.of("ROLE_ADMIN"));

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals(Set.of("ROLE_USER"), result.get(0).getRoles());
        assertEquals("user2", result.get(1).getUsername());
        assertEquals(Set.of("ROLE_ADMIN"), result.get(1).getRoles());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetAllUsers_noUsersFound_throwsException() {
        when(userRepository.findAll()).thenReturn(List.of());

        assertThrows(RecordNotFoundException.class, () -> userService.getAllUsers());
        verify(userRepository, times(1)).findAll();
    }


    @Test
    void testGetUserById_userDoesNotExist_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateUser_createsContactInfoIfNotExists() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existinguser");
        existingUser.setRoles(Set.of("ROLE_USER"));
        existingUser.setContactInfo(null); // Geen ContactInfo aanwezig

        UserInputDto inputDto = new UserInputDto();
        inputDto.setUsername("updateduser");
        inputDto.setPassword("newpassword");
        inputDto.setAuthorities(List.of("ROLE_ADMIN"));
        ContactInfoDto contactInfoDto = new ContactInfoDto("test@test.com", "City", "1234AB", "Street 1", "123456789");
        inputDto.setContactInfo(contactInfoDto);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");

        userService.updateUser(1L, inputDto);

        assertEquals("updateduser", existingUser.getUsername());
        assertEquals("encodedNewPassword", existingUser.getPassword());
        assertEquals(Set.of("ROLE_ADMIN"), existingUser.getRoles());

        assertNotNull(existingUser.getContactInfo());
        assertEquals("test@test.com", existingUser.getContactInfo().getEmail());
        assertEquals("City", existingUser.getContactInfo().getCity());
        assertEquals("1234AB", existingUser.getContactInfo().getPostalCode());
        assertEquals("Street 1", existingUser.getContactInfo().getAddress());
        assertEquals("123456789", existingUser.getContactInfo().getPhoneNumber());

        verify(userRepository, times(1)).save(existingUser);
    }


    @Test
    void testUpdateUser_userNotFound_throwsRecordNotFoundException() {
        Long userId = 1L;
        UserInputDto inputDto = new UserInputDto();
        inputDto.setUsername("nonexistentuser");
        inputDto.setPassword("password");
        inputDto.setAuthorities(List.of("ROLE_USER"));

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> userService.updateUser(userId, inputDto)
        );

        assertEquals("User with ID " + userId + " not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void testDeleteUser_userExists_deletesUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_userDoesNotExist_throwsException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(RecordNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, times(0)).deleteById(anyLong());
    }
}
