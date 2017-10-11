package utility;

import java.io.*;
import java.util.*;

import objects.Bot;
import objects.Map;

public class GameListGenerator 
{

	public static void GenerateGames(int rounds, Vector<Map> maps, Vector<Bot> bots, String TournamentType, boolean RandomizeGames) 
	{
		try 
		{
			FileWriter fstream = new FileWriter("games.txt");
			
			BufferedWriter out = new BufferedWriter(fstream);
			
			List<Object[]> games;
			if(TournamentType.equalsIgnoreCase("1VsAll"))
			{
				games = generate1VsAll(rounds, maps, bots, out);
			}
			else
			{
				games = generateRoundRobin(rounds, maps, bots, out);
			}
			
			if(RandomizeGames)
			{
				Collections.shuffle(games);
			}
			
			int gameID = 0;
			for (Object[] game : games)
			{
				out.write(String.format("%7d %5d %20s %20s %35s", gameID, game[0], game[1], game[2], game[3]) + System.getProperty("line.separator"));
				gameID++;
			}
			
			out.write("");
			out.flush();
			out.close();
			
			System.out.println("Generation Complete");
			
		} 
		catch (Exception e) 
		{
			System.err.println("Error: " + e.getMessage());
		}
	}

	private static List<Object[]> generateRoundRobin(int rounds, Vector<Map> maps, Vector<Bot> bots, BufferedWriter out) throws IOException 
	{
		List<Object[]> games = new LinkedList<>();
		int roundNum = 0;
		for (int i = 0; i < rounds; i++) 
		{
			for(Map m : maps)
			{
				for (int j = 0; j < bots.size(); j++) 
				{
					for (int k = j+1; k < bots.size(); k++) 
					{						
						if (roundNum % 2 == 0) 
						{
							games.add(new Object[] { roundNum, bots.get(j).getName(), bots.get(k).getName(), m.getMapName() });
						} 
						else 
						{
							games.add(new Object[] { roundNum, bots.get(k).getName(), bots.get(j).getName(), m.getMapName() });
						}
					}
				}
				roundNum++;
			}
		}
		return games;
	}
	
	private static List<Object[]> generate1VsAll(int rounds, Vector<Map> maps, Vector<Bot> bots, BufferedWriter out) throws IOException 
	{
		List<Object[]> games = new LinkedList<>();
		int roundNum = 0;
		for (int i = 0; i < rounds; i++) 
		{
			for(Map m : maps)
			{
				for (int k = 1; k < bots.size(); k++) 
				{
					games.add(new Object[] { roundNum, bots.get(0).getName(), bots.get(k).getName(), m.getMapName() });
				}
				roundNum++;
			}
		}
		return games;
	}
}
