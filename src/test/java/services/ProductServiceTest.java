package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ro.ctrln.dtos.ProductDTO;
import ro.ctrln.entities.Product;
import ro.ctrln.enums.Currencies;
import ro.ctrln.exceptions.InvalidProductCodeException;
import ro.ctrln.mappers.ProductMapper;
import ro.ctrln.repositories.ProductRepository;
import ro.ctrln.services.ProductService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ProductServiceTest { //In aceasta clasa am folosit @Autowired
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRepository productRepository;

    @TestConfiguration
    static class ProductServiceTestContextConfiguration{

        @MockBean
        private ProductMapper productMapper;

        @MockBean
        private ProductRepository productRepository;

        @Bean
        public ProductService productService(){
            return new ProductService(productRepository,productMapper);
        }
    }

    @Test
    public void addProduct(){
        Product productOne = new Product();
        productOne.setCode("aProductCode");
        productOne.setPrice(100);
        productOne.setStock(1);
        productOne.setValid(true);
        productOne.setCurrency(Currencies.USD);
        productOne.setDescription("A beautiful product!");

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode("aProductCode");
        productDTO.setPrice(100);
        productDTO.setStock(1);
        productDTO.setValid(true);
        productDTO.setCurrency(Currencies.USD);
        productDTO.setDescription("A beautiful product!");

        /*Mockito.*/when(productMapper.toEntity(any())).thenReturn(productOne);//nu mai trebuie sa facem mockito pentruca am facut import ↑

        productService.addProduct(productDTO,1);

        verify(productMapper).toEntity(productDTO);//verify este din mockito
        verify(productRepository).save(productOne);
    }

    @Test
    public void getProduct_whenProductIsNotInDB_shouldThrowAnException(){
        try {
            productService.getProduct("code");
        } catch (InvalidProductCodeException e) {
            assert true;
            return;
        }
        assert false;
    }

    @Test
    public void getProduct_whenProductIsInDB_shouldReturnIt() throws InvalidProductCodeException {
        Product product = new Product();
        product.setCode("aCode");

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode("aCode");

        when(productRepository.findByCode(any())).thenReturn(Optional.of(product));
        when(productMapper.toDTO(any())).thenReturn(productDTO);

        ProductDTO returnedProductDTO = productService.getProduct("aCode");

        assertThat(returnedProductDTO.getCode()).isEqualTo("aCode");

        verify(productRepository).findByCode("aCode");
        verify(productMapper).toDTO(product);
    }































}
