package clientGUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import client.ClientUI;
import client.ParkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Discount;

public class ConfirmDiscounts implements Initializable {

	@FXML
	private HBox logo;

	@FXML
	private Button BackBtn;

	@FXML
	private TableView<Discount> table;

	@FXML
	private TableColumn<Discount, String> park_column;

	@FXML
	private TableColumn<Discount, Double> discount_column;

	@FXML
	private TableColumn<Discount, String> start_column;

	@FXML
	private TableColumn<Discount, String> end_column;

	@FXML
	private TableColumn<Discount, Void> accept_column;

	@FXML
	private TableColumn<Discount, Void> decline_column;

	@FXML
	private TextArea text;

	private Stage stage;

	@FXML
	void getBackBtn(ActionEvent event) throws IOException {
		table.getItems().clear();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/clientGUI/DepartmentManagerHome.fxml").openStream());
		DepartmentManagerHome dmh = loader.getController();
		dmh.start(stage, root);
	}

	public void start(Stage primaryStage, Pane root) {
		stage = primaryStage;
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/clientGUI/AllPages.css").toExternalForm());
		stage.setTitle("Confirm Discounts");
		stage.setScene(scene);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		ClientUI.parkCC.accept("getdiscountRequests");

		addAcceptDeclineButtons();

		park_column.setCellValueFactory(new PropertyValueFactory<>("parkNum"));
		discount_column.setCellValueFactory(new PropertyValueFactory<>("discount"));
		start_column.setCellValueFactory(new PropertyValueFactory<>("startDate"));
		end_column.setCellValueFactory(new PropertyValueFactory<>("endDate"));

		table.setItems(ParkClient.discountRequestsList);
	}

	private void addAcceptDeclineButtons() {

		// ADD ACCEPT BUTTONS
		Callback<TableColumn<Discount, Void>, TableCell<Discount, Void>> cellFactory = new Callback<TableColumn<Discount, Void>, TableCell<Discount, Void>>() {

			@Override
			public TableCell<Discount, Void> call(final TableColumn<Discount, Void> param) {

				final TableCell<Discount, Void> cell = new TableCell<Discount, Void>() {

					private final Button btn = new Button("Accept");

					{
						btn.setOnAction((ActionEvent event) -> {
							Discount discount = getTableView().getItems().get(getIndex());

							ClientUI.parkCC.accept("acceptDiscount " + discount);
							ClientUI.parkCC.accept("deleteDiscountRequest " + discount);
							table.getItems().remove(discount);

							try {
								FXMLLoader popup = new FXMLLoader();
								ParkClient.popUpTitle = "Discount Accepted";
								ParkClient.popUpTxt = "The discount is accepted and will be added";
								Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
								PopUp pop = popup.getController();
								pop.start(root);
								// pp.start();
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

		accept_column.setCellFactory(cellFactory);

		// ADD DECLINE BUTTONS
		Callback<TableColumn<Discount, Void>, TableCell<Discount, Void>> cellFactory2 = new Callback<TableColumn<Discount, Void>, TableCell<Discount, Void>>() {

			@Override
			public TableCell<Discount, Void> call(final TableColumn<Discount, Void> param) {

				final TableCell<Discount, Void> cell2 = new TableCell<Discount, Void>() {

					private final Button btnd = new Button("Decline");

					{
						btnd.setOnAction((ActionEvent event) -> {
							Discount discount = getTableView().getItems().get(getIndex());

							ClientUI.parkCC.accept("deleteDiscountRequest " + discount);
							table.getItems().remove(discount);
							try {
								FXMLLoader popup = new FXMLLoader();
								ParkClient.popUpTitle = "Discount Declined";
								ParkClient.popUpTxt = "The discount is declined and will be deleted from the requested list";
								Pane root = popup.load(getClass().getResource("/clientGUI/PopUp.fxml").openStream());
								PopUp pop = popup.getController();
								pop.start(root);
								// pp.start();
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
							setGraphic(btnd);
						}
					}
				};
				return cell2;
			}
		};

		decline_column.setCellFactory(cellFactory2);
	}
}