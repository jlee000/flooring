package flooring.view;
import java.math.BigDecimal;
import java.util.Scanner;

import org.springframework.stereotype.Component;

@Component
public class UserIOConsoleImpl implements UserIO {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void print(String s) {
        System.out.println(s);
    }

    @Override
    public String readString(String s) {
        System.out.print(s);
        return scanner.nextLine();
    }

    @Override
    public int readInt(String s) {
        while(true){
            try{
                System.out.print(s);
                return Integer.parseInt(scanner.nextLine());
            }catch(NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    @Override
    public long readLong(String s) {
        while(true){
            try{
                System.out.print(s);
                return Long.parseLong(scanner.nextLine());
            }catch(NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    @Override
    public float readFloat(String s) {
        while(true){
            try{
                System.out.print(s);
                return Float.parseFloat(scanner.nextLine());
            }catch(NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    @Override
    public double readDouble(String s) {
        while(true){
            try{
                System.out.print(s);
                return Double.parseDouble(scanner.nextLine());
            }catch(NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    @Override
    public BigDecimal readBigDecimal(String s) {
        while(true){
            try{
                System.out.print(s);
                return new BigDecimal(scanner.nextLine());
            }catch(NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}