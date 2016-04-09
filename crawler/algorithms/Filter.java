package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import model.DashVideo;
import model.Format;
import model.NonDash;
import model.SearchModel;
import model.VideoInformation;

public class Filter
{
	private SearchModel model;

	public Filter(SearchModel model)
	{
		this.model = model;
	}

	public void filter(int[] resolutions, long viewCount)
	{
		ArrayList<VideoInformation> videos = model.getVideos();

		// Filter ViewCounts
		if (viewCount > 0)
		{
			for (Iterator<VideoInformation> Iter = videos.iterator(); Iter
					.hasNext();)
			{
				VideoInformation video = Iter.next();
				if (video.getViewCount() < viewCount)
				{
					Iter.remove();
				}
			}
		}

		if (resolutions.length != 0)
		{

			// Filter Resolutions
			ArrayList<VideoInformation> removing = new ArrayList<VideoInformation>();
			for (Iterator<VideoInformation> Iter = videos.iterator(); Iter
					.hasNext();)
			{
				VideoInformation video = Iter.next();
				boolean[] array = new boolean[resolutions.length];
				for (int p = 0; p < array.length; p++)
				{
					array[p] = false;
				}
				HashMap<Integer, Format> videoFormats = video.getFormats();
				for (Integer key : videoFormats.keySet())
				{
					int type = videoFormats.get(key).getType();
					switch (type)
					{
					case 0:
						NonDash nondash = (NonDash) videoFormats.get(key);
						for (int k = 0; k < resolutions.length; k++)
						{
							if (nondash.getResolution() == resolutions[k])
							{
								array[k] = true;
							}
						}
						break;
					case 1:
						DashVideo dashvideo = (DashVideo) videoFormats.get(key);
						for (int k = 0; k < resolutions.length; k++)
						{
							if (dashvideo.getResolution() == resolutions[k])
							{
								array[k] = true;
							}
						}
						break;
					default:
					}

				}

				for (int b = 0; b < array.length; b++)
				{
					if (!array[b])
					{
						removing.add(video);
					}
				}

			}

			for (VideoInformation v : removing)
			{
				videos.remove(v);
			}
		}
	}
}
