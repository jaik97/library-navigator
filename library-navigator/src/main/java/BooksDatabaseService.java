/*
 * BooksDatabaseService.java
 *
 * The service threads for the books database server.
 * This class implements the database access service, i.e. opens a JDBC connection
 * to the database, makes and retrieves the query, and sends back the result.
 *
 * author: 2498893
 *
 */

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
//import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.Socket;

import java.util.StringTokenizer;

import java.sql.*;
import javax.sql.rowset.*;
    //Direct import of the classes CachedRowSet and CachedRowSetImpl will fail becuase
    //these clasess are not exported by the module. Instead, one needs to impor
    //javax.sql.rowset.* as above.



public class BooksDatabaseService extends Thread{

    private Socket serviceSocket = null;
    private String[] requestStr  = new String[2]; //One slot for author's name and one for library's name.
    private ResultSet outcome   = null;

	//JDBC connection
    private String USERNAME = Credentials.USERNAME;
    private String PASSWORD = Credentials.PASSWORD;
    private String URL      = Credentials.URL;



    //Class constructor
    public BooksDatabaseService(Socket aSocket){
        
		//TO BE COMPLETED
        this.serviceSocket = aSocket;
        System.out.println("This is the service socket address "+this.serviceSocket);
        //this.start();

    }


    //Retrieve the request from the socket
    public String[] retrieveRequest()
    {
        this.requestStr[0] = ""; //For author
        this.requestStr[1] = ""; //For library
		
		String tmp = "";
        try {

			//TO BE COMPLETED
            InputStream outcomeStream = serviceSocket.getInputStream();
            InputStreamReader outcomeStreamReader = new InputStreamReader(outcomeStream);
            StringBuffer stringBuffer = new StringBuffer();
            char x;
            while (true) //Read until terminator character is found
            {
                x = (char) outcomeStreamReader.read();
                if (x == '#')
                    break;
                stringBuffer.append(x);
            }
            String s = stringBuffer.toString();
            System.out.println("the received string is "+ s);
            String[] result = s.split(";");
            this.requestStr[0] = result[0];
            this.requestStr[1] = result[1];
            //this.serviceSocket.close();

         }catch(IOException e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
        return this.requestStr;
    }


    //Parse the request command and execute the query
    public boolean attendRequest()
    {
        boolean flagRequestAttended = true;
		
		this.outcome = null;
		
		String sql = ""; //TO BE COMPLETED- Update this line as needed.
		
		
		try {
			//Connet to the database
			//TO BE COMPLETED
			DriverManager.registerDriver(new org.postgresql.Driver());
            String dbURL = URL;
            String user = USERNAME;
            String password = PASSWORD;
            Connection con = DriverManager.getConnection(dbURL, user, password);


			//Make the query
			//TO BE COMPLETED
            Statement stmt = con.createStatement();
            sql = "SELECT\n" +
                    "\tbook.title,\n" +
                    "\tbook.publisher,\n" +
                    "\tbook.genre,\n" +
                    "\tbook.rrp,\n" +
                    "\tCOUNT(bookcopy.copyid)\n" +
                    "FROM\n" +
                    "\tbook inner join bookcopy on book.bookid = bookcopy.bookid inner join author on author.authorid = book.authorid inner join library on library.libraryid = bookcopy.libraryid \n" +
                    "WHERE\n" +
                    "\tauthor.familyname = " + "'" + requestStr[0] + "'" + "AND library.city = " + "'" + requestStr[1] + "'" +"\n" +
                    "GROUP BY\n" +
                    "\tbook.title,\n" +
                    "\tbook.publisher,\n" +
                    "\tbook.genre,\n" +
                    "\tbook.rrp;";
			
			//Process query
			//TO BE COMPLETED -  Watch out! You may need to reset the iterator of the row set.
            //sql
            ResultSet rs = stmt.executeQuery(sql);

            this.outcome = null;
            RowSetFactory aFactory = RowSetProvider.newFactory();
            CachedRowSet crs = aFactory.createCachedRowSet();
            crs.populate(rs);
            this.outcome = crs;
            //Resetting the iterator of the row set
            rs.beforeFirst();

			//Clean up
			//TO BE COMPLETED
            stmt.close();
            con.close();
			
		} catch (Exception e)
		{ System.out.println(e); }

        return flagRequestAttended;
    }



    //Wrap and return service outcome
    public void returnServiceOutcome(){
        try {
			//Return outcome
			//TO BE COMPLETED
			OutputStream BoutcomeStream = this.serviceSocket.getOutputStream();
            //System.out.println("OutputStreamStage passed");
            ObjectOutputStream outcomeStreamWriter = new ObjectOutputStream(BoutcomeStream);
            outcomeStreamWriter.writeObject(this.outcome);
            BoutcomeStream.flush();
            System.out.println("Service thread " + this.getId() + ": Service outcome returned; " + this.outcome);
            
			//Terminating connection of the service socket
			//TO BE COMPLETED
            this.serviceSocket.close();

        }catch (IOException e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
    }


    //The service thread run() method
    public void run()
    {
		try {
			System.out.println("\n============================================\n");
            //Retrieve the service request from the socket
            this.retrieveRequest();
            System.out.println("Service thread " + this.getId() + ": Request retrieved: "
						+ "author->" + this.requestStr[0] + "; library->" + this.requestStr[1]);

            //Attend the request
            boolean tmp = this.attendRequest();

            //Send back the outcome of the request
            if (!tmp)
                System.out.println("Service thread " + this.getId() + ": Unable to provide service.");
            this.returnServiceOutcome();

        }catch (Exception e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
        //Terminate service thread (by exiting run() method)
        System.out.println("Service thread " + this.getId() + ": Finished service.");
    }

}
