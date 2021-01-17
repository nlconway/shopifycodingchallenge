import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Connector {
    private String repositoryName;
    private String filePath;
    Statement statement;
    Connection con;
    int sqlCode = 0;
    String sqlState = "00000";
    Boolean validConnection = false;

    public Connector(String repositoryName, String filePath, String url, String username, String password) {
        this.repositoryName = repositoryName;
        this.filePath = filePath;

        // Register the driver.  You must register the driver before you can use it.
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            con = DriverManager.getConnection(url, username, password);
             statement = con.createStatement ( ) ;
            if(con != null) validConnection = true;
        } catch (Exception cnfe) {
            System.out.print("FAILED CONNECTION");
            System.out.println("Class not found");
            validConnection = false;
        }

        validConnection = true;

    }

    public boolean create() {

        // Creating a table
        if (!validConnection) {
            System.out.print("Can't create, invalid connection");
            return false;}
        try {
            statement = con.createStatement();
            String createSQL = "CREATE TABLE " + repositoryName + "users (id SERIAL, userName VARCHAR (25), firstName VARCHAR (25) , lastName VARCHAR (25), password VARCHAR (25), PRIMARY KEY (id));";
//                    + "ALTER TABLE " + repositoryName + "users AUTO_INCREMENT=100;";
            System.out.println(createSQL);
            statement.executeUpdate(createSQL);
            System.out.println("Creating next table");
            createSQL = "CREATE TABLE " + repositoryName + " (id SERIAL, address VARCHAR (100), name VARCHAR (25), userId int NOT NULL, PRIMARY KEY (id), FOREIGN KEY (userId) REFERENCES " + repositoryName + "users(id));";
//                    + "ALTER TABLE " + repositoryName + " AUTO_INCREMENT=100;";
            System.out.println(createSQL);
            statement.executeUpdate(createSQL);
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Failure creating table");
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return false;
        }
        return true;
    }

    public boolean delete() {
        try {
            // Dropping a table
            String dropSQL = "DROP TABLE " + repositoryName;
            System.out.println(dropSQL);
            statement.executeUpdate(dropSQL);
            System.out.println("DONE");
             dropSQL = "DROP TABLE " + repositoryName + "users";
            System.out.println(dropSQL);
            statement.executeUpdate(dropSQL);
            System.out.println("DONE");
            statement.close();
            con.close();
            return true;
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE

            // Your code to handle errors comes here;
            // something more meaningful than a print would be good
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return false;
        }
    }

    //Add URL to database
    //Save image to database
    //Returns ID
    public int addImageFromUrl(String imageUrl, String name) {

        try {
//            URL imageUrl = new URL(url);
//            InputStream in = new BufferedInputStream(imageUrl.openStream());
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            byte[] buf = new byte[1024];
//            int n = 0;
//            while (-1 != (n = in.read(buf))) {
//                out.write(buf, 0, n);
//            }
//            out.close();
//            in.close();
//            byte[] response = out.toByteArray();
//
//            FileOutputStream fos = new FileOutputStream("C://" + name);
//            fos.write(response);
//            fos.close();
            URL url = new URL(imageUrl);
            String fileName = url.getFile();
//            String destName = "./figures" + fileName.substring(fileName.lastIndexOf("/"));
            String destName = filePath + name;

            System.out.println(destName);

            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destName);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        } catch (IOException e) {
            System.out.println("Failed in adding image to folder: " + e);
            return 0;
        }
        try {
            String insertSQL = "INSERT INTO " + repositoryName + " (address, name, userId) VALUES "
                    + "(  \'" + imageUrl + "\' , \'" + name + "\', " + Session.getUser().getId() + "  ) ";
            System.out.println(insertSQL);
            statement.executeUpdate(insertSQL);
            System.out.println("DONE");
            String querySQL = "SELECT * from " + repositoryName + " WHERE name = \'" + name + "\'";
            java.sql.ResultSet rs = statement.executeQuery(querySQL);
            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE

            // Your code to handle errors comes here;
            // something more meaningful than a print would be good
//            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);

            System.out.println("Failed in adding image to database");
            return 0;
        }
        return 0;
        //Add image to database
    }

    //Remove image from database
    //Delete from folder
    //Return true upon success
    public boolean deleteImage(int id) {
        try {
            String querySQL = "DELETE FROM " + repositoryName + " WHERE id = " + id + ";";
            System.out.println(querySQL);
            statement.executeUpdate(querySQL);
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return false;
        }
        return true;
    }

    public boolean deleteUser(int id) {
        try {
            String querySQL = "DELETE FROM " + repositoryName + "users WHERE id = " + id + ";";
            System.out.println(querySQL);
            statement.executeUpdate(querySQL);
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return false;
        }
        return true;
    }

    //Searches database using id, returns object of url
    public Image getImage(String name, int userId) {

        try {
            String querySQL = "SELECT * from " + repositoryName + "users WHERE id = " + userId + ";";
            System.out.println(querySQL);
            java.sql.ResultSet rs = statement.executeQuery(querySQL);
            User user = null;
            if (rs.next()) {
                user = new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("userName"));
            }
            if (user == null) return null;

            querySQL = "SELECT * from " + repositoryName + " WHERE name = \'" + name + "\' AND userId = " + userId + ";";
            System.out.println(querySQL);
            rs = statement.executeQuery(querySQL);
            if (rs.next()) {
                return new Image(rs.getInt("id"), rs.getString("address"), rs.getString("name"), user);
            }
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Failure in loading image");
//            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return null;
        }
        return null;
    }

    //Gets all images for user id
    public List<Image> getImages(int userId) {
        List<Image> imageList = new ArrayList<>();
        try {
//            String querySQL = "SELECT * from " + repositoryName + "user WHERE id = " + userId + ";";
//            java.sql.ResultSet rs = statement.executeQuery(querySQL);
//            User user = null;
//            if (rs.next()) {
//                user = new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("userName"));
//
//            }
            User user = getUser(userId);
            if (user == null) return imageList;

            String querySQL = "SELECT * from " + repositoryName + " WHERE userId = " + userId + ";";
            java.sql.ResultSet rs = statement.executeQuery(querySQL);

            while (rs.next()) {
                imageList.add(new Image(rs.getInt("id"), rs.getString("address"), rs.getString("name"), user));
            }
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return null;
        }
        return imageList;
    }

    public User getUser(int id) {
        try {
            String querySQL = "SELECT * from " + repositoryName + "users WHERE id = " + id + ";";
            System.out.println(querySQL);
            java.sql.ResultSet rs = statement.executeQuery(querySQL);
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("userName"));
            }
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            System.out.println("No User Found");
            return null;
        }
        return null;
    }

    public User loginUser(String userName, String password) {
        try {
            String querySQL = "SELECT * from " + repositoryName + "users WHERE userName = \'" + userName + "\' AND password = \'" + password + "\'";
            System.out.println(querySQL);
            java.sql.ResultSet rs = statement.executeQuery(querySQL);
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("userName"));
            }
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return null;
        }
        return null;
    }

    public boolean addUser(String firstName, String lastName, String userName, String password) {
        try {
            System.out.println("Valid Connection: " + validConnection);
            String insertSQL = "INSERT INTO " + repositoryName + "users (userName, firstName, lastName, password) VALUES ( \'" +
                    userName + "\' , \'" + firstName + "\', \'" + lastName + "\', \'" + password + "\' ) ";
            statement.executeUpdate(insertSQL);
            return true;

        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            // Your code to handle errors comes here;
            // something more meaningful than a print would be good
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return false;
        }
    }
}
