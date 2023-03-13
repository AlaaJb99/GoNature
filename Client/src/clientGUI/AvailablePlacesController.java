package clientGUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ParkClient;
import client.ClientUI;
import client.OrderController;
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

public class AvailablePlacesController implements Initializable {

	@FXML
	private HBox logo;

	@FXML
	private TableView<Order> AvaiPlacesTable;

	@FXML
	private TableColumn<Order, String> VisitDate;

	@FXML
	private TableColumn<Order, String> VisitTime;

	@FXML
	private TableColumn<Order, Void> choose;

	@FXML
	private Button exitBtn;

	private Stage stage;

	private Scene thiScene;

	private ObservableList<Order> list = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		VisitDate.setCellValueFactory(new PropertyValueFactory<Order, String>("visitDate"));
		VisitTime.setCellValueFactory(new PropertyValueFactory<Order, String>("visitTime"));
		addButtonToTable();
		list.addAll(ParkClient.available);
		AvaiPlacesTable.setItems(list);
	}

	@FXML
	void getExitBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/SignInVisitorPage.fxml").openStream());
		SignInVisitorPageController signInController = loader.getController();
		signInController.start(stage, root);
	}

	public void setParametres(Stage primaryStage, Pane root, Scene scene, String title) {
		stage = primaryStage;
		thiScene = new Scene(root);
	}

	public void start() {
		thiScene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		stage.setTitle("Available Places");
		stage.setScene(thiScene);
	}

	private void addButtonToTable() {
		Callback<TableColumn<Order, Void>, TableCell<Order, Void>> cellFactory = new Callback<TableColumn<Order, Void>, TableCell<Order, Void>>() {
			@Override
			public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
				final TableCell<Order, Void> cell = new TableCell<Order, Void>() {
					private final Button btn = new Button("Choose");
					{
						btn.setFont(Font.font("Modern No. 20", 20));
						btn.setOnAction((ActionEvent event) -> {
							Order order = getTableView().getItems().get(getIndex());
							ClientUI.parkCC
									.accept("makeorder " + order.toString() + " " + VisitorController.v1.getId());
							FXMLLoader loader = new FXMLLoader();
							Pane root;
							try {
								root = loader
										.load(getClass().getResource("/clientGUI/OrderConfirmation.fxml").openStream());
								OrderConfirmationController orderConfirmationController = loader.getController();
								orderConfirmationController.start(root, OrderController.price);
							} catch (IOException e) {
								e.printStackTrace();
							}
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
		choose.setCellFactory(cellFactory);
	}
}