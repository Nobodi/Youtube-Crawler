package model;

public class DashVideo extends Format
{
	// 1 = DASH (Video)
	private final static int TYPE = 1;

	private int resolution;
	private String encoding;
	private boolean highFrameRate;

	public DashVideo(int itag, String container, int resolution,
			String encoding, boolean highFrameRate)
	{
		super(itag, TYPE, container);
		this.resolution = resolution;
		this.encoding = encoding;
		this.highFrameRate = highFrameRate;
	}

	public int getResolution()
	{
		return resolution;
	}

	public void setResoultion(int resoultion)
	{
		this.resolution = resoultion;
	}

	public String getEncoding()
	{
		return encoding;
	}

	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}

	public boolean isHighFrameRate()
	{
		return highFrameRate;
	}

	public void setHighFrameRate(boolean highFrameRate)
	{
		this.highFrameRate = highFrameRate;
	}

}
