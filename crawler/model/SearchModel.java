package model;

import interfaces.ISearchListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultBoundedRangeModel;

import algorithms.Filter;
import algorithms.Search;
import algorithms.SearchRunnable;

public class SearchModel
{
	// Number of Threads for the Search depending on Number of Processors
	private int number_of_threads = 50;

	// Model to see Progress of Search in ProgressBar
	private DefaultBoundedRangeModel progressModel;

	// Values to split the Search, if more than 500 Results are required
	private float searchTime;
	private int searchSplitValue;
	private ArrayList<String> dates;

	// Flag to Stop the Search-Process
	boolean flag;

	// Search Criterias
	private long results;
	private Calendar publishedBefore, publishedAfter;
	private String videoDuration, searchTerm, channel, sort, category;

	// Filter Criterias
	private long viewCount;
	private int[] resolutions;

	// Objects representing Search and Filter
	private Search searchAlgorithm;
	private Filter filter;

	// API-Key
	private String apiKey;

	// Map that contains all of Youtubes formats
	private HashMap<Integer, Format> formatMap;
	// ArrayList that contains the Objects for the Video-Informations
	private ArrayList<VideoInformation> videos;
	private ArrayList<ISearchListener> listeners;

	public SearchModel()
	{
		// Initialize Search and Filter
		searchAlgorithm = new Search(this);
		filter = new Filter(this);
		// Default Number of Results Value
		results = 5;
		// Set Default Values for Time and Term
		searchTime = 0;
		searchTerm = "";
		// Initialize resolutions
		resolutions = new int[0];

		flag = false;

		formatMap = new HashMap<Integer, Format>();
		videos = new ArrayList<VideoInformation>();
		listeners = new ArrayList<ISearchListener>();
		// Initialize the Progress-Bar
		progressModel = new DefaultBoundedRangeModel();
		progressModel.setMinimum(0);
		// Method to fill the HashMap with the Youtube formats
		this.initiateMap();
	}

	public long getResults()
	{
		return results;
	}

	public void setResults(long results)
	{
		this.results = results;
	}

	public Calendar getPublishedBefore()
	{
		return publishedBefore;
	}

	public void setPublishedBefore(Calendar publishedBefore)
	{
		this.publishedBefore = publishedBefore;
	}

	public Calendar getPublishedAfter()
	{
		return publishedAfter;
	}

	// for ease of processing the Calendar-Object can be transformatted into a
	// String
	public String getCalendartoString(Calendar input)
	{

		if (input == null)
		{
			return "";
		}

		String result = input.get(Calendar.YEAR) + "-";
		// +1 because of internal representation of Months in Calendar (0-11)
		if ((input.get(Calendar.MONTH) + 1) > 9)
		{
			result = result + (input.get(Calendar.MONTH) + 1) + "-";
		} else
		{
			result = result + "0" + (input.get(Calendar.MONTH) + 1) + "-";
		}

		if (input.get(Calendar.DAY_OF_MONTH) > 9)
		{
			result = result + input.get(Calendar.DAY_OF_MONTH);
		} else
		{
			result = result + "0" + input.get(Calendar.DAY_OF_MONTH);
		}
		result = result + "T00:00:00Z";
		return result;
	}

	public void setPublishedAfter(Calendar publishedAfter)
	{
		this.publishedAfter = publishedAfter;
	}

	public String getVideoDuration()
	{
		return videoDuration;
	}

	public void setVideoDuration(String videoDuration)
	{
		this.videoDuration = videoDuration;
	}

	public String getSort()
	{
		return sort;
	}

