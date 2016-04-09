package model;

import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel
{

	private static final long serialVersionUID = 1L;
	private String[] columns =
	{ "Nummer", "ID", "Titel", "Kategorie", "Ver\u00f6ffentlichung", "Gesehen",
			"Kommentare", "Like", "Dislike", "Formate" };

	private SearchModel model;

	public TableModel(SearchModel model)
	{
		this.model = model;
	}

	@Override
	public int getColumnCount()
	{
		return columns.length;
	}

	@Override
	public int getRowCount()
	{
		return model.getVideos().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		VideoInformation video = model.getVideos().get(rowIndex);
		Object o = "";
		switch (columnIndex)
		{
		case 0:
			o = rowIndex + 1;
			break;
		case 1:
			o = video.getId();
			break;
		case 2:
			o = video.getTitle();
			break;
		case 3:
			o = video.getCategory();
			break;
		case 4:
			o = video.getReleaseDate();
			break;
		case 5:
			o = video.getViewCount();
			break;
		case 6:
			o = video.getCommentCount();
			break;
		case 7:
			o = video.getLikeCount();
			break;
		case 8:
			o = video.getDislikeCount();
			break;
		case 9:
			String temp = "";

			for (Map.Entry<Integer, Format> entry : video.getFormats()
					.entrySet())
			{
				try
				{
					temp = temp + entry.getValue().getItag() + ", ";
				} catch (NullPointerException ne)
				{
					System.out.println(video.getTitle());
					System.out.println(entry.getValue());
				}
			}

			if (temp.length() > 2)
			{
				o = temp.substring(0, temp.length() - 2);
			}
			break;
		default:
			o = "Error";
		}
		return o;
	}

	@Override
	public String getColumnName(int index)
	{
		return columns[index];
	}

}
