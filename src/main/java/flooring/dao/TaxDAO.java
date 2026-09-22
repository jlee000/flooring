package flooring.dao;

import java.util.List;

import flooring.exception.OrderException;
import flooring.model.Tax;

public interface TaxDAO {
    List<Tax> getTaxes() throws OrderException;
    Tax getTaxRate(String taxType) throws OrderException;
}
