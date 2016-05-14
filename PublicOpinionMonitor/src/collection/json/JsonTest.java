/**
 * This is a test class of this package
 * */
package collection.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONException;

public class JsonTest
{
	public static void main(String[] args)
	{
		//JsonTest demo = new JsonTest();
		//demo.test1();
		//demo.test2("/home/youyou/1.json");
		//System.out.println("jiasng'as".replace("'", "\\'"));
	}
	
	public void test1() throws JSONException
	{
		Scanner in = new Scanner(System.in);
		String jsonString = "";
		
		if (in.hasNextLine())
		{
			jsonString += in.nextLine();
		}
		
		ToJsonObject jiSu = new JiSuToJsonObject();
		JiSuObject object = jiSu.toJsonObject(jsonString);
		
		System.out.println(object.toString());
		
		in.close();
	}
	
	public void test2(String path)
	{
		try
		{
			BufferedReader bReader = new BufferedReader(new FileReader(new File(path)));
			String line = bReader.readLine();
			System.out.println(line);
			bReader.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			System.err.println("There hasn't file on: " + path);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			System.err.println("Can't open file: " + path);
			e.printStackTrace();
		}
	}
}
