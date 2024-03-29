package com.github.quaoz.gui;

import com.github.quaoz.managers.RecordManager;
import com.github.quaoz.managers.UserManager;
import com.github.quaoz.structures.Record;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.tinylog.Logger;

public class RecordsForm {

	private ArrayList<Record> records;
	private JButton backButton;
	private JPanel panel;
	private JTable table;
	private JScrollPane recordsScrollPane;
	private JButton saveButton;
	private JLabel saveLable;

	public RecordsForm() {
		$$$setupUI$$$();
		// Hacky way to prevent editing
		table.setDefaultEditor(Object.class, null);

		backButton.addActionListener(e ->
			GUI.getInstance().render(GUI.Content.PAST_CONTENT)
		);

		saveButton.addActionListener(e -> {
			File saveFile = Paths
				.get(System.getProperty("user.home"), "Downloads")
				.resolve("export-" + Instant.now() + ".csv")
				.toFile();

			try (
				CSVWriter writer = (CSVWriter) new CSVWriterBuilder(
					new FileWriter(saveFile)
				)
					.build()
			) {
				records.forEach(record ->
					writer.writeNext(
						new String[] {
							String.valueOf(record.id()),
							record.species(),
							record.location(),
							record.date(),
							String.valueOf(record.size()),
							record.user(),
						}
					)
				);
				saveLable.setText("Saved data to: " + saveFile);
			} catch (IOException err) {
				Logger.error(err, "Unable to write CSV file at " + saveFile.getPath());
				throw new RuntimeException(err);
			}
		});

		table.addMouseListener(
			new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int row = table.rowAtPoint(e.getPoint());

					if (table.columnAtPoint(e.getPoint()) == 6) {
						if (
							new ConfirmRemoveDialog(
								"Are you sure you want to remove this record?",
								"Yes",
								"No"
							)
								.isOk()
						) {
							RecordManager.getInstance().remove(records.get(row).id());
							((DefaultTableModel) table.getModel()).removeRow(row);
						}
					}
				}
			}
		);
	}

	public JPanel resolve() {
		saveLable.setText("");

		records =
			RecordManager
				.getInstance()
				.getSpecies(GUI.getInstance().getRecord().name());

		ArrayList<String> columnNames = new ArrayList<>(
			Arrays.asList("ID", "Species", "Location", "Date", "Size", "User")
		);
		boolean auth = UserManager.getInstance().isMod();

		if (auth) {
			columnNames.add("Remove");
		}

		String[][] data = new String[records.size()][columnNames.size()];

		int count = 0;
		for (Record record : records) {
			data[count][0] = String.valueOf(record.id());
			data[count][1] = record.species();
			data[count][2] = record.location();
			data[count][3] = record.date();
			data[count][4] = String.valueOf(record.size());
			data[count][5] = record.user();

			if (auth) {
				data[count][6] = "Remove";
			}

			count++;
		}

		table.setModel(new DefaultTableModel(data, columnNames.toArray()));

		return panel;
	}

	// spotless:off

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		panel = new JPanel();
		panel.setLayout(new FormLayout("fill:675px:grow,fill:max(d;4px):noGrow", "center:367px:grow,center:32px:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,center:9px:noGrow"));
		panel.setMinimumSize(new Dimension(768, 768));
		panel.setPreferredSize(new Dimension(768, 768));
		recordsScrollPane = new JScrollPane();
		CellConstraints cc = new CellConstraints();
		panel.add(recordsScrollPane, cc.xyw(1, 1, 2, CellConstraints.FILL, CellConstraints.FILL));
		table = new JTable();
		recordsScrollPane.setViewportView(table);
		backButton = new JButton();
		this.$$$loadButtonText$$$(backButton, this.$$$getMessageFromBundle$$$("ia", "back"));
		panel.add(backButton, cc.xy(1, 2));
		saveButton = new JButton();
		saveButton.setText("Save As CSV");
		panel.add(saveButton, cc.xy(1, 3));
		saveLable = new JLabel();
		saveLable.setText("");
		panel.add(saveLable, cc.xy(1, 5));
		final Spacer spacer1 = new Spacer();
		panel.add(spacer1, cc.xy(1, 6, CellConstraints.FILL, CellConstraints.DEFAULT));
	}

	private static Method $$$cachedGetBundleMethod$$$ = null;

	private String $$$getMessageFromBundle$$$(String path, String key) {
		ResourceBundle bundle;
		try {
			Class<?> thisClass = this.getClass();
			if ($$$cachedGetBundleMethod$$$ == null) {
				Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
				$$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
			}
			bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
		} catch (Exception e) {
			bundle = ResourceBundle.getBundle(path);
		}
		return bundle.getString(key);
	}

	/**
	 * @noinspection ALL
	 */
	private void $$$loadButtonText$$$(AbstractButton component, String text) {
		StringBuffer result = new StringBuffer();
		boolean haveMnemonic = false;
		char mnemonic = '\0';
		int mnemonicIndex = -1;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '&') {
				i++;
				if (i == text.length()) break;
				if (!haveMnemonic && text.charAt(i) != '&') {
					haveMnemonic = true;
					mnemonic = text.charAt(i);
					mnemonicIndex = result.length();
				}
			}
			result.append(text.charAt(i));
		}
		component.setText(result.toString());
		if (haveMnemonic) {
			component.setMnemonic(mnemonic);
			component.setDisplayedMnemonicIndex(mnemonicIndex);
		}
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return panel;
	}

	// spotless:on
}
