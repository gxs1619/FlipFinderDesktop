
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FlipFinder extends Application {

    TextField buyQuantity;
    TextField sellQuantity;
    TextField profit;
    Scene scene;
    private TableView<Item> table;
    Label response;

    public static void main(String[] args) {
            launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Socket socket = new Socket("localhost",5151);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        primaryStage.setTitle("Old School Flip Finder");
        Button retrieve = new Button("Find Items");
        Text buyq = new Text("Buy Quantity: ");
        buyQuantity = new TextField();
        HBox buy = new HBox(buyq,buyQuantity);
        Text sellq = new Text("Sell Quantity: ");
        sellQuantity = new TextField();
        HBox sell = new HBox(sellq, sellQuantity);
        Text margin = new Text("Profit: ");
        profit = new TextField();
        HBox prof = new HBox(margin,profit);
        BorderPane root = new BorderPane();
        scene = new Scene(root, 300, 300);
        retrieve.setOnAction(event -> {
            SQLRetriever sql = new SQLRetriever();
            ObservableList<Item> list = FXCollections.observableArrayList(sql.retrieveWithFilters(Integer.parseInt(buyQuantity.getText()),
                    Integer.parseInt(sellQuantity.getText()),
                    Integer.parseInt(profit.getText())));
            table = new TableView();
            response = new Label();
            table.setRowFactory(tv -> {
                TableRow<Item> row = new TableRow<>();
                row.setOnMouseClicked(event1 -> {
                    if(! row.isEmpty() && event1.getButton() == MouseButton.PRIMARY
                            && event1.getClickCount() == 2){
                        Item item = row.getItem();
                        refreshItem(item, out);
                        primaryStage.setScene(getResponseScene(scene,primaryStage,sql, list));
                        try {
                            response = new Label(br.readLine());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return row;
            });
            createTable(list);
            primaryStage.setScene(getResponseScene(scene, primaryStage, sql, list));

        });
        VBox vbox = new VBox(buy,sell,prof,retrieve);

        root.setCenter(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void createTable(ObservableList<Item> list){
        TableColumn idCol = new TableColumn("ID");
        idCol.setMinWidth(100);
        idCol.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(250);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        TableColumn sellPriceCol = new TableColumn("Sell Price");
        sellPriceCol.setMinWidth(100);
        sellPriceCol.setCellValueFactory(
                new PropertyValueFactory<>("sellPrice"));
        TableColumn buyPriceCol = new TableColumn("Buy Price");
        buyPriceCol.setMinWidth(100);
        buyPriceCol.setCellValueFactory(
                new PropertyValueFactory<>("buyPrice"));
        TableColumn sellQuantCol = new TableColumn("Sell Quantity");
        sellQuantCol.setMinWidth(100);
        sellQuantCol.setCellValueFactory(
                new PropertyValueFactory<>("sellQuantity"));
        TableColumn buyQuantCol = new TableColumn("Buy Quantity");
        buyQuantCol.setMinWidth(100);
        buyQuantCol.setCellValueFactory(
                new PropertyValueFactory<>("buyQuantity"));
        TableColumn profitCol = new TableColumn("Profit");
        profitCol.setMinWidth(100);
        profitCol.setCellValueFactory(
                new PropertyValueFactory<>("margin"));
        TableColumn lastUpdatedCol = new TableColumn("Last Updated");
        lastUpdatedCol.setMinWidth(200);
        lastUpdatedCol.setCellValueFactory(
                new PropertyValueFactory<>("lastUpdated"));

        table.setItems(list);
        table.getColumns().addAll(idCol, nameCol, sellPriceCol, buyPriceCol, sellQuantCol, buyQuantCol, profitCol, lastUpdatedCol);

    }

    public Scene getResponseScene(Scene scene, Stage primaryStage, SQLRetriever sql, ObservableList<Item> list) {
        VBox responseFormat = new VBox();
        ObservableList<Item> list1 = FXCollections.observableArrayList(sql.retrieveWithFilters(Integer.parseInt(buyQuantity.getText()),
                Integer.parseInt(sellQuantity.getText()),
                Integer.parseInt(profit.getText())));
        table = new TableView();
        response = new Label();
        createTable(list1);
        responseFormat.getChildren().add(table);
        responseFormat.getChildren().add(response);
        Button back = new Button("Back");
        back.setOnAction(event -> {
            primaryStage.setScene(scene);
            primaryStage.show();
        });
        Button refresh = new Button("Refresh");
        refresh.setOnAction(event2 -> {
            getResponseScene(scene,primaryStage, sql, list1);
        });
        HBox hbox = new HBox(back,refresh);
        hbox.setSpacing(100);
        responseFormat.getChildren().add(hbox);
        return new Scene(responseFormat, 1200, 750);
    }

    public void refreshItem(Item item, PrintWriter out){
        System.out.println("requesting info on " + item.getId());
        out.println("update,"+item.getId());
    }
}
