package views;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import interfaces.ISearchListener;
import model.SearchModel;
import model.TableModel;

public class ResultView extends JPanel implements ISearchListener
{

	private static final long serialVersionUID = 1L;
	private TableModel tableModel;

	public ResultView(SearchModel model)
	{
		this.setLayout(new BorderLayout());
		model.addSearchListener(this);

		tableModel = new TableModel(model);

		// Old Try with Table
		this.add(new JScrollPane(new JTable(tableModel)), BorderLayout.CENTER);
	}

	@Override
	public void results_available()
	{
		tableModel.fireTableDataChanged();
	}

	@Override
	public void results_in_progress()
	{

	}
}
