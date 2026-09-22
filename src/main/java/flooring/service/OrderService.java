package flooring.service;

import java.time.LocalDate;
import java.util.List;

import flooring.exception.OrderException;
import flooring.exception.PersistenceException;
import flooring.model.Order;

public interface OrderService {
    List<Order> getOrders(LocalDate date) throws PersistenceException;
    Order getOrderById(LocalDate date, int orderId) throws PersistenceException;
    void addOrder(LocalDate date, Order order)throws OrderException, PersistenceException;
    Order editOrder(LocalDate date, Order updatedOrder) throws OrderException, PersistenceException;
    Order deleteOrder(LocalDate date, int orderId) throws OrderException, PersistenceException;
    void exportOrderData() throws PersistenceException;
    int setOrderNumber(LocalDate date) throws PersistenceException;
    Order calculateOrder(LocalDate date, Order order) throws OrderException, PersistenceException;
}
