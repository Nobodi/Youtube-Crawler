package views;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.Border;

import interfaces.ISearchListener;
import model.SearchModel;

public class ProgressView extends JPanel implements ISearchListener
{
	private static final long serialVersionUID = 1L;

	private JTextField searchTime;
	private SearchModel model;

	public ProgressView(SearchModel model)
	{
		this.model = model;
		model.addSearchListener(this);
		this.setLayout(new GridBagLayout());
		final DefaultBoundedRangeModel progressModel = model.getProgressModel();
		JProgressBar progressBar = new JProgressBar(progressModel);
		Border border = BorderFactory.createTitledBorder("Suche...");
		progressBar.setBorder(border);

		this.add(progressBar);

		JPanel information = new JPanel();
		information.setLayout(new GridLayout(1, 2));
		information.add(new JLabel("Suchzeit: "));
		searchTime = new JTextField();
		searchTime.setPreferredSize(new Dimension(120, 20));
		searchTime.setMinimumSize(new Dimension(120, 20));
		searchTime.setEditable(false);
		information.add(searchTime);
		this.add(information);
	}

	@Override
	public void results_available()
	{
		searchTime.setText("" + model.getSearchTime() + " Sekunden");
	}

	@Override
	public void results_in_progress()
	{

	}

}
