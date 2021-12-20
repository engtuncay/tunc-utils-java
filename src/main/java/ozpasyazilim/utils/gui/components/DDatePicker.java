package ozpasyazilim.utils.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.ItemSelectable;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import ozpasyazilim.utils.gui.icons.IconManager;


/**
 * AviCenna calander combobox class.
 * @author
 * @since AC 2.0
 */
public class DDatePicker extends JFormattedTextField implements ItemSelectable
{
	///////////////////////////////////////////////////
	private Integer FirstDayOfWeek = null;

	// UI parameters
	private class UIParams
    {
		private Color borderColor;
		private int borderThickness;
		private Color colorBack;
		private Color colorFore;
		private Font font;
		private Dimension dimension;

        public UIParams(Color borderColor, int borderThickness, Color colorBack, Color colorFore, Font font, Dimension dimension)
        {
			this.borderColor = borderColor;
			this.borderThickness = borderThickness;
			this.colorBack = colorBack;
			this.colorFore = colorFore;
			this.font = font;
			this.dimension = dimension;
		}
		public Color getColorBack() {return colorBack;}
		public Color getColorFore() {return colorFore;}
		public Dimension getDimension() {return dimension;}
		public Font getFont() {return font;}
		public void setColorBack(Color color) {colorBack = color;}
		public void setColorFore(Color color) {colorFore = color;}
		public void setDimension(Dimension dimension) {this.dimension = dimension;}
		public void setFont(Font font) {this.font = font;}
		public int getBorderThickness() {return borderThickness;}
		public void setBorderThickness(int i) {borderThickness = i;}
		public Color getBorderColor() {return borderColor;}
		public void setBorderColor(Color color) {borderColor = color;}
	}//UIParams

	private static final int CELLWIDTH = 28;
	private static final int CELLHEIGHT = 21;

	// set default values - week day
	private UIParams paramsWeekDay = new UIParams
    (
		new Color(32, 60, 134),
		0,
		new Color(255, 255, 255),
		new Color(32, 60, 134),
		new Font("Dialog", Font.PLAIN, 12),
		new Dimension(CELLWIDTH, CELLHEIGHT)
	);

	// set default values - week end
	private UIParams paramsWeekEnd = new UIParams
    (
		new Color(32, 60, 134),
		0,
		new Color(190, 204, 239),
		new Color(32, 60, 134),
		new Font("Dialog", Font.PLAIN, 12),
		new Dimension(CELLWIDTH, CELLHEIGHT)
	);

	// set default values - selected day
	private UIParams paramsSelected = new UIParams
    (
		new Color(32, 60, 134),
		0,
		new Color(32, 60, 134),
		new Color(255, 255, 255),
		new Font("Dialog", Font.PLAIN, 12),
		new Dimension(CELLWIDTH, CELLHEIGHT)
	);

	// set default values - week
	private UIParams paramsWeek = new UIParams
    (
		new Color(32, 60, 134),
		0,
		new Color(54, 97, 205),
		new Color(190, 204, 239),
		new Font("Dialog", Font.PLAIN, 12),
		new Dimension(CELLWIDTH, CELLHEIGHT)
	);

    // set default values - empty day
	private UIParams paramsEmpty = new UIParams
    (
		new Color(32, 60, 134),
		0,
		new Color(192, 192, 192),
		new Color(128, 128, 128),
		new Font("Dialog", Font.PLAIN, 12),
		new Dimension(CELLWIDTH, CELLHEIGHT)
	);

    // set default values - week day labels
	private UIParams paramsLabelDay = new UIParams
    (
		new Color(32, 60, 134),
		0,
		new Color(122, 150, 223),
		new Color(190, 204, 239),
		new Font("Dialog", Font.PLAIN, 12),
		new Dimension(CELLWIDTH, CELLHEIGHT)
	);

    // set default values - month
	private UIParams paramsMonth = new UIParams
    (
		new Color(54, 97, 205),
		1,
		Color.white,
		Color.black,
		new Font("Dialog", Font.PLAIN, 12),
		new Dimension(85, 22)
	);

    // set default values - empty day
	private UIParams paramsYear = new UIParams
    (
		new Color(54, 97, 205),
		1,
		Color.white,
		Color.black,
		new Font("Dialog", Font.PLAIN, 12),
		new Dimension(55, 22)
	);

	private Rectangle iconRectangle = null;
        private Rectangle clickRectangle = null;
        private Color clickColor = SystemColor.LIGHT_GRAY;

	//////////////////////////////////////////////////////////////////
	private static class BorderComponent extends JComponent
    {
		private Dimension preferredSize = null;
		public void paint(Graphics g)
        {
			Graphics2D g2d = (Graphics2D)g;
			Color col = g2d.getColor();
			g2d.setColor(getBackground());
			g2d.fillRect(0, 0, getWidth(), getHeight());
			g2d.setColor(col);
		}
		public Dimension getPreferredSize() {return preferredSize;}
		public void setPreferredSize(Dimension dimension) {preferredSize = dimension;}
		public Dimension getSize() {return preferredSize;}
		public void setSize(Dimension dimension) {preferredSize = dimension;}
	}
	//////////////////////////////////////////////////////////////////

	private String dateformat = "dd.MM.yyyy";

	private JPopupMenu popup = null;

	private JLabel selectedLabel = null;

	private int selectedMonth = 0;

	private int selectedYear = 0;

	private long time = 0;

	private LinkedList dayList = new LinkedList();

	private LinkedList weekList = new LinkedList();

	private int maxYear = 2200;

	private int minYear = 1800;

