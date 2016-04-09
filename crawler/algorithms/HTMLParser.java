package algorithms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

public class HTMLParser extends Parser
{

	public ArrayList<Integer> getItags(String id) throws Exception
	{
		try
		{
			String sourcecode = sendGet(id);

			// Cut off the beginning of the Source-Code
			sourcecode = sourcecode.substring(19000, sourcecode.length());
			// Get relevant Part of the Source Code
			int begin = sourcecode.indexOf("<script>var ytplayer = ytplayer") + 1000;
			int end = sourcecode.indexOf("{ytplayer.load();}}());</script>");

			if (end < 0)
			{
				end = sourcecode.indexOf("());</script>");
			}

			String part = sourcecode.substring(begin, end);
			// Decode relevant Part
			part = URLDecoder.decode(part.toString(), "UTF-8");

			// Split decoded Code
			String[] parts = part.split(";");

			// Filter Lines with itag-Expressions
			String relevantCode = "";
			for (int i = 0; i < parts.length; i++)
			{
				if (parts[i].contains("itag"))
				{
					relevantCode = relevantCode + parts[i];
				}
			}

			// Get ITags
			String subString = "";
			ArrayList<String> arr = new ArrayList<String>();
			while (true)
			{
				subString = getSubString(relevantCode);
				if (subString == null)
				{
					break;
				}
				arr.add(subString);
				relevantCode = relevantCode.substring(0,
						relevantCode.lastIndexOf("itag"));
			}

			arr = removeIrrelevantStrings(arr);
			ArrayList<Integer> itags = getIntegersOfItag(arr);

			// Every ITag is two times in one Line
			for (int i = 0; i < itags.size(); i += 2)
			{
				itags.remove(i);
				i--;
			}

			return itags;
		} catch (Exception e)
		{
			// If an Error occurs return an empty Lists
			return new ArrayList<Integer>();
		}
	}

	// HTTP GET Request to get the HTML-Code of the Youtube-Site
	public String sendGet(String id) throws Exception
	{
		String urlString = "https://www.youtube.com/watch?v=" + id;

		URL url = new URL(urlString);

		InputStreamReader isr = new InputStreamReader(url.openConnection()
				.getInputStream());
		BufferedReader br = new BufferedReader(isr);

		String line = "";
		String sourcecode = "";

		// ITag-Expressions are in the first 280 Lines
		int lineCount = 0;
		while ((line = br.readLine()) != null && lineCount < 280)
		{
			sourcecode += line;
			lineCount++;
		}

		// Close Reader
		br.close();
		isr.close();

		return sourcecode;
	}

	ArrayList<String> removeIrrelevantStrings(ArrayList<String> arr)
	{
		for (int index = 0; index < arr.size(); index++)
		{
			// Substrings with % are irrelevant, substring with itag/0 and with
			// itag\\/0
			if (arr.get(index).contains("itag%")
					| arr.get(index).contains("itag/0")
					| arr.get(index).contains("itag\\/0"))
			{
				arr.remove(index);
				index--;
			}
		}

		return arr;
	}

	public static void main(String[] args) throws Exception
	{

		HTMLParser t = new HTMLParser();
		ArrayList<Integer> itags = t.getItags("MOiyD26cJ2A");
		for (int i = 0; i < itags.size(); i++)
		{
			System.out.println(itags.get(i));
		}

	}
}
