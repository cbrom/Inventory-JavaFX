package org.example.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.converter.NumberStringConverter;
import org.example.App;
import org.example.models.Customer;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private static final int COMPACT = 20;
    private static final int EXTENDED = 30;
    private static SimpleIntegerProperty status;
    public static final int CREATING=0, EDITING=1, SEARCHING=2, DELETING=3, NAVIGATING=4, BROWSING=5, NOTHING=6;
    public static final int COMPACTLIST = 11;
    private static Customer customer;
    private static int NavigationPage = 0;

    /*
    customers information tableview and its columns
     */
    @FXML
    TableView contentTableView;
    @FXML
    TableColumn statusColumn;
    @FXML
    TableColumn nameColumn;
    @FXML
    TableColumn customerClassColumn;
    @FXML
    TableColumn ageColumn;
    @FXML
    TableColumn phoneNumberColumn;
    @FXML
    TableColumn locationColumn;
    @FXML
    TableColumn stageColumn;
    @FXML
    TableColumn resultColumn;
    @FXML
    TableColumn levelPriceColumn;
    @FXML
    TableColumn discountColumn;
    @FXML
    TableColumn costColumn;
    @FXML
    TableColumn commentColumn;

    /*
    customer info detail text fields
     */

    @FXML
    TextField nameTextField;
    @FXML
    TextField ageTextField;
    @FXML
    TextField phoneNumberTextField;
    @FXML
    TextField locationTextField;
    @FXML
    TextField statusTextField;
    @FXML
    TextField classTextField;
    @FXML
    TextField stageTextField;
    @FXML
    TextField resultTextField;
    @FXML
    TextField levelPriceTextField;
    @FXML
    TextField discountTextField;
    @FXML
    TextField costTextField;
    @FXML
    TextField commentTextField;
    @FXML
    TextField detailValue;
    @FXML
    ImageView customerImage;

    /*
    buttons
     */
    @FXML
    Button editButton;
    @FXML
    Button deleteButton;
    @FXML
    Button createButton;
    @FXML
    Button previousButton;
    @FXML
    Button nextButton;

    /*
    pagination
     */
    @FXML
    Label paginationLabel;

    /*
    Header fields
     */
    @FXML
    TextField searchTextField;
    @FXML
    ImageView userImageView;

    /*
    Radio Buttons
     */
    @FXML
    RadioButton extendedRadioButton;
    @FXML
    RadioButton compactRadioButton;

    ToggleGroup viewToggleGroup = new ToggleGroup();

    ObservableList<Customer> data;

    public void setToggleRadioButtons() {
        compactRadioButton.setSelected(true);
        extendedRadioButton.setToggleGroup(viewToggleGroup);
        compactRadioButton.setToggleGroup(viewToggleGroup);
    }

    public int getView() {
        RadioButton rd  = (RadioButton)viewToggleGroup.getSelectedToggle();
        String selected = rd.getText();
        if (selected.equals("Compact")) {
            return COMPACT;
        } else if (selected.equals("Extended" )) {
            return EXTENDED;
        }
        return -1;
    }


    public void setDetailsEditable(boolean value) {
        nameTextField.setEditable(value);
        ageTextField.setEditable(value);
        phoneNumberTextField.setEditable(value);
        locationTextField.setEditable(value);
        statusTextField.setEditable(value);
        classTextField.setEditable(value);
        stageTextField.setEditable(value);
        resultTextField.setEditable(value);
        levelPriceTextField.setEditable(value);
        discountTextField.setEditable(value);
        costTextField.setEditable(value);
        commentTextField.setEditable(value);
    }

    public void setDetailsToValue() {

        setDetailsValue(nameTextField);
        setDetailsValue(ageTextField);
        setDetailsValue(phoneNumberTextField);
        setDetailsValue(locationTextField);
        setDetailsValue(statusTextField);
        setDetailsValue(classTextField);
        setDetailsValue(stageTextField);
        setDetailsValue(resultTextField);
        setDetailsValue(levelPriceTextField);
        setDetailsValue(discountTextField);
        setDetailsValue(costTextField);
        setDetailsValue(commentTextField);
    }

    public void setDetailsValue(TextField field) {
        field.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            detailValue.setText(field.getText());
        });
    }

    @FXML
    public void extendedViewClicked(ActionEvent event) {
        previousButton.setDisable(true);
        nextButton.setDisable(true);
        NavigationPage = 0;

        try {
            setPagination();
            data = FXCollections.observableArrayList();
            if (getStatus() == SEARCHING) {
                data.addAll(customer.search(searchTextField.getText(), NavigationPage * COMPACTLIST, -1));
            } else {
                data.addAll(customer.getAll(NavigationPage * COMPACTLIST, -1));
            }

            contentTableView.setItems(data);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public void compactViewClicked(ActionEvent event) {
        previousButton.setDisable(false);
        nextButton.setDisable(false);
        NavigationPage = 0;

        try {
            navigate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public void navNext(ActionEvent actionEvent) {
        try {
            int total = getTotal();
            if (NavigationPage < total/COMPACTLIST) {
                NavigationPage += 1;

                navigate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @FXML
    public void navPrevious(ActionEvent actionEvent) {
        try {
            if (NavigationPage > 0) {
                NavigationPage -= 1;

                navigate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void navigate() throws SQLException {
        setPagination();
        data = FXCollections.observableArrayList();
        if (getStatus() == SEARCHING) {
            data.addAll(customer.search(searchTextField.getText(), NavigationPage * COMPACTLIST, COMPACTLIST));
        } else {
            data.addAll(customer.getAll(NavigationPage * COMPACTLIST, COMPACTLIST));
        }

        contentTableView.setItems(data);
    }

    @FXML
    private void createOnAction(ActionEvent event) {
        contentTableView.getSelectionModel().clearSelection();
        Controller.customer.empty();
        customer.setId(10);
        Controller.setStatus(CREATING);
        setDetailsEditable(true);
    }

    @FXML
    private void deleteOnAction(ActionEvent event) {
        if (Controller.getStatus() == CREATING) {
            Controller.customer.empty();
            Controller.setStatus(NOTHING);
            setDetailsEditable(false);
        } else if (Controller.getStatus() == EDITING) {
            Controller.setStatus(BROWSING);
            setDetailsEditable(false);
        } else if (Controller.getStatus() == BROWSING || Controller.getStatus() == SEARCHING) {
            Customer selectedCustomer = (Customer)contentTableView.getSelectionModel().getSelectedItem();
            try {
                selectedCustomer.delete();
                File file = new File(selectedCustomer.getImageUrl());
                file.delete();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            contentTableView.getItems().remove(selectedCustomer);
            Controller.customer.empty();
            System.out.println("Delete here and update");
        }
        contentTableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void editOnAction(ActionEvent event) {
        if (Controller.getStatus() == CREATING) {
            setDetailsEditable(false);
            Controller.setStatus(NOTHING);
            Customer createdCustomer = new Customer();
            createdCustomer.copyFromCustomer(Controller.customer);
            try {
                createdCustomer.save();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            contentTableView.getItems().add(0, createdCustomer);
            Controller.customer.setId(-1);
            contentTableView.getSelectionModel().clearSelection();
        } else if (Controller.getStatus() == EDITING) {
            System.out.println("Save here");
            Customer selectedCustomer = (Customer)contentTableView.getSelectionModel().getSelectedItem();
            String prevUrl = selectedCustomer.getImageUrl();
            selectedCustomer.copyFromCustomer(Controller.customer);
            selectedCustomer.update();
            if (!selectedCustomer.getImageUrl().equals(prevUrl)) {
                File file = new File(prevUrl);
                file.delete();
            }
            Controller.setStatus(BROWSING);
            setDetailsEditable(false);
            contentTableView.getSelectionModel().clearSelection();
        }
        else if (Controller.getStatus() == BROWSING || Controller.getStatus() == SEARCHING) {
            Controller.setStatus(EDITING);
            setDetailsEditable(true);
        }
    }

    @FXML
    public void searchOnAction(ActionEvent action) {
        String searchString = searchTextField.getText();
        try {
            NavigationPage = 0;
            setStatus(SEARCHING);
            if (getView() == COMPACT) {
                ArrayList<Customer> searchResults = customer.search(searchString, NavigationPage, COMPACTLIST);
                data = FXCollections.observableArrayList();
                data.addAll(searchResults);
                contentTableView.setItems(data);
                NavigationPage = 0;
            } else if (getView() == EXTENDED) {
                ArrayList<Customer> searchResults = customer.search(searchString, NavigationPage, -1);
                data = FXCollections.observableArrayList();
                data.addAll(searchResults);
                contentTableView.setItems(data);
                NavigationPage = 0;
            }

            setPagination();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void calendarClicked(MouseEvent event) {
        try {
            Runtime.getRuntime().exec("open " + App.class.getResource("sample.ics").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void imageMouseClick(MouseEvent event) {
        if ((Controller.getStatus() == CREATING ) || (Controller.getStatus() == EDITING)) {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(customerImage.getScene().getWindow());

            FileChooser.ExtensionFilter imageFilter
                    = new FileChooser.ExtensionFilter("Image File Formats", "*.jpg", "*.png", "*.jpeg");
            fileChooser.getExtensionFilters().add(imageFilter);
            if (file != null) {

                Image image1 = null;
                try {
                    image1 = new Image(file.toURI().toURL().toExternalForm());
                    customerImage.setImage(image1);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                File mainDir = new File(System.getProperty("user.home")+System.getProperty("file.separator") + "/user-management/img/");
                if (!mainDir.exists()) {
                    mainDir.mkdirs();
                }
                File out = new File(System.getProperty("user.home")+System.getProperty("file.separator") + "/user-management/img/" + System.currentTimeMillis() + file.getName());
                try {
                    Files.copy(file.toPath(), out.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    customer.setImageUrl(out.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void buttonsBinder() {
        StringBinding editBinder = new StringBinding() {
            {
                super.bind(status);
            }
            @Override
            protected String computeValue() {
                switch (status.get()) {
                    case CREATING:
                        System.out.println("Creating");
                        return "Save";
                    case EDITING:
                        System.out.println("Editing");
                        return "Update";
                    case BROWSING:
                        System.out.println("Browsing");
                        return "Edit";
                    case SEARCHING:
                        System.out.println("Searching");
                        return "Edit";
                    case NOTHING:
                        System.out.println("Nothing");
                        return "";
                    default:
                        return "";
                }
            }
        };

        BooleanBinding editButtonDisableBinder = new BooleanBinding() {
            {
                super.bind(status);
                super.bind(nameTextField.textProperty());
                super.bind(ageTextField.textProperty());
                super.bind(phoneNumberTextField.textProperty());
                super.bind(locationTextField.textProperty());
                super.bind(statusTextField.textProperty());
                super.bind(classTextField.textProperty());
                super.bind(stageTextField.textProperty());
                super.bind(resultTextField.textProperty());
                super.bind(levelPriceTextField.textProperty());
                super.bind(discountTextField.textProperty());
                super.bind(costTextField.textProperty());
                super.bind(commentTextField.textProperty());
            }
            @Override
            protected boolean computeValue() {
                if (getStatus() == BROWSING || getStatus() == SEARCHING ) {
                    return false;
                }
                if ( (getStatus() == EDITING ||
                        getStatus() == CREATING) &&
                        isDetailsEmpty())
                {
                    return true;
                }

                return false;
            }
        };

        editButton.textProperty().bind(editBinder);

        editButton.disableProperty().bind(editButtonDisableBinder);
        editButton.visibleProperty().bind(Bindings.notEqual(-1, Controller.customer.idProperty()).and(Bindings.notEqual(NOTHING, Controller.status)));

        StringBinding deleteBinder = new StringBinding() {
            {
                super.bind(status);
            }
            @Override
            protected String computeValue() {
                switch (status.get()) {
                    case CREATING:
                        System.out.println("Creating");
                        return "Cancel";
                    case EDITING:
                        System.out.println("Editing");
                        return "Cancel";
                    case BROWSING:
                        System.out.println("Browsing");
                        return "Delete";
                    case SEARCHING:
                        System.out.println("Searching");
                        return "Delete";
                    case NOTHING:
                        System.out.println("Nothing");
                        return "";
                    default:
                        return "";
                }
            }
        };

        deleteButton.textProperty().bind(deleteBinder);
        deleteButton.visibleProperty().bind(Bindings.notEqual(-1, Controller.customer.idProperty()).and(Bindings.notEqual(NOTHING, Controller.status)));
    }

    public boolean isDetailsEmpty() {
        return (nameTextField.getText().equals("") ||
                ageTextField.getText().equals("") ||
                phoneNumberTextField.getText().equals("") ||
                locationTextField.getText().equals("") ||
                statusTextField.getText().equals("") ||
                classTextField.getText().equals("") ||
                stageTextField.getText().equals("") ||
                resultTextField.getText().equals("") ||
                levelPriceTextField.getText().equals("") ||
                discountTextField.getText().equals("") ||
                costTextField.getText().equals("") ||
                commentTextField.getText().equals(""));
    }

    public void numberConstraint(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public void detailsNumberConstraints () {
        numberConstraint(ageTextField);
        numberConstraint(levelPriceTextField);
        numberConstraint(discountTextField);
        numberConstraint(costTextField);
    }

    public void bindTableColumns() {
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<Customer,String>("status")
        );
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<Customer,String>("name")
        );
        customerClassColumn.setCellValueFactory(
                new PropertyValueFactory<Customer,String>("customerClass")
        );

        ageColumn.setCellValueFactory(
                new PropertyValueFactory<Customer,Integer>("age")
        );

        phoneNumberColumn.setCellValueFactory(
                new PropertyValueFactory<Customer,String>("phoneNumber")
        );
        locationColumn.setCellValueFactory(
                new PropertyValueFactory<Customer,String>("location")
        );
        stageColumn.setCellValueFactory(
                new PropertyValueFactory<Customer,String>("stage")
        );
        resultColumn.setCellValueFactory(
                new PropertyValueFactory<Customer,String>("result")
        );
        levelPriceColumn.setCellValueFactory(
                new PropertyValueFactory<Customer,Integer>("levelPrice")
        );
        discountColumn.setCellValueFactory(
                new PropertyValueFactory<Customer,Integer>("discount")
        );
        costColumn.setCellValueFactory(
                new PropertyValueFactory<Customer,Integer>("cost")
        );
        commentColumn.setCellValueFactory(
                new PropertyValueFactory<Customer,String>("comment")
        );
    }

    public int getTotal() throws SQLException {
        if (getStatus() == SEARCHING) {
            return customer.countSearch(searchTextField.getText());
        } else {
            return customer.countRecords();
        }
    }

    public void setPagination() throws SQLException {
        if (getView() == COMPACT) {
            int total = getTotal();
            paginationLabel.setText((NavigationPage + 1) + " of /" + ((total/COMPACTLIST) + 1));
        } else if (getView() == EXTENDED) {
            paginationLabel.setText("1 of / 1");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        status = new SimpleIntegerProperty(NOTHING);
        customer = new Customer();
        detailsNumberConstraints();
        bindTableColumns();
        buttonsBinder();
        setDetailsToValue();
        setToggleRadioButtons();

        data = FXCollections.observableArrayList();

        try {
            setPagination();
            data.addAll(customer.getAll(NavigationPage * COMPACTLIST, COMPACTLIST));
            contentTableView.setItems(data);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        customer.nameProperty().bindBidirectional(nameTextField.textProperty());
        ageTextField.textProperty().bindBidirectional(customer.ageProperty(), new NumberStringConverter());
        customer.phoneNumberProperty().bindBidirectional(phoneNumberTextField.textProperty());
        customer.locationProperty().bindBidirectional(locationTextField.textProperty());
        customer.statusProperty().bindBidirectional(statusTextField.textProperty());
        customer.customerClassProperty().bindBidirectional(classTextField.textProperty());
        customer.stageProperty().bindBidirectional(stageTextField.textProperty());
        customer.resultProperty().bindBidirectional(resultTextField.textProperty());
        levelPriceTextField.textProperty().bindBidirectional(customer.levelPriceProperty(), new NumberStringConverter());
        discountTextField.textProperty().bindBidirectional(customer.discountProperty(), new NumberStringConverter());
        costTextField.textProperty().bindBidirectional(customer.costProperty(), new NumberStringConverter());
        customer.commentProperty().bindBidirectional(commentTextField.textProperty());

        contentTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            int changed = Controller.getStatus() == SEARCHING ? SEARCHING : BROWSING;
            if (newSelection != null) {
                Controller.setStatus(changed);
                Customer c = (Customer) contentTableView.getSelectionModel().getSelectedItem();
                setCurrentCustomer(c);
                System.out.println(Controller.customer);

            } else {
                Controller.setStatus(changed);
                emptyCurrentCustomer();
            }
        });

    }

    public void setDummyData() {
        Customer cr = new Customer("RT P004", "Abebe Kebede", "A", 27,
                "+251932848537", "Addis Ababa", "m+2", "Middle Part",
                34_000, 32_000, 30_000, "What is next?", "img");
        cr.setId(20);

        data = FXCollections.observableArrayList(
                cr

        );

        contentTableView.setItems(data);
    }

    public void searchDummyData() {
        Customer cr = new Customer("RT P004", "Abebe Kebede", "A", 27,
                "+251932848537", "Addis Ababa", "m+2", "Middle Part",
                34_000, 32_000, 30_000, "What is next?", "img");
        cr.setId(20);

        data = FXCollections.observableArrayList(
                cr,
                new Customer("RT P004", "Abebe Kebede", "A", 27,
                        "+251932848537", "Addis Ababa", "m+2", "Middle Part",
                        34_000, 32_000, 30_000, "What is next?", "img"),
                new Customer("RT P044", "Kebede Abebe", "B", 30,
                        "+251932848537", "Kombolcha", "m+3", "Front Part",
                        34_000, 32_000, 30_000,
                        "He wants to do it fast, he is coming!", "img"),
                new Customer("RT P004", "Abebe Kebede", "A", 27,
                        "+251932848537", "Addis Ababa", "m+2", "Middle Part",
                        34_000, 32_000, 30_000, "What is next?", "img"),
                new Customer("RT P044", "Kebede Abebe", "B", 30,
                        "+251932848537", "Kombolcha", "m+3", "Front Part",
                        34_000, 32_000, 30_000,
                        "He wants to do it fast, he is coming!", "img")

        );

        contentTableView.setItems(data);
    }

    public void setCurrentCustomer(Customer selectedCustomer) {
        File file = new File(selectedCustomer.getImageUrl());
        Image image1 = null;
        try {
            image1 = new Image(file.toURI().toURL().toExternalForm());
            customerImage.setImage(image1);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        customer.copyFromCustomer(selectedCustomer);
    }

    public void emptyCurrentCustomer() {
        customer.empty();
    }

    public static int getStatus() {
        return status.get();
    }

    public static SimpleIntegerProperty statusProperty() {
        return status;
    }

    public static void setStatus(int status) {
        Controller.status.set(status);
    }
}
