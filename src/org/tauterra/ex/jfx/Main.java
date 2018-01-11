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
package org.tauterra.ex.jfx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.tauterra.ex.jfx.model.Person;
import org.tauterra.ex.jfx.ui.PersonListCell;

/**
 *
 * @author Nicholas Folse
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ListView<Person> listView = new ListView<>();
        listView.setCellFactory((lv) -> {
            return PersonListCell.newInstance();
        });

        ObservableList<Person> persons = FXCollections.observableArrayList();
        persons.add(new Person()
                .id(5)
                .firstName("Bob")
                .lastName("Styles")
                .phoneNumber("123-4567"));
        persons.add(new Person()
                .id(17)
                .firstName("Sarah")
                .lastName("Jacobs")
                .phoneNumber("234-9328"));

        listView.setItems(persons);

        Scene scene = new Scene(new BorderPane(listView), 400, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("press F2 to print person records to stdout");
        primaryStage.show();

        scene.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent e) -> {
            if (e.getCode().equals(KeyCode.F2)) {
                persons.forEach(p -> System.out.println(p.toString()));
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
