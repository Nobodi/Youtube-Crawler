package model;

public class NonDash extends Format
{
	// 0 = Non-DASH
	private final static int TYPE = 0;

	private int resolution, audioBitrate;
	private String videoEncoding, audioEncoding, videoProfile;

	public NonDash(int itag, String container, int resolution,
			String videoEncoding, String audioEncoding, int audioBitrate,
			String videoProfile)
	{
		super(itag, TYPE, container);
		this.resolution = resolution;
		this.videoEncoding = videoEncoding;
		this.audioEncoding = audioEncoding;
		this.audioBitrate = audioBitrate;
		this.videoProfile = videoProfile;
	}

	public int getResolution()
	{
		return resolution;
	}

	public void setResolution(int resolution)
	{
		this.resolution = resolution;
	}

	public int getAudioBitrate()
	{
		return audioBitrate;
	}

	public void setAudioBitrate(int audioBitrate)
	{
		this.audioBitrate = audioBitrate;
	}

	public String getVideoEncoding()
	{
		return videoEncoding;
	}

	public void setVideoEncoding(String videoEncoding)
	{
		this.videoEncoding = videoEncoding;
	}

	public String getAudioEncoding()
	{
		return audioEncoding;
	}

	public void setAudioEncoding(String audioEncoding)
	{
		this.audioEncoding = audioEncoding;
	}

	public String getVideoProfile()
	{
		return videoProfile;
	}

	public void setVideoProfile(String videoProfile)
	{
		this.videoProfile = videoProfile;
	}
}
