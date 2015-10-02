package KOSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * The class is used to retrieve a list of pathways link to KO number.
 * 
 * Example URL http://www.genome.jp/kegg-bin/show_pathway?ko01212+K09458
 *
 * Return the pathway id and URL.
 * @author Raul Alvarado
 *
 *
 */
public class GetPathway {

	

	public ArrayList<ArrayList<String>> Search(String KONumber)
	{
		
		WebDriver driver = new HtmlUnitDriver(); 
		boolean pathwayexist = false;
		driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(120, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
		ArrayList<ArrayList<String>> table  = new ArrayList<ArrayList<String>>();
		
		driver.get("http://www.genome.jp/dbget-bin/www_bget?ko:" + KONumber);
		
		//check if pathway exists			
		List<WebElement> ths = driver.findElements(By.tagName("th"));
		
		
		for (int i=0;i<ths.size();i++)
		{
			//System.out.println(ths.get(i).getText());
			if (ths.get(i).getText().contains("Pathway"))
			{
				pathwayexist=true;
			}
		
		}
		
		
		if (pathwayexist==true)
		{
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
			
		}
		driver.close();
		return table;

		
	}
}
