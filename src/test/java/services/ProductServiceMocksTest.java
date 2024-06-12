package services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ro.ctrln.mappers.ProductMapper;
import ro.ctrln.repositories.ProductRepository;
import ro.ctrln.services.ProductService;

//@RunWith(SpringRunner.class)
public class ProductServiceMocksTest { //merge sa facem o clasa de test folosind @InjectMocks,@Mock si @MockBean

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductMapper productMapper; // ProductMapper este o clasa de aia treabuie adnotata cu @Mock

    @MockBean
    private ProductRepository productRepository; //ProductRepository  este o interfata de aia trebuie adnotata cu @MockBean

    @Test
    public void contextLoads(){

    }
}
