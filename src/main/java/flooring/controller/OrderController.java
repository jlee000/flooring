package flooring.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import flooring.exception.OrderException;
import flooring.exception.PersistenceException;
import flooring.model.Order;
import flooring.service.OrderServiceImpl;
import flooring.view.OrderView;

@Component
public class OrderController {
    
private final OrderServiceImpl orderService;
private final OrderView orderView;

    public OrderController(OrderServiceImpl orderService, OrderView orderView) {
        this.orderService = orderService;
        this.orderView = orderView;
    }

    public void run(){
        
        mainMenu:
        while (true) {
            int choice = orderView.printMenu();
            switch (choice) {
                case 1: //Display Orders
                    try{
                        LocalDate date = orderView.getOrderDate();
                        List<Order> orders = orderService.getOrders(date);

                        if(orders.isEmpty()){
                            orderView.displayMessage("No orders found for given date.");
                        }else{
                            orderView.displayOrders(orders);
                        }
                    }catch(PersistenceException e) {
                        orderView.displayMessage(e.getMessage());
                    }
                    break;

                case 2: //Add an Order
                    try{
                        LocalDate date = orderView.getFutureOrderDate();
                        Order order = orderView.addOrder();
                        order.setOrderNumber(orderService.setOrderNumber(date));
                        order = orderService.calculateOrder(date, order);
                        orderView.displayOrders(order);

                        if (orderView.confirm("Confirm order? (Y/N): ")) {
                            orderService.addOrder(date, order);
                            orderView.displayMessage("Order added successfully.");
                        } else {
                            orderView.displayMessage("Order not added.");
                        }
                    }catch(OrderException | PersistenceException e) {
                        orderView.displayMessage(e.getMessage());
                    }
                    break;

                case 3://Edit an Order
                    try{
                        LocalDate date = orderView.getOrderDate();
                        int orderNumber = orderView.getOrderById();
                        Order order = orderService.getOrderById(date, orderNumber);

                        if (order == null) {
                            orderView.displayMessage("Order not found.");
                            break;
                        }

                        Order updatedOrder = orderView.editOrder(order);
                        updatedOrder = orderService.calculateOrder(date, updatedOrder);
                        orderView.displayOrders(updatedOrder);

                        if (orderView.confirm("Save changes? (Y/N): ")) {
                            orderService.editOrder(date, updatedOrder);
                            orderView.displayMessage("Order updated successfully.");
                        } else {
                            orderView.displayMessage("Order not updated.");
                        }
                    }catch(OrderException | PersistenceException e) {
                        orderView.displayMessage(e.getMessage());
                    }
                    break;

                case 4: //Remove an Order
                    try{
                        LocalDate date = orderView.getOrderDate();
                        int orderNumber = orderView.getOrderById();
                        Order orderToDelete = orderService.getOrderById(date, orderNumber);

                        if (orderToDelete == null) {
                            orderView.displayMessage("Order not found.");
                            break;
                        }

                        orderView.displayOrders(orderToDelete);

                        if (orderView.confirm("Confirm delete? (Y/N): ")) {
                            orderService.deleteOrder(date, orderNumber);
                            orderView.displayMessage("Order deleted successfully.");
                        } else {
                            orderView.displayMessage("Order not deleted.");
                        }                        

                    }catch(OrderException | PersistenceException e) {
                        orderView.displayMessage(e.getMessage());
                    }
                    break;

                case 5: //Export All Data
                    try{
                        orderService.exportOrderData();
                        orderView.displayMessage("All data exported successfully.");
                    }catch(PersistenceException e) {
                        orderView.displayMessage(e.getMessage());
                    }
                    break;

                case 6: //Quit
                    break mainMenu;

                default:
                    orderView.displayMessage("Incorrect value entered.");
            }
           if(!orderView.confirm("Go back to Main Menu? (Y/N): ")){
                break;
            }
        }
        orderView.displayMessage("Exiting.");
    }

}