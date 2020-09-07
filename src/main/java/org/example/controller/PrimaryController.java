package org.example.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.models.User;

public class PrimaryController implements Initializable {

    private static User user;

    @FXML
    Button logoutButton;
    @FXML
    Button signinButton;
    @FXML
    TextField userNameTextField;
    @FXML
    TextField passwordTextField;

    private static SimpleBooleanProperty isLoggedIn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("init");
        user = new User();
        isLoggedIn = new SimpleBooleanProperty(false);
        bindProperties();
        try {
            if (user.isLoggedIn()) {
                isLoggedIn.set(true);
                User luser = User.getLoggedInUser();
                user.setUserName(luser.getUserName());
                user.setId(luser.getId());
                user.setPassword(luser.getPassword());
                user.setIsLoggedIn(luser.isLoggedIn());
            } else {
                isLoggedIn.set(false);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        checkCreateUser();


    }

    @FXML
    public void login(ActionEvent actionEvent){
        if (isLoggedIn.get() == true) {
            user.updateUser();
        } else {
            try {
                if (user.login()) {
                    isLoggedIn.set(true);
                    switchToDashboard();
                }
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    @FXML
    public void logout(ActionEvent actionEvent) {
        try {
            user.logout();
            isLoggedIn.set(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void checkCreateUser() {
        try {
            if (user.countUsers() == 0) {
                user.setUserName("root");
                user.setPassword("root");
                user.createUser();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void bindProperties() {
        user.userNameProperty().bindBidirectional(userNameTextField.textProperty());
        user.passwordProperty().bindBidirectional(passwordTextField.textProperty());

        StringBinding updateBinder = new StringBinding() {
            {
                super.bind(isLoggedIn);
            }
            @Override
            protected String computeValue() {
                if (isLoggedIn.get()) {
                    return "Update";
                } else {
                    return "Login";
                }
            }
        };

        logoutButton.visibleProperty().bind(Bindings.not(Bindings.not(isLoggedIn)));
        signinButton.textProperty().bind(updateBinder);
    }

    @FXML
    private void switchToDashboard() throws IOException {
        if (isLoggedIn.get()) {
            App.setRoot("sample");
        }

    }
}
