# JavaFX FXML Controller Pattern
This post describes a simple and repeatable pattern for developing self-contained JavaFX panels based on FXML. The key aspect of the pattern is placing the FXML loading code in a static method of the component controller. This may not be applicable to all use-cases, but it greatly simplifies most use-cases while providing good encapsulation of functionality.

As an example, we’ll show how to develop an editable ListView, with a custom ListCell.

# TLDR;

### General

* The FXML Controller is the entry-point for your FXML-based component
* Optionally rename Controller classes by removing "Controller" from the class name
* Use @FXML injection to inject child-elements into the controller.
* Implement FXML-loading in a static method of the controller. Return a reference to the controller.

### Custom ListCell<>

* There is no "best" solution for implementing a custom ListCell<>.
* Read the ListView<> javadoc for implementation guidelines.
* The ListCell<> base class is relatively flexible so you should be able to implement the behavior you desire.
* The example in this repository works well for general use-cases; refer to the last code snippet for details.
* Clone this repository, then run it in Netbeans to see the custom list editor in action.

# Background and Setup

Our objective is to develop a simple form containing a ListView based on a custom editable ListCell. Furthermore, we want to encapsulate functionality as much as possible and hide low-level details like FXML loading.

We’ll use the [SceneBuilder](http://gluonhq.com/products/scene-builder/) to build our form components and [Netbeans](https://netbeans.org/) for editing.

The tutorial assumes you have a basic understanding of how to use SceneBuilder and know the basic elements of FXML, such as what fx:id’s are and how to wrangle size constraints on elements. The tutorial also assumes that you understand basic Java concepts such as classes, functions, and static methods. The explanation about field validation uses css and styleClasses.

Our view will show a list of “Persons” represented by a data structure with the following fields:
**id**: integer, immutable
**first_name**: string
**last_name**: string
**phone_number**: string (only allows numbers and dash)

## Classes Used

ObservableList, FXCollections, ListView, ListCell, ListCell

# Initial Setup

First, create a simple JavaFX project in Netbeans. Don’t worry about starting out with FXML. We’re going to show how to use FXML with our custom ListCell. 


![Initial Project Setup](https://d2mxuefqeaa7sj.cloudfront.net/s_8162C89F2A7BD2333A99C45778CFA55BFD905D6F4730B4CFA9329AC6B616D096_1515638132916_image.png)


Rename the packages in your project. You should have a base package for application-classes, and  ‘ui’ package for UI-elements (FXML and Controllers). Your project should look something like the screenshot to the right.

Don’t put your FXML into a resources source tree because SceneBuilder doesn’t support this. Name your FXML files using the same naming convention you use for Java classes.

Build the PersonListCell.fxml in SceneBuilder. You can add the FXML in Netbeans, then edit it in SceneBuilder, or start by creating a new file in SceneBuilder. Either way, you can automatically create/update the controller in Netbeans by right-clicking on an .fxml file and choosing “create controller”.


## PersonListCell.fxml

The cell editor should look something like the following screenshot:

![PersonListCell.fxml (SceneBuilder)](https://d2mxuefqeaa7sj.cloudfront.net/s_8162C89F2A7BD2333A99C45778CFA55BFD905D6F4730B4CFA9329AC6B616D096_1515638672735_image.png)
![PersonListCell hierarchy showing fx:id's](https://d2mxuefqeaa7sj.cloudfront.net/s_8162C89F2A7BD2333A99C45778CFA55BFD905D6F4730B4CFA9329AC6B616D096_1515638786586_image.png)


The form’s hierarchy should look something like the screenshot on the right. The screenshot shows the fx:id of each element. You can preview the form in SceneBuilder to verify resizing behavior.

Next, create/update the controller by right-clicking PersonListCell.fxml in Netbeans, and choosing “Create Controller”.

![Updating the PersonListCellController](https://d2mxuefqeaa7sj.cloudfront.net/s_8162C89F2A7BD2333A99C45778CFA55BFD905D6F4730B4CFA9329AC6B616D096_1515639143051_image.png)


We now have the basic skeleton of of custom ListCell. Next, we need to implement two key pieces of logic to meet our objectives. First, we will implement FXML loading in our controller class. Second, we will implement the ListCell<> interface on our controller.

## FXML Loading

One of the frustrating aspects of using FXML is that you have to load the FXML in your application in order to use your form. The built-in FXML loader works fine, but it’s a little confusing to use. Also, the built-in templates don’t give any hints at how to encapsulate form loading or how controllers relate to forms.

Here is an example of how we would like to use FXML-based forms (in general):

    /* desired way of using form (not "out-of-box" behavior) */
    MyCustomForm form = MyCustomForm.newInstance();
    Scene scene = new Scene(form.getRoot(), 600, 250);
    primaryStage.setScene(scene);
    primaryStage.show();
    

To make this work, we need to do two things:

- First, rename the XyzController class to Xyz. (In the example above, the “MyCustomForm” class is the form controller.)
- Next, implement FXML loading in a static method in the controller.

**Rename Controller (optional)**
Personally, I find the “Controller” designation on my controller classes to be redundant. I also prefer to have a 1-1 relationship between .fxml and .java files.

You can perform the rename in Netbeans or SceneBuilder. In Netbeans, right-click the controller class in Netbeans, then choose Refactor > Rename (or select the class and press F2, or select the class and press Ctrl-R). Remove “Controller” from the name. Next, right click the .fxml file and choose “edit” to force Netbeans to open the .fxml file in the internal editor. Notice that the controller can’t be found. Manually rename the controller class in the FXML element definition (scroll all the way to the right).

Reopen the .fxml in SceneBuilder and verify that the controller class was renamed. The controller definition should look like the following screenshot.

![PersonListCell.fxml Controller Definition (after renaming controller class)](https://d2mxuefqeaa7sj.cloudfront.net/s_8162C89F2A7BD2333A99C45778CFA55BFD905D6F4730B4CFA9329AC6B616D096_1515641884270_image.png)


**Implement FXML Loading**
Add a static `.newInstance()` method to the controller class. The method will load the FXML and return an instance to the controller.

The following FXML loading code snippet is based on our example, PersonListCell.


    /* PersonListCell (Controller) */
    public class PersonListCell implements Initializable {
    ...
       public static PersonListCell newInstance() {
           FXMLLoader loader = new FXMLLoader(
               PersonListCell.class.getResource("PersonListCell.fxml"));
           try {
               loader.load();
               return loader.getController();
           } catch (IOException ex) {
               LOG.log(Level.SEVERE, null, ex);
               return null;
           }
        }
    ...
    }

Performing the FXML loading in this manner hides this implementation detail from the rest of our application and simplifies how we use our custom component. 

You need to add an accessor method on the controller to expose the root GridPane element. This is easy since the FXML loader manages injecting references to these objects into the controller class. (btw, the Netbeans controller code builder added the `root` member to our class; if confused, see “Make Controller” above).


    /* PersonListCell (Controller) (cont.) */
    public class PersonListCell implements Initializable {
    ...
        public GridPane getRoot() {
            return root;
        }
    ...
    }

Add accessors for any view elements you want to expose to hosting code.

If you’ve setup everything correctly, you should be able to test your custom component by loading in `Main.java`. Notice that we’ve greatly simplified how we use our custom component. Our client code is uncluttered by unnecessary boilerplate making the code highly readable and easy to maintain.


    /* Main.java */
    ...
    public class Main extends Application {
        @Override
        public void start(Stage primaryStage) {
            PersonListCell form = PersonListCell.newInstance();
            Scene scene = new Scene(new StackPane(form.getRoot()), 400, 150);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        ...
    }

Running this code should produce a view like shown in the following screenshot:

![Test view of our custom PersonListCell component](https://d2mxuefqeaa7sj.cloudfront.net/s_8162C89F2A7BD2333A99C45778CFA55BFD905D6F4730B4CFA9329AC6B616D096_1515643455381_image.png)


So far, we’ve created a custom view component and implemented the code required to load and instantiate instances of the view. The FXML loading code was implemented in a static method of the controller class. 

## Custom ListCell Implementation

In order to use our PersonListCell in a ListView, we need to extend the ListCell<> base class, and override a few methods to enable interaction. 

First, implement a data model class, Person. You have two choices in this regard. You can make a standard JavaBeans compatible data class using getters and setters, or you can make a JavaFX-style data class using properties. We choose the latter, since this example is concerned with JavaFX. Refer to the code listings below for the complete listing of Person.java.

Next, add `extends ListCell<Person>` to the PersonListCell class definition:
 

    ...
    public class PersonListCell extends ListCell<Person> implements Initializable {
    ...

and add `setGraphic(root)` to the initialize(…) method of PersonListCell:


    /* PersonListCell.java */
    ...
        @Override
        public void initialize(URL url, ResourceBundle rb) {
            setGraphic(root);
        }
    ...

To test our progress, we update Main.java to use our PersonListCell: 


    /* Main.java (updated to use PersonListCell in a ListView) */
    ...
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
    
            Scene scene = new Scene(new BorderPane(listView), 400, 150);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        ...
    }

At this point, you should be able to run your test application, but you won’t see any data in the ListView. This is because we haven’t overridden the necessary methods of ListCell<> on our PersonListCell class.

Before we go any further, we need to decide how we expect users to interact with our list view. Do we want to allow users to freely edit data in the list view, or do we want to force users to double-click each cell before allowing editing? The [ListView<> java docs](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/ListView.html#ListView--) introduce this discussion, but don’t provide detailed direction. Even so, I recommend you read the java docs in detail to understand the alternatives.

In our case, we want to allow users to freely edit fields and we don’t want to require users to double-click element to enter “edit” mode. Unfortunately, we need to do quite a bit of customization to get all this working. The following screenshot highlights several things we need to fix.

![Current state of our custom ListCell indicating several things we need to fix](https://d2mxuefqeaa7sj.cloudfront.net/s_8162C89F2A7BD2333A99C45778CFA55BFD905D6F4730B4CFA9329AC6B616D096_1515647357217_image.png)


**Populating Values and Hiding Non-Existent Elements**
We can populate values and hide non-existent elements by overriding the `updateItem(…)` method of Cell<T>. Netbeans includes some convenient tools to help us do this efficiently, as shown in the following screenshots.

![](https://d2mxuefqeaa7sj.cloudfront.net/s_8162C89F2A7BD2333A99C45778CFA55BFD905D6F4730B4CFA9329AC6B616D096_1515647780755_image.png)
![](https://d2mxuefqeaa7sj.cloudfront.net/s_8162C89F2A7BD2333A99C45778CFA55BFD905D6F4730B4CFA9329AC6B616D096_1515647872863_image.png)


Implement `updateItems(…)` as shown in the following listing. Make sure you call `super.updateItem(item, empty);` 


    /* PersonListCell (updateItems detail) */
    ...
        @Override
        protected void updateItem(Person item, boolean empty) {
            super.updateItem(item, empty); // <-- Important
            getRoot().getChildrenUnmodifiable().forEach(c -> c.setVisible(!empty));
            if (!empty) {
                id.textProperty().set(item.id().toString());
                firstName.textProperty().set(item.firstName());
                lastName.textProperty().set(item.lastName());
                phoneNumber.textProperty().set(item.phoneNumber());
            }
        }
    ...

Now, our test harness shows the data in our list and hides empty cells as shown in the following screenshot.

![Our custom PersonListCell in use, after implementing updateItems(...)](https://d2mxuefqeaa7sj.cloudfront.net/s_8162C89F2A7BD2333A99C45778CFA55BFD905D6F4730B4CFA9329AC6B616D096_1515648439065_image.png)


Next, we need to adjust the interactive behavior of the ListCell. For example, we need to somehow track selection so that we can only edit text fields of in the selected cell (but not force users to double-click the cell to enter “edit-mode”). Then we need to update the data model with changes when the active ListCell looses focus.


> Note: The implementation details presented below are highly dependent on your application. The solution shown here might not be best for your application, but it should still serve as a good starting point for your implementation.

For the purpose of this tutorial, we’re going to implement a very lenient update model. Basically, we’re going to apply all changes to any field as soon as they occur with no cell-level validation. A more advanced implementation might include adding cell-level “update” & “cancel” buttons that only appear when the cell is being edited.

We need to make several changes to PersonListCell to get updates working correctly. Instead of describing each of these individually we present an annotated code listing.


    /* PersonListCell (implementing change detection and model updates) */
    ...
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

        public static PersonListCell newInstance() {
            FXMLLoader loader = new FXMLLoader(
                   PersonListCell.class.getResource("PersonListCell.fxml"));
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
    ...


## Alternative Implementation

Instead of updating text field values directly, we could have used binding between the model and the cell fields. Binding would simplify the implementation in our simple example, but it would make it more difficult to implement cell-level validation/confirmation.

# Versions

This post is based on Java8 with JavaFX

