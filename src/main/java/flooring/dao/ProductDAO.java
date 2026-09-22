package flooring.dao;

import java.util.List;

import flooring.exception.OrderException;
import flooring.model.Product;

public interface ProductDAO {
    List<Product> getProducts() throws OrderException;
    Product getProduct(String productType) throws OrderException;
}