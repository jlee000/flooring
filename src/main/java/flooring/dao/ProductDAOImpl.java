package flooring.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import flooring.exception.OrderException;
import flooring.model.Product;

@Component
public class ProductDAOImpl implements ProductDAO {

    private static final String FILEPATH = "src/main/resources/products/Products.txt";

    @Override
    public List<Product> getProducts() throws OrderException {

        List<Product> products = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(FILEPATH))) {
            reader.readLine();//Skip header
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Product product = new Product();
                product.setProductType(data[0]);
                product.setCostPerSquareFoot(new BigDecimal(data[1]));
                product.setLaborCostPerSquareFoot(new BigDecimal(data[2]));
                products.add(product);
            }
        }catch(IOException | NumberFormatException e) {
            throw new OrderException("Could not load products.", e);
        }
        return products;
    }

    @Override
    public Product getProduct(String productType) throws OrderException {
        List<Product> products = getProducts();
        return products.stream()
            .filter(product ->
                    productType.equalsIgnoreCase(product.getProductType()))
            .findFirst()
            .orElse(null);     
    }
}