	private boolean monthString = true;

	private class EventHandler implements MouseListener, MouseMotionListener, AncestorListener
    {
	    //MouseListener
		public void mouseClicked(MouseEvent e) {
		//    DDatePicker.this.eventMouseClicked(e);

		}
		public void mousePressed(MouseEvent e) {clickColor=SystemColor.GRAY; repaint();}
		public void mouseReleased(MouseEvent e) {clickColor=SystemColor.LIGHT_GRAY; repaint();DDatePicker.this.eventMouseClicked(e);}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	    //MouseMotionListener
        public void mouseDragged(MouseEvent e) {}
        public void mouseMoved(MouseEvent e) {
		    DDatePicker.this.eventMouseMoved(e);
        }
	    //AncestorListener
		public void ancestorAdded(AncestorEvent event) {
		    DDatePicker.this.eventAncestorAdded(event);
		}
		public void ancestorRemoved(AncestorEvent event) {
		    DDatePicker.this.eventAncestorRemoved(event);
		}
		public void ancestorMoved(AncestorEvent event) {
		    DDatePicker.this.eventAncestorMoved(event);
		}
	}//EventHandler

	private EventHandler eventHandler = new EventHandler();

	private static class LabelDay
    {
		int x;
		int y;
		JLabel label;
		boolean bEnabled;
		public LabelDay (int x, int y, JLabel label, boolean bEnabled)
		{
			this.x = x;
			this.y = y;
			this.label = label;
			this.bEnabled = bEnabled;
		}
		public LabelDay() {}
	}//LabelDay

	private class SpinnerEvent implements ChangeListener
    {
		public void stateChanged(ChangeEvent e) {
			Object source = e.getSource();
			if(source instanceof SpinnerListModel)
            {
				SpinnerListModel model = (SpinnerListModel)source;
				selectedMonth = model.getList().indexOf(model.getValue());
			}
			else if(source instanceof SpinnerNumberModel)
            {
				SpinnerNumberModel model = (SpinnerNumberModel)source;
				selectedYear = ((Number)model.getValue()).intValue();
			}
			refreshCalendar();
		}
	}//SpinnerEvent
	private SpinnerEvent spinnerEvent = new SpinnerEvent();

	private class ActionUP extends AbstractAction
    {
		public void actionPerformed(ActionEvent e)
        {
		    if(!isPopup())
		        return;
			Point p = getLocation(selectedLabel);
			p.y--;
			shiftSelection(p);
		}
	}//ActionUP
	private Action actionUP = new ActionUP();

	private class ActionDOWN extends AbstractAction
    {
		public void actionPerformed(ActionEvent e)
        {
//		    if(!isPopup())
//		        return;
			if(e.getSource()==DDatePicker.this && (popup==null || !popup.isVisible()))
            {
				showPopup();
				return;
			}
			Point p = getLocation(selectedLabel);
			p.y++;
			shiftSelection(p);
		}
	}//ActionDOWN
	private Action actionDOWN = new ActionDOWN();

	private class ActionLEFT extends AbstractAction
    {
		public void actionPerformed(ActionEvent e)
        {
		    if(!isPopup())
		        return;
			Point p = getLocation(selectedLabel);
			p.x--;
			shiftSelection(p);
		}
	}//ActionLEFT
	private Action actionLEFT = new ActionLEFT();

	private class ActionRIGHT extends AbstractAction
    {
		public void actionPerformed(ActionEvent e)
        {
		    if(!isPopup())
		        return;
			Point p = getLocation(selectedLabel);
			p.x++;
			shiftSelection(p);
		}
	}//ActionRIGHT
	private Action actionRIGHT = new ActionRIGHT();

	private class ActionENTER extends AbstractAction
    {
		public void actionPerformed(ActionEvent e)
        {
		    if(!isPopup())
		        fireActionPerformed();
		    else
            {
				hidePopup();
				makeSelection(selectedLabel);
		    }
		}
	}//ActionENTER
	private Action actionENTER = new ActionENTER();

	private class ActionESCAPE extends AbstractAction
    {
		public void actionPerformed(ActionEvent e)
        {
		    if(!isPopup())
		        return;
			hidePopup();
		}
	}//ActionESCAPE
	private Action actionESCAPE = new ActionESCAPE();

	private class ActionSPACE extends AbstractAction
    {
		public void actionPerformed(ActionEvent e)
        {
		    if(!isPopup())
		        return;
			if(e.getSource()==DDatePicker.this && (popup==null || !popup.isVisible()))
            {
				showPopup();
				return;
			}
			hidePopup();
			makeSelection(selectedLabel);
		}
	}//ActionSPACE
	private Action actionSPACE = new ActionSPACE();

    private class ACDateFormat extends SimpleDateFormat
    {
        public ACDateFormat(String pattern)
        {
            super(pattern);
        }
        public Object parseObject(String source) throws ParseException {
            if(source == null || "".equals(source))
            {
                //setValue(null);
                return null;
            }
            return super.parseObject(source);
        }
    }

    /**
     * Default constructor.
     */
    public DDatePicker()
    {
        this(new Date());
    }

    /**
     * Value constructor.
     */
    public DDatePicker(Date value)
    {
        super();
        DateFormatter defaultDateFormatter = new DateFormatter(new ACDateFormat(dateformat));
        DateFormatter displayDateFormatter = new DateFormatter(new ACDateFormat(dateformat));
        DateFormatter editDateFormatter = new DateFormatter(new ACDateFormat(dateformat));
        editDateFormatter.setOverwriteMode(true);
        DefaultFormatterFactory factory = new DefaultFormatterFactory(defaultDateFormatter, displayDateFormatter, editDateFormatter);
        setFormatterFactory(factory);

        //setFocusLostBehavior(JFormattedTextField.PERSIST);

        setValue(value);
        setColumns(dateformat.length());
        installListeners();
    }

