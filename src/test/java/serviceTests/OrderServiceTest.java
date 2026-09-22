package serviceTests;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import flooring.dao.OrderDAO;
import flooring.dao.ProductDAO;
import flooring.dao.TaxDAO;
import flooring.exception.OrderException;
import flooring.exception.PersistenceException;
import flooring.model.Order;
import flooring.model.Product;
import flooring.model.Tax;
import flooring.service.OrderServiceImpl;

@ExtendWith (MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderDAO orderDao;

    @Mock
    private TaxDAO taxDao;

    @Mock
    private ProductDAO productDao;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void testSetOrderNumber() throws PersistenceException{
        //Arrange
        Order order1 = new Order();
        order1.setOrderNumber(1);
        Order order2 = new Order();
        order2.setOrderNumber(3);
        Order order3 = new Order();
        order3.setOrderNumber(7);

        when(orderDao.getOrders(LocalDate.of(2026, 10, 10))).thenReturn(Arrays.asList(order1, order2, order3));

        //Act
        int result = orderService.setOrderNumber(LocalDate.of(2026, 10, 10));

        //Assert
        assertEquals(8, result, "Next order number should be 8");       
    }

    @Test 
    public void testCalculateOrder() throws OrderException, PersistenceException{
        //Arrange
        LocalDate date = LocalDate.of(2026, 10, 10);
        Order order = new Order("James", "TX", "Carpet", new BigDecimal(100));
        Tax tax = new Tax("TX", "Texas", new BigDecimal("10.0"));
        Product product = new Product("Carpet", new BigDecimal("10.0"), new BigDecimal("10.0"));

        when(taxDao.getTaxRate("TX")).thenReturn(tax);
        when(productDao.getProduct("Carpet")).thenReturn(product);

        //Act
        Order result = orderService.calculateOrder(date, order);

        //Assert
        assertEquals(new BigDecimal("10.0"), result.getTaxRate());
        assertEquals(new BigDecimal("10.0"), result.getCostPerSquareFoot());
        assertEquals(new BigDecimal("10.0"), result.getLaborCostPerSquareFoot());

        //materialCost; (Area * CostPerSquareFoot)
        assertEquals(new BigDecimal("1000.0"), result.getMaterialCost());

        //laborCost; (Area * LaborCostPerSquareFoot)
        assertEquals(new BigDecimal("1000.0"), result.getLaborCost());
        
        //tax; (MaterialCost + LaborCost) * (TaxRate/100)
        assertEquals(new BigDecimal("200.0"), result.getTax().setScale(1));
        
        //total; (MaterialCost + LaborCost + Tax)
        assertEquals(new BigDecimal("2200.0"), result.getTotal().setScale(1));
    }
}
