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
package org.tauterra.ex.jfx.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Nicholas Folse
 */
public class Person {

    private final IntegerProperty idProperty = new SimpleIntegerProperty(0);
    private final StringProperty firstNameProperty = new SimpleStringProperty();
    private final StringProperty lastNameProperty = new SimpleStringProperty();
    private final StringProperty phoneNumberProperty = new SimpleStringProperty();

    public Person() {
    }

    public IntegerProperty idProperty() {
        return idProperty;
    }

    public Integer id() {
        return idProperty().get();
    }

    public Person id(Integer value) {
        idProperty().set(value);
        return this;
    }

    public StringProperty firstNameProperty() {
        return firstNameProperty;
    }

    public String firstName() {
        return firstNameProperty().get();
    }

    public Person firstName(String value) {
        firstNameProperty().set(value);
        return this;
    }

    public StringProperty lastNameProperty() {
        return lastNameProperty;
    }

    public String lastName() {
        return lastNameProperty().get();
    }

    public Person lastName(String value) {
        lastNameProperty().set(value);
        return this;
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumberProperty;
    }

    public String phoneNumber() {
        return phoneNumberProperty().get();
    }

    public Person phoneNumber(String value) {
        phoneNumberProperty().set(value);
        return this;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id() + ", firstName=" + firstName() + ", lastName=" + lastName() + ", phoneNumber=" + phoneNumber() + '}';
    }

    
    

}