	public void setSort(String sort)
	{
		this.sort = sort;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getSearchTerm()
	{
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm)
	{
		this.searchTerm = searchTerm;
	}

	public String getChannel()
	{
		return channel;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	public long getViewCount()
	{
		return viewCount;
	}

	public void setViewCount(long viewCount)
	{
		this.viewCount = viewCount;
	}

	public int[] getResolutions()
	{
		return resolutions;
	}

	public void setResolutions(int[] resolutions)
	{
		this.resolutions = resolutions;
	}

	public Search getSearchAlgorithm()
	{
		return searchAlgorithm;
	}

	public void setVideos(ArrayList<VideoInformation> videos)
	{
		this.videos = videos;
	}

	public ArrayList<VideoInformation> getVideos()
	{
		return videos;
	}

	public void addVideo(VideoInformation vidModel)
	{
		this.videos.add(vidModel);
	}

	public void deleteVideo(VideoInformation vidModel)
	{
		this.videos.remove(vidModel);
	}

	public HashMap<Integer, Format> getFormatMap()
	{
		return formatMap;
	}

	public DefaultBoundedRangeModel getProgressModel()
	{
		return progressModel;
	}

	public float getSearchTime()
	{
		return searchTime;
	}

	public boolean isFlag()
	{
		return flag;
	}

	public void setFlag(boolean flag)
	{
		this.flag = flag;
	}

	public String getApiKey()
	{
		return apiKey;
	}

	public void setApiKey(String apiKey)
	{
		this.apiKey = apiKey;
	}

	// Method to split Dates over a given Period if the Number of Search Results
	// are greater than 500
	public boolean saveDates()
	{
		if (publishedBefore == null && publishedAfter == null)
		{
			// Actual Date
			publishedBefore = Calendar.getInstance();
			publishedBefore.setTime(new Date());
			// First Youtube-Video published at 23.04.2005
			publishedAfter = Calendar.getInstance();
			publishedAfter.set(2005, 3, 23);
		} else if (publishedBefore == null && publishedAfter != null)
		{
			// Actual Date
			publishedBefore = Calendar.getInstance();
			publishedBefore.setTime(new Date());
		} else if (publishedBefore != null && publishedAfter == null)
		{
			// First Youtube-Video published at 23.04.2005
			publishedAfter = Calendar.getInstance();
			publishedAfter.set(2005, 3, 23);
		}

		long time = publishedBefore.getTime().getTime()
				- publishedAfter.getTime().getTime(); // Difference in ms
		long days = Math.round((double) time / (24. * 60. * 60. * 1000.));
		int splitValue = (int) Math.ceil(results / 500) + 1;

		// Flag to check wether the Number of Results fit in the given Period
		boolean flag = false;
		// Method to check that the number of Results fit in the given Period
		if (days < splitValue)
		{
			// Adapt the Value
			splitValue = (int) days;
			results = days * 500;
			flag = true;
		}

		int add = (int) (days / splitValue);
		searchSplitValue = (int) (results / (int) splitValue);
		// Initialize ArrayList
		dates = new ArrayList<String>();
		dates.add(this.getCalendartoString(publishedAfter));
		Calendar temp = publishedAfter;
		for (int i = 0; i < (splitValue - 1); i++)
		{
			temp = (Calendar) temp.clone();
			temp.add(Calendar.DAY_OF_MONTH, add);
			dates.add(this.getCalendartoString(temp));
		}

		dates.add(this.getCalendartoString(publishedBefore));

		return flag;
	}

	public void startSearch() throws Exception
	{
		this.setViewCount(0);
		fire_results_progress();
		// Delete old Search-Results
		this.videos.clear();
		// Set ProgressBar to Zero
		progressModel.setValue(0);
		// Value to measure the Search Time
		long start = System.currentTimeMillis();

		boolean flag = false;

		// Array to catch Threads and join them at the end of the
		// Search-List-Videos Process
		Thread[] threads = null;
		// Start general Search to get Videos
		if (results > 500)
		{
			// Flag to check wether the Number of Results fit in the given
			// Period
			flag = this.saveDates();

			threads = new Thread[dates.size()];

			for (int i = 0; i < dates.size() - 1; i++)
			{
				// Index to use in Thread
				final int index = i;
				if (i == dates.size() - 2)
				{
					// Set the Value to the searchSplitValue + Rest at the
					// last
					// Search-Cycle
					this.searchSplitValue += (int) this.results
							% searchSplitValue;
				}

				// Thread to find Videos that match with the inserted
				// criterias
				Thread t = new Thread(new Runnable()
				{

					@Override
					public void run()
					{
						try
						{
							searchAlgorithm.search(searchTerm, videoDuration,
									channel, dates.get(index + 1),
									dates.get(index), searchSplitValue, sort,
									category, "");
						} catch (IOException e)
						{
							e.printStackTrace();
						}

					}
				});

				t.start();
				threads[i] = t;

			}
		} else
		{
			searchAlgorithm.search(this.searchTerm, this.videoDuration,
					this.channel, this.getCalendartoString(publishedBefore),
					this.getCalendartoString(publishedAfter), this.results,
					this.sort, this.category, "");
		}

		// Join Threads and start Search to request informations about single
		// videos
		if (threads != null)
		{
			for (int i = 0; i < threads.length - 1; i++)
			{
				threads[i].join();
			}
		}

		progressModel.setMaximum(videos.size());
		// Split founded Videos for the use in Threads

		if (number_of_threads > this.results)
		{
			number_of_threads = (int) this.results;
		}
		int value = videos.size() / number_of_threads;
		// Start and End-Point in the Video-Array for the Threads to divide the
		// videos among the Threads
		int s = 0;
		int e = value;

		// Array to Join all Threads at the end of the Search
		threads = new Thread[number_of_threads];
		for (int i = 0; i < number_of_threads; i++)
		{
			if (i == number_of_threads - 1)
			{
				e = videos.size();
			}
			// Create new Thread with appropriate SearchRunnable
			Thread t = new Thread(new SearchRunnable(this, s, e));
			// Start Thread
			t.start();
			threads[i] = t;
			// Adapt the start and end-Points of the Video-Array
			s = e;
			e = e + value;
		}

		// Join Threads and show Results
		for (int i = 0; i < threads.length; i++)
		{
			threads[i].join();
		}
		// searchAlgorithm.deepSearch(videos);

		// Save SearchTime
		searchTime = (System.currentTimeMillis() - start) / 1000.F;
		// Inform the Views
		this.fire_results_available();

		// Throw Exception to inform User that the Number of Results is smaller
		// than the inserted
		if (flag)
		{
			throw new IndexOutOfBoundsException();
		}
	}

	public void startFilter() throws Exception
	{
		this.fire_results_progress();
		this.filter.filter(resolutions, viewCount);
		this.fire_results_available();
	}

	// Insert different Formats of Youtube-Videos into Map
	public void initiateMap()
	{
		// Initiate Map with DashVideo
		DashVideo vid = new DashVideo(133, "MP4", 240, "H.264", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(134, "MP4", 360, "H.264", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(135, "MP4", 480, "H.264", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(136, "MP4", 720, "H.264", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(137, "MP4", 1080, "H.264", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(138, "MP4", 2160, "H.264", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(160, "MP4", 144, "H.264", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(242, "WebM", 240, "VP9", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(243, "WebM", 360, "VP9", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(244, "WebM", 480, "VP9", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(247, "WebM", 720, "VP9", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(248, "WebM", 1080, "H.264", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(264, "MP4", 1440, "H.264", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(266, "MP4", 2160, "H.264", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(271, "WebM", 1440, "VP9", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(278, "WebM", 144, "VP9", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(298, "MP4", 720, "H.264", true);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(299, "MP4", 1080, "H.264", true);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(302, "WebM", 720, "VP9", true);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(303, "WebM", 1080, "VP9", true);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(308, "WebM", 1440, "VP9", true);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(313, "WebM", 2160, "VP9", false);
		formatMap.put(vid.getItag(), vid);
		vid = new DashVideo(315, "WebM", 2160, "VP9", true);
		formatMap.put(vid.getItag(), vid);

		// Initiate Map with DashAudio
		DashAudio aud = new DashAudio(139, "M4A", "AAC", 48);
		formatMap.put(aud.getItag(), aud);
		aud = new DashAudio(140, "M4A", "AAC", 128);
		formatMap.put(aud.getItag(), aud);
		aud = new DashAudio(141, "M4A", "AAC", 256);
		formatMap.put(aud.getItag(), aud);
		aud = new DashAudio(171, "WebM", "Vorbis", 128);
		formatMap.put(aud.getItag(), aud);
		aud = new DashAudio(172, "WebM", "Vorbis", 192);
		formatMap.put(aud.getItag(), aud);
		aud = new DashAudio(249, "WebM", "opus", 50);
		formatMap.put(aud.getItag(), aud);
		aud = new DashAudio(250, "WebM", "opus", 70);
		formatMap.put(aud.getItag(), aud);
		aud = new DashAudio(251, "WebM", "opus", 160);
		formatMap.put(aud.getItag(), aud);

		// Initiate Map with NonDash
		NonDash ndash = new NonDash(5, "FLV", 240, "Sor H.263", "MP3", 64, "");
		formatMap.put(ndash.getItag(), ndash);
		ndash = new NonDash(17, "3GP", 144, "MPEG-4 Visual", "AAC", 24,
				"Simple");
		formatMap.put(ndash.getItag(), ndash);
		ndash = new NonDash(18, "MP4", 360, "H.264", "AAC", 96, "Baseline");
		formatMap.put(ndash.getItag(), ndash);
		ndash = new NonDash(22, "MP4", 720, "H.264", "AAC", 192, "High");
		formatMap.put(ndash.getItag(), ndash);
		ndash = new NonDash(36, "3GP", 240, "MPEG-4 Visual", "AAC", 32,
				"Simple");
		formatMap.put(ndash.getItag(), ndash);
		ndash = new NonDash(43, "WebM", 360, "VP8", "Vorbis", 128, "");
		formatMap.put(ndash.getItag(), ndash);
		ndash = new NonDash(82, "MP4", 360, "H.264", "AAC", 96, "3D");
		formatMap.put(ndash.getItag(), ndash);
		ndash = new NonDash(83, "MP4", 240, "H.264", "AAC", 96, "3D");
		formatMap.put(ndash.getItag(), ndash);
		ndash = new NonDash(84, "MP4", 720, "H.264", "AAC", 192, "3D");
		formatMap.put(ndash.getItag(), ndash);
		ndash = new NonDash(85, "MP4", 1080, "H.264", "AAC", 192, "3D");
		formatMap.put(ndash.getItag(), ndash);
		ndash = new NonDash(100, "WebM", 360, "VP8", "Vorbis", 128, "3D");
		formatMap.put(ndash.getItag(), ndash);

		// Initiate LiveStreaming
		LiveStreaming stream = new LiveStreaming(92, "TS", 240, "H.264", "AAC",
				48);
		formatMap.put(stream.getItag(), stream);
		stream = new LiveStreaming(93, "TS", 360, "H.264", "AAC", 128);
		formatMap.put(stream.getItag(), stream);
		stream = new LiveStreaming(94, "TS", 480, "H.264", "AAC", 128);
		formatMap.put(stream.getItag(), stream);
		stream = new LiveStreaming(95, "TS", 720, "H.264", "AAC", 256);
		formatMap.put(stream.getItag(), stream);
		stream = new LiveStreaming(96, "TS", 1080, "H.264", "AAC", 256);
		formatMap.put(stream.getItag(), stream);
		stream = new LiveStreaming(127, "TS", 0, "", "AAC", 96);
		formatMap.put(stream.getItag(), stream);
		stream = new LiveStreaming(128, "TS", 0, "", "AAC", 96);
		formatMap.put(stream.getItag(), stream);
		stream = new LiveStreaming(132, "TS", 240, "H.264", "AAC", 48);
		formatMap.put(stream.getItag(), stream);
		stream = new LiveStreaming(151, "TS", 72, "H.264", "AAC", 24);
		formatMap.put(stream.getItag(), stream);
	}

	public void addSearchListener(ISearchListener l)
	{
		listeners.add(l);
	}

	public void removeSearchListener(final ISearchListener l)
	{
		listeners.remove(l);
	}

	// Inform Listeners, that the Search is complete
	public void fire_results_available()
	{
		for (final ISearchListener currentListener : this.listeners)
		{
			currentListener.results_available();
		}
	}

	// Inform Listeners, that the Search is in Progress
	public void fire_results_progress()
	{
		for (final ISearchListener currentListener : this.listeners)
		{
			currentListener.results_in_progress();
		}
	}

	public void printVideoInformation()
	{
		if (videos.isEmpty())
		{
			System.out.println("Anzahl Videos: " + this.videos.size());
			System.out.println("Keine Video-Informationen verfügbar!");
		} else
		{
			Iterator<VideoInformation> it = videos.iterator();
			int i = 0;
			while (it.hasNext())
			{
				VideoInformation v = it.next();
				System.out.println("Ergebnisnummer: " + ++i);
				System.out.println("ID: " + v.getId());
				System.out.println("Titel: " + v.getTitle());
				System.out.println("Erscheinungsdatum: " + v.getReleaseDate());
				System.out.println("-----------------------------------");
			}
		}
	}
}
