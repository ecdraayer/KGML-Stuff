package KOSearch;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class GetPathway {

	
	public ArrayList<ArrayList<String>> Search(String KONumber)
	{
		
		WebDriver driver = new HtmlUnitDriver(); 
		ArrayList<ArrayList<String>> table  = new ArrayList<ArrayList<String>>();
		
		driver.get("http://www.genome.jp/dbget-bin/www_bget?ko:" + KONumber);
		WebElement form = driver.findElement(By.name("form1"));
		List<WebElement> tables = form.findElements(By.tagName("table"));
		List<WebElement> trs = tables.get(3).findElements(By.tagName("tr"));
		
		int i=0;
		for (WebElement tr : trs)
		{	
			
			WebElement a =  tr.findElement(By.tagName("a"));
			table.add(new ArrayList<String>());
			table.get(i).add(tr.getText().trim().replace("\n","- "));
			table.get(i).add(a.getAttribute("href"));			
			i++;
		}
		return table;

		
	}
}
