package flooring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import flooring.controller.OrderController;
import flooring.exception.OrderException;

public class Main {
    public static void main(String[] args) throws OrderException {
        try (AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext("flooring")) {
            OrderController controller = appContext.getBean(OrderController.class);
            controller.run();
        }
    }
}