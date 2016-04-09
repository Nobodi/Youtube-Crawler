package controller;

import interfaces.ISearchListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import model.SearchModel;
import views.ProgressView;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;

public class SearchController extends JPanel implements ActionListener,
		ISearchListener
{
	private static final long serialVersionUID = 1L;

	private SearchModel model;
	private JFrame frame;
	private JComboBox<String> durationList, sortList, categoryList;
	private JTextField searchField, channelField, pBeforeField, pAfterField,
			resultsField;
	private JButton search;

	public SearchController(SearchModel model, JFrame frame)
	{
		this.model = model;
		this.frame = frame;
		this.setLayout(new GridBagLayout());
		model.addSearchListener(this);

		// Video Duration Panel
		JPanel durationPanel = new JPanel();
		durationPanel.setLayout(new GridLayout(1, 2));
		durationPanel.add(new JLabel("Videol\u00e4nge: "));

		// Duration Selection List
		String comboBoxListDuration[] = { "egal", "kurz (<4min.)",
				"mittel (4<x<=20min.)", "lang (>20min.)" };

		// JComboBox with Duration Selection
		durationList = new JComboBox<String>(comboBoxListDuration);
		durationList.setSelectedItem(comboBoxListDuration[0]);

		durationPanel.add(durationList);

		// Sort-Results Panel
		JPanel sortPanel = new JPanel();
		sortPanel.setLayout(new GridLayout(1, 2));
		sortPanel.add(new JLabel("Sortieren nach: "));

		// Sort-Selection List
		String comboBoxListSort[] = { "Relevanz", "Datum", "Bewertung",
				"Titel", "Gesehen" };

		// JComboBox with Sort Selection
		sortList = new JComboBox<String>(comboBoxListSort);
		sortList.setSelectedItem(comboBoxListSort[0]);

		sortPanel.add(sortList);

		// Category Panel
		JPanel categoryPanel = new JPanel();
		categoryPanel.setLayout(new GridLayout(1, 2));
		categoryPanel.add(new JLabel("Kategorie: "));

		// Category Selection List
		String comboBoxListCategory[] = { "Keine", "Musik", "Sport", "Film",
				"Nachrichten", "Spiele", "Unterhaltung" };

		// JComboBox with Category Selection
		categoryList = new JComboBox<String>(comboBoxListCategory);

		categoryPanel.add(categoryList);

		// Published Before Panel
		JPanel pBeforePanel = new JPanel();
		pBeforePanel.setLayout(new GridLayout(1, 2));
		pBeforePanel.add(new JLabel("Ver\u00F6ffentlicht vor: "));

		// Create Date Field
		pBeforeField = new JTextField();
		pBeforeField.setSize(new Dimension(100, 25));
		pBeforeField.setMaximumSize(new Dimension(100, 25));
		pBeforePanel.add(pBeforeField);

		// Published After Panel
		JPanel pAfterPanel = new JPanel();
		pAfterPanel.setLayout(new GridLayout(1, 2));
		pAfterPanel.add(new JLabel("Ver\u00F6ffentlicht nach: "));

		// Create Date Field
		pAfterField = new JTextField();
		pAfterPanel.add(pAfterField);

		// Search Term Panel
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new GridLayout(1, 2));
		searchPanel.add(new JLabel("Suchbegriff: "));

		// Create Search Term Field
		searchField = new JTextField();
		searchPanel.add(searchField);

		// Channel Panel
		JPanel channelPanel = new JPanel();
		channelPanel.setLayout(new GridLayout(1, 2));
		channelPanel.add(new JLabel("Channel ID: "));

		// Create Channel Input Field
		channelField = new JTextField();
		channelPanel.add(channelField);

		// Create Results Panel
		JPanel resultsPanel = new JPanel();
		resultsPanel.setLayout(new GridLayout(1, 2));
		resultsPanel.add(new JLabel("Ergebnisanzahl: "));

		// Create Number of Results Input Field
		resultsField = new JTextField();
		resultsPanel.add(resultsField);

		// Create Search Button
		search = new JButton("Suche");
		search.setPreferredSize(new Dimension(700, 50));
		search.setMinimumSize(new Dimension(700, 50));
		search.addActionListener(this);

		// Generate Structure-Panel1
		JPanel first = new JPanel();
		first.setLayout(new GridLayout(3, 1, 10, 10));
		first.add(searchPanel);
		first.add(pBeforePanel);
		first.add(pAfterPanel);

		// Generate Structure-Panel2
		JPanel second = new JPanel();
		second.setLayout(new GridLayout(3, 1, 10, 10));
		second.add(resultsPanel);
		second.add(channelPanel);

		// Generate Structure-Panel3
		JPanel third = new JPanel();
		third.setLayout(new GridLayout(3, 1, 10, 10));
		third.add(durationPanel);
		third.add(sortPanel);
		third.add(categoryPanel);

		// Add Panels to Main Panel
		this.add(first);
		this.add(second);
		this.add(third);

		// Add Search Button
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.insets = new Insets(10, 0, 10, 10);
		this.add(search, c);
		// Add Progress View
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		this.add(new ProgressView(model), c);
	}

	// Parse Date Inputs
	public Calendar parseDate(String input)
	{
		if (input.equals(""))
		{
			return null;
		}
		Calendar c = Calendar.getInstance();
		String[] dateValues = input.split("-");
		try
		{
			// -1 because of internal representation of Months in Calendar(0-11)
			c.set(Integer.parseInt(dateValues[0]),
					Integer.parseInt(dateValues[1]) - 1,
					Integer.parseInt(dateValues[2]));
		} catch (Exception e)
		{
			// Show Dialog, if Input has wrong symbols/format
			JOptionPane.showMessageDialog(frame,
					"EingabeFehler. Format: Jahr-Monat-Tag (Bsp.:2000-01-20)",
					"Eingabefehler", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return c;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Abbruch"))
		{
			System.out.println("Abbruch");
		}
		if (e.getActionCommand().equals("Suche"))
		{
			// Get Search Input and Channel ID
			model.setSearchTerm(searchField.getText());
			searchField.setText("");
			model.setChannel(channelField.getText());
			channelField.setText("");

			// Get Duration Input
			int i = durationList.getSelectedIndex();
			switch (i)
			{
			case 1:
				model.setVideoDuration("short");
				break;
			case 2:
				model.setVideoDuration("medium");
				break;
			case 3:
				model.setVideoDuration("long");
				break;
			default:
				model.setVideoDuration("any");
			}

			// Get Sort Input
			int sort = sortList.getSelectedIndex();
			switch (sort)
			{
			case 1:
				model.setSort("date");
				break;
			case 2:
				model.setSort("rating");
				break;
			case 3:
				// If no Title is inserted, the sort-value will not be set to
				// title
				if (!model.getSearchTerm().equals(""))
				{
					model.setSort("title");
				}
				break;
			case 4:
				model.setSort("viewCount");
				break;
			default:
				model.setSort("relevance");
			}

			// Get Category Input
			int category = categoryList.getSelectedIndex();
			switch (category)
			{
			// Music
			case 1:
				model.setCategory("10");
				break;
			// Sports
			case 2:
				model.setCategory("17");
				break;
			// Film
			case 3:
				model.setCategory("30");
				break;
			// News
			case 4:
				model.setCategory("25");
				break;
			// Games
			case 5:
				model.setCategory("20");
				break;
			// Entertainment
			case 6:
				model.setCategory("24");
				break;
			default:
				model.setCategory(null);
			}

			// Get Date Inputs and Check them
			if (!pBeforeField.getText().isEmpty())
			{
				Calendar c = parseDate(pBeforeField.getText());
				if (c != null)
				{
					model.setPublishedBefore(c);
					pBeforeField.setText("");
					pBeforeField.setBorder(UIManager
							.getBorder("TextField.border"));
				} else
				{
					pBeforeField.setBorder(new LineBorder(Color.RED));
					return;
				}
			} else
			{
				pBeforeField.setBorder(UIManager.getBorder("TextField.border"));
			}

			if (!pAfterField.getText().isEmpty())
			{
				Calendar c = parseDate(pAfterField.getText());
				if (c != null)
				{
					model.setPublishedAfter(c);
					pAfterField.setText("");
					pAfterField.setBorder(new LineBorder(Color.BLACK));
				} else
				{
					pAfterField.setBorder(new LineBorder(Color.RED));
					return;
				}
			} else
			{
				pAfterField.setBorder(UIManager.getBorder("TextField.border"));
			}

			// Get and Check Number of Results Input
			try
			{
				if (!resultsField.getText().isEmpty())
				{
					int value = Integer.parseInt(resultsField.getText());
					if (value > 10000 || value < 1)
					{
						resultsField.setBorder(new LineBorder(Color.RED));
						JOptionPane
								.showMessageDialog(
										frame,
										"Die Anzahl der Ergebnisse muss zwischen 1 und 10000 liegen.",
										"Achtung",
										JOptionPane.INFORMATION_MESSAGE);
						return;
					} else
					{
						model.setResults(value);
						resultsField.setText("");
						resultsField.setBorder(UIManager
								.getBorder("TextField.border"));
					}
				} else
				{
					model.setResults(5);
					resultsField.setBorder(UIManager
							.getBorder("TextField.border"));
				}
			} catch (NumberFormatException nfe)
			{
				resultsField.setBorder(new LineBorder(Color.RED));
				JOptionPane.showMessageDialog(frame,
						"EingabeFehler. Nur Zahlen sind erlaubt.",
						"Eingabefehler", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Start Search
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						model.startSearch();
					} catch (GoogleJsonResponseException e1)
					{
						JOptionPane.showMessageDialog(frame,
								"Fehler bei der Suchanfrage. Nachricht: "
										+ e1.getDetails().getMessage(),
								"Anfragefehler", JOptionPane.ERROR_MESSAGE);
						model.fire_results_available();
					} catch (SocketTimeoutException | NoRouteToHostException
							| UnknownHostException e3)
					{
						JOptionPane
								.showMessageDialog(
										frame,
										"Fehler bei der Suchanfrage. Die Verbindung wurde unterbrochen.",
										"Anfragefehler",
										JOptionPane.ERROR_MESSAGE);
						model.fire_results_available();
					} catch (IndexOutOfBoundsException ie)
					{
						JOptionPane
								.showMessageDialog(
										frame,
										"Anzahl der Ergebnisse ist zu gro\u00df f\u00fcr den angegebenen Zeitraum. Maximale Ergebnisse pro Tag = 500.",
										"Achtung",
										JOptionPane.INFORMATION_MESSAGE);
						model.fire_results_available();
					} catch (Exception e2)
					{
						e2.printStackTrace();
						JOptionPane.showMessageDialog(frame,
								"Fehler bei der Suchanfrage.", "Anfragefehler",
								JOptionPane.ERROR_MESSAGE);
						model.fire_results_available();
					}

				}
			}).start();
			;

		}

	}

	@Override
	public void results_in_progress()
	{
		// Deactivate Button till the search/filter is in progress
		search.setEnabled(false);
	}

	@Override
	public void results_available()
	{
		// Activate Button after Search/Filter
		search.setEnabled(true);
	}
}
