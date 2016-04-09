package model;

public class DashAudio extends Format
{
	// 2 = DASH (Video)
	private final static int TYPE = 2;

	private String encoding;
	private int bitrate;

	public DashAudio(int itag, String container, String encoding, int bitRate)
	{
		super(itag, TYPE, container);
		this.encoding = encoding;
		this.bitrate = bitRate;
	}

	public String getEncoding()
	{
		return encoding;
	}

	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}

	public int getBitRate()
	{
		return bitrate;
	}

	public void setBitRate(int bitRate)
	{
		this.bitrate = bitRate;
	}
}
