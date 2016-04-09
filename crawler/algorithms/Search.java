package algorithms;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import model.Format;
import model.SearchModel;
import model.VideoInformation;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

public class Search
{
	private static final String PROPERTIES_FILENAME = "youtube.properties";

	private SearchModel model;
	private Parser parser;

	private YouTube youtube;

	public Search(SearchModel model)
	{
		this.model = model;
		this.parser = new FileParser();
	}

	// Method to add Videos to ArrayList of the Model
	private void extractVideoIDs(Iterator<SearchResult> iteratorSearchResults)
	{
		while (iteratorSearchResults.hasNext())
		{
			SearchResult res = iteratorSearchResults.next();
			model.addVideo(new VideoInformation(res.getId().getVideoId(), res
					.getSnippet().getTitle(), res.getSnippet().getPublishedAt()
					.toString()));
		}
	}

	public void search(String searchTerm, String duration, String channelID,
			String publishedBefore, String publishedAfter,
			long numberOfResults, String sort, String category,
			String nextPageToken) throws IOException
	{
		// Read the developer key from the properties file.
		Properties properties = new Properties();

		if (model.getApiKey() != null)
		{
			// Load API-Key from model
			properties.setProperty("youtube.apikey", model.getApiKey());
		} else
		{
			// Load API-Key from File
			InputStream in = Search.class.getResourceAsStream("/"
					+ PROPERTIES_FILENAME);
			properties.load(in);
		}

		youtube = new YouTube.Builder(new NetHttpTransport(),
				new JacksonFactory(), new HttpRequestInitializer()
				{
					public void initialize(HttpRequest request)
							throws IOException
					{
					}
				}).setApplicationName("test").build();

		// Define the API request for retrieving search results.
		YouTube.Search.List search = youtube.search().list("id,snippet");

		// Set your developer key from the {{ Google Cloud Console }} for
		// non-authenticated requests. See:
		// {{ https://cloud.google.com/console }}
		String apiKey = properties.getProperty("youtube.apikey");
		search.setKey(apiKey);

		// Set Parameter for Search
		// Restrict the search results to only include videos. See:
		// https://developers.google.com/youtube/v3/docs/search/list#type
		search.setType("video");

		// Set inserted Search Term
		search.setQ(searchTerm);

		// Set Order of the Results
		search.setOrder(sort);

		// Set Category
		search.setVideoCategoryId(category);

		// Set Video Channel ID
		if (!channelID.equals(""))
		{
			search.setChannelId(channelID);
		}

		// Set Video durations
		search.setVideoDuration(duration);

		// Set Dates (publishedafter, publishedbefore)
		// Example: DateTime date = new DateTime("2012-12-24T00:00:00Z");
		if (!publishedBefore.isEmpty())
		{
			DateTime date = new DateTime(publishedBefore);
			search.setPublishedBefore(date);
		}
		if (!publishedAfter.isEmpty())
		{
			DateTime date = new DateTime(publishedAfter);
			search.setPublishedAfter(date);
		}

		// If more than 50 Results are request, than set nextPageToken
		if (!nextPageToken.isEmpty())
		{
			search.setPageToken(nextPageToken);
		}
		// To increase efficiency, only retrieve the fields that the
		// application uses.
		// Distinguish Number of Results; if more than 50 you have to use
		// nextPageToken to get next Page with 50 more results
		if (numberOfResults > 50)
		{
			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/publishedAt,snippet/thumbnails/default/url),nextPageToken");
			search.setMaxResults((long) 50);
		} else
		{
			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/publishedAt,snippet/thumbnails/default/url)");
			search.setMaxResults(numberOfResults);
		}

		// Call the API
		SearchListResponse searchResponse = search.execute();
		List<SearchResult> searchResultList = searchResponse.getItems();

		// Save Video-Informationen if the returned Results larger than zero
		if (searchResultList != null)
		{
			extractVideoIDs(searchResultList.iterator());
		}

		// Search next Results if Input of NumberofResults is larger than 50
		if (numberOfResults > 50)
		{
			nextPageToken = searchResponse.getNextPageToken();
			numberOfResults -= 50;
			if (nextPageToken != null)
			{
				search(searchTerm, duration, channelID, publishedBefore,
						publishedAfter, numberOfResults, sort, category,
						nextPageToken);
			}
		}
	}

	public void getVideoInformations(ArrayList<VideoInformation> videos)
			throws Exception
	{
		// Read the developer key from the properties file.
		Properties properties = new Properties();
		try
		{
			InputStream in = Search.class.getResourceAsStream("/"
					+ PROPERTIES_FILENAME);
			properties.load(in);

		} catch (IOException e)
		{
			System.err.println("There was an error reading "
					+ PROPERTIES_FILENAME + ": " + e.getCause() + " : "
					+ e.getMessage());
			System.exit(1);
		}

		youtube = new YouTube.Builder(new NetHttpTransport(),
				new JacksonFactory(), new HttpRequestInitializer()
				{
					@Override
					public void initialize(HttpRequest request)
							throws IOException
					{
					}
				}).setApplicationName("Videofilter").build();

		Iterator<VideoInformation> it = videos.iterator();

		while (it.hasNext())
		{
			// Get more Informations about the Videos with the Youtube API
			// filter Video with specific ID (list)
			VideoInformation vid = it.next();
			YouTube.Videos.List listVideosRequest = youtube.videos()
					.list("snippet,contentDetails,statistics,status,player")
					.setId(vid.getId());
			String apiKey = properties.getProperty("youtube.apikey");
			listVideosRequest.setKey(apiKey);
			VideoListResponse listResponse = listVideosRequest.execute();

			List<Video> videoList = listResponse.getItems();

			// Save Information to single Video
			if (videoList != null)
			{
				Iterator<Video> iteratorVideoResults = videoList.iterator();
				if (!iteratorVideoResults.hasNext())
				{
					System.out
							.println(" There aren't any results for your query.");
					return;
				}

				while (iteratorVideoResults.hasNext())
				{
					Video singleVideo = iteratorVideoResults.next();
					vid.setChannelID(singleVideo.getSnippet().getChannelId());
					vid.setCategory(singleVideo.getSnippet().getCategoryId());

					// Set Values with short if else check wether the returned
					// Object is null or not
					vid.setViewCount(singleVideo.getStatistics().getViewCount() == null ? 0
							: singleVideo.getStatistics().getViewCount()
									.intValue());

					vid.setCommentCount(singleVideo.getStatistics()
							.getCommentCount() == null ? 0 : singleVideo
							.getStatistics().getCommentCount().intValue());

					vid.setLikeCount(singleVideo.getStatistics().getLikeCount() == null ? 0
							: singleVideo.getStatistics().getLikeCount()
									.intValue());

					vid.setDislikeCount(singleVideo.getStatistics()
							.getDislikeCount() == null ? 0 : singleVideo
							.getStatistics().getDislikeCount().intValue());
				}

			}

			// Get Itags of the Video
			ArrayList<Integer> itags = new ArrayList<Integer>();
			itags = parser.getItags(vid.getId());

			HashMap<Integer, Format> f = model.getFormatMap();
			for (int i = 0; i < itags.size(); i++)
			{
				// If Itag isn't in the HashMap do nothing
				if (f.get(itags.get(i)) != null)
				{
					vid.addFormat(itags.get(i), f.get(itags.get(i)));
				}
			}
			// Update the Progress-Bar
			model.getProgressModel().setValue(
					(model.getProgressModel().getValue() + 1));
		}
	}
}
