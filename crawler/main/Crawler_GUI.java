package main;

import interfaces.ISearchListener;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import model.SearchModel;
import model.VideoInformation;
import views.ResultView;
import bibliothek.extension.gui.dock.theme.EclipseTheme;
import bibliothek.gui.DockController;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.station.split.SplitDockGrid;
import controller.FilterController;
import controller.SearchController;

public class Crawler_GUI extends JFrame implements ActionListener,
		ISearchListener
{
	private static final long serialVersionUID = 1L;
	private SearchModel model;
	private JMenuItem save, apiKey, close;

	public Crawler_GUI()
	{
		this.setTitle("Youtube-Video-Crawler");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout());

		// Create Menu
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		apiKey = new JMenuItem("API Key importieren");
		apiKey.addActionListener(this);
		save = new JMenuItem("Daten abspeichern");
		save.addActionListener(this);
		close = new JMenuItem("Beenden");
		close.addActionListener(this);
		menu.add(apiKey);
		menu.addSeparator();
		menu.add(save);
		menu.addSeparator();
		menu.add(close);
		bar.add(menu);
		this.setJMenuBar(bar);

		// Create Data-Model
		model = new SearchModel();
		model.addSearchListener(this);

		// DockingFrames
		SplitDockStation station = new SplitDockStation();

		// Add Components
		SearchController searchControl = new SearchController(model, this);
		// this.add(searchControl);

		// DockingFrames
		DockController controller = new DockController();
		controller.setTheme(new EclipseTheme());

		DefaultDockable inputDock = new DefaultDockable("Eingabe");
		inputDock.add(searchControl);

		DefaultDockable filterControlDock = new DefaultDockable("Filter");
		FilterController filterControl = new FilterController(model, this);
		filterControlDock.add(filterControl);

		DefaultDockable resultViewDock = new DefaultDockable("Ergebnisse");
		ResultView resultView = new ResultView(model);
		resultViewDock.add(resultView);

		SplitDockGrid grid = new SplitDockGrid();

		grid.addDockable(0, 0, 100, 80, inputDock);
		grid.addDockable(0, 80, 40, 100, filterControlDock);
		grid.addDockable(40, 80, 100, 100, resultViewDock);

		station.dropTree(grid.toTree());

		controller.add(station);
		controller.setFocusedDockable(inputDock, true);

		this.add(station);

		this.setSize(800, 600);
		this.setLocation(200, 200);
		this.setVisible(true);

	}

	public static void main(String[] args)
	{
		new Crawler_GUI();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.save)
		{
			if (model.getVideos().isEmpty())
			{
				// Show Dialog, after save-process
				JOptionPane.showMessageDialog(this,
						"Keine Daten zum Abspeichern vorhanden.",
						"Information", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			try
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
				{
					File file = fileChooser.getSelectedFile();

					FileWriter writer = new FileWriter(file);

					ArrayList<VideoInformation> videos = model.getVideos();
					for (int i = 0; i < videos.size(); i++)
					{
						writer.write(videos.get(i).toString());
						writer.write(System.getProperty("line.separator"));
						writer.flush();
					}
					writer.close();

					// Show Dialog, after save-process
					JOptionPane.showMessageDialog(this,
							"Speicherung erfolgreich.", "Information",
							JOptionPane.INFORMATION_MESSAGE);
				}

			} catch (Exception ex)
			{
				// Show Dialog, if something goes wrong
				JOptionPane.showMessageDialog(this,
						"Bei der Speicherung ist ein Fehler aufgetreten.",
						"Fehler", JOptionPane.ERROR_MESSAGE);
			}

		} else if (e.getSource() == this.apiKey)
		{
			String input = JOptionPane.showInputDialog("API Key eingeben");
			model.setApiKey(input);
		} else if (e.getSource() == this.close)
		{
			System.exit(0);
		}
	}

	@Override
	public void results_in_progress()
	{
		apiKey.setEnabled(false);
		save.setEnabled(false);
	}

	@Override
	public void results_available()
	{
		apiKey.setEnabled(true);
		save.setEnabled(true);
	}

}
