package flooring.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import flooring.exception.PersistenceException;
import flooring.model.Order;

@Component
public class OrderDAOImpl implements OrderDAO {
    private static final String DIRECTORY = "src/main/resources/Orders";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMddyyyy");

    private File getFile(LocalDate date) {
        return new File(DIRECTORY,"Orders_" + date.format(DATE_FORMAT) + ".txt");
    }

    @Override
    public Order getOrderById(LocalDate date, int orderNumber) throws PersistenceException {
        List<Order> orders = getOrders(date);
        for(Order order : orders){
            if (order.getOrderNumber() == orderNumber) {
                return order;
            }
        }
        return null;
    }

    @Override
    public List<Order> getOrders(LocalDate date) throws PersistenceException {
        List<Order> orders = new ArrayList<>();
        File file = getFile(date);

        if(!file.exists()) {
            return orders;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {            
            reader.readLine(); //Skip header
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Order order = new Order();
                order.setOrderNumber(Integer.parseInt(data[0]));
                order.setCustomerName(data[1]);
                order.setState(data[2]);
                order.setTaxRate(new BigDecimal(data[3]));
                order.setProductType(data[4]);
                order.setArea(new BigDecimal(data[5]));
                order.setCostPerSquareFoot(new BigDecimal(data[6]));
                order.setLaborCostPerSquareFoot(new BigDecimal(data[7]));
                order.setMaterialCost(new BigDecimal(data[8]));
                order.setLaborCost(new BigDecimal(data[9]));
                order.setTax(new BigDecimal(data[10]));
                order.setTotal(new BigDecimal(data[11]));

                orders.add(order);
            }
        }catch(IOException | NumberFormatException e) {
            throw new PersistenceException("Could not load orders", e);
        }
        return orders;
    }

    @Override
    public void saveOrders(LocalDate date, List<Order> orders) throws PersistenceException {
        File directory = new File(DIRECTORY);
        directory.mkdirs();
        File file = getFile(date);
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");

            for (Order o : orders) {
                pw.println(o.getOrderNumber() + "," + o.getCustomerName() + "," + o.getState() + "," + o.getTaxRate() + "," + o.getProductType() + "," + o.getArea() + "," + o.getCostPerSquareFoot() + "," + o.getLaborCostPerSquareFoot() + "," + o.getMaterialCost() + "," + o.getLaborCost() + "," + o.getTax() + "," + o.getTotal());
            }
        }catch(IOException e) {
            throw new PersistenceException("Could not save orders.", e);
        }
    }

    @Override
    public Order addOrder(LocalDate date, Order order) throws PersistenceException {
        List<Order> orders = getOrders(date);
        orders.add(order);
        saveOrders(date, orders);
        return order;
    }

    @Override
    public Order editOrder(LocalDate date, Order order) throws PersistenceException {
        List<Order> orders = getOrders(date);
        for(int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderNumber() == order.getOrderNumber()) {
                orders.set(i, order);
                saveOrders(date, orders);
                return order;
            }
        }
        return null;
    }

    @Override
    public Order deleteOrder(LocalDate date, int orderNumber) throws PersistenceException {
        List<Order> orders = getOrders(date);
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderNumber() == orderNumber) {
                Order deletedOrder = orders.remove(i);
                saveOrders(date, orders);
                return deletedOrder;
            }
        }
        return null;
    }
}