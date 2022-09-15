package com.github.quaoz.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** Generalises all {@link DocumentListener} methods */
@FunctionalInterface
public interface SimpleDocumentListener extends DocumentListener {

/**
* Gives notification that there was a change to the document. Called in place of {@link
* DocumentListener#changedUpdate(DocumentEvent)}, {@link
* DocumentListener#insertUpdate(DocumentEvent)} and {@link
* DocumentListener#removeUpdate(DocumentEvent)}.
*
* @param e the document event
*/
void update(DocumentEvent e);

@Override
default void insertUpdate(DocumentEvent e) {
	update(e);
}

@Override
default void removeUpdate(DocumentEvent e) {
	update(e);
}

@Override
default void changedUpdate(DocumentEvent e) {
	update(e);
}
}
