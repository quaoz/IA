package com.github.quaoz.gui;

import com.github.quaoz.structures.SimpleStack;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import javax.swing.*;

public class GUI {
	private JFrame frame;
	private HomeLoggedOut homeLoggedOut;
	private HomeLoggedIn homeLoggedIn;
	private SignInForm signIn;
	private SignUpForm signUp;
	private AdvancedSearchForm advancedSearch;
	private Profile profile;
	private SubmitRecordForm submitRecord;
	private SearchResultsForm searchResults;
	private RecordForm record;
	private SimpleStack<Content> callStack;
	private Content currentContent;

	/*
	 * Panels:				UI		Connected
	 * 	- Home logged in	x
	 * 	- Home logged out	x
	 * 	- Sign in			x
	 * 	- Sign up			x
	 * 	- Advanced search	x
	 * 	- Profile			x
	 * 	- Submit record		x
	 * 	- Search results	x
	 * 	- Record			x
	 * */

	public static void main(String[] args) {
		new GUI().init();
	}

	public GUI init() {
		// Initialise base frame
		frame = new JFrame("IA");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create the GUI panels
		homeLoggedOut = new HomeLoggedOut(this);
		homeLoggedIn = new HomeLoggedIn(this);
		signIn = new SignInForm(this);
		signUp = new SignUpForm(this);
		advancedSearch = new AdvancedSearchForm(this, searchResults);
		profile = new Profile(this);
		submitRecord = new SubmitRecordForm(this);
		searchResults = new SearchResultsForm(this);
		record = new RecordForm(this);

		currentContent = Content.HOME_LOGGED_OUT;
		callStack = new SimpleStack<>();
		render(Content.HOME_LOGGED_OUT);
		frame.setVisible(true);

		Logger.info("GUI created");
		return this;
	}

	public void render(@NotNull Content content) {
		switch (content) {
			case HOME_LOGGED_OUT -> frame.setContentPane(homeLoggedOut.resolve());
			case HOME_LOGGED_IN -> frame.setContentPane(homeLoggedIn.resolve());
			case SIGN_IN -> frame.setContentPane(signIn.resolve());
			case SIGN_UP -> frame.setContentPane(signUp.resolve());
			case ADVANCED_SEARCH -> frame.setContentPane(advancedSearch.resolve());
			case PROFILE -> frame.setContentPane(profile.resolve());
			case SUBMIT_RECORD -> frame.setContentPane(submitRecord.resolve());
			case SEARCH_RESULTS -> frame.setContentPane(searchResults.resolve());
			case RECORD -> frame.setContentPane(record.resolve());
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
		PAST_CONTENT,
	}
}
