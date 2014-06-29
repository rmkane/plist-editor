package com.rmkane.editor.examples;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.swing.UIManager;

import org.apache.log4j.Logger;

public class ConsoleApplication extends Application {
	public static final Logger LOG = Logger.getLogger(ConsoleApplication.class);

	public static final String TITLE = "Console";
	public static final int APP_WIDTH = 600;
	public static final int APP_HEIGHT = 400;
	public static final String LF = System.lineSeparator();

	private final String STYLE_PATH = loadStyle("ConsoleApplication.css");

	/** JavaFX Components */

	private Stage stage;
	private Scene appScene;
	private BorderPane pane;
	private TextArea txtAra;
	private MenuBar menuBar;
	private Menu fileMenu;
	private MenuItem newMenuItem;
	private FileChooser fileChooser;
	private ExtensionFilter extFilter;

	/** Standard Java JDK components */

	public Stage getStage() {
		return this.stage;
	}

	public ConsoleApplication() {
		super();
		LOG.info("ConsoleApplication::ConsoleApplication()");
	}

	@Override
	public void init() throws Exception {
		super.init();
		LOG.info("App::init()");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		LOG.info("ConsoleApplication::start()");

		this.stage = primaryStage;

		createMenu();

		txtAra = new TextArea();
		txtAra.getStyleClass().add("editor");

		extFilter = new ExtensionFilter("Plist File (*.plist)", "*.plist");
		fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		//fileChooser.getExtensionFilters().add(extFilter);

		pane = new BorderPane();
		pane.setPadding(new Insets(4, 8, 4, 8));
		pane.setTop(menuBar);
		pane.setCenter(txtAra);

		appScene = new Scene(pane, APP_WIDTH, APP_HEIGHT);
		appScene.getStylesheets().add(STYLE_PATH);

		primaryStage.setTitle(TITLE);
		primaryStage.setScene(appScene);
		primaryStage.show();
	}

	protected void createMenu() {
		menuBar = new MenuBar();
		fileMenu = new Menu("File");
		newMenuItem = new MenuItem("New");

		newMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				File f = fileChooser.showOpenDialog(getStage());

				if (f != null) {
					txtAra.appendText(String.format("Selected: %s%n",
							f.getName()));
				} else {
					System.out.println("No FILE selected...");
				}
			}
		});

		menuBar.getMenus().add(fileMenu);
		fileMenu.getItems().add(newMenuItem);
	}

	public static void main(String[] args) {
		launch(args);
	}

	private static final String loadStyle(String path) {
		return ConsoleApplication.class.getResource(path).toExternalForm();
	}

	@SuppressWarnings("unused")
	private static final void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}