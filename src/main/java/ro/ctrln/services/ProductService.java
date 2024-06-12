package ro.ctrln.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ctrln.dtos.ProductDTO;
import ro.ctrln.entities.Product;
import ro.ctrln.exceptions.InvalidProductCodeException;
import ro.ctrln.mappers.ProductMapper;
import ro.ctrln.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

//    @Autowired
    private final ProductRepository productRepository; // @RequiredArgsConstructor inseamna ca trebuie sa fie finale ↓

    //    @Autowired
    private final ProductMapper productMapper;

    public void addProduct(ProductDTO productDTO, long costumerId) {
        Product product = productMapper.toEntity(productDTO);
        productRepository.save(product);
    }

    public ProductDTO getProduct(String productCode) throws InvalidProductCodeException {
        Product product = getProductEntity(productCode); //Cream un produs
        return productMapper.toDTO(product) ;
    }

    public void updateProduct(ProductDTO productDTO, Long costumerId) throws InvalidProductCodeException {
        log.info("Costumer with id {} is trying to update product!",costumerId,productDTO.getCode());
        if (productDTO.getCode()==null){
            throw new InvalidProductCodeException();
        }

        Product product = getProductEntity(productDTO.getCode()); //Actualizam un produs
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCurrency(productDTO.getCurrency());
        product.setValid(productDTO.isValid());

        productRepository.save(product);
    }

    public void deleteProduct(String productCode, Long costumerId) throws InvalidProductCodeException {
        log.info("Costumer with id {} is trying to update product!", costumerId, productCode); //log.info vine din @Slf4j
        if (productCode == null) {
            throw new InvalidProductCodeException();
        }

        Product product = getProductEntity(productCode); //Stergem un produs
        productRepository.delete(product);

    }

    private Product getProductEntity(String productCode) throws InvalidProductCodeException {
        Optional<Product> product = productRepository.findByCode(productCode);
        if (!product.isPresent()){
            throw new InvalidProductCodeException();
        }
        return product.get();
    }

    public List<ProductDTO> getAllProducts() {

        return productRepository.findAll().stream().map(productMapper::toDTO).collect(Collectors.toList());
    }


}
