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
    private Statement statement;
    private Connection con;
    private int sqlCode = 0;
    private String sqlState = "00000";
    Boolean validConnection = false;

    //Registers driver
    //Logins using postgres credentials
    Connector(String repositoryName, String filePath, String url, String username, String password) {
        this.repositoryName = repositoryName;
        this.filePath = filePath;

        // Register the driver.  You must register the driver before you can use it.
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            con = DriverManager.getConnection(url, username, password);
            statement = con.createStatement();
            if (con != null) Session.setValidConnection(true);
        } catch (Exception e) {
            System.out.println("Invalid connection credentials");
            Session.setValidConnection(false);
        }
        Session.setValidConnection(true);


    }

    //Creates image table in database
    //Creates user table in database
    //If folder given by path does not exist, creates folder
    //If fails returns false
    boolean create() {

        // Creating a table
        if (!Session.isValidConnection()) {
            System.out.print("Can't create, invalid connection");
            return false;
        }
        try {
            statement = con.createStatement();
            String createSQL = "CREATE TABLE " + repositoryName + "users (id SERIAL, userName VARCHAR (25) UNIQUE, firstName VARCHAR (25) , lastName VARCHAR (25), password VARCHAR (25), PRIMARY KEY (id));";
            statement.executeUpdate(createSQL);
            createSQL = "CREATE TABLE " + repositoryName + " (id SERIAL, address VARCHAR (200), name VARCHAR (25), userId int NOT NULL, PRIMARY KEY (id), UNIQUE(name, userId), FOREIGN KEY (userId) REFERENCES " + repositoryName + "users(id));";
            statement.executeUpdate(createSQL);
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Failure creating table");
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return false;
        }
        File theDir = new File(filePath);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        return true;
    }


    //Deletes tables from database
    //Deletes folder that contains images locally
    //Closes the connections
    boolean delete() {
        try {
            // Dropping a table
            String dropSQL = "DROP TABLE " + repositoryName;
            statement.executeUpdate(dropSQL);
            dropSQL = "DROP TABLE " + repositoryName + "users";
            statement.executeUpdate(dropSQL);
            statement.close();
            con.close();
            File file = new File(filePath);
            return file.delete();
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Error deleting tables");
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return false;
        }
    }

    //Add URL to database
    //Save image to database
    //Returns ID of image or 0 if failed
    int addImageFromUrl(String imageUrl, String name) {
        int imageId = 0;

        try {
            String insertSQL = "INSERT INTO " + repositoryName + " (address, name, userId) VALUES "
                    + "(  \'" + imageUrl + "\' , \'" + name + "\', " + Session.getActiveUser().getId() + "  ) ";
            statement.executeUpdate(insertSQL);
            String querySQL = "SELECT * FROM " + repositoryName + " WHERE name = \'" + name + "\' AND userId = " + Session.getActiveUser().getId() + ";";
            java.sql.ResultSet rs = statement.executeQuery(querySQL);
            if (rs.next()) imageId = rs.getInt("id");
            else return 0;


            URL url = new URL(imageUrl);
            String destName = filePath + imageId;

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
            //Failed to download image, Delete info from database
            String querySQL = "DELETE FROM " + repositoryName + " WHERE id = " + imageId + ";";
            try {
                statement.executeUpdate(querySQL);
            } catch (SQLException ex) {
                return 0;
            }
            return 0;
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Error adding image to database");
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return 0;
        }
        return imageId;
    }

    //Remove image from database
    //Delete from folder
    //Return true upon success, false upon fail
    public boolean deleteImage(int id) {
        boolean success = true;
        try {
            String querySQL = "DELETE FROM " + repositoryName + " WHERE id = " + id + ";";
            statement.executeUpdate(querySQL);
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Error deleting image from table");
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            success = false;
        }
        try {
            File file = new File(filePath + id);
            file.delete();
        } catch (Exception e) {
            System.out.println("Error deleting file from folder");
            success = false;
        }
        return success;
    }

    //Delete user that matches userid
    public boolean deleteUser(int id) {
        try {
            String querySQL = "DELETE FROM " + repositoryName + "users WHERE id = " + id + ";";
            statement.executeUpdate(querySQL);
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Error deleting user");
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return false;
        }
        return true;
    }

    //Searches database using id, returns object of url
    Image getImage(String name, int userId) {

        try {
            String querySQL = "SELECT * from " + repositoryName + "users WHERE id = " + userId + ";";
            java.sql.ResultSet rs = statement.executeQuery(querySQL);
            User user = null;
            if (rs.next()) {
                user = new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("userName"));
            }
            if (user == null) return null;

            querySQL = "SELECT * from " + repositoryName + " WHERE name = \'" + name + "\' AND userId = " + userId + ";";
            rs = statement.executeQuery(querySQL);
            if (rs.next()) {
                return new Image(rs.getInt("id"), rs.getString("address"), rs.getString("name"), user);
            }
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Error getting image info from table");
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return null;
        }
        return null;
    }

    //Gets all images assigned to user
    List<Image> getImages(int userId) {
        List<Image> imageList = new ArrayList<>();
        try {
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
            System.out.println("Error getting image info from tables");
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return null;
        }
        return imageList;
    }

    //Returns user that matches id
    User getUser(int id) {
        try {
            String querySQL = "SELECT * from " + repositoryName + "users WHERE id = " + id + ";";
            java.sql.ResultSet rs = statement.executeQuery(querySQL);
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("userName"));
            }
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Error getting user info from table");
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return null;
        }
        return null;
    }

    //Returns User that matches username and password
    User loginUser(String userName, String password) {
        try {
            String querySQL = "SELECT * from " + repositoryName + "users WHERE userName = \'" + userName + "\' AND password = \'" + password + "\'";
            java.sql.ResultSet rs = statement.executeQuery(querySQL);
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("userName"));
            }
        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Error getting user info from table");
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return null;
        }
        return null;
    }


    //Adds user to database using firstname, lastname, username and password
    boolean addUser(String firstName, String lastName, String userName, String password) {
        try {
            String insertSQL = "INSERT INTO " + repositoryName + "users (userName, firstName, lastName, password) VALUES ( \'" +
                    userName + "\' , \'" + firstName + "\', \'" + lastName + "\', \'" + password + "\' ) ";
            statement.executeUpdate(insertSQL);
            return true;

        } catch (SQLException e) {
            sqlCode = e.getErrorCode(); // Get SQLCODE
            sqlState = e.getSQLState(); // Get SQLSTATE
            System.out.println("Error adding user info to database");
            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            return false;
        }
    }
}
