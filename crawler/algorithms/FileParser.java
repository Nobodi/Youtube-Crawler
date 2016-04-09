package algorithms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

public class FileParser extends Parser
{
	private final String USER_AGENT = "Mozilla/5.0";

	public ArrayList<Integer> getItags(String id) throws Exception
	{
		String result = this.sendGet(id);

		if (result.contains("status=fail"))
		{
			Parser p = new HTMLParser();
			return p.getItags(id);
		} else
		{

			// Decode Request
			String r = URLDecoder.decode(result.toString(), "UTF-8");
			// System.out.println(r);

			// Arraylist to save itag-substrings
			ArrayList<String> arr = new ArrayList<String>();
			String subString = "";

			// Filter itag-expressions
			while (true)
			{
				subString = this.getSubString(r);
				if (subString == null)
				{
					break;
				}
				arr.add(subString);
				r = r.substring(0, r.lastIndexOf("itag"));
			}

			arr = this.removeIrrelevantStrings(arr);
			return this.getIntegersOfItag(arr);
		}
	}

	// HTTP GET Request to get the File of the Video-URLs
	public String sendGet(String id) throws Exception
	{

		String url = "http://www.youtube.com/get_video_info?video_id=" + id;

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional - default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null)
		{
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}

	ArrayList<String> removeIrrelevantStrings(ArrayList<String> arr)
	{
		for (int i = 0; i < arr.size(); i++)
		{
			// Substrings with % are irrelevant and substring with itag/0
			if (arr.get(i).contains("itag%") | arr.get(i).contains("itag/0"))
			{
				arr.remove(i);
				i--;
			}
		}

		return arr;
	}

	public static void main(String[] args) throws Exception
	{
		FileParser t = new FileParser();
		ArrayList<Integer> itags = t.getItags("W2WgTE9OKyg");
		System.out.println("Size: " + itags.size());
		for (int i = 0; i < itags.size(); i++)
		{
			System.out.println(itags.get(i));
		}

	}
}
