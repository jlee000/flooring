package flooring.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import flooring.dao.ExportDAO;
import flooring.dao.OrderDAO;
import flooring.dao.ProductDAO;
import flooring.dao.TaxDAO;
import flooring.exception.OrderException;
import flooring.exception.PersistenceException;
import flooring.model.Order;
import flooring.model.Product;
import flooring.model.Tax;

@Component
public class OrderServiceImpl implements OrderService{

    private final OrderDAO orderDao;
    private final TaxDAO taxDao;
    private final ProductDAO productDao;
    private final ExportDAO exportDAO;

    public OrderServiceImpl(OrderDAO orderDao, ProductDAO productDao, TaxDAO taxDao, ExportDAO exportDAO) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
        this.exportDAO = exportDAO;
    }    

    @Override
	public List<Order> getOrders(LocalDate date) throws PersistenceException {
		return orderDao.getOrders(date);
	}

    @Override
    public Order getOrderById(LocalDate date, int orderId) throws PersistenceException {
        return orderDao.getOrderById(date, orderId);
    }

    @Override
    public void addOrder(LocalDate date, Order order)throws OrderException, PersistenceException {
        orderDao.addOrder(date, order);
    }
	
    @Override
    public Order editOrder(LocalDate date, Order updatedOrder) throws  OrderException, PersistenceException {    
        return orderDao.editOrder(date, updatedOrder);
    }

    @Override
    public Order deleteOrder(LocalDate date, int orderId) throws OrderException, PersistenceException {
        return orderDao.deleteOrder(date, orderId);
    }

    @Override
    public void exportOrderData() throws PersistenceException {
        exportDAO.exportOrderData();
    }

    
    @Override
    public int setOrderNumber(LocalDate date) throws PersistenceException {
        int maxOrderNumber = 0;
        List<Order> orders = orderDao.getOrders(date);

        for (Order existingOrder : orders) {
            if (existingOrder.getOrderNumber() > maxOrderNumber) {
                maxOrderNumber = existingOrder.getOrderNumber();
            }
        }
        return maxOrderNumber + 1;
    }

    @Override
    public Order calculateOrder(LocalDate date, Order order) throws OrderException, PersistenceException {
        Tax taxRate = taxDao.getTaxRate(order.getState());
        if (taxRate == null) {
            throw new OrderException("State is not available.");
        }
        Product product = productDao.getProduct(order.getProductType());
        if (product == null) {
            throw new OrderException("Product is not available.");
        }
        order.setTaxRate(taxRate.getTaxRate());
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());

        //materialCost; (Area * CostPerSquareFoot)
        BigDecimal materialCost = order.getArea().multiply(order.getCostPerSquareFoot());
        
        //laborCost; (Area * LaborCostPerSquareFoot)
        BigDecimal laborCost = order.getArea().multiply(order.getLaborCostPerSquareFoot());
        
        //tax; (MaterialCost + LaborCost) * (TaxRate/100)
        BigDecimal tax = materialCost.add(laborCost).multiply(order.getTaxRate().divide(new BigDecimal("100")));

        //total; (MaterialCost + LaborCost + Tax)
        BigDecimal total = materialCost.add(laborCost).add(tax);

        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);

        return order;
    }  
}
