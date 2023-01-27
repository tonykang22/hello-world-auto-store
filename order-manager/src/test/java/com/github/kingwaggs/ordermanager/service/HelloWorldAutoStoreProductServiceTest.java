package com.github.kingwaggs.ordermanager.service;

import com.github.kingwaggs.ordermanager.domain.product.HelloWorldAutoStoreProduct;
import com.github.kingwaggs.ordermanager.repository.HelloWorldAutoStoreProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HelloWorldAutoStoreProductServiceTest {

    @Mock
    private HelloWorldAutoStoreProductRepository hwasProductRepository;

    @InjectMocks
    private HelloWorldAutoStoreProductService helloWorldAutoStoreProductService;

    @Test
    @DisplayName("[Destination Id로 Hwas Product 조회] 성공 시")
    void getHwasProductByDestinationIdSuccessfully() {
        // given
        String destinationId = "13334285554";
        HelloWorldAutoStoreProduct hwasProduct = mock(HelloWorldAutoStoreProduct.class);
        when(hwasProductRepository.findByDestinationId(destinationId)).thenReturn(Optional.of(hwasProduct));

        // when
        HelloWorldAutoStoreProduct hwasProductByDestinationId =
                helloWorldAutoStoreProductService.getHwasProductByDestinationId(destinationId);

        // then
        verify(hwasProductRepository, times(1)).findByDestinationId(destinationId);
        assertNotNull(hwasProductByDestinationId);
    }

}