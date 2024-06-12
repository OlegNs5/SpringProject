package ro.ctrln.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;
import ro.ctrln.dtos.ProductDTO;
import ro.ctrln.exceptions.InvalidProductCodeException;
import ro.ctrln.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

@Autowired
    private ProductService productService;


    @GetMapping("/{productCode}")
    public ProductDTO getProduct(@PathVariable String productCode)throws InvalidProductCodeException {
        return productService.getProduct(productCode);
    }

    @PostMapping("/{costumerId}")
    public void addProduct(@RequestBody ProductDTO productDTO,@PathVariable long costumerId){
        productService.addProduct(productDTO,costumerId);
    }

    @PutMapping("/{costumerId}")
    public void updateProduct(@RequestBody ProductDTO productDTO,@PathVariable Long costumerId) throws InvalidProductCodeException {
        productService.updateProduct(productDTO,costumerId);
    }

    @DeleteMapping("/{productCode}/{costumerId}")
    public void deleteProduct(@PathVariable String productCode,@PathVariable Long costumerId) throws InvalidProductCodeException {
        productService.deleteProduct(productCode,costumerId);

    }

    @GetMapping
    public List<ProductDTO> getProduct(){
        return productService.getAllProducts();
    }

}
