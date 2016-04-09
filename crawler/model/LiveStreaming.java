package model;

public class LiveStreaming extends Format
{
	// 3 = Live-Streaming
	private final static int TYPE = 3;

	private int resolution, bitrate;
	private String videoEncoding, audioEncoding;

	public LiveStreaming(int itag, String container, int resolution,
			String videoEncoding, String audioEncoding, int bitrate)
	{
		super(itag, TYPE, container);
		this.resolution = resolution;
		this.videoEncoding = videoEncoding;
		this.audioEncoding = audioEncoding;
		this.bitrate = bitrate;
	}

	public int getResolution()
	{
		return resolution;
	}

	public void setResolution(int resolution)
	{
		this.resolution = resolution;
	}

	public int getBitrate()
	{
		return bitrate;
	}

	public void setBitrate(int bitrate)
	{
		this.bitrate = bitrate;
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

}
