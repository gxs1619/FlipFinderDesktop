import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLRetriever {
    private Connection connect() {
        String url = "jdbc:sqlite:/Users/griffin/Projects/OSRSFlipFinderDB.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private Item createItem(ResultSet rs) {
        Item i = new Item();
        try {
            i.setId(rs.getInt("id"));
            i.setName(rs.getString("name"));
            i.setSellPrice(rs.getInt("SellPrice"));
            i.setSellQuantity(rs.getInt("SellQuantity"));
            i.setBuyPrice(rs.getInt("BuyPrice"));
            i.setBuyQuantity(rs.getInt("BuyQuantity"));
            i.setMargin(rs.getInt("Profit"));
            i.setLastUpdated(rs.getString("LastUpdated"));
        } catch (SQLException ex) {
        }
        return i;
    }

    public List<Item> retrieveWithFilters(int buyQuant, int sellQuant, int profit){
        String sql = "SELECT Id, name, SellPrice, SellQuantity, BuyPrice, BuyQuantity, Profit, LastUpdated "
                + "FROM Items WHERE SellQuantity > ? " + "AND BuyQuantity > ? "
                + "AND Profit > ?";
        List<Item> arraylist = new ArrayList<Item>();

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setInt(1, sellQuant);
            pstmt.setInt(2, buyQuant);
            pstmt.setInt(3, profit);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                Item i = createItem(rs);
                arraylist.add(i);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return arraylist;
    }
    public Item retrieveItem(int id){
        String sql = "SELECT Id, name, SellPrice, SellQuantity, BuyPrice, BuyQuantity, Profit, LastUpdated "
                + "FROM Items WHERE Id = ?";
        Item i = new Item();
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                i = createItem(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

}
