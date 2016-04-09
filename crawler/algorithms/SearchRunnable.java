package algorithms;

import java.util.ArrayList;

import model.SearchModel;
import model.VideoInformation;

public class SearchRunnable implements Runnable
{
	private SearchModel model;
	private int start, end;

	public SearchRunnable(SearchModel model, int start, int end)
	{
		this.model = model;
		this.start = start;
		this.end = end;
	}

	@Override
	public void run()
	{
		ArrayList<VideoInformation> videos = model.getVideos();
		ArrayList<VideoInformation> v = new ArrayList<VideoInformation>();
		// Obtain the appropriate Videos
		for (int i = start; i < end; i++)
		{
			v.add(videos.get(i));
		}
		try
		{
			// Start the Search with the appropriate Videos
			model.getSearchAlgorithm().getVideoInformations(v);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
