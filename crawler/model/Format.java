package model;

public abstract class Format
{
	// Class to represent the different Formats
	// see: https://en.wikipedia.org/wiki/YouTube#Quality_and_formats
	// 0 = Non-DASH
	// 1 = DASH (Video)
	// 2 = DASH (Audio)
	// 3 = Live Streaming
	private int type;

	// Itag of the Format
	private int itag;

	// Container of the Format
	private String container;

	public Format(int itag, int type, String container)
	{
		this.itag = itag;
		this.type = type;
		this.container = container;
	}

	public String getContainer()
	{
		return container;
	}

	public void setContainer(String container)
	{
		this.container = container;
	}

	public int getItag()
	{
		return itag;
	}

	public void setItag(int itag)
	{
		this.itag = itag;
	}

	public int getType()
	{
		return this.type;
	}
}
