package controller;

import interfaces.ISearchListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import model.SearchModel;

public class FilterController extends JPanel implements ActionListener,
		ISearchListener
{

	private static final long serialVersionUID = 1L;

	private SearchModel model;
	// MainFrame to show Dialogs
	private JFrame frame;
	private JButton filterButton;
	private JTextField viewCountField;
	private JList<String> resolutionList;

	public FilterController(SearchModel model, JFrame frame)
	{
		this.model = model;
		this.frame = frame;
		this.setLayout(new GridBagLayout());
		// Add FilterController-Object because the Button have to be disabled
		// during a search/filter process
		model.addSearchListener(this);

		// ViewCount Panel
		JPanel viewCountPanel = new JPanel();
		viewCountPanel.setLayout(new GridLayout(2, 1));
		viewCountPanel.add(new JLabel("Klickzahlen \u00FCber: "));

		// Create ViewCount Input Field
		viewCountField = new JTextField();
		viewCountField.setPreferredSize(new Dimension(160, 20));
		viewCountField.setMinimumSize(new Dimension(160, 20));
		viewCountPanel.add(viewCountField);

		// Video Resolution Panel
		JPanel resolutionPanel = new JPanel();
		resolutionPanel.setLayout(new BorderLayout());
		resolutionPanel.add(new JLabel("Aufl\u00F6sung: "), BorderLayout.NORTH);

		// Resolution Selection List
		String[] resolutions =
		{ "144p", "240p", "360p", "480p", "720p", "1080p" };

		// Create Resolution List
		resolutionList = new JList<String>(resolutions);

		// Add Resolution List to a Scrollbar
		JScrollPane resolutionScrollPane = new JScrollPane();
		resolutionScrollPane.setViewportView(resolutionList);
		resolutionScrollPane.setPreferredSize(new Dimension(70, 150));
		resolutionScrollPane.setMinimumSize(new Dimension(70, 150));

		// Add ScrollPane to Panel
		resolutionPanel.add(resolutionScrollPane, BorderLayout.CENTER);

		// Create Filter Button
		filterButton = new JButton("Start");
		filterButton.setPreferredSize(new Dimension(160, 50));
		filterButton.setMinimumSize(new Dimension(160, 50));
		filterButton.addActionListener(this);

		// Add Panels to Main Panel
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 40, 0, 10);
		this.add(resolutionPanel, c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 10, 0);
		this.add(viewCountPanel, c);
		c.gridy = 2;
		this.add(filterButton, c);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Start"))
		{
			// If no Results are searched before than theres shown an
			// Information
			if (model.getVideos().isEmpty())
			{
				JOptionPane
						.showMessageDialog(
								frame,
								"Es liegen keine Videos zur Filterung vor. Bitte starten Sie eine Suche.",
								"Achtung", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// Get Resolutions
			int[] arr = resolutionList.getSelectedIndices();
			if (arr.length != 0)
			{
				for (int i = 0; i < arr.length; i++)
				{
					switch (arr[i])
					{
					case 0:
						arr[i] = 144;
						break;
					case 1:
						arr[i] = 240;
						break;
					case 2:
						arr[i] = 360;
						break;
					case 3:
						arr[i] = 480;
						break;
					case 4:
						arr[i] = 720;
						break;
					case 5:
						arr[i] = 1080;
						break;
					default:
						arr[i] = 144;
					}
				}
				// Save choosed Resolutions
				model.setResolutions(arr);
			}

			// Get ViewCount Input
			// Get and Check Number of the ViewCountField
			try
			{
				if (!viewCountField.getText().isEmpty())
				{
					long value = Long.parseLong(viewCountField.getText());
					if (value < 1)
					{
						// Message for the User that the Input is under 1
						JOptionPane.showMessageDialog(frame,
								"Die Anzahl der Klickzahlen muss zwischen 1 und "
										+ Long.MAX_VALUE + " liegen.",
								"Achtung", JOptionPane.INFORMATION_MESSAGE);
						return;
					} else
					{
						// Save ViewCount Value to Model
						model.setViewCount(value);
						// Delete Input of the Field
						viewCountField.setText("");
						// Set Border of the Field to normal color
						viewCountField.setBorder(UIManager
								.getBorder("TextField.border"));
					}
				}
				// If the Input was wrong and then it starts the filter with
				// nothing in the Inputfield, the Inputfield border has to be
				// changed because there's no error anymore
				else
				{
					viewCountField.setBorder(UIManager
							.getBorder("TextField.border"));
				}
			} catch (NumberFormatException nfe)
			{
				// Mark the Inputfield where the Error occured
				viewCountField.setBorder(new LineBorder(Color.RED));
				// Message that the Input wasn't a number
				JOptionPane.showMessageDialog(frame,
						"EingabeFehler. Nur Zahlen sind erlaubt.",
						"Eingabefehler", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Start Filter
			try
			{
				model.startFilter();
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void results_in_progress()
	{
		// Deactivate Button till the search/filter is in progress
		filterButton.setEnabled(false);
	}

	@Override
	public void results_available()
	{
		// Activate Button after Search/Filter
		filterButton.setEnabled(true);
	}
}
