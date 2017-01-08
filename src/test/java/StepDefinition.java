import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinition {
	WebDriver driver;
	private WebElement clickedDay;

	@Before()
	public void setup() {
		driver = new FirefoxDriver();
	}

	@After()
	public void tearDown() {
		driver.quit();
		driver = null;
	}

	@Given("^I navigate to the weather page$")
	public void navigate() {
		driver.get("https://weather-acceptance.herokuapp.com/");
	}

	@When("^I do nothing else$")
	public void nothing() {

	}

	@When("^I click on a day$")
	public void i_click_on_a_day() throws Throwable {
		// click on any one of the days
		Integer randomDay = (int) ((Math.random() * 4) + 1);
		this.clickedDay = driver.findElement(By
				.cssSelector("span[data-test='day-" + randomDay + "']"));
		this.clickedDay.click();
		Assert.assertTrue(
				"After clicking on a day there should be one visible forecast details section",
				numDetailsSectionsDisplayed() == 1);
	}

	@When("^I click on the day again$")
	public void i_click_on_the_day_again() throws Throwable {
		this.clickedDay.click();
		Assert.assertTrue(
				"After clicking on a day there should be no visible forecast details sections",
				numDetailsSectionsDisplayed() == 0);
	}

	@Then("^the forecast for \"(.*)\" is displayed$")
	public void verifyInitialPage(final String expectedCity) {
		// Check the static part of title line
		List<WebElement> list = driver
				.findElements(By
						.xpath("//*[contains(text(),'Five Day Weather Forecast for')]"));
		Assert.assertTrue("'Five Day Weather Forecast for' text not found",
				list.size() > 0);
		// Check the city
		String city = driver.findElement(By.id("city")).getAttribute("value");
		Assert.assertTrue("City should be " + expectedCity + "but it is "
				+ city, city.equals(expectedCity));
	}

	@Then("^the forecast for \"(.*)\" days is displayed$")
	public void the_forecast_for_days_is_displayed(int arg1) throws Throwable {
		// locate the forecast section for each day
		List<WebElement> elements = driver.findElements(By
				.cssSelector("span[data-test^='day-']"));
		Assert.assertTrue("There should be 5 days of forcasts displayed",
				elements.size() == 5);
	}

	@Then("^none of the details sections are displayed$")
	public void none_of_the_details_sections_are_displayed() throws Throwable {
		Assert.assertTrue(
				"None of the forecast details sections should be visible",
				numDetailsSectionsDisplayed() == 0);
	}

	@Then("^the three hourly forecast is displayed$")
	public void the_three_hourly_forecast_is_displayed() throws Throwable {
		// we determine that the 3 hourly details forecast is displayed by
		// checking that the number of visible details sections = 1
		Assert.assertTrue(
				"There should be one visible forecast details section",
				numDetailsSectionsDisplayed() == 1);
	}

	@Then("^the three hourly forecast is hidden$")
	public void the_three_hourly_forecast_is_hidden() throws Throwable {
		Assert.assertTrue(
				"After clicking on a day there should be one visible forecast details section",
				numDetailsSectionsDisplayed() == 0);
	}

	// Counts the number of visible details sections based on the height of
	// the 'details' div
	int numDetailsSectionsDisplayed() {
		List<WebElement> detailsDiv = driver.findElements(By
				.cssSelector("div[class='details']"));
		int numVisibleDetails = 0;
		for (WebElement e : detailsDiv) {
			String style = e.getAttribute("style");
			if (!style.contains("max-height: 0px;")) {
				numVisibleDetails++;
			}
		}
		return numVisibleDetails;
	}

}
