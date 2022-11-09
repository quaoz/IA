package com.github.quaoz.gui;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import com.formdev.flatlaf.util.SystemInfo;
import com.github.quaoz.structures.Moth;
import com.github.quaoz.structures.SimpleStack;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

public class GUI {

	private static GUI gui;
	private final JFrame frame;
	private final Home home;
	private final SignInForm signInForm;
	private final SignUpForm signUpForm;
	private final AdvancedSearchForm advancedSearchForm;
	private final SubmitRecordForm submitRecordForm;
	private final AddMothForm addMothForm;
	private final Profile profileForm;
	private final ApproveRequests approveRequestsForm;
	private final SearchResultsForm searchResultsForm;
	private final RecordForm recordForm;
	private final RecordsForm recordsForm;
	private final SimpleStack<Content> callStack;
	private Moth record;
	private Content currentContent;

	private GUI() {
		Logger.info("Creating GUI...");

		if (SystemInfo.isMacOS) {
			System.setProperty("apple.awt.application.name", "IA");
			System.setProperty("apple.awt.application.appearance", "system");
		}

		FlatArcDarkOrangeIJTheme.setup();

		// Initialise base frame
		frame = new JFrame("IA");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		if (SystemInfo.isMacFullWindowContentSupported) {
			frame
				.getRootPane()
				.putClientProperty("apple.awt.transparentTitleBar", true);
		}

		// Create the GUI panels
		signInForm = new SignInForm();
		signUpForm = new SignUpForm();
		advancedSearchForm = new AdvancedSearchForm();
		submitRecordForm = new SubmitRecordForm();
		addMothForm = new AddMothForm();
		profileForm = new Profile();
		approveRequestsForm = new ApproveRequests();
		searchResultsForm = new SearchResultsForm();
		recordForm = new RecordForm();
		recordsForm = new RecordsForm();
		home = new Home();

		currentContent = Content.HOME;
		callStack = new SimpleStack<>();
		render(Content.HOME);

		Logger.info("Finished creating GUI");
	}

	/**
	 * Gets the gui instance
	 *
	 * @return The gui instance
	 */
	public static synchronized GUI getInstance() {
		return gui;
	}

	/**
	 * Initialises the gui instance
	 */
	public static synchronized void init() {
		if (gui == null) {
			gui = new GUI();
		}
	}

	/**
	 * Makes the gui visible
	 */
	public void show() {
		frame.setVisible(true);
	}

	public Moth getRecord() {
		return record;
	}

	public void setRecord(Moth record) {
		this.record = record;
	}

	public void render(@NotNull Content content) {
		switch (content) {
			case HOME -> frame.setContentPane(home.resolve());
			case SIGN_IN -> frame.setContentPane(signInForm.resolve());
			case SIGN_UP -> frame.setContentPane(signUpForm.resolve());
			case ADVANCED_SEARCH -> frame.setContentPane(advancedSearchForm.resolve());
			case PROFILE -> frame.setContentPane(profileForm.resolve());
			case SUBMIT_RECORD -> frame.setContentPane(submitRecordForm.resolve());
			case SEARCH_RESULTS -> frame.setContentPane(searchResultsForm.resolve());
			case RECORD -> frame.setContentPane(recordForm.resolve());
			case ADD_MOTH -> frame.setContentPane(addMothForm.resolve());
			case RECORDS -> frame.setContentPane(recordsForm.resolve());
			case APPROVE_REQUESTS -> frame.setContentPane(approveRequestsForm.resolve());
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
		HOME,
		SIGN_IN,
		SIGN_UP,
		ADVANCED_SEARCH,
		PROFILE,
		SUBMIT_RECORD,
		SEARCH_RESULTS,
		RECORD,
		ADD_MOTH,
		RECORDS,
		APPROVE_REQUESTS,
		PAST_CONTENT,
	}
}
