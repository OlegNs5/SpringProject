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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//import static javafx.beans.binding.Bindings.when;
//import static net.bytebuddy.matcher.ElementMatchers.any;
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

    @Test
    public void getProducts(){
        List<Product> products = new ArrayList<>();
        Product productOne = new Product();
        productOne.setCode("aCode");

        Product productTwo = new Product();
        productTwo.setCode("bCode");

        products.add(productOne);
        products.add(productTwo);

        ProductDTO productOneDTO = new ProductDTO();
        productOneDTO.setCode("aCode");

        ProductDTO productTwoDTO = new ProductDTO();
        productTwoDTO.setCode("bCode");

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDTO(productOne)).thenReturn(productOneDTO);
        when(productMapper.toDTO(productTwo)).thenReturn(productTwoDTO);

        List<ProductDTO> productList = productService.getAllProducts();
        assertThat(productList).hasSize(2);
        assertThat(productList).containsOnly(productOneDTO,productTwoDTO);

        verify(productRepository).findAll();
        verify(productMapper).toDTO(productOne);
        verify(productMapper).toDTO(productTwo);

    }

    @Test
    public void updateProduct_whenProductCodeIsNull_shouldThrowAnException(){
        ProductDTO productDTO = new ProductDTO();
        InvalidProductCodeException invalidProductCodeException =
                catchThrowableOfType(() -> productService.updateProduct(productDTO,1L), InvalidProductCodeException.class);

        assertThat(invalidProductCodeException).isNotNull();
    }

    @Test
    public void updateProduct_whenProductDTOIsNull_shouldThrowAnException(){
        InvalidProductCodeException invalidProductCodeException =
                catchThrowableOfType(() -> productService.updateProduct(null,1L), InvalidProductCodeException.class);

        assertThat(invalidProductCodeException).isNotNull();
    }

    @Test
    public void updateProduct_whenProductCodeIsValid_shouldUpdateTheProduct() throws InvalidProductCodeException {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode("a new code");
        productDTO.setDescription("a new description");

        Product product = new Product();
        product.setCode("code");
        product.setDescription("descriptioon");

        when(productRepository.findByCode(any())).thenReturn(Optional.of(product)); //any() orice apel
        productService.updateProduct(productDTO,1L);

        verify(productRepository).findByCode(productDTO.getCode());
        verify(productRepository).save(product);

    }
    @Test
    public void deleteProduct_whenProductDTOIsNull_shouldThrowAnException(){

        InvalidProductCodeException invalidProductCodeException =
                catchThrowableOfType(() -> productService.deleteProduct(null,1L), InvalidProductCodeException.class);

        assertThat(invalidProductCodeException).isNotNull();
    }

    @Test
    public void deleteProduct_whenProductCodeIsValid_shouldDeleteTheProduct() throws InvalidProductCodeException {
        Product product = new Product();
        product.setCode("aCode");
        when(productRepository.findByCode(any())).thenReturn(Optional.of(product));
        productService.deleteProduct("aCode",1L);

         verify(productRepository).findByCode("aCode");
          verify(productRepository).delete(product);

    }
}