import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinition {
	private WebDriver driver;
	private WebElement dayElement;
	private String targetUrl = "https://weather-acceptance.herokuapp.com/";
	// private String targetUrl = "http://localhost:3000/";
	private TestHelper helper;
	private int selectedDay;

	@Before()
	public void setup() {
		// set the driver path based on os
		String chromeDriverPath = "chromedriver\\chromedriver.exe";
		String os = System.getProperty("os.name");
		if (os.contains("Mac")) {
			chromeDriverPath = "chromedriver/chromedriver";
		}
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);

		driver = new ChromeDriver();

		this.helper = new TestHelper(driver);
	}

	@After()
	public void tearDown() {
		driver.quit();
		driver = null;
	}

	@Given("^I navigate to the weather page$")
	public void navigate() {
		driver.get(targetUrl);
	}

	@When("^I do nothing else$")
	public void nothing() {

	}

	@When("^I click on a day at random$")
	public void i_click_on_a_day() throws Throwable {
		// click on any one of the days
		this.selectedDay = (int) ((Math.random() * 4) + 1);
		this.dayElement = driver.findElement(By.cssSelector("span[data-test='day-" + this.selectedDay + "']"));
		this.dayElement.click();
		assertThat("After clicking on a day there should be one visible forecast details section",
				helper.numDetailsSectionsDisplayed(), is(1));
	}

	@When("^I click on day \"(.*)\"$")
	public void i_click_on_day(final int day) throws Throwable {
		// click on the specified day
		this.selectedDay = day;
		this.dayElement = driver.findElement(By.cssSelector("span[data-test='day-" + this.selectedDay + "']"));
		this.dayElement.click();
		assertThat("After clicking on a day there should be one visible forecast details section",
				helper.numDetailsSectionsDisplayed(), is(1));
	}

	@When("^I click on the day again$")
	public void i_click_on_the_day_again() throws Throwable {
		this.dayElement.click();
		assertThat("After clicking on a day there should be no visible forecast details sections",
				helper.numDetailsSectionsDisplayed(), is(0));
	}

	@Then("^the forecast for \"(.*)\" is displayed$")
	public void verifyInitialPage(final String expectedCity) {
		// Check the static part of title line
		List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'Five Day Weather Forecast for')]"));
		assertThat("'Five Day Weather Forecast for' text not found", list.size(), is(1));
		// Check the city
		String city = driver.findElement(By.id("city")).getAttribute("value");
		assertThat("The displayed city is correct", city, is(expectedCity));
	}

	@Then("^the forecast for \"(.*)\" days is displayed$")
	public void the_forecast_for_days_is_displayed(int arg1) throws Throwable {
		// locate the forecast section for each day
		List<WebElement> elements = driver.findElements(By.cssSelector("span[data-test^='day-']"));
		assertThat("The correct number of forecast days are displayed", elements.size(), is(5));
	}

	@Then("^none of the details sections are displayed$")
	public void none_of_the_details_sections_are_displayed() throws Throwable {
		assertThat("None of the forecast details sections should be visible", helper.numDetailsSectionsDisplayed(),
				is(0));
	}

	@Then("^the three hourly forecast is displayed$")
	public void the_three_hourly_forecast_is_displayed() throws Throwable {
		// we determine that the 3 hourly details forecast is displayed by
		// checking that the number of visible details sections = 1
		// Assert.assertTrue(
		// "There should be one visible forecast details section (" +
		// helper.numDetailsSectionsDisplayed() + ")",
		// helper.numDetailsSectionsDisplayed() == 1);
	}

	@Then("^the three hourly forecast is hidden$")
	public void the_three_hourly_forecast_is_hidden() throws Throwable {
		assertThat("After clicking on a day there is one visible forecast details section",
				helper.numDetailsSectionsDisplayed(), is(0));
	}

	@Then("^the summary displays the most dominant condition$")
	public void the_summary_displays_the_most_dominant_condition() throws Throwable {
		TestHelper.CONDITION summaryCondition = helper.getSummaryConditions(this.selectedDay);
		TestHelper.CONDITION detailsDominantCondition = helper.getDetailsDominantCondition(this.selectedDay);
		assertThat("The summary condition is the dominant condition from the details section", summaryCondition,
				is(detailsDominantCondition));
	}

	@Then("^the summary displays the most dominant wind speed$")
	public void the_summary_displays_the_most_dominant_wind_speed() throws Throwable {
		int summaryWindSpeed = helper.getSummaryWindSpeed(this.selectedDay);
		int dominantWindSpeed = helper.getDominantWindSpeed(this.selectedDay);
		assertThat(summaryWindSpeed, is(dominantWindSpeed));
	}

	@Then("^the summary displays the most dominant wind direction$")
	public void the_summary_displays_the_most_dominant_wind_direction() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@Then("^the summary displays the aggregate rainfall$")
	public void the_summary_displays_the_aggregate_rainfall() throws Throwable {
		int summaryRainfall = helper.getSummaryRainfall(this.selectedDay);
		int aggregateRainfall = helper.getAggregateRainfall(this.selectedDay);
		assertThat(summaryRainfall, is(aggregateRainfall));
	}

	@Then("^the summary displays the min and max temperatures$")
	public void the_summary_displays_the_min_and_max_temperatures() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

}
