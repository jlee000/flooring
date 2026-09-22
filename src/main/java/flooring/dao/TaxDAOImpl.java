package flooring.dao;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import flooring.exception.OrderException;
import flooring.model.Tax;

@Component
public class TaxDAOImpl implements TaxDAO {

    private static final String FILEPATH = "src/main/resources/taxes/Taxes.txt";

    @Override
    public List<Tax> getTaxes() throws OrderException {
        List<Tax> taxes = new ArrayList<>();
        
        try(BufferedReader reader = new BufferedReader(new FileReader(FILEPATH))) {
            reader.readLine();//Skip header
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Tax tax = new Tax();
                tax.setStateAbbreviation(data[0]);
                tax.setStateName(data[1]);
                tax.setTaxRate(new BigDecimal(data[2]));
                taxes.add(tax);
            }
        }catch(IOException | NumberFormatException e) {
            throw new OrderException("Could not load taxes.", e);
        }
        return taxes;
    }

    @Override
    public Tax getTaxRate(String state) throws OrderException {
        List<Tax> taxes = getTaxes();
        return taxes.stream()
            .filter(tax ->
                    state.equalsIgnoreCase(tax.getStateAbbreviation())
                    || state.equalsIgnoreCase(tax.getStateName()))
            .findFirst()
            .orElse(null);
    }
}
