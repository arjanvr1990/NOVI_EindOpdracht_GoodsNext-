package com.arjanvanraamsdonk.goodsnext.unity;

import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoDto;
import com.arjanvanraamsdonk.goodsnext.dtos.ShopDto;
import com.arjanvanraamsdonk.goodsnext.dtos.ShopInputDto;
import com.arjanvanraamsdonk.goodsnext.exceptions.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.ContactInfo;
import com.arjanvanraamsdonk.goodsnext.models.PhotoUpload;
import com.arjanvanraamsdonk.goodsnext.models.Shop;
import com.arjanvanraamsdonk.goodsnext.repositories.ContactInfoRepository;
import com.arjanvanraamsdonk.goodsnext.repositories.PhotoUploadRepository;
import com.arjanvanraamsdonk.goodsnext.repositories.ShopRepository;
import com.arjanvanraamsdonk.goodsnext.services.ShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShopServiceTest {

    private ShopService shopService;

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private ContactInfoRepository contactInfoRepository;

    @Mock
    private PhotoUploadRepository photoUploadRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shopService = new ShopService(shopRepository, contactInfoRepository, photoUploadRepository);
    }

    @Test
    void testGetAllShops_returnsShopDtos() {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopName("Test Shop");

        List<Shop> shops = new ArrayList<>();
        shops.add(shop);

        when(shopRepository.findAll()).thenReturn(shops);

        List<ShopDto> result = shopService.getAllShops();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Shop", result.get(0).getShopName());
        verify(shopRepository, times(1)).findAll();
    }

    @Test
    void testGetAllShopsByName_noShopsFound_returnsEmptyList() {
        String shopName = "Nonexistent";
        when(shopRepository.findByShopNameContainingIgnoreCase(shopName)).thenReturn(List.of());

        List<ShopDto> result = shopService.getAllShopsByName(shopName);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(shopRepository, times(1)).findByShopNameContainingIgnoreCase(shopName);
    }

    @Test
    void testGetShopById_shopExists_returnsShopDto() {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopName("Test Shop");

        when(shopRepository.findById(1L)).thenReturn(Optional.of(shop));

        ShopDto result = shopService.getShopById(1L);

        assertNotNull(result);
        assertEquals("Test Shop", result.getShopName());
        verify(shopRepository, times(1)).findById(1L);
    }

    @Test
    void testGetShopById_shopNotFound_throwsException() {
        when(shopRepository.findById(1L)).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> shopService.getShopById(1L)
        );

        assertEquals("Shop not found with ID: 1", exception.getMessage());
        verify(shopRepository, times(1)).findById(1L);
    }

    @Test
    void testAddShop_validInputWithLogo_returnsShopDto() {
        ShopInputDto inputDto = new ShopInputDto();
        inputDto.setShopName("Test Shop");
        inputDto.setLogo(1L);

        PhotoUpload logo = new PhotoUpload();
        logo.setId(1L);

        Shop savedShop = new Shop();
        savedShop.setShopId(1L);
        savedShop.setShopName("Test Shop");
        savedShop.setLogo(logo);

        when(photoUploadRepository.findById(1L)).thenReturn(Optional.of(logo));
        when(shopRepository.save(any(Shop.class))).thenReturn(savedShop);

        ShopDto result = shopService.addShop(inputDto);

        assertNotNull(result);
        assertEquals("Test Shop", result.getShopName());
        assertEquals("1", result.getLogo());
        verify(photoUploadRepository, times(1)).findById(1L);
        verify(shopRepository, times(1)).save(any(Shop.class));
    }

    @Test
    void testAddShop_validInputWithoutLogo_returnsShopDto() {
        ShopInputDto inputDto = new ShopInputDto();
        inputDto.setShopName("Test Shop");

        Shop savedShop = new Shop();
        savedShop.setShopId(1L);
        savedShop.setShopName("Test Shop");

        when(shopRepository.save(any(Shop.class))).thenReturn(savedShop);

        ShopDto result = shopService.addShop(inputDto);

        assertNotNull(result);
        assertEquals("Test Shop", result.getShopName());
        assertNull(result.getLogo());
        verify(photoUploadRepository, never()).findById(anyLong());
        verify(shopRepository, times(1)).save(any(Shop.class));
    }

    @Test
    void testAddShop_logoNotFound_throwsRecordNotFoundException() {
        ShopInputDto inputDto = new ShopInputDto();
        inputDto.setShopName("Test Shop");
        inputDto.setLogo(99L);

        when(photoUploadRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecordNotFoundException.class, () -> shopService.addShop(inputDto));
        assertEquals("PhotoUpload not found with ID: 99", exception.getMessage());
        verify(photoUploadRepository, times(1)).findById(99L);
        verify(shopRepository, never()).save(any(Shop.class));
    }

    @Test
    void testAddShop_nullInput_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> shopService.addShop(null));
        assertEquals("Input data for creating shop cannot be null", exception.getMessage());
        verify(shopRepository, never()).save(any(Shop.class));
        verify(photoUploadRepository, never()).findById(anyLong());
    }

    @Test
    void testAddShop_duplicateShopThrowsException() {
        ShopInputDto shopInputDto = new ShopInputDto();
        shopInputDto.setShopName("Existing Shop");
        shopInputDto.setLogo(1L);

        when(shopRepository.findByShopNameContainingIgnoreCase("Existing Shop")).thenReturn(List.of(new Shop()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            shopService.addShop(shopInputDto);
        });

        assertEquals("Shop with name Existing Shop already exists.", exception.getMessage());

        verify(shopRepository, times(1)).findByShopNameContainingIgnoreCase("Existing Shop");
    }



    @Test
    void testUpdateShop_updatesShop() {
        Long shopId = 1L;
        Shop existingShop = new Shop();
        existingShop.setShopId(shopId);
        existingShop.setShopName("Old Shop Name");

        ShopInputDto inputDto = new ShopInputDto();
        inputDto.setShopName("Updated Shop Name");

        ContactInfoDto contactInfoDto = new ContactInfoDto();
        contactInfoDto.setEmail("updated@email.com");
        contactInfoDto.setCity("Updated City");
        contactInfoDto.setPostalCode("1234AB");
        contactInfoDto.setAddress("Updated Address");
        contactInfoDto.setPhoneNumber("123456789");
        inputDto.setContactInfo(contactInfoDto);

        PhotoUpload logo = new PhotoUpload();
        logo.setId(1L);
        inputDto.setLogo(1L);

        when(shopRepository.findById(shopId)).thenReturn(Optional.of(existingShop));
        when(contactInfoRepository.save(any(ContactInfo.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(photoUploadRepository.findById(1L)).thenReturn(Optional.of(logo));
        when(shopRepository.save(any(Shop.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShopDto result = shopService.updateShop(shopId, inputDto);

        assertNotNull(result);
        assertEquals("Updated Shop Name", result.getShopName());
        assertEquals("updated@email.com", result.getContactInfo().getEmail());
        assertEquals("Updated City", result.getContactInfo().getCity());
        assertEquals("1234AB", result.getContactInfo().getPostalCode());
        assertEquals("Updated Address", result.getContactInfo().getAddress());
        assertEquals("123456789", result.getContactInfo().getPhoneNumber());
        assertEquals("1", result.getLogo());

        verify(shopRepository, times(1)).findById(shopId);
        verify(contactInfoRepository, times(1)).save(any(ContactInfo.class));
        verify(photoUploadRepository, times(1)).findById(1L);
        verify(shopRepository, times(1)).save(existingShop);
    }

    @Test
    void testUpdateShop_throwsExceptionWhenLogoNotFound() {
        Long shopId = 1L;
        Shop existingShop = new Shop();
        existingShop.setShopId(shopId);
        existingShop.setShopName("Old Shop Name");

        ShopInputDto inputDto = new ShopInputDto();
        inputDto.setShopName("Updated Shop Name");
        inputDto.setLogo(2L); // Non-existent logo ID

        when(shopRepository.findById(shopId)).thenReturn(Optional.of(existingShop));
        when(photoUploadRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            shopService.updateShop(shopId, inputDto);
        });

        assertEquals("PhotoUpload not found with ID: 2", exception.getMessage());

        verify(shopRepository, times(1)).findById(shopId);
        verify(photoUploadRepository, times(1)).findById(2L);
        verify(shopRepository, never()).save(any(Shop.class));
    }

    @Test
    void testUpdateShop_throwsExceptionWhenShopNotFound() {
        Long shopId = 1L;
        ShopInputDto inputDto = new ShopInputDto();
        inputDto.setShopName("Updated Shop Name");

        when(shopRepository.findById(shopId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            shopService.updateShop(shopId, inputDto);
        });

        assertEquals("Shop not found with ID: " + shopId, exception.getMessage());

        verify(shopRepository, times(1)).findById(shopId);
        verify(shopRepository, never()).save(any(Shop.class));
    }



    @Test
    void testUpdatePartialShop_updatesFieldsCorrectly() {
        Long shopId = 1L;
        Shop existingShop = new Shop();
        existingShop.setShopId(shopId);
        existingShop.setShopName("Old Shop Name");

        ShopInputDto inputDto = new ShopInputDto();
        inputDto.setShopName("Updated Shop Name");

        ContactInfoDto contactInfoDto = new ContactInfoDto();
        contactInfoDto.setEmail("updated@email.com");
        contactInfoDto.setCity("Updated City");
        contactInfoDto.setPostalCode("1234AB");
        contactInfoDto.setAddress("Updated Address");
        contactInfoDto.setPhoneNumber("123456789");
        inputDto.setContactInfo(contactInfoDto);

        PhotoUpload logo = new PhotoUpload();
        logo.setId(1L);
        inputDto.setLogo(1L);

        ContactInfo savedContactInfo = new ContactInfo();
        savedContactInfo.setEmail("updated@email.com");
        savedContactInfo.setCity("Updated City");
        savedContactInfo.setPostalCode("1234AB");
        savedContactInfo.setAddress("Updated Address");
        savedContactInfo.setPhoneNumber("123456789");

        when(shopRepository.findById(shopId)).thenReturn(Optional.of(existingShop));
        when(contactInfoRepository.save(any(ContactInfo.class))).thenReturn(savedContactInfo);
        when(photoUploadRepository.findById(1L)).thenReturn(Optional.of(logo));
        when(shopRepository.save(any(Shop.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShopDto result = shopService.updatePartialShop(shopId, inputDto);

        assertNotNull(result);
        assertEquals("Updated Shop Name", result.getShopName());
        assertEquals("updated@email.com", result.getContactInfo().getEmail());
        assertEquals("Updated City", result.getContactInfo().getCity());
        assertEquals("1234AB", result.getContactInfo().getPostalCode());
        assertEquals("Updated Address", result.getContactInfo().getAddress());
        assertEquals("123456789", result.getContactInfo().getPhoneNumber());
        assertEquals("1", result.getLogo());

        verify(shopRepository, times(1)).findById(shopId);
        verify(contactInfoRepository, times(1)).save(any(ContactInfo.class));
        verify(photoUploadRepository, times(1)).findById(1L);
        verify(shopRepository, times(1)).save(existingShop);
    }

    @Test
    void testUpdatePartialShop_throwsExceptionWhenShopNotFound() {
        Long shopId = 1L;
        ShopInputDto inputDto = new ShopInputDto();

        when(shopRepository.findById(shopId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            shopService.updatePartialShop(shopId, inputDto);
        });

        assertEquals("Shop not found with ID: " + shopId, exception.getMessage());

        verify(shopRepository, times(1)).findById(shopId);
        verifyNoInteractions(contactInfoRepository, photoUploadRepository);
    }

    @Test
    void testUpdatePartialShop_throwsExceptionWhenLogoNotFound() {
        Long shopId = 1L;
        Shop existingShop = new Shop();
        existingShop.setShopId(shopId);

        ShopInputDto inputDto = new ShopInputDto();
        inputDto.setLogo(2L);

        when(shopRepository.findById(shopId)).thenReturn(Optional.of(existingShop));
        when(photoUploadRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            shopService.updatePartialShop(shopId, inputDto);
        });

        assertEquals("PhotoUpload not found with ID: 2", exception.getMessage());

        verify(shopRepository, times(1)).findById(shopId);
        verify(photoUploadRepository, times(1)).findById(2L);
        verify(shopRepository, never()).save(any(Shop.class));
    }

    @Test
    void testUpdatePartialShop_updatesOnlyShopName() {
        Long shopId = 1L;
        Shop existingShop = new Shop();
        existingShop.setShopId(shopId);
        existingShop.setShopName("Old Shop Name");

        ShopInputDto inputDto = new ShopInputDto();
        inputDto.setShopName("New Shop Name");

        when(shopRepository.findById(shopId)).thenReturn(Optional.of(existingShop));
        when(shopRepository.save(any(Shop.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShopDto result = shopService.updatePartialShop(shopId, inputDto);

        assertNotNull(result);
        assertEquals("New Shop Name", result.getShopName());
        assertNull(result.getContactInfo());
        assertNull(result.getLogo());

        verify(shopRepository, times(1)).findById(shopId);
        verify(shopRepository, times(1)).save(existingShop);
        verifyNoInteractions(contactInfoRepository, photoUploadRepository);
    }


    @Test
    void testDeleteShop_shopExists_deletesShop() {
        when(shopRepository.existsById(1L)).thenReturn(true);

        shopService.deleteShop(1L);

        verify(shopRepository, times(1)).existsById(1L);
        verify(shopRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteShop_shopNotFound_throwsException() {
        when(shopRepository.existsById(1L)).thenReturn(false);

        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> shopService.deleteShop(1L)
        );

        assertEquals("Shop not found with ID: 1", exception.getMessage());
        verify(shopRepository, times(1)).existsById(1L);
        verify(shopRepository, never()).deleteById(anyLong());
    }

    @Test
    void testAddShop_withContactInfo() {
        ShopInputDto shopInputDto = new ShopInputDto();
        shopInputDto.setShopName("Test Shop");

        ContactInfoDto contactInfoDto = new ContactInfoDto();
        contactInfoDto.setEmail("test@test.com");
        contactInfoDto.setCity("Test City");
        contactInfoDto.setPostalCode("1234AB");
        contactInfoDto.setAddress("Test Street 1");
        contactInfoDto.setPhoneNumber("123456789");
        shopInputDto.setContactInfo(contactInfoDto);

        Shop savedShop = new Shop();
        savedShop.setShopId(1L);
        savedShop.setShopName("Test Shop");
        ContactInfo savedContactInfo = new ContactInfo();
        savedContactInfo.setEmail("test@test.com");
        savedContactInfo.setCity("Test City");
        savedContactInfo.setPostalCode("1234AB");
        savedContactInfo.setAddress("Test Street 1");
        savedContactInfo.setPhoneNumber("123456789");
        savedShop.setContactInfo(savedContactInfo);

        when(shopRepository.save(any(Shop.class))).thenReturn(savedShop);

        ShopDto result = shopService.addShop(shopInputDto);

        assertNotNull(result);
        assertEquals("Test Shop", result.getShopName());
        assertNotNull(result.getContactInfo());
        assertEquals("test@test.com", result.getContactInfo().getEmail());
        assertEquals("Test City", result.getContactInfo().getCity());
        assertEquals("1234AB", result.getContactInfo().getPostalCode());
        assertEquals("Test Street 1", result.getContactInfo().getAddress());
        assertEquals("123456789", result.getContactInfo().getPhoneNumber());

        verify(shopRepository, times(1)).save(any(Shop.class));
    }
}
