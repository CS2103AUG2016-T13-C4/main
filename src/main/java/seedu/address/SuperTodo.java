package seedu.address;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SuperTodo extends Application {
    
    private Stage primaryStage;
    private VBox rootLayout;
    
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("SuperTodo");
		
		initRootLayout();
		
		showTaskOverview();
	}


	
	/**
	 * initialize root layout
	 */
	
	public void initRootLayout() {
	    try { 
	        // load root layout from fxml file
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(MainApp.class.getResource("view/AppWindow.fxml"));
	    rootLayout = (VBox) loader.load();
	    
	    
	    // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	    
	}
	
	/**
	 * shows the task overview inside the root layout
	 */
	public void showTaskOverview() {
	    try {
	        // load task overview
	        FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonListPanel.fxml"));
            VBox taskOverview;
            taskOverview = (VBox) loader.load();
            
            // set location of task overview pane
	    } catch (IOException e){
	        e.printStackTrace();
	    }
	}
	
	
	/*
	 * returns the main stage
	 * @return
	 */
	public Stage getPrimaryStage() {
	    return primaryStage;
	}
	
	 public static void main(String[] args) {
        launch(args);
    }
	

	
//	private Scene scene;
//	private Group root;
//	private Stage stage;
//	
//	scene = new Scene(root);
//    stage.setScene(scene);
//    stage.setMaxWidth(490);
//    stage.setMinWidth(490);
//    stage.setMaxHeight(650);
//    stage.setMinHeight(650);
//    stage.show();
//    displayTasks();
}
