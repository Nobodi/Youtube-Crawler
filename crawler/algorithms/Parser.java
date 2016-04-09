package algorithms;

import java.util.ArrayList;

public abstract class Parser
{
	// Get the Itags for the Video
	abstract ArrayList<Integer> getItags(String id) throws Exception;

	// HTTP GET Request
	abstract String sendGet(String id) throws Exception;

	// Method to remove unimportant itag-substrings from the Arraylist
	abstract ArrayList<String> removeIrrelevantStrings(ArrayList<String> arr);

	// Method to filter itag-expressions
	protected String getSubString(String input)
	{
		// Index of Itag in String
		int n = 0;
		// Variable for the result-String
		String result = "";

		// Get Index of itag in String
		n = input.lastIndexOf("itag");
		// n becomes smaller than 0 if there's no more itag-substring in
		// whole String; if "itag" substring stands at the end of the whole
		// String there's no more number after that substring
		if (n < 0 || n + 8 > input.length())
		{
			return null;
		}
		// Get the itag-substring with the following number
		result = input.substring(input.lastIndexOf("itag"), (n + 8));

		// Check wether the last symbol of the substring is a number or not
		StringBuffer buf = new StringBuffer(result);
		char c = buf.charAt(buf.length() - 1);

		if (c < '0' || c > '9')
		{
			result = buf.deleteCharAt(buf.length() - 1).toString();
			c = buf.charAt(buf.length() - 1);
			// Check wehter the next to the last symbol of the substring is
			// a number or not, because there are some itags which are
			// smaller than 10
			if (c < '0' || c > '9')
			{
				result = buf.deleteCharAt(buf.length() - 1).toString();
			}
		}
		return result;
	}

	// Method to get the Integer-Values of the different itags
	protected ArrayList<Integer> getIntegersOfItag(ArrayList<String> arr)
	{
		ArrayList<Integer> result = new ArrayList<Integer>(arr.size());

		String temp = "";
		for (int i = 0; i < arr.size(); i++)
		{
			temp = arr.get(i);
			// Cut off the irrelevant 'itag=' substring and Parse it to Integer
			// Try-Catch if something goes wrong with parseInt
			try
			{
				result.add(Integer.parseInt(temp.substring(5, temp.length())));
			} catch (NumberFormatException e)
			{

			}
		}

		return result;
	}
}
