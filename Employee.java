
import java.io.*;
public class Employee implements Serializable
{
    private String firstName;
    private String surname;
    private int empNumber;
    private double credit;
    
    public Employee()
    {
        
    }
    
    public Employee(String fName, String lName, int empNum, double cred)
    {
        setFirstName(fName);
        setSurname(lName);
        setEmpNumber(empNum);
        setCredit(cred);
    }
    
    public void setFirstName(String fName)
    {
        firstName = fName;
    }
    
    public void setSurname(String sName)
    {
        surname = sName;
    }
    
    public void setEmpNumber(int empNum)
    {
        empNumber = empNum;
    }
    
    public void setCredit(double credAmt)
    {
        credit = credAmt;
    }
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public String getSurname()
    {
        return surname;
    }
    
    public int getEmpNumber()
    {
        return empNumber;
    }
    
    public double getCredit()
    {
        return credit;
    }
    
    //this method must be edited to display strings properly(in order)
    @Override
    public String toString()
    {
        return String.format("%-15s%-12s%-16d%.2f", getFirstName(), getSurname(),
                getEmpNumber(), getCredit());
    }      
}
