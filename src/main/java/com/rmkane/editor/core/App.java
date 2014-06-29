package com.rmkane.editor.core;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.Modifier;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.rmkane.editor.core.utils.IOUtils;

public class App extends Application {
	public static final Logger LOG = Logger.getLogger(App.class);

	public static final String TITLE = "PList Editor";
	public static final int APP_WIDTH = 1024;
	public static final int APP_HEIGHT = 768;
	public static final String LF = System.lineSeparator();

	private static final Desktop DESKTOP = Desktop.getDesktop();
	private static final String APP_DATA = System.getenv("APPDATA");
	private static final String BACKUP_DIR = APP_DATA
			+ "/Apple Computer/MobileSync/Backup";

	private final String STYLE_PATH = loadStyle("App.css");

	private ExtensionFilter extFilter;
	private FileChooser fileChooser;

	/** JavaFX Components */
	private Stage stage;
	private Scene appScene;
	private BorderPane pane;
	private TextArea txtAra;

	private MenuBar menuBar;
	private Menu fileMenu;
	private Menu editMenu;

	private File getRootDir() {
		File rootDir = null;
		try {
			rootDir = new File(BACKUP_DIR);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rootDir;
	}

	public Stage getStage() {
		return this.stage;
	}

	public App() {
		super();
		LOG.info("App::App()");
	}

	@Override
	public void init() throws Exception {
		super.init();
		LOG.info("App::init()");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		LOG.info("App::start()");

		this.stage = primaryStage;

		extFilter = new ExtensionFilter("PList File (*.plist)", "*.plist");
		fileChooser = new FileChooser();

		// fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setInitialDirectory(getRootDir());

		createMenuBar();

		txtAra = new TextArea();
		txtAra.getStyleClass().add("editor");

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

	protected void createMenuBar() {
		menuBar = new MenuBar();

		fileMenu = createMenu(menuBar, "File");

		createMenuItem(fileMenu, "New", KeyCode.N, KeyCombination.CONTROL_DOWN,
				new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				clear();
				txtAra.setText(generateNewPList());
			}
		});

		createMenuItem(fileMenu, "Open", KeyCode.O,
				KeyCombination.CONTROL_DOWN, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				File file = fileChooser.showOpenDialog(getStage());
				if (file != null) {
					clear();
					String plist = IOUtils.decode(file);
					txtAra.appendText(plist);
				}
			}
		});

		createMenuItem(fileMenu, "Save", KeyCode.S,
				KeyCombination.CONTROL_DOWN, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				File file = fileChooser.showSaveDialog(getStage());
				if (file != null) {
					String rawText = txtAra.getText();
					NSObject root = IOUtils.parse(rawText);
					IOUtils.write(file, root);
				}
			}
		});

		createMenuItem(fileMenu, "Exit", KeyCode.E,
				KeyCombination.CONTROL_DOWN, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Platform.exit();
			}
		});

		editMenu = createMenu(menuBar, "Edit");

		createMenuItem(editMenu, "Clear", KeyCode.C,
				KeyCombination.CONTROL_DOWN, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				txtAra.clear();
			}
		});
	}

	protected Menu createMenu(MenuBar menuBar, String name) {
		Menu menu = new Menu(name);
		menuBar.getMenus().add(menu);
		return menu;
	}

	protected MenuItem createMenuItem(Menu menu, String name, KeyCode keyCode,
			Modifier modifier, EventHandler<ActionEvent> action) {
		MenuItem menuItem = new MenuItem(name);
		menuItem.setMnemonicParsing(true);
		menuItem.setAccelerator(new KeyCodeCombination(keyCode, modifier));
		menuItem.setOnAction(action);
		menu.getItems().add(menuItem);
		return menuItem;
	}

	protected static String generateNewPList() {
		NSObject obj = new NSDictionary();
		return obj.toXMLPropertyList();
	}

	protected void clear() {
		txtAra.clear();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void openFile(File fh) {
		try {
			DESKTOP.open(fh);
		} catch (IOException e) {
			LOG.fatal(e.getMessage());
		}
	}

	private static final String loadStyle(String path) {
		return App.class.getResource(path).toExternalForm();
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