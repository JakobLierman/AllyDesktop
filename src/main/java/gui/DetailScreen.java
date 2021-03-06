package gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.PopupMessage;

import javax.naming.NameAlreadyBoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * The type Detail screen.
 */
public class DetailScreen extends AnchorPane implements Initializable {

    private Stage stage;
    private final DomainController domainController;
    private final ProductManager productManager;
    private Object item;
    @FXML
    private Text txtTitle;
    @FXML
    private JFXTextField tfName;
    @FXML
    private JFXTextArea tfDescription;
    @FXML
    private Text txtTableTitle;
    @FXML
    private JFXListView<Object> lvSelectableItems;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private Text txtSelectMultiple;

    /**
     * Instantiates a new Detail screen.
     *
     * @param domainController the domain controller
     * @param item             the item (can be an empty new item)
     */
    public DetailScreen(final DomainController domainController, Object item) {
        this.domainController = domainController;
        productManager = domainController.getProductManager();
        this.item = item;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DetailScreen.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        // loader.setResources(ResourceBundle.getBundle("Bundle", new Locale("en")));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Sets SelectionMode and disables/enables description textarea according to type
        if (item instanceof Product) {
            lvSelectableItems.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tfDescription.setDisable(false);
        } else {
            lvSelectableItems.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            tfDescription.setDisable(true);
        }

        fillList();
        setText();
        fillFields(item);
    }

    // Fills list according to type
    private void fillList() {
        ObservableList<Object> itemList = FXCollections.observableArrayList();
        if (item instanceof Product)
            itemList.addAll(productManager.getIngredients());
        else
            itemList.addAll(productManager.getAllergens());
        lvSelectableItems.setItems(itemList);
    }

    // Sets all text items according to type
    private void setText() {
        btnSave.setText(domainController.getText("Save"));
        btnCancel.setText(domainController.getText("Cancel"));
        txtSelectMultiple.setText(domainController.getText("selectMultiple"));
        tfDescription.setPromptText(domainController.getText("Description"));
        if (item instanceof Product) {
            if (((Product) item).getName().isEmpty())
                txtTitle.setText(domainController.getText("addProduct"));
            else
                txtTitle.setText(domainController.getText("alterProduct"));
            tfName.setPromptText(domainController.getText("nameOf") + " " + domainController.getText("Product").toLowerCase());
            txtTableTitle.setText(domainController.getText("Ingredients"));
            btnAdd.setText(domainController.getText("addIngredient"));
        } else {
            if (((Ingredient) item).getName().isEmpty())
                txtTitle.setText(domainController.getText("addIngredient"));
            else
                txtTitle.setText(domainController.getText("alterIngredient"));
            tfName.setPromptText(domainController.getText("nameOf") + " " + domainController.getText("Ingredient").toLowerCase());
            txtTableTitle.setText(domainController.getText("Allergens"));
            txtSelectMultiple.setVisible(false);
            btnAdd.setVisible(false);
        }
    }

    // Fills fields if given item isn't new
    private void fillFields(Object item) {
        if (item instanceof Product) {
            if (!((Product) item).getName().isEmpty()) {
                tfName.setText(((Product) item).getName());
                if (((Product) item).getDescription() != null && !((Product) item).getDescription().isEmpty())
                    tfDescription.setText(((Product) item).getDescription());
                // Selects ingredients
                lvSelectableItems.getItems()
                        .stream()
                        .filter(i -> ((Product) item).getIngredients().contains(i))
                        .forEach(i -> lvSelectableItems.getSelectionModel().select(i));
            }
        } else {
            if (!((Ingredient) item).getName().isEmpty()) {
                tfName.setText(((Ingredient) item).getName());
                // Selects allergen
                lvSelectableItems.getItems()
                        .stream()
                        .filter(i -> ((Ingredient) item).getAllergen().equals(i))
                        .forEach(i -> lvSelectableItems.getSelectionModel().select(i));
            }
        }
    }

    /**
     * Cancels all changes and closes window.
     *
     * @param event the event
     */
    @FXML
    void handleCancel(ActionEvent event) {
        stage = (Stage) getScene().getWindow();
        stage.close();
    }

    /**
     * Saves item and closes window.
     *
     * @param event the event
     */
    @FXML
    void handleSave(ActionEvent event) {
        try {
            if (item instanceof Product) {
                Set list = Set.of(lvSelectableItems.getSelectionModel().getSelectedItems().toArray());
                if (((Product) item).getName().isEmpty())
                    productManager.createProduct(tfName.getText(), tfDescription.getText(), list);
                else
                    productManager.alterProduct(((Product) item).getName(), tfName.getText(), tfDescription.getText(), list);
            } else {
                if (((Ingredient) item).getName().isEmpty())
                    productManager.createIngredient(tfName.getText(), (Allergen) lvSelectableItems.getSelectionModel().getSelectedItem());
                else
                    productManager.alterIngredient(((Ingredient) item).getName(), tfName.getText(), (Allergen) lvSelectableItems.getSelectionModel().getSelectedItem());
            }

            // Close window
            stage = (Stage) getScene().getWindow();
            stage.close();
        } catch (NullPointerException | NameAlreadyBoundException e) {
            PopupMessage.showErrorMessage(
                    domainController.getText("Oops"),
                    domainController.getText("Wrong"),
                    e.getMessage());
        }
    }

    /**
     * Opens a new screen in which the user can add an ingredient.
     *
     * @param event the event
     */
    @FXML
    void handleAddIngredient(ActionEvent event) {
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setTitle(domainController.getText("addIngredient"));
        newStage.setScene(new Scene(new DetailScreen(domainController, new Ingredient())));
        newStage.showAndWait();
        // Refreshes the list
        fillList();
    }
}
