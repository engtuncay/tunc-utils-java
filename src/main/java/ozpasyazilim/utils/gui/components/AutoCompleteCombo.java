package ozpasyazilim.utils.gui.components;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.apache.log4j.Logger;

/**
 * Autocomplete combobox with filtering and
 * text inserting of new text
 * 
 * @author Exterminator13
 */
public class AutoCompleteCombo extends JComboBox {

	private static final Logger logger = Logger.getLogger(AutoCompleteCombo.class);
	private Model model = new Model();
	private final JTextComponent textComponent = (JTextComponent) getEditor().getEditorComponent();
	private boolean modelFilling = false;

	private boolean updatePopup;

	public AutoCompleteCombo() {

		setEditable(true);

		logger.debug("setPattern() called from constructor");

		setPattern(null);
		updatePopup = false;

		textComponent.setDocument(new AutoCompleteDocument());
		setModel(model);
		setSelectedItem(null);

		new Timer(20, e -> {
			if (updatePopup && isDisplayable()) {
				setPopupVisible(false);
				if (model.getSize() > 0) {
					setPopupVisible(true);
				}
				updatePopup = false;
			}
		}).start();
	}

	private class AutoCompleteDocument extends PlainDocument {

		boolean arrowKeyPressed = false;

		public AutoCompleteDocument() {
			textComponent.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();
					if (key == KeyEvent.VK_ENTER) {
						logger.debug("[key listener] enter key pressed");
						// there is no such element in the model for now
						String text = textComponent.getText();
						if (!model.data.contains(text)) {
							logger.debug("addToTop() called from keyPressed() - cancelled");
							// addToTop(text);
						}
					} else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
						// arrowKeyPressed = true;
						// logger.debug("arrow key pressed");
					}
				}
			});
		}

		void updateModel() throws BadLocationException {
			String textToMatch = getText(0, getLength());
			logger.debug("setPattern() called from updateModel()");
			setPattern(textToMatch);
		}

		@Override
		public void remove(int offs, int len) throws BadLocationException {

			if (modelFilling) {
				logger.debug("[remove] model is being filled now");
				return;
			}

			super.remove(offs, len);
			if (arrowKeyPressed) {
				arrowKeyPressed = false;
				logger.debug("[remove] arrow key was pressed, updateModel() was NOT called");
			} else {
				logger.debug("[remove] calling updateModel()");
				updateModel();
			}
			clearSelection();
		}

		@Override
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

			if (modelFilling) {
				logger.debug("[insert] model is being filled now");
				return;
			}

			// insert the String into the document
			super.insertString(offs, str, a);
			logger.debug("[insert-str] :" + str);
			// if (enterKeyPressed) {
			// logger.debug("[insertString] enter key was pressed");
			// enterKeyPressed = false;
			// return;
			// }

			String text = getText(0, getLength());

			// FIXME: orjinaline göre iptal edildi
			logger.debug("[insert-gettext] :" + text);
			if (arrowKeyPressed) {
				logger.debug("[insert] arrow key was pressed, updateModel() was NOT called");
				model.setSelectedItem(text);
				logger.debug(String.format("[insert] model.setSelectedItem(%s)", text));
				arrowKeyPressed = false;
			} else if (!text.equals(getSelectedItem())) {
				logger.debug("[insert] calling updateModel() - cancelled");
				updateModel();

			}

			clearSelection();
		}

	}

	public void setText(ComboItem text) {
		if (model.data.contains(text.getLabel())) {
			setSelectedItem(text);
		} else {
			addToTop(text);
			setSelectedIndex(0);
		}
	}

	public String getText() {
		return getEditor().getItem().toString();
	}

	private String previousPattern = null;

	private void setPattern(String pattern) {

		if (pattern != null && pattern.trim().isEmpty()) pattern = null;

		if (previousPattern == null && pattern == null || pattern != null && pattern.equals(previousPattern)) {
			logger.debug("[setPatter] pattern is the same as previous: " + previousPattern);
			return;
		}

		previousPattern = pattern;

		modelFilling = true;
		// logger.debug("setPattern(): start");

		model.setPattern(pattern);

		if (logger.isDebugEnabled()) {
			StringBuilder b = new StringBuilder(100);
			b.append("pattern filter '").append(pattern == null ? "null" : pattern).append("' set:\n");
			for (int i = 0; i < model.getSize(); i++) {
				b.append(", ").append('[').append(model.getElementAt(i)).append(']');
			}
			int ind = b.indexOf(", ");
			if (ind != -1) {
				b.delete(ind, ind + 2);
			}
			// b.append('\n');
			logger.debug(b);
		}
		// logger.debug("setPattern(): end");
		modelFilling = false;
		if (pattern != null) updatePopup = true;
	}

	private void clearSelection() {
		int i = getText().length();
		textComponent.setSelectionStart(i);
		textComponent.setSelectionEnd(i);
	}

	// @Override
	// public void setSelectedItem(Object anObject) {
	// super.setSelectedItem(anObject);
	// clearSelection();
	// }

	public synchronized void addToTop(ComboItem aString) {
		model.addToTop(aString);
	}

	@SuppressWarnings("rawtypes")
	private class Model extends AbstractListModel implements ComboBoxModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		// String pattern;
		Object selected;
		final String delimiter = ";;;";
		final int limit = 20;

		class Data {

			private List<ComboItem> list = new ArrayList<>(limit);
			private List<ComboItem> lowercase = new ArrayList<>(limit);
			private List<ComboItem> filtered;

			void add(ComboItem s) {
				list.add(s);
				s.setLabel(s.getValue().toLowerCase());
				lowercase.add(s);

			}

			void addToTop(ComboItem s) {
				list.add(0, s);
				s.setLabel(s.getValue().toLowerCase());
				lowercase.add(0, s);
			}

			void remove(int index) {
				list.remove(index);
				lowercase.remove(index);
			}

			List<ComboItem> getList() {
				return list;
			}

			List<ComboItem> getFiltered() {
				if (filtered == null) filtered = list;
				return filtered;
			}

			int size() {
				return list.size();
			}

			void setPattern(String pattern) {
				if (pattern == null || pattern.isEmpty()) {
					filtered = list;
					AutoCompleteCombo.this.setSelectedItem(model.getElementAt(0));
					logger.debug(String.format("[setPattern] combo.setSelectedItem(null)"));
				} else {
					filtered = new ArrayList<>(limit);
					pattern = pattern.toLowerCase();
					for (int i = 0; i < lowercase.size(); i++) {
						// case insensitive search
						if (lowercase.get(i).getLabel().contains(pattern)) {
							filtered.add(list.get(i));
						}
					}
					AutoCompleteCombo.this.setSelectedItem(pattern);
					logger.debug(String.format("[setPattern] combo.setSelectedItem(%s)", pattern));
				}
				logger.debug(String.format("pattern:'%s', filtered: %s", pattern, filtered));
			}

			boolean contains(String s) {
				if (s == null || s.trim().isEmpty()) return true;
				s = s.toLowerCase();
				for (ComboItem item : lowercase) {
					if (item.equals(s)) {
						return true;
					}
				}
				return false;
			}
		}

		Data data = new Data();

		void readData() {

			ComboItem comboItem1 = new ComboItem("Afghanistan", "Afghanistanvalue");
			ComboItem comboItem2 = new ComboItem("Afgan", "Afganvalue");
			ComboItem comboItem3 = new ComboItem("Albania", "AlbeniaValue");

			data.add(comboItem1);
			data.add(comboItem2);
			data.add(comboItem3);

		}

		boolean isThreadStarted = false;

		void writeData() {
			StringBuilder b = new StringBuilder(limit * 60);

			for (ComboItem url : data.getList()) {
				b.append(delimiter).append(url);
			}
			b.delete(0, delimiter.length());

			// waiting thread is already being run
			if (isThreadStarted) {
				return;
			}

			// we do saving in different thread
			// for optimization reasons (saving may take much time)
			new Thread(() -> {
				// we do sleep because saving operation
				// may occur more than one per waiting period
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ex) {
				}
				// we need this synchronization to
				// synchronize with AutoCompleteCombo.addElement method
				// (race condition may occur)
				synchronized (AutoCompleteCombo.this) {

					// HERE MUST BE SAVING OPERATION
					// (SAVING INTO FILE OR SOMETHING)
					// don't forget replace readData() method
					// to read saved data when creating bean

					isThreadStarted = false;
				}
			}).start();
			isThreadStarted = true;
		}

		public Model() {
			readData();
		}

		public void setPattern(String pattern) {

			int size1 = getSize();

			data.setPattern(pattern);

			int size2 = getSize();

			if (size1 < size2) {
				fireIntervalAdded(this, size1, size2 - 1);
				fireContentsChanged(this, 0, size1 - 1);
			} else if (size1 > size2) {
				fireIntervalRemoved(this, size2, size1 - 1);
				fireContentsChanged(this, 0, size2 - 1);
			}
		}

		public void addToTop(ComboItem aString) {
			if (aString == null || data.contains(aString.getLabel())) return;
			if (data.size() == 0) data.add(aString);
			else
				data.addToTop(aString);

			while (data.size() > limit) {
				int index = data.size() - 1;
				data.remove(index);
			}

			setPattern(null);
			model.setSelectedItem(aString);
			logger.debug(String.format("[addToTop] model.setSelectedItem(%s)", aString));

			// saving into options
			if (data.size() > 0) {
				writeData();
			}
		}

		@Override
		public Object getSelectedItem() {
			return selected;
		}

		@Override
		public void setSelectedItem(Object anObject) {
			if ((selected != null && !selected.equals(anObject)) || selected == null && anObject != null) {
				selected = anObject;
				fireContentsChanged(this, -1, -1);
			}
		}

		@Override
		public int getSize() {
			return data.getFiltered().size();
		}

		@Override
		public Object getElementAt(int index) {
			return data.getFiltered().get(index);
		}

	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {

			// Logger root = Logger.getRootLogger();
			// root.addAppender(new ConsoleAppender(new PatternLayout("%d{ISO8601} [%5p] %m at %l%n")));
			Logger root = Logger.getRootLogger();
			//root.addAppender(new ConsoleAppender(new PatternLayout("%d{ISO8601} %m at %L%n")));

			// BasicConfigurator.configure();

			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new GridLayout(3, 1));
			final JLabel label = new JLabel("label ");
			frame.add(label);
			final AutoCompleteCombo combo = new AutoCompleteCombo();
			// combo.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			//
			// @Override
			// public void keyReleased(KeyEvent e) {
			// if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			// String text = combo.getEditor().getItem().toString();
			// if(text.isEmpty())
			// return;
			// combo.addToTop(text);
			// }
			// }
			// });
			frame.add(combo);
			JComboBox combo2 = new JComboBox(new String[] { "Itema1", "Itema2", "Itemb3", "Itemb4" });
			combo2.setEditable(true);
			frame.add(combo2);
			frame.pack();
			frame.setSize(500, frame.getHeight());
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}

}
