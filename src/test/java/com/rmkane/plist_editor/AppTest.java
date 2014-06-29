package com.rmkane.plist_editor;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.rmkane.editor.core.App;

public class AppTest {
	private static App app;

	@BeforeClass
	public static void setUp() {
		app = new App();
	}

	@Test
	public void appTest() {
		assertNotNull(app);
	}
}