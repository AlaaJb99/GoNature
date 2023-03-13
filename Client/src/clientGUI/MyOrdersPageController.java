package clientGUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import client.ParkClient;
import client.ClientUI;
import client.VisitorController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Order;

public class MyOrdersPageController implements Initializable {

	@FXML
	private HBox logo;

	@FXML
	private TableView<Order> MyOrdersTable;

	@FXML
	private TableColumn<Order, String> OrderNumber;

	@FXML
	private TableColumn<Order, String> Park;

	@FXML
	private TableColumn<Order, String> VisitDate;

	@FXML
	private TableColumn<Order, String> VisitTime;

	@FXML
	private TableColumn<Order, Integer> NumberOfVisitors;

	@FXML
	private TableColumn<Order, String> Email;

	@FXML
	private TableColumn<Order, String> phoneNum;

	@FXML
	private TableColumn<Order, String> orderType;

	@FXML
	private TableColumn<Order, Void> cancel;

	@FXML
	private Button backBtn;

	private Stage stage;

	private ObservableList<Order> list = FXCollections.observableArrayList(ParkClient.orders);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		OrderNumber.setCellValueFactory(new PropertyValueFactory<Order, String>("orderNum"));
		Park.setCellValueFactory(new PropertyValueFactory<Order, String>("parkName"));
		VisitDate.setCellValueFactory(new PropertyValueFactory<Order, String>("visitDate"));
		VisitTime.setCellValueFactory(new PropertyValueFactory<Order, String>("visitTime"));
		NumberOfVisitors.setCellValueFactory(new PropertyValueFactory<Order, Integer>("visitorsNum"));
		Email.setCellValueFactory(new PropertyValueFactory<Order, String>("Email"));
		phoneNum.setCellValueFactory(new PropertyValueFactory<Order, String>("telNum"));
		orderType.setCellValueFactory(new PropertyValueFactory<Order, String>("orderType"));
		addButtonToTable();
		MyOrdersTable.setItems(list);
	}

	@FXML
	void getBackBtn(ActionEvent event) throws IOException {
		VisitorController.v1.setId("");
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/SignInVisitorPage.fxml").openStream());
		SignInVisitorPageController signInController = loader.getController();
		signInController.start(stage, root);
	}

	public void start(Stage primaryStage, Pane root) {
		stage = primaryStage;
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		stage.setTitle("Visitor Orders");
		stage.setScene(scene);
	}

	private void addButtonToTable() {
		Callback<TableColumn<Order, Void>, TableCell<Order, Void>> cellFactory = new Callback<TableColumn<Order, Void>, TableCell<Order, Void>>() {
			@Override
			public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
				final TableCell<Order, Void> cell = new TableCell<Order, Void>() {
					private final Button btn = new Button("Cancel");
					{
						btn.setFont(Font.font("Modern No. 20", 20));
						btn.setOnAction((ActionEvent event) -> {
							Order order = getTableView().getItems().get(getIndex());
							ClientUI.parkCC
									.accept("cancelOrder " + VisitorController.v1.getId() + " " + order.toString());
							ClientUI.parkCC.accept("sendWaitng " + order.toString());
							ClientUI.parkCC.accept("getVisitorOrders " + VisitorController.v1.getId());
							MyOrdersTable.getItems().clear();
							list.clear();
							list.addAll(ParkClient.orders);
							MyOrdersTable.setItems(list);

						});
					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(btn);
						}
					}

				};
				return cell;
			}
		};
		cancel.setCellFactory(cellFactory);
	}
}