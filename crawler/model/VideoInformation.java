package model;

import java.util.HashMap;

public class VideoInformation
{
	private String channelID, category;
	private long viewCount;
	private int likeCount, dislikeCount, commentCount;
	private String id, title, releaseDate;
	private HashMap<Integer, Format> formats;

	public VideoInformation(String id, String title, String releaseDate)
	{
		this.id = id;
		this.title = title;
		this.releaseDate = releaseDate;
		this.formats = new HashMap<Integer, Format>();
	}

	public String getChannelID()
	{
		return channelID;
	}

	public void setChannelID(String channelID)
	{
		this.channelID = channelID;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public HashMap<Integer, Format> getFormats()
	{
		return this.formats;
	}

	public void setFormats(HashMap<Integer, Format> formats)
	{
		this.formats = formats;
	}

	public void addFormat(int itag, Format format)
	{
		this.formats.put(itag, format);
	}

	public String getId()
	{
		return this.id;
	}

	public String getTitle()
	{
		return this.title;
	}

	public String getReleaseDate()
	{
		return this.releaseDate;
	}

	public long getViewCount()
	{
		return viewCount;
	}

	public void setViewCount(long viewCount)
	{
		this.viewCount = viewCount;
	}

	public int getLikeCount()
	{
		return likeCount;
	}

	public void setLikeCount(int likeCount)
	{
		this.likeCount = likeCount;
	}

	public int getDislikeCount()
	{
		return dislikeCount;
	}

	public void setDislikeCount(int dislikeCount)
	{
		this.dislikeCount = dislikeCount;
	}

	public int getCommentCount()
	{
		return commentCount;
	}

	public void setCommentCount(int commentCount)
	{
		this.commentCount = commentCount;
	}

	public int getNumberOfFormats()
	{
		return this.formats.size();
	}

	@Override
	public String toString()
	{
		String formats = "";

		for (Integer key : this.formats.keySet())
		{
			formats = formats + key + "|";
		}

		if (formats.length() > 0)
		{
			formats = formats.substring(0, formats.length() - 1);
		} else
		{
			formats = "0";
		}

		String delimiter = ",";
		return this.id + delimiter + this.channelID + delimiter + this.category
				+ delimiter + this.releaseDate + delimiter + this.viewCount
				+ delimiter + this.commentCount + delimiter + this.likeCount
				+ delimiter + this.dislikeCount + delimiter + formats;
	}
}
