package modelTests;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import flooring.model.Order;


public class OrderTest {

    @Test
    public void testOrderConstructorAndId(){
        //Arange
        String customerName = "James";
        String state = "TX";
        String productType = "Tile";
        BigDecimal area = new BigDecimal("1000");

        //Act
        Order order = new Order(customerName, state, productType, area);
        order.setOrderNumber(1);

        //Assert
        assertEquals(1, order.getOrderNumber(), "Order number should be 1");
        assertEquals(customerName, order.getCustomerName(), "Customer name should be James");
        assertEquals(state, order.getState(),"State should be TX");
        assertEquals(productType, order.getProductType(), "Product type should be Tile");
        assertEquals(area, order.getArea(), "Area should be 1000");
    }

    @Test
    public void testOrderParameters(){
        //Arrange
        Order order = new Order("James", "TX", "Tile", new BigDecimal(1000));

        //Act
        order.setOrderNumber(1);
        order.setTaxRate(new BigDecimal(10));
        order.setCostPerSquareFoot(new BigDecimal(20));
        order.setMaterialCost(new BigDecimal(20));
        order.setLaborCost(new BigDecimal(20));
        order.setTax(new BigDecimal(20));
        order.setTotal(new BigDecimal(1000));

        //Assert
        assertEquals(new BigDecimal(10), order.getTaxRate(), "Tax rate should be 10");
        assertEquals(new BigDecimal(20), order.getCostPerSquareFoot(), "Cost per square foot should be 20");
        assertEquals(new BigDecimal(20), order.getMaterialCost(), "Material cost should be 20");
        assertEquals(new BigDecimal(20), order.getLaborCost(), "Labour cost should be 20");
        assertEquals(new BigDecimal(20), order.getTax(), "Tax should be 20");
        assertEquals(new BigDecimal(1000), order.getTotal(), "Total should be 1000");    
    }
}