    private void installListeners()
    {
        addAncestorListener(eventHandler);
        addMouseListener(eventHandler);
        addMouseMotionListener(eventHandler);
        registerKeyboardActions(this, JComponent.WHEN_FOCUSED);
    //	registerKeyboardActions(popup, JComponent.WHEN_IN_FOCUSED_WINDOW);
    //	registerKeyboardActions(popup, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * @see javax.swing.event.AncestorListener#ancestorMoved(javax.swing.event.AncestorEvent)
     */
    public void eventAncestorMoved(AncestorEvent event)
    {
        hidePopup();
    }

    /**
     * @see javax.swing.event.AncestorListener#ancestorRemoved(javax.swing.event.AncestorEvent)
     */
    public void eventAncestorRemoved(AncestorEvent event)
    {
        hidePopup();
    }

    /**
     * @see javax.swing.event.AncestorListener#ancestorAdded(javax.swing.event.AncestorEvent)
     */
    public void eventAncestorAdded(AncestorEvent event)
    {
        hidePopup();
    }

    /**
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    private void eventMouseMoved(MouseEvent e)
    {
    //    if(iconRectangle == null)
        if(clickRectangle == null)
            return;
       // if(iconRectangle.contains(e.getPoint()))
       // if(clickRectangle.contains(e.getPoint()))
        if(clickRectangle.getX() <=e.getX())
           setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        else
            setCursor(new Cursor(Cursor.TEXT_CURSOR));

    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    private void eventMouseClicked(MouseEvent e)
    {
        if(!isEnabled() || !isEditable())
            return;
        try
        {
            Object source = e.getSource();
            if(source==this)
            {
               // if(iconRectangle.contains(e.getPoint()))

                  if(clickRectangle==null || clickRectangle.contains(e.getPoint()))
                  {

                    eventMouseClickedTextField(e);
                  }

            }
            else if(source instanceof JLabel)
                eventMouseClickedDay(e);
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
    }

    /*
     * Textfield clicked.
     */
    private void eventMouseClickedTextField(MouseEvent e)
    {
        if(!SwingUtilities.isLeftMouseButton(e))
            return;
        if(e.getClickCount()>1) {
            // double clicked
        }
        else {
            // single clicked
        }

      //  if(System.currentTimeMillis()-time<120)

       //    return;
        showPopup();
    }

    /**
     * If one of the labels contained in popup clicked this method activated.
     * @param e java.awt.event.MouseEvent
     */
    private void eventMouseClickedDay(MouseEvent e)
    {
      if(!SwingUtilities.isLeftMouseButton(e))
          return;
      JLabel label = (JLabel)e.getSource();
      if(!label.isEnabled())
          return;
      makeSelection(label);
      hidePopup();
        /*if(e.getClickCount() > 1)
        {
            // double clicked
            makeSelection(label);
            hidePopup();
        }
        else
        {
            getViewNode().requestFocusInWindow();
            // single clicked
            shiftSelection(label);
        }*/
    }

    /*
     * Shifts the selection to given label.
     */
    private void shiftSelection(JLabel label)
    {
        Point p = getLocation(label);
        shiftSelection(getDay(p.x, p.y));
    }

    /*
     * Shifts the selection given day label.
     */
    private void shiftSelection(LabelDay labelDay)
    {
        if(labelDay==null)
            return;
        if(labelDay.bEnabled)
        {
            LabelDay labelDaySelected = getDay(selectedLabel);
            if(labelDaySelected.bEnabled)
            {
                if(labelDaySelected.x<5)
                    updateUI(selectedLabel, paramsWeekDay);
                else
                    updateUI(selectedLabel, paramsWeekEnd);
            }
            else
                updateUI(selectedLabel, paramsEmpty);
            updateUI(labelDay.label, paramsSelected);
            //labelDay.label.setBorder(getSelectedBorder());
            selectedLabel = labelDay.label;
        }
    }

    /*
     * Shifts the selection to the label at the given point.
     */
    private void shiftSelection(Point p)
    {
        LabelDay labelDay = getDay(p.x, p.y);
        if(labelDay!=null && labelDay.bEnabled)
            shiftSelection(labelDay.label);
    }

    /*
     * Returns the location of the given label.
     */
    private Point getLocation(JLabel label)
    {
        Point p = new Point(-1, -1);
        if(label!=null)
        {
            String s = label.getName();
            if(s==null)
                return p;
            p.x = Integer.parseInt(s.substring(0, s.indexOf(',')));
            p.y = Integer.parseInt(s.substring(s.indexOf(',')+1));
        }
        return p;
    }

    /*
     * Makes selection.
     */
    private void makeSelection(JLabel label)
    {
        if(label==null)
            return;
        Date oldValue = getDate();
        String day = label.getText();
        Calendar cal = getCalendar();
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        cal.set(Calendar.MONTH, selectedMonth);
        cal.set(Calendar.YEAR, selectedYear);
        Date newValue = cal.getTime();
        setValue(newValue);
        if(oldValue != null)
            fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, oldValue, ItemEvent.DESELECTED));
        if(newValue != null)
            fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, newValue, ItemEvent.SELECTED));
        fireActionPerformed();
    }

    /*
     * Creates the popup of the combobox.
     */
    private JPopupMenu createPopup()
    {
        if(popup==null)
        {
            popup = new JPopupMenu() {
                public void setVisible(boolean b)
                {
                    if(!b)
                        time = System.currentTimeMillis();
                    super.setVisible(b);
                }
            };
            registerKeyboardActions(popup, JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
        popup.removeAll();
        return popup;
    }

    private boolean isPopup()
    {
        if(popup == null)
            return false;
        return popup.isVisible();
    }

    /*
     * Hides the popup.
     */
    private void hidePopup()
    {
        if(popup != null) {
            popup.setVisible(false);
            //popup.transferFocus();
            requestFocus();
        }
    }

    /*
     * Shows the popup.
     */
    private void showPopup()
    {
    //	if(!isCalendarEditable())
    //		return;
        popup = createPopup();
        popup.add(createCalendarPanel());
        popup.pack();
        popup.show(this, 0, getHeight());
    }

    /*
     * Registers the important keyboard actions.
     */
    private void registerKeyboardActions(JComponent component, int condition)
    {
        component.getInputMap(condition).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "CalendarUP");
        component.getInputMap(condition).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "CalendarDOWN");
        component.getInputMap(condition).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "CalendarLEFT");
        component.getInputMap(condition).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "CalendarRIGHT");
        component.getInputMap(condition).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "CalendarENTER");
        component.getInputMap(condition).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CalendarESCAPE");
        component.getInputMap(condition).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "CalendarSPACE");

        component.getActionMap().put("CalendarUP", actionUP);
        component.getActionMap().put("CalendarDOWN", actionDOWN);
        component.getActionMap().put("CalendarLEFT", actionLEFT);
        component.getActionMap().put("CalendarRIGHT", actionRIGHT);
        component.getActionMap().put("CalendarENTER", actionENTER);
        component.getActionMap().put("CalendarESCAPE", actionESCAPE);
        component.getActionMap().put("CalendarSPACE", actionSPACE);
    }

    /*
     * Creates a view to be view on the popup of the combobox.
     */
    private JPanel createCalendarPanel()
    {
        System.gc();
        //
        Calendar cal = getCalendar();
        //
        JPanel panelCalendar = new JPanel();
        panelCalendar.setLayout(new BorderLayout(0,0));
        panelCalendar.setBorder(null);

        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        panelTop.setBorder(null);
        panelTop.setPreferredSize (
            new Dimension(
                4 + paramsMonth.getDimension().width + paramsYear.getDimension().width,
                4 + Math.max(paramsMonth.getDimension().height, paramsYear.getDimension().height)
            )
        );
        panelCalendar.add(panelTop, BorderLayout.NORTH);

        ////////////////////////////////////////////////////////////
        //
        selectedMonth = cal.get(Calendar.MONTH);
        selectedYear = cal.get(Calendar.YEAR);
        ////////////////////////////////////////////////////////////
        // Month
        SpinnerListModel modelMonth = new SpinnerListModel();
        LinkedList listMonth = new LinkedList();
        String[] months = new DateFormatSymbols().getMonths();
        if(isShowMonthStrings())
        {
            for(int i=0; i<months.length-1; i++)
                listMonth.add(months[i]);
            modelMonth.setList(listMonth);
            modelMonth.setValue(months[selectedMonth]);
        }
        else
        {
            for(int i=0; i<months.length-1; i++)
                listMonth.add(String.valueOf(i+1));
            modelMonth.setList(listMonth);
            modelMonth.setValue(String.valueOf(selectedMonth+1));
        }
        modelMonth.addChangeListener(spinnerEvent);

        JSpinner spinnerMonth = new JSpinner(modelMonth);

        JFormattedTextField textFieldMonth = ((JSpinner.ListEditor)spinnerMonth.getEditor()).getTextField();
        textFieldMonth.setEditable(false);
        //updateUI(spinnerMonth, paramsMonth);
        updateUI(textFieldMonth, paramsMonth);
        spinnerMonth.setBorder (
            BorderFactory.createLineBorder(
                paramsMonth.getBorderColor(),
                paramsMonth.getBorderThickness()
            )
        );
        spinnerMonth.setPreferredSize(getMonthDimension());

        //JLabel exitLabel=new JLabel("X");
         JLabel exitLabel=new JLabel("");
         exitLabel.setIcon(IconManager.getRefuseImage());
        exitLabel.addMouseListener(new MouseListener()
        {
          public void mousePressed(MouseEvent e)
          {
            hidePopup();
          }

          public void mouseClicked(MouseEvent e)
          {
          }

          public void mouseReleased(MouseEvent e)
          {
          }

          public void mouseEntered(MouseEvent e)
          {
          }

          public void mouseExited(MouseEvent e)
          {
          }
        });
        exitLabel.setPreferredSize(new Dimension(50,20));
        panelTop.add(exitLabel);
        panelTop.add(spinnerMonth);

        ////////////////////////////////////////////////////////////
        // Year
        SpinnerNumberModel modelYear = new SpinnerNumberModel();
        modelYear.setValue(new Long(cal.get(Calendar.YEAR)));
        modelYear.setMinimum(new Long(getMinYear()));
        modelYear.setMaximum(new Long(getMaxYear()));
        modelYear.addChangeListener(spinnerEvent);

        JSpinner spinnerYear = new JSpinner(modelYear);
        JFormattedTextField.AbstractFormatterFactory ff = new JFormattedTextField.AbstractFormatterFactory()
        {
            private NumberFormatter nf = null;
            public AbstractFormatter getFormatter(JFormattedTextField tf)
            {
                if(nf == null)
                {
                    DecimalFormat df = new DecimalFormat();
                    df.setGroupingUsed(false);
                    nf = new NumberFormatter();
                    nf.setFormat(df);
                }
                return nf;
            }
        };

        JFormattedTextField textFieldYear = ((JSpinner.NumberEditor)spinnerYear.getEditor()).getTextField();
        textFieldYear.setFormatterFactory(ff);
        textFieldYear.setEditable(false);
        //updateUI(spinnerYear, paramsYear);
        updateUI(textFieldYear, paramsYear);
        spinnerYear.setBorder (
            BorderFactory.createLineBorder(
                paramsYear.getBorderColor(),
                paramsYear.getBorderThickness()
            )
        );
        spinnerYear.setPreferredSize(getYearDimension());
        panelTop.add(spinnerYear);

        ////////////////////////////////////////////////////////////

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelBottom.setBorder(null);
        panelCalendar.add(panelBottom, BorderLayout.SOUTH);

        ////////////////////////////////////////////////////////////
        addBorderHorizontal(panelBottom);
        addBorderVertical(panelBottom);
        //
        JLabel lbl = new JLabel();
        lbl.setText("");
        updateUI(lbl, paramsLabelDay);
        panelBottom.add(lbl);
        // Weekdays
        String[] days = new DateFormatSymbols().getShortWeekdays();
        for(int i=0; i<7; i++)
        {
            //
            addBorderVertical(panelBottom);
            //
            int index = getFirstDayOfWeek() + i;
            JLabel label = new JLabel();
            label.setText( days[ index<8 ? index : ((index-1)%7)+1 ]);
            label.setHorizontalAlignment(JLabel.CENTER);
            updateUI(label, paramsLabelDay);
            panelBottom.add(label);
        }
        addBorderVertical(panelBottom);

        ////////////////////////////////////////////////////////////
        // Days
        dayList.clear();
        weekList.clear();
        for(int i=0; i<42; i++)
        {
            if(i%7==0) {
                addBorderHorizontal(panelBottom);
                addBorderVertical(panelBottom);
                panelBottom.add(createWeek());
                addBorderVertical(panelBottom);
            }
            //
            panelBottom.add(createDay());
            addBorderVertical(panelBottom);
        }
        addBorderHorizontal(panelBottom);
        int index = 7 - getDaysInFirstWeek(cal) + cal.get(Calendar.DAY_OF_MONTH) -1;
        LabelDay selected = getDay(index % 7, index / 7);
        selectedLabel = selected.label;
        selectedLabel.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        refreshCalendar();
        ////////////////////////////////////////////////////////////
        int thickness = paramsWeekDay.borderThickness;
        panelBottom.setPreferredSize (
            new Dimension(
                (9*thickness) + (8*paramsWeekDay.getDimension().width),
                (8*thickness) + (7*paramsWeekDay.getDimension().height)
            )
        );
        ///////////////////////////////
        panelCalendar.setPreferredSize (
            new Dimension(
                Math.max(panelTop.getPreferredSize().width, panelBottom.getPreferredSize().width),
                panelTop.getPreferredSize().height + panelBottom.getPreferredSize().height
            )
        );
        ///////////////////////////////
        // Draw borders
        ///////////////////////////////
        return panelCalendar;
    }

    /*
     * Initializes the given component with user interface values.
     */
    private void updateUI(JComponent component, UIParams uiParams)
    {
        component.setOpaque(true);
        component.setBackground(uiParams.getColorBack());
        component.setForeground(uiParams.getColorFore());
        component.setFont(uiParams.getFont());
        component.setPreferredSize(uiParams.getDimension());
        component.setSize(uiParams.getDimension());
    }

    /*
     * Returns the days of first week of the month of given calender.
     */
    private int getDaysInFirstWeek(Calendar cal)
    {
        int tt = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int firstDay = cal.getFirstDayOfWeek();
        int count = 7;
        for(int i=0; i<7; i++)
        {
            if(day == firstDay)
            {
                cal.set(Calendar.DAY_OF_MONTH, tt);
                return count;
            }
            else
            {
                count--;
                day--;
                if(day<1)
                    day=7;
            }
        }
        // never comes here ( I hope! :)
        cal.set(Calendar.DAY_OF_MONTH, tt);
        return 0;
    }

    /*
     * Refreshes the calendar view.
     * Happens when month or year spinners chaned.
     */
    private void refreshCalendar()
    {
        int nToday = Integer.parseInt(selectedLabel.getText());
        Calendar cal = getCalendar();
        cal.set(Calendar.MONTH, selectedMonth);
        cal.set(Calendar.YEAR, selectedYear);
        //
        Date dPrev = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        int nPrev = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.setTime(dPrev);
        //
        int n = getDaysInFirstWeek(cal);
        int x=0, y=0;
        for(int i=0; i<7-n; i++) {
            LabelDay labelDay = getDay(x, y);
            labelDay.label.setText(String.valueOf(nPrev+n-7+i+1));
            labelDay.bEnabled = false;
            updateUI(labelDay.label, paramsEmpty);
            x++;
            if(x>6) {
                x = 0;
                y++;
            }
        }
        int nDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i=0; i<nDay; i++)
        {
            LabelDay labelDay = getDay(x, y);
            labelDay.label.setText(String.valueOf(i+1));
            labelDay.bEnabled = true;
            if(x<5)
                updateUI(labelDay.label, paramsWeekDay);
            else
                updateUI(labelDay.label, paramsWeekEnd);
            x++;
            if(x>6) {
                x = 0;
                y++;
            }
        }
        int nExcess = 42 - ((7-n) + nDay);
        for(int i=0; i<nExcess; i++)
        {
            LabelDay labelDay = getDay(x, y);
            labelDay.label.setText(String.valueOf(i+1));
            labelDay.bEnabled = false;
            updateUI(labelDay.label, paramsEmpty);
            x++;
            if(x>6) {
                x = 0;
                y++;
            }
        }
        // update the selected cell
        nToday = Math.min(nToday, nDay);
        int index = 7 - n + nToday -1;
        LabelDay selected = getDay(index % 7, index / 7);
        selectedLabel = selected.label;
        shiftSelection(selectedLabel);
        // update the week numbers
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int nWeek = cal.get(Calendar.WEEK_OF_YEAR);
        for(int i=0; i<6; i++)
        {
            JLabel labelWeek = (JLabel)weekList.get(i);
            labelWeek.setText(String.valueOf(nWeek++));
        }
    }

    /*
     * Adds the given label to day list.
     */
    private LabelDay addDay(JLabel label)
    {
        LinkedList list = null;
        if(dayList.size()<1)
        {
            list = new LinkedList();
            dayList.add(list);
        }
        else
        {
            list = (LinkedList)dayList.getLast();
            if(list.size()==7) {
                list = new LinkedList();
                dayList.add(list);
            }
        }
        LabelDay labelDay = new LabelDay (
            list.size(),
            dayList.indexOf(list),
            label,
            false
        );
        label.setName(labelDay.x+","+labelDay.y);
        list.add(labelDay);
        return labelDay;
    }

    /*
     * Returns the day label at the given coordinate.
     */
    private LabelDay getDay(int x, int y)
    {
        if(y<0 || dayList.size()<=y)
            return null;
        LinkedList list = (LinkedList)dayList.get(y);
        if(x<0 || list.size()<=x)
            return null;
        return (LabelDay)list.get(x);
    }

    /*
     * Returns the day label of the given label.
     */
    private LabelDay getDay(JLabel label)
    {
        Point p = getLocation(label);
        return getDay(p.x, p.y);
    }

    /**
     * Returns a calendar instance of current date.
     * @return java.util.Calendar
     */
    public Calendar getCalendar()
    {
        Date date = getDate();
        if(date == null)
            date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(getFirstDayOfWeek());
        return cal;
    }

    /*
     * Adds the day names header to given view.
     */
    private void addBorderVertical(JPanel panel)
    {
        int thickness = paramsWeekDay.borderThickness;
        if(thickness<1)
            return;
        Dimension dim = paramsWeekDay.getDimension();
        BorderComponent border = new BorderComponent();
        border.setPreferredSize(new Dimension(thickness, dim.height));
        border.setBackground(paramsWeekDay.borderColor);
        panel.add(border);
    }

    /*
     * Adds the week number left header to given view.
     */
    private void addBorderHorizontal(JPanel panel)
    {
        int thickness = paramsWeekDay.borderThickness;
        if(thickness<1)
            return;
        Dimension dim = paramsWeekDay.getDimension();
        BorderComponent border = new BorderComponent();
        border.setPreferredSize(new Dimension((9 * thickness) + (8 * dim.width),thickness));
        border.setBackground(paramsWeekDay.borderColor);
        panel.add(border);
    }

    /*
     * Creates a label to be used as a day number.
     */
    private JLabel createDay()
    {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.addMouseListener(eventHandler);
        addDay(label);
        return label;
    }

    /*
     * Creates a label to be used as week number.
     */
    private JLabel createWeek()
    {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        label.addMouseListener(eventHandler);
        updateUI(label, paramsWeek);
        weekList.add(label);
        return label;
    }

    /**
     * Returns the value.
     * @return java.util.Date
     */
    public Date getDate()
    {
        return (Date)getValue();
    }

    /**
     * Sets the value.
     * @param date java.util.Date
     */
    public void setDate(Date date)
    {
        setValue(date);
    }

    public void setTime(Long time)
    {
      setValue(new Date(time));
    }

    /**
     * Returns the date as calendar.
     * @return java.util.Calendar
     */
    public Calendar getDateAsCalendar()
    {
        Date date = getDate();
        if(date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * Sets the date as calendar.
     * @param datecreated java.util.Calendar
     */
    public void setDateAsCalendar(Calendar cal)
    {
        if(cal == null)
            setDate(null);
        else
            setDate(cal.getTime());
    }

    /**
     * Returns the maximum year value.
     * @return int
     */
    public int getMaxYear()
    {
        return maxYear;
    }

    /**
     * Sets the maximum year value.
     * @param year
     */
    public void setMaxYear(int year)
    {
        maxYear = year;
    }

    /**
     * Returns the minimum year value.
     * @return int
     */
    public int getMinYear()
    {
        return minYear;
    }

    /**
     * Sets the minimum year value.
     * @param year
     */
    public void setMinYear(int year)
    {
        minYear = year;
    }

    /**
     * Sets cell dimension for all kinds of cells.
     * @param dimension java.awt.Dimension
     */
    public void setCellDimension(Dimension dimension)
    {
        paramsWeekDay.setDimension(dimension);
        paramsWeekEnd.setDimension(dimension);
        paramsSelected.setDimension(dimension);
        paramsEmpty.setDimension(dimension);
        paramsWeek.setDimension(dimension);
        paramsLabelDay.setDimension(dimension);
    }

    /**
     * Returns the dimension of all kinds of cells.
     * @return java.awt.Dimension
     */
    public Dimension getCellDimension()
    {
        return paramsWeekDay.getDimension();
    }

    /**
     * Sets cell border thickness for all kinds of cells.
     * @param thickness int
     */
    public void setCellBorderThickness(int thickness)
    {
        paramsWeekDay.setBorderThickness(thickness);
        paramsWeekEnd.setBorderThickness(thickness);
        paramsSelected.setBorderThickness(thickness);
        paramsEmpty.setBorderThickness(thickness);
        paramsWeek.setBorderThickness(thickness);
        paramsLabelDay.setBorderThickness(thickness);
    }

    /**
     * Returns the cell border thickness.
     * @return int
     */
    public int getCellBorderThickness()
    {
        return paramsWeekDay.getBorderThickness();
    }

    /**
     * Sets cell border color for all kinds of cells.
     * @param borderColor java.awt.Color
     */
    public void setCellBorderColor(Color borderColor)
    {
        paramsWeekDay.setBorderColor(borderColor);
        paramsWeekEnd.setBorderColor(borderColor);
        paramsSelected.setBorderColor(borderColor);
        paramsEmpty.setBorderColor(borderColor);
        paramsWeek.setBorderColor(borderColor);
        paramsLabelDay.setBorderColor(borderColor);
    }

    /**
     * Returns the color of cell borders.
     * @return java.awt.Color
     */
    public Color getCellBorderColor()
    {
        return paramsWeekDay.getBorderColor();
    }

    //////////////////////////////////////////////////////////////////
    // week day
    public Color getWeekDayBackground()
    {
        return paramsWeekDay.getColorBack();
    }
    public Color getWeekDayForeground()
    {
        return paramsWeekDay.getColorFore();
    }
    public Font getWeekDayFont()
    {
        return paramsWeekDay.getFont();
    }
    public void setWeekDayBackground(Color color)
    {
        paramsWeekDay.setColorBack(color);
    }
    public void setWeekDayForeground(Color color)
    {
        paramsWeekDay.setColorFore(color);
    }
    public void setWeekDayFont(Font font)
    {
        paramsWeekDay.setFont(font);
    }

    //////////////////////////////////////////////////////////////////
    // week end
    public Color getWeekEndBackground()
    {
        return paramsWeekEnd.getColorBack();
    }
    public Color getWeekEndForeground()
    {
        return paramsWeekEnd.getColorFore();
    }
    public Font getWeekEndFont()
    {
        return paramsWeekEnd.getFont();
    }
    public void setWeekEndBackground(Color color)
    {
        paramsWeekEnd.setColorBack(color);
    }
    public void setWeekEndForeground(Color color)
    {
        paramsWeekEnd.setColorFore(color);
    }
    public void setWeekEndFont(Font font)
    {
        paramsWeekEnd.setFont(font);
    }

    //////////////////////////////////////////////////////////////////
    // selected cell
    public Color getSelectedBackground()
    {
        return paramsSelected.getColorBack();
    }
    public Color getSelectedForeground()
    {
        return paramsSelected.getColorFore();
    }
    public Font getSelectedFont()
    {
        return paramsSelected.getFont();
    }
    public void setSelectedBackground(Color color)
    {
        paramsSelected.setColorBack(color);
    }
    public void setSelectedForeground(Color color)
    {
        paramsSelected.setColorFore(color);
    }
    public void setSelectedFont(Font font)
    {
        paramsSelected.setFont(font);
    }

    //////////////////////////////////////////////////////////////////
    // empty(disabled) cell
    public Color getEmptyBackground()
    {
        return paramsEmpty.getColorBack();
    }
    public Color getEmptyForeground()
    {
        return paramsEmpty.getColorFore();
    }
    public Font getEmptyFont()
    {
        return paramsEmpty.getFont();
    }
    public void setEmptyBackground(Color color)
    {
        paramsEmpty.setColorBack(color);
    }
    public void setEmptyForeground(Color color)
    {
        paramsEmpty.setColorFore(color);
    }
    public void setEmptyFont(Font font)
    {
        paramsEmpty.setFont(font);
    }

    public Color getDayLabelBackground()
    {
        return paramsLabelDay.getColorBack();
    }
    public Color getDayLabelForeground()
    {
        return paramsLabelDay.getColorFore();
    }
    public Font getDayLabelFont()
    {
        return paramsLabelDay.getFont();
    }
    public void setDayLabelBackground(Color color)
    {
        paramsLabelDay.setColorBack(color);
    }
    public void setDayLabelForeground(Color color)
    {
        paramsLabelDay.setColorFore(color);
    }
    public void setDayLabelFont(Font font)
    {
        paramsLabelDay.setFont(font);
    }

    public Color getMonthBackground()
    {
        return paramsMonth.getColorBack();
    }
    public Color getMonthForeground()
    {
        return paramsMonth.getColorFore();
    }
    public Dimension getMonthDimension()
    {
        return paramsMonth.getDimension();
    }
    public Font getMonthFont()
    {
        return paramsMonth.getFont();
    }
    public void setMonthBackground(Color color)
    {
        paramsMonth.setColorBack(color);
    }
    public void setMonthForeground(Color color)
    {
        paramsMonth.setColorFore(color);
    }
    public void setMonthCellDimension(Dimension dimension)
    {
        paramsMonth.setDimension(dimension);
    }
    public void setMonthFont(Font font)
    {
        paramsMonth.setFont(font);
    }

    public Color getYearBackground()
    {
        return paramsYear.getColorBack();
    }
    public Color getYearForeground()
    {
        return paramsYear.getColorFore();
    }
    public Dimension getYearDimension()
    {
        return paramsYear.getDimension();
    }
    public Font getYearFont()
    {
        return paramsYear.getFont();
    }
    public void setYearBackground(Color color)
    {
        paramsYear.setColorBack(color);
    }
    public void setYearForeground(Color color)
    {
        paramsYear.setColorFore(color);
    }
    public void setYearCellDimension(Dimension dimension)
    {
        paramsYear.setDimension(dimension);
    }
    public void setYearFont(Font font)
    {
        paramsYear.setFont(font);
    }

    /**
     * Sets the month String policy.
     * @param b boolean
     */
    public void setShowMonthStrings(boolean b)
    {
        monthString = true;
    }

    /**
     * Returns the month String policy.
     * @return boolean
     */
    public boolean isShowMonthStrings()
    {
        return monthString;
    }

    /**
     * Returns the fist day of week for this component.
     * The value is one of java.util.Calendar.SUNDAY, ...
     * @return int
     */
    public int getFirstDayOfWeek()
    {
        if(FirstDayOfWeek==null)
            return Calendar.getInstance().getFirstDayOfWeek();
        return FirstDayOfWeek.intValue();
    }

    /**
     * Sets the first day of week for this component.
     * The value must be one of java.util.Calendar.SUNDAY, ...
     * @param n int
     */
    public void setFirstDayOfWeek(int n)
    {
        if(    n==Calendar.SUNDAY
            || n==Calendar.MONDAY
            || n==Calendar.TUESDAY
            || n==Calendar.WEDNESDAY
            || n==Calendar.THURSDAY
            || n==Calendar.FRIDAY
            || n==Calendar.SATURDAY
        ) {
            this.FirstDayOfWeek = new Integer(n);
        }
        else
            throw new IllegalArgumentException("Must use one of the day constants in java.util.Calendar. e.g. java.util.Calendar.MONDAY");
    }

    /**
     * @see java.awt.ItemSelectable#getSelectedObjects()
     */
    public Object[] getSelectedObjects()
    {
        return new Date[] {getDate()};
    }

    /**
     * @see java.awt.ItemSelectable#addItemListener(java.awt.event.ItemListener)
     */
    public void addItemListener(ItemListener l)
    {
        listenerList.add(ItemListener.class, l);
    }

    /**
     * @see java.awt.ItemSelectable#removeItemListener(java.awt.event.ItemListener)
     */
    public void removeItemListener(ItemListener l)
    {
        listenerList.remove(ItemListener.class, l);
    }

    /**
     * @see java.swing.JComboBox#fireItemStateChanged(java.awt.event.ItemEvent)
     */
    protected void fireItemStateChanged(ItemEvent e)
    {
        Object[] listeners = listenerList.getListenerList();
        for(int i = listeners.length-2; i>=0; i-=2)
            if (listeners[i]==ItemListener.class)
                ((ItemListener)listeners[i+1]).itemStateChanged(e);
    }

    /**
     * @see java.awt.Component#paint(java.awt.Graphics)
     */
    public void paint(Graphics g)
    {
        super.paint(g);
        ////////////////////
        // draw the icon
        paintIcon(g);
    }

    /*
     * Paints the icon on given graphics.
     */
    private void paintIcon(Graphics g)
    {
        Dimension dim = getSize();
        Insets insetsBorder = null;
        if(getBorder()!=null)
            insetsBorder = getBorder().getBorderInsets(this);
        else
            insetsBorder = new Insets(0,0,0,0);

        // Draw icon
        int x = dim.width-insetsBorder.right-10;
        int y = 0;
        int width = 15;
        int height = dim.height;
        iconRectangle = new Rectangle(x, y, width, height);
        clickRectangle = new Rectangle(x-10,0,20,this.getHeight());

        paintIconOnScreen(	this,
                            g,
                            dim.width-insetsBorder.right-10,
                            dim.height/2);

    }

    /*
     * Paint icon to given cordinates.
     */
    private void paintIconOnScreen(Component c, Graphics g, int x, int y)
    {
      BufferedImage image=IconManager.getBufferedImage("16calendar.png");

      if (image!=null)
        g.drawImage(image,this.getWidth()-image.getWidth(),1,this);
      else
      {
        Color col = g.getColor();
        g.setColor(clickColor);
        Rectangle recto=clickRectangle;
        recto.setBounds(recto.x ,1,recto.width,recto.height-3);
        ((Graphics2D)g).fill(clickRectangle);
        g.setColor(Color.black);
        ((Graphics2D)g).fill(new Polygon(new int[]{x-4, x+4, x  },new int[]{y-2, y-2, y+2},3));
        g.setColor(col);
      }
    }

    /**
     * Application resource of the component.
     * This value is used to set the text of the component.
     */
    protected String arKey = null;

    /**
     * @see com.datasel.avicenna.clien.ACComponent#getARKey()
     */
    public String getARKey()
    {
        return arKey;
    }

    /**
     * @see com.datasel.avicenna.clien.ACComponent#setARKey(java.lang.String)
     */
    public void setARKey(String arKey)
    {
        this.arKey = arKey;
    }

    /**
     * Icon key of the component.
     * This value used to set the icon of the component.
     */
    protected String iconKey = null;

    /**
     * @see com.datasel.avicenna.clien.ACComponent#getIconKey()
     */
    public String getIconKey()
    {
        return iconKey;
    }

    /**
     * @see com.datasel.avicenna.clien.ACComponent#setIconKey(java.lang.String)
     */
    public void setIconKey(String iconKey)
    {
        this.iconKey = iconKey;
    }
}




