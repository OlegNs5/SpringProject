package ro.ctrln.dtos;

import lombok.Data;
import org.aspectj.weaver.ast.Or;

import java.util.List;

@Data
public class OrderDTO {

    private List<OrderProductDTO> products;

}
