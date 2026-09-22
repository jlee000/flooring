package flooring.view;

import java.math.BigDecimal;

public interface UserIO {
    void print(String s);
    String readString(String s);
    int readInt(String s);
    long readLong(String prosmpt);
    float readFloat(String s);
    double readDouble(String s);
    BigDecimal readBigDecimal(String s);
}
