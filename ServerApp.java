


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.sql.*;

/**
 *
 * @author Rafiq Roman
 */
public class ServerApp
{
    
    
    // Server socket
    private ServerSocket listener;
    
    // Client connection
    private Socket client;
    
    private ObjectInputStream in;
    private ObjectInputStream in2;
    
    private Statement s;
    /** Creates a new instance of ServerApp */
    public ServerApp()
    {
        
    
        // Create server socket
        try {
            listener = new ServerSocket(12345, 10);
            
    
                    String filename = "Hungry Hippo.mdb";
                    String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
                    dbURL+= filename.trim() + ";DriverID=22;READONLY=true}"; 
                    String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
            

                    Class.forName(driverName);
                
                    Connection con = DriverManager.getConnection(dbURL, "",""); 
                       s = con.createStatement();
                    
            
        }
        catch (IOException ioe)
        {
          System.out.println("IO Exception: " + ioe.getMessage());
        }
         catch (Exception err) 
        {
            System.out.println("ERROR: " + err);
        }
    }
    
    public void listen()
    {
        // Start listening for client connections
        try {
          client = listener.accept();  
          
          processClient();
        }
        catch(IOException ioe)
        {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
    }
    
    public void processClient()
    {
        // Communicate with the client
        
        // First step: initiate channels
        try {
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            
            // Step 2: communicate
            String msg;
            do
            {
                
                msg = (String)in.readObject();
                
                
                if(msg.equalsIgnoreCase("Create"))
                {
                    String create_Table_stmt = "create table FoodMenu(FoodID INTEGER, Category VARCHAR(30), FoodItem  VARCHAR(30), Price DOUBLE, Quantity INTEGER )";
                    String create_Table_stmt2 = "create table Employee(empNum INTEGER, FirstName VARCHAR(30), LastName  VARCHAR(30), Credit DOUBLE )";
                    
                    s.executeUpdate(create_Table_stmt);
                    s.executeUpdate(create_Table_stmt2);        
                    
                    loadTables();
                }
                else if(msg.equalsIgnoreCase("Insert Employee"))
                {       
                    String sel = (String)in.readObject();
                    
                    try
                    {
                            s.executeUpdate(sel);
  
                    }
                    catch(Exception e)
                    {
                        System.out.println(e);
                    }
 
                }
                else if(msg.equalsIgnoreCase("Delete employee id"))
                {
                
                    String strnum = (String)in.readObject();
                    int num = Integer.parseInt(strnum);
                     
                    try
                    {
                        s.executeUpdate("DELETE FROM Employee WHERE empNum = " + num);
                      //  out.writeObject("Delete successful");
                     }
                    catch(Exception e)
                    {
                        System.out.println(e);
                    }
                }
                 else if(msg.equalsIgnoreCase("Delete food"))
                {
                    
                    String strNum = (String)in.readObject();
                    int num = Integer.parseInt(strNum);
                     
                    try
                    {
                        s.executeUpdate("DELETE FROM FoodMenu WHERE foodid = " + num);
                      //  out.writeObject("Delete successful");
                     }
                    catch(Exception e)
                    {
                        System.out.println(e);
                    }
                }
                else if(msg.equalsIgnoreCase("Insert Item"))
                { 
                    
                    String upd = (String)in.readObject();
                    try
                    {
                            s.executeUpdate(upd);
                           // out.writeObject("Insert successful");
                    }
                    catch(Exception e)
                    {
                        System.out.println(e);
                    }
                    
                    
                }
                else if(msg.equalsIgnoreCase("View Employees"))
                {
                    Employee e = new Employee();
                    
                    s.executeQuery("select * from Employee ORDER BY empnum"); // select the data from the table
                    ResultSet rs = s.getResultSet(); 
                    
                    if (rs != null) // if rs == null, then there is no ResultSet to view        
                    while ( rs.next() ) // this will step through our data row-by-row
                    {
                   
                         e = new Employee( rs.getString(2), rs.getString(3), rs.getInt(1), rs.getDouble(4));
                     
                         out.writeObject(e);
                         
            
                    }
                    e = new Employee( "","", -99, 0);
                    out.writeObject(e);
                   
                }
                else if(msg.equalsIgnoreCase("View FoodMenu"))
                {
                    
                    int i = 0;
                    s.executeQuery("select COUNT(foodid) from FoodMenu"); // select the data from the table
                    ResultSet rs = s.getResultSet(); 
                    if (rs != null) // if rs == null, then there is no ResultSet to view        
                   while ( rs.next() ) // this will step through our data row-by-row
                    {
                   
                         i = rs.getInt(1);
                       
                    }
                    out.writeObject(i);
                    FoodMenuItem f = new FoodMenuItem();
         
                    s.executeQuery("select * from FoodMenu ORDER BY foodid"); // select the data from the table
                    ResultSet t = s.getResultSet(); 
              
   
                    if (t != null) // if rs == null, then there is no ResultSet to view        
                    while ( t.next() ) // this will step through our data row-by-row
                    {
                         out.writeObject(t.getInt(1));
                         f = new FoodMenuItem( t.getString(3), t.getString(2), t.getDouble(4));
                     
                         out.writeObject(f);
                   
            
                    }
               
                   
                }
                else if(msg.equalsIgnoreCase("View report"))
                {
                    
                    int i = 0;
                    s.executeQuery("select COUNT(foodid) from FoodMenu"); // select the data from the table
                    ResultSet rs = s.getResultSet(); 
                    if (rs != null) // if rs == null, then there is no ResultSet to view        
                   while ( rs.next() ) // this will step through our data row-by-row
                    {
                   
                         i = rs.getInt(1);
                       
                    }
                    out.writeObject(i);
                    FoodMenuItem f = new FoodMenuItem();
         
                    s.executeQuery("select * from FoodMenu ORDER BY quantity DESC"); // select the data from the table
                    ResultSet t = s.getResultSet(); 
              
   
                    if (t != null) // if rs == null, then there is no ResultSet to view        
                    while ( t.next() ) // this will step through our data row-by-row
                    {
                   
                         f = new FoodMenuItem( t.getString(3), t.getString(2), t.getDouble(4));
                         
                         out.writeObject(f);
                         out.writeObject(t.getInt(1));
                         out.writeObject(t.getInt(5));
            
                    }
               
                   
                }
                else if(msg.equalsIgnoreCase("load Combo"))
                {
                 
                    try
                    {
                         int size = 0;
                        s.executeQuery("select COUNT(empnum) from Employee"); // select the data from the table
                        ResultSet rs = s.getResultSet(); 
                        if (rs != null) // if rs == null, then there is no ResultSet to view        
                       while ( rs.next() ) // this will step through our data row-by-row
                        {
                       
                             size = rs.getInt(1);
                           
                        }
                            
                        int[] num = new int[size];
                        
                        int i = 0;
                        s.executeQuery("SELECT empNum FROM Employee ORDER BY empNum"); // select the data from the tab
                        rs = s.getResultSet(); 
                        if (rs != null) // if rs == null, then there is no ResultSet to view        
                            while ( rs.next() ) // this will step through our data row-by-row
                            {
                                
                                //out.writeObject(rs.getString(1));
                                num[i] = rs.getInt(1);
                                //out.flush();
                                
                                i++;
                            }
                            
                            out.writeObject(num);
                            //out.writeObject(i);
                            out.flush();
                            
                     }
                    catch(Exception e)
                    {
                        System.out.println("Exception : "  + e);
                    }
            
    
                }
                else if(msg.equalsIgnoreCase("get info"))
                {
                    try
                    {
                        String num = (String)in.readObject();
                        
                        int n = Integer.parseInt(num);
                        s.executeQuery("SELECT firstName, lastName, credit FROM Employee WHERE empNum = " + n ); // select the data from the tab
                        ResultSet rs = s.getResultSet(); 
                        if (rs != null) // if rs == null, then there is no ResultSet to view        
                            while ( rs.next() ) // this will step through our data row-by-row
                            {
                                Employee e = new Employee( rs.getString(1), rs.getString(2), 0 , rs.getDouble(3));
                                out.writeObject(e);
                                out.flush();
                            }
                            
                           
                            
                     }
                    catch(Exception e)
                    {
                        System.out.println(e);
                    }
                }
             
                else if(msg.equalsIgnoreCase("load list"))
                {
                    try
                    {
                         int size = 0;
                        s.executeQuery("select COUNT(foodid) from FoodMenu"); // select the data from the table
                        ResultSet rs = s.getResultSet(); 
                        if (rs != null) // if rs == null, then there is no ResultSet to view        
                       while ( rs.next() ) // this will step through our data row-by-row
                        {
                       
                             size = rs.getInt(1);
                           
                        }
                      
                        int i = 0;
                        FoodMenuItem fm;
                        String food = (String)in.readObject();
                      
                       
                        String[] temp = new String [size];
                        
                        s.executeQuery("SELECT FoodItem FROM FoodMenu WHERE category = '" +food+ "'" ); // select the data from the tab
                        rs = s.getResultSet(); 
                        if (rs != null) // if rs == null, then there is no ResultSet to view        
                            while ( rs.next() ) // this will step through our data row-by-row
                            {
                                temp[i] = rs.getString(1) ;
                               
                                
                                i++;
                            }
                        
                         //  out.writeObject(i);
                         //  out.flush();
                           out.writeObject(temp);
                            out.flush();
                     }
                    catch(Exception e)
                    {
                        System.out.println(e);
                    }
                    
                }
                else if(msg.equalsIgnoreCase("get price"))
                {
                    String food = (String)in.readObject();
                    
                    s.executeQuery("SELECT price FROM FoodMenu WHERE fooditem = '" +food+ "'" ); // select the data from the tab
                    ResultSet rs = s.getResultSet(); 
                        
                    if (rs != null) // if rs == null, then there is no ResultSet to view        
                            while ( rs.next() ) // this will step through our data row-by-row
                            {
                               out.writeObject (rs.getDouble(1)) ;
                                out.flush();
                            }
                        
                }
                else if(msg.equalsIgnoreCase("update credit"))
                {
                    try
                    {
                        
                      
                        String upd = (String)in.readObject();
                        s.executeUpdate(upd ); 
                    }
                    catch( Exception e)
                    {
                        System.out.println(e);
                    }
                      
                        
                }
                else if(msg.equalsIgnoreCase("update quantity"))
                {
                    try
                    {
                        
                        //int size = (int)in.readObject();
                        String[] item = (String[])in.readObject();
                        int quant = 0;
                       // double bal = (double)in.readObject();
                        for(int i = 0 ; i < item.length; i++)
                        {
                            s.executeQuery("SELECT quantity FROM FoodMenu WHERE fooditem = '" +item[i]+ "'" ); // select the data from the tab
                            ResultSet rs = s.getResultSet(); 
                        
                            if (rs != null) // if rs == null, then there is no ResultSet to view        
                                    while ( rs.next() ) // this will step through our data row-by-row
                                    {
                                        quant = rs.getInt(1);
                                        quant++;
                                        
                                        
                                    }
                             s.executeUpdate("UPDATE foodmenu SET quantity = " + quant + " WHERE fooditem = '" + item[i] + "'"  );         
                        }
                    //    s.executeUpdate("UPDATE employee SET credit = " + bal + " WHERE empid = " +id ); 
                    }
                    
                    catch( Exception e)
                    {
                        System.out.println(e);
                    }
     
                        
                }
                 else if(msg.equalsIgnoreCase("get id"))
                 {
                      int i = 1;
                      int id = 0;
                        s.executeQuery("select foodid from FoodMenu ORDER BY foodid"); // select the data from the table
                        ResultSet rs = s.getResultSet(); 
                        if (rs != null) // if rs == null, then there is no ResultSet to view        
                       while ( rs.next() ) // this will step through our data row-by-row
                        {
                       
                             if( i != rs.getInt(1))
                             {
                                id = i;
                                break;
                            }
                             i++;
                        }
                        if(id == 0)
                            id = i;
                            
                        out.writeObject(id);
                 }
                 else if(msg.equalsIgnoreCase("check id"))
                {
                    try
                    {
                        String result = "false";
                        
                        String strNum = (String)in.readObject();
                        int num = Integer.parseInt(strNum);
                        
                      
                        s.executeQuery("SELECT empnum FROM Employee" ); // select the data from the tab
                        ResultSet rs = s.getResultSet(); 
                        if (rs != null) // if rs == null, then there is no ResultSet to view        
                            while ( rs.next() ) // this will step through our data row-by-row
                            {
                                if(rs.getInt(1) == num)
                                    result = "true";
                            }
                            
                        out.writeObject(result);   
                            
                     }
                    catch(Exception e)
                    {
                        System.out.println(e);
                    }
                }
                 else if(msg.equalsIgnoreCase("check food id"))
                {
                    try
                    {
                        String result = "false";
                        String strNum = (String)in.readObject();
                        int num = Integer.parseInt(strNum);
                        
                      
                        s.executeQuery("SELECT foodid FROM FoodMenu" ); // select the data from the tab
                        ResultSet rs = s.getResultSet(); 
                        if (rs != null) // if rs == null, then there is no ResultSet to view        
                            while ( rs.next() ) // this will step through our data row-by-row
                            {
                                if(rs.getInt(1) == num)
                                    result = "true";
                            }
                            
                        out.writeObject(result);   
                            
                     }
                    catch(Exception e)
                    {
                        System.out.println(e);
                    }
                }
                else if(msg.equalsIgnoreCase("Terminate"))
                {
                     s.executeUpdate("DROP TABLE employee");
                     s.executeUpdate("DROP TABLE foodMenu");
                     out.writeObject("Terminate");
                     
                }
                    
                    
               
            }while(!msg.equalsIgnoreCase("terminate"));
            // Step 3:close down
            out.close();
            in.close();
            client.close();        
        }
        catch (IOException ioe)
        {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
        catch (ClassNotFoundException cnfe)
        {
            System.out.println("Class not found: " + cnfe.getMessage());
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    public void loadTables()
    {
        
                    
                    try
                    {
                       // int i = 0;
                        ObjectInputStream  in = new ObjectInputStream(new FileInputStream("Employees.ser"));
                       
                        
                        Employee emp;
                        
                    
                        while(true)
                        {
                            emp = (Employee)in.readObject();
                            
                            s.executeUpdate("insert into Employee values(" + emp.getEmpNumber() + ",'" + emp.getFirstName() + "','" + emp.getSurname() + "'," + emp.getCredit() + ")");
                            
                            
                        }
                    
                     }
                    catch(Exception e)
                    {
                       
                    }
                    
                    
                    try
                    {
                         ObjectInputStream  in2 = new ObjectInputStream(new FileInputStream("Foods.ser"));
                         FoodMenuItem foodItem;
                         while(true)
                        {
                            foodItem = (FoodMenuItem)in2.readObject();
                            s.executeUpdate("insert into FoodMenu values(" + foodItem.getFoodId() + ",'" + foodItem.getCategory() + "','" + foodItem.getFoodItem() + "'," + foodItem.getPrice() + ", " + 0 + ")");
                           
                        }
                        
                       
                    }
                    catch(Exception e)
                    {
                        return;
                    }
        
        
    }
  
    public static void main(String[] args)
    {
        // Create application
        ServerApp server = new ServerApp();
        
        // Start waiting for connections
        server.listen();
    }    
}
