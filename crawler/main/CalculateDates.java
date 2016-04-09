package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalculateDates
{

	public CalculateDates()
	{
		// TODO Auto-generated constructor stub
	}

	public static ArrayList<Calendar> test()
	{
		// Test
		// Eingegebenes hohes Datum (hier auf aktuelles Datum gesetzt)
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		System.out.println(calendar.getTime());
		Calendar input = Calendar.getInstance();
		// Eingegebenes niedriges Datum
		input.set(2010, 5, 30);
		long time = calendar.getTime().getTime() - input.getTime().getTime(); // Differenz
																				// in
																				// ms
		long days = Math.round((double) time / (24. * 60. * 60. * 1000.));
		System.out.println("Differenz in ms: " + time);
		System.out.println("Differenz in Tagen: " + days);
		double searchResults = 900;
		System.out.println("SearchResults: " + searchResults);
		int splitValue = (int) Math.ceil(searchResults / 500);
		System.out.println("SplitValue: " + splitValue);
		int add = (int) (days / splitValue);
		int searchSplitValue = (int) (searchResults / (int) splitValue);
		System.out.println("SearchSplitValue: " + searchSplitValue);
		System.out.println("Additionswert:" + add);

		ArrayList<Calendar> dates = new ArrayList<Calendar>();
		dates.add(input);
		Calendar temp = input;
		for (int i = 0; i < (splitValue - 1); i++)
		{
			temp = (Calendar) temp.clone();
			temp.add(Calendar.DAY_OF_MONTH, add);
			dates.add(temp);
		}

		dates.add(calendar);

		return dates;
	}

	public static void main(String[] args)
	{
		ArrayList<Calendar> dates = test();

		for (int i = 0; i < dates.size(); i++)
		{
			System.out.println(i + ": " + dates.get(i).getTime());
		}

	}

}
