package com.github.quaoz.gui;


import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import com.formdev.flatlaf.util.SystemInfo;
import com.github.quaoz.Moth;
import com.github.quaoz.structures.Pair;
import com.github.quaoz.structures.SimpleStack;
import java.util.ArrayList;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

public class GUI {
	private final JFrame frame;
	private final HomeLoggedOut homeLoggedOut;
	private final HomeLoggedIn homeLoggedIn;
	private final SignInForm signInForm;
	private final SignUpForm signUpForm;
	private final AdvancedSearchForm advancedSearchForm;
	private final SubmitRecordForm submitRecordForm;
	private final AddMothForm addMothForm;
	private final SimpleStack<Content> callStack;
	private ArrayList<Moth> searchResults;
	private Moth record;
	private Content currentContent;

	public GUI() {
		if (SystemInfo.isMacOS) {
			System.setProperty("apple.awt.application.name", "IA");
			System.setProperty("apple.awt.application.appearance", "system");
		}

		// FlatOneDarkIJTheme.setup();
		FlatArcDarkOrangeIJTheme.setup();

		// Initialise base frame
		frame = new JFrame("IA");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		if (SystemInfo.isMacFullWindowContentSupported) {
			frame.getRootPane().putClientProperty("apple.awt.transparentTitleBar", true);
		}

		// Create the GUI panels
		homeLoggedOut = new HomeLoggedOut();
		homeLoggedIn = new HomeLoggedIn();
		signInForm = new SignInForm();
		signUpForm = new SignUpForm();
		advancedSearchForm = new AdvancedSearchForm();
		submitRecordForm = new SubmitRecordForm();
		addMothForm = new AddMothForm();

		currentContent = Content.HOME_LOGGED_OUT;
		callStack = new SimpleStack<>();
		render(Content.HOME_LOGGED_OUT);

		frame.setVisible(true);
		Logger.info("GUI created");
	}

	public static void main(String[] args) {
		new GUI();
	}

	public ArrayList<Moth> getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(@NotNull ArrayList<Pair<Moth, Double>> searchResults) {
		ArrayList<Moth> moth = new ArrayList<>();
		searchResults.forEach((p) -> moth.add(p.getKey()));
		this.searchResults = moth;
	}

	public Moth getRecord() {
		return record;
	}

	public void setRecord(Moth record) {
		this.record = record;
	}

	public void render(@NotNull Content content) {
		switch (content) {
			case HOME_LOGGED_OUT -> frame.setContentPane(homeLoggedOut.resolve());
			case HOME_LOGGED_IN -> frame.setContentPane(homeLoggedIn.resolve());
			case SIGN_IN -> frame.setContentPane(signInForm.resolve());
			case SIGN_UP -> frame.setContentPane(signUpForm.resolve());
			case ADVANCED_SEARCH -> frame.setContentPane(advancedSearchForm.resolve());
			case PROFILE -> frame.setContentPane(new Profile().resolve()); // Created ad hoc
			case SUBMIT_RECORD -> frame.setContentPane(submitRecordForm.resolve());
			case SEARCH_RESULTS -> frame.setContentPane(
					new SearchResultsForm().resolve()); // Created ad hoc
			case RECORD -> frame.setContentPane(new RecordForm().resolve()); // Created ad hoc
			case ADD_MOTH -> frame.setContentPane(addMothForm.resolve());
			case PAST_CONTENT -> {
				render(callStack.pop());
				callStack.pop();
			}
		}

		if (content != Content.PAST_CONTENT) {
			callStack.push(currentContent);
			currentContent = content;
		}

		frame.pack();
	}

	enum Content {
		HOME_LOGGED_OUT,
		HOME_LOGGED_IN,
		SIGN_IN,
		SIGN_UP,
		ADVANCED_SEARCH,
		PROFILE,
		SUBMIT_RECORD,
		SEARCH_RESULTS,
		RECORD,
		ADD_MOTH,
		PAST_CONTENT,
	}
}
