package flooring.dao;

import flooring.exception.PersistenceException;

public interface ExportDAO {
    void exportOrderData() throws PersistenceException;
}
