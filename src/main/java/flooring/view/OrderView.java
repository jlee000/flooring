package flooring.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.stereotype.Component;

import flooring.model.Order;

@Component
public class OrderView{

    UserIO userIO;

    public OrderView(UserIO userIO) {
        this.userIO = userIO;
    }

    public int printMenu(){
        userIO.print("");
        userIO.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        userIO.print("* <<Flooring Program>>");
        userIO.print("* 1. Display Orders");
        userIO.print("* 2. Add an Order");
        userIO.print("* 3. Edit an Order");
        userIO.print("* 4. Remove an Order");
        userIO.print("* 5. Export All Data");
        userIO.print("* 6. Quit");
        userIO.print("*");
        userIO.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

        return userIO.readInt("Choice: ");
    }

    public boolean confirm(String message){
        String answer = userIO.readString(message);
        return answer.equalsIgnoreCase("Y");
    }

    public void displayMessage(String message){
        userIO.print(message);
    }

    public void displayOrders(List<Order> orders){
        for (Order order : orders) {
            userIO.print(order.toString());
            userIO.print("");
        }
    }

    public void displayOrders(Order order){
        if (order == null) {
            userIO.print("Order not found.");
        } else {
            userIO.print("");
            userIO.print(order.toString());
        }
    }

    public int getOrderById(){
        return userIO.readInt("Order Number: ");
    }

    public Order addOrder(){ 
        String name = userIO.readString("Customer Name: ");
        while (name.trim().isEmpty() || name.contains(",") || name.contains(".")) {
            name = userIO.readString("Customer Name cannot be empty or contain , or . : ");
        }

        String state = readState(null, false);
        String productType = readProduct(null, false);
        BigDecimal area = readArea("Area: (100+): ", false);
        
        return new Order(name, state, productType, area);
    }

    public Order editOrder(Order order){
        userIO.print("Press Enter to keep the existing value Or input new order details:");
        
        String name = userIO.readString("Customer Name (" + order.getCustomerName() + "): ");


        String state = readState(order.getState(), true);

        String productType = readProduct(order.getProductType(), true);

        BigDecimal area = readArea("Area (100+) (current: " + order.getArea() + "): ", true);

        if (!name.isBlank()) {
            order.setCustomerName(name);
        }

        order.setState(state);

        order.setProductType(productType);

        if (area != null) {
            order.setArea(area);
        }

        return order;
    }

    public LocalDate getOrderDate(){
        while(true){
            String date = userIO.readString("Order Date (MM/dd/yyyy): ");
            try{
                return LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            }catch(DateTimeParseException e) {
                userIO.print("Invalid date. Please use MM/dd/yyyy.");
            }
        }
    }

    public LocalDate getFutureOrderDate(){
        while(true){
            LocalDate date = getOrderDate();
            if (date.isAfter(LocalDate.now())) {
                return date;
            }
            userIO.print("Order date must be in the future and in format MM/dd/yyyy.");
        }
    }

    public BigDecimal readArea(String givenArea, boolean allowBlank) {
        while(true) {
            String input = userIO.readString(givenArea);

            if (input.isBlank()) {
                if(allowBlank){
                    return null;
                }
                userIO.print("Area is required (100+)");
                continue;
            }

            try {
                BigDecimal area = new BigDecimal(input);

                if(area.compareTo(new BigDecimal("100")) >= 0) {
                    return area;
                }

                userIO.print("Area must be 100+");
            }catch(NumberFormatException e) {
                userIO.print("Please enter a valid number.");
            }
        }
    }

    public String readState(String givenState, boolean allowBlank) {
        while (true) {
            String state = userIO.readString("State (TX=Texas, WA=Washington, KY=Kentucky, CA=California): ");

            if (state.isBlank()) {
                if (allowBlank) {
                    return givenState;
                }
                userIO.print("Enter State.");
                continue;
            }

            if (state.equalsIgnoreCase("TX") 
                    || state.equalsIgnoreCase("Texas")
                    || state.equalsIgnoreCase("WA")
                    || state.equalsIgnoreCase("Washington")
                    || state.equalsIgnoreCase("KY")
                    || state.equalsIgnoreCase("Kentucky")
                    || state.equalsIgnoreCase("CA")
                    || state.equalsIgnoreCase("California")) {
                return state;
            }

            userIO.print("Invalid state. (TX=Texas, WA=Washington, KY=Kentucky, CA=California): ");
        }
    }

    public String readProduct(String givenProduct, boolean allowBlank) {
        while (true) {
            String product = userIO.readString("Product type (Carpet, Laminate, Tile, Wood): ");

            if (product.isBlank()) {
                if (allowBlank) {
                    return givenProduct;
                }

                userIO.print("Enter Product type.");
                continue;
            }

            if (product.equalsIgnoreCase("Carpet")
                    || product.equalsIgnoreCase("Laminate")
                    || product.equalsIgnoreCase("Tile")
                    || product.equalsIgnoreCase("Wood")) {
                return product;
            }

            userIO.print("Invalid product. (Carpet, Laminate, Tile, Wood): ");
        }
    }

}
