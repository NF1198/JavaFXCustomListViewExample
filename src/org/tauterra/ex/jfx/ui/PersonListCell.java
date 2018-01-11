/*
 * Copyright 2018 Nicholas Folse.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tauterra.ex.jfx.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.tauterra.ex.jfx.model.Person;

/**
 * FXML Controller class
 *
 * @author Nicholas Folse
 */
public class PersonListCell extends ListCell<Person> implements Initializable {

    @FXML
    private Label id;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField phoneNumber;
    @FXML
    private GridPane root;
    private Person model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // initialize a newly created cell to unselected status
        updateSelected(false);
        // add a un-focused listener to each child-item that triggers commitEdit(...)
        getRoot().getChildrenUnmodifiable().forEach(c -> {
            c.focusedProperty().addListener((obj, prev, curr) -> {
                if (!curr) {
                    commitEdit(model);
                }
            });
        });
        // set ListCell graphic
        setGraphic(root);
    }

    public GridPane getRoot() {
        return root;
    }

    private static final Logger LOG = Logger.getLogger(PersonListCell.class.getName());

    public static PersonListCell getInstance() {
        FXMLLoader loader = new FXMLLoader(PersonListCell.class.getResource("PersonListCell.fxml"));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    protected void updateItem(Person item, boolean empty) {
        super.updateItem(item, empty); // <-- Important
        // make empty cell items invisible
        getRoot().getChildrenUnmodifiable().forEach(c -> c.setVisible(!empty));
        // update valid cells with model data
        if (!empty && item != null && !item.equals(this.model)) {
            id.textProperty().set(item.id().toString());
            firstName.textProperty().set(item.firstName());
            lastName.textProperty().set(item.lastName());
            phoneNumber.textProperty().set(item.phoneNumber());
        }
        // keep a reference to the model item in the ListCell
        this.model = item;
    }

    @Override
    public void commitEdit(Person newValue) {
        // if newValue isn't defined, use this.model
        newValue = (newValue == null) ? this.model : newValue;
        super.commitEdit(newValue); // <-- important
        // update the model with values from the text fields
        newValue.firstName(firstName.textProperty().get());
        newValue.lastName(lastName.textProperty().get());
        newValue.phoneNumber(phoneNumber.textProperty().get());
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
        // update UI hints based on selected state
        getRoot().getChildrenUnmodifiable().forEach(c -> {
            // setting mouse-transparent to false ensure that
            // the cell will get selected we click on a field in
            // a non-selected cell
            c.setMouseTransparent(!selected);
            // focus-traversable prevents users from "tabbing"
            // out of the currently selected cell
            c.setFocusTraversable(selected);
        });
        if (selected) {
            // start editing when the cell is selected
            startEdit();
        } else {
            if (model != null) {
                // commit edits if the cell becomes unselected
                // we're not keeping track of "dirty" state
                // so this will commit changes even to unmodified cells
                commitEdit(model);
            }
        }
    }

}
