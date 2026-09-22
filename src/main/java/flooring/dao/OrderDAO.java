package flooring.dao;

import java.time.LocalDate;
import java.util.List;

import flooring.exception.PersistenceException;
import flooring.model.Order;

public interface OrderDAO {
    List<Order> getOrders(LocalDate date) throws PersistenceException;
    void saveOrders(LocalDate date, List<Order> orders) throws PersistenceException;
    Order getOrderById(LocalDate date, int orderId) throws PersistenceException;
    Order addOrder(LocalDate date, Order order) throws PersistenceException;
    Order editOrder(LocalDate date, Order order) throws PersistenceException;
    Order deleteOrder(LocalDate date, int orderId) throws PersistenceException;     
}
