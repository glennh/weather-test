import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TestHelper {
	private WebDriver driver;

	public enum CONDITION {
		RAIN, CLOUDS
	};

	private Map<TestHelper.CONDITION, Integer> conditionPriorities = new HashMap<TestHelper.CONDITION, Integer>();

	public TestHelper() {
		// This constructor is only used when unit testing the non selenium
		// functions in this class.
	}

	public TestHelper(WebDriver driver) {
		this.driver = driver;

		// Initialise the weather conditions priorities.
		this.conditionPriorities.put(CONDITION.RAIN, 1);
		this.conditionPriorities.put(CONDITION.CLOUDS, 2);

	}

	// Counts the number of visible details sections based on the height of
	// the 'details' div.
	public int numDetailsSectionsDisplayed() {
		List<WebElement> detailsDiv = driver.findElements(By.cssSelector("div[class='details']"));
		int numVisibleDetails = 0;
		for (WebElement e : detailsDiv) {
			String style = e.getAttribute("style");
			if (!style.contains("max-height: 0px;")) {
				numVisibleDetails++;
			}
		}
		return numVisibleDetails;
	}

	// Gets the weather conditions for the day summary.
	public CONDITION getSummaryConditions(int day) {
		// To ensure we get the condition for the day summary and not the day
		// details we search for the specific description-day label.
		WebElement conditionElement = driver.findElement(By.cssSelector("svg[data-test='description-" + day + "']"));
		String condition = conditionElement.getAttribute("aria-label");
		return CONDITION.valueOf(condition.toUpperCase());
	}

	// Gets the weather conditions for the day expanded details.
	public List<CONDITION> getDetailsConditions(int day) {
		List<CONDITION> conditions = new ArrayList<CONDITION>();
		// A key element of the selenium selector is the final '-'. This ensures
		// we only get the conditions for the detail elements and not for the
		// summary.
		List<WebElement> conditionElements = driver
				.findElements(By.cssSelector("svg[data-test^='description-" + day + "-']"));
		for (WebElement conditionElement : conditionElements) {
			String condition = conditionElement.getAttribute("aria-label");

			CONDITION c = CONDITION.valueOf(condition.toUpperCase());
			if (!conditions.contains(c)) {
				conditions.add(c);
			}
		}
		return conditions;
	}

	// Gets the dominant weather condition for the specified day
	// ([GH 13/01/2017] When this was written I thought the dominant weather
	// condition was the 'worst' weather of the day.
	// Now I think it may be the most common weather condition of the day. In a
	// real situation I'd ask a product owner
	// or SME. I'm going to leave the current implementation in place.)
	public CONDITION getDetailsDominantCondition(int day) {
		CONDITION dominantCondition;
		List<CONDITION> conditions = getDetailsConditions(day);

		// Initialise the dominant condition to the first one in the list.
		dominantCondition = conditions.get(0);
		int priority = this.conditionPriorities.get(conditions.get(0));

		// Check for a more dominant condition
		for (CONDITION condition : conditions) {
			priority = this.conditionPriorities.get(condition);
			if (this.conditionPriorities.get(dominantCondition) > priority) {
				dominantCondition = condition;
			}
		}
		return dominantCondition;
	}

	// Gets the wind speed for the day summary.
	public int getSummaryWindSpeed(int day) {
		// To ensure we get the wind speed for the day summary and not the day
		// details we search for the specific description-day label.
		WebElement windSpeedElement = driver.findElement(By.cssSelector("span[data-test='speed-" + day + "']"));
		String windSpeed = windSpeedElement.getText().replaceAll("kph", "");
		// Convert the wind speed to an integer. If we've got a value for
		// wind speed that's not an int an error will be thrown... thats
		// what we want.
		return Integer.parseInt(windSpeed);
	}

	// Gets the wind speeds for the day expanded details.
	public List<Integer> getWindSpeeds(int day) {
		List<Integer> windSpeeds = new ArrayList<Integer>();
		// A key element of the selenium selector is the final '-'. This ensures
		// we only get the wind speed for the detail elements and not for the
		// summary.
		List<WebElement> windSpeedElements = driver
				.findElements(By.cssSelector("span[data-test^='speed-" + day + "-']"));
		for (WebElement windSpeedElement : windSpeedElements) {
			String windSpeed = windSpeedElement.getText().replaceAll("kph", "");
			// Convert the wind speed to an integer. If we've got a value for
			// wind speed that's not an int an error will be thrown... thats
			// what we want.
			windSpeeds.add(Integer.parseInt(windSpeed));
		}
		return windSpeeds;
	}

	// Gets the dominant wind speed for the specified day
	// This is the wind speed that occurs most often. If there are multiple wind
	// speeds
	// with the same number of occurrences we use the average of those values.
	// (this approach needs to be agreed by product owner / SME)
	public int getDominantWindSpeed(int day) {
		List<Integer> windSpeeds = getWindSpeeds(day);
		return getDominantValue(windSpeeds);
	}

	// Gets the summary rainfall
	public int getSummaryRainfall(int day) {
		// To ensure we get the railfall for the day summary and not the day
		// details we search for the specific description-day label.
		WebElement rainfallElement = driver.findElement(By.cssSelector("span[data-test='rainfall-" + day + "']"));
		String rainfall = rainfallElement.getText().replaceAll("mm", "");
		// Convert the rainfall to an integer. If we've got a value for
		// wind speed that's not an int an error will be thrown... thats
		// what we want.
		return Integer.parseInt(rainfall);
	}

	// Gets the aggregate rainfall for the day expanded details.
	public int getAggregateRainfall(int day) {
		int aggregateRainfall = 0;
		// A key element of the selenium selector is the final '-'. This ensures
		// we only get the rainfall for the detail elements and not for the
		// summary.
		List<WebElement> rainfallElements = driver
				.findElements(By.cssSelector("span[data-test^='rainfall-" + day + "-']"));
		for (WebElement rainfallElement : rainfallElements) {
			String rainfall = rainfallElement.getText().replaceAll("mm", "");
			// Convert the rainfall to an integer. If we've got a value for
			// wind speed that's not an int an error will be thrown... thats
			// what we want.
			aggregateRainfall += Integer.parseInt(rainfall);
		}
		return aggregateRainfall;
	}

	// Gets the summary maximum and minimum temperatures as a string: max,min.
	public String getSummaryTemps(int day) {
		WebElement maxTempElement = driver.findElement(By.cssSelector("span[data-test='maximum-" + day + "']"));
		WebElement minTempElement = driver.findElement(By.cssSelector("span[data-test='minimum-" + day + "']"));

		String maxTemp = maxTempElement.getText();
		// remove the degree symbol
		maxTemp = maxTemp.substring(0, maxTemp.length() - 1);

		String minTemp = minTempElement.getText();
		// remove the degree symbol
		minTemp = minTemp.substring(0, minTemp.length() - 1);

		return maxTemp + "," + minTemp;
	}

	// Gets the highest
	public String getDayMayMinTemps(int day) {
		List<WebElement> maxTempElements = driver
				.findElements(By.cssSelector("span[data-test^='maximum-" + day + "-']"));
		List<WebElement> minTempElements = driver
				.findElements(By.cssSelector("span[data-test^='maximum-" + day + "-']"));
		List<Integer> minTemps = new ArrayList<Integer>();
		List<Integer> maxTemps = new ArrayList<Integer>();

		// Get max temps as integers'
		for (WebElement element : maxTempElements) {
			//
			String tempString = element.getText();
			// remove the degree symbol
			tempString = tempString.substring(0, tempString.length() - 1);
			// Convert the temperature to an integer. If we've got a value for
			// that's not an int an error will be thrown... thats
			// what we want.
			int temp = Integer.parseInt(tempString);
			maxTemps.add(temp);
		}

		// Get min temps as integers
		for (WebElement element : minTempElements) {
			//
			String tempString = element.getText();
			// remove the degree symbol
			tempString = tempString.substring(0, tempString.length() - 1);
			// Convert the temperature to an integer. If we've got a value for
			// that's not an int an error will be thrown... thats
			// what we want.
			int temp = Integer.parseInt(tempString);
			minTemps.add(temp);
		}

		// Return the highest max temp and loweest min temp of the day.
		int maxTemp = Collections.max(maxTemps);
		int minTemp = Collections.min(minTemps);
		return maxTemp + "," + minTemp;
	}

	// Gets the dominant value of a list of integers
	public int getDominantValue(List<Integer> values) {
		// Find the most common element
		// Create a hash of value <> number of times the value occurs
		Map<Integer, Integer> occurances = new HashMap<Integer, Integer>();
		for (int windSpeed : values) {
			int count = occurances.containsKey(windSpeed) ? occurances.get(windSpeed) : 0;
			occurances.put(windSpeed, count + 1);
		}
		// Get the highest occurrence value
		int maxOcurrences = 0;
		for (Map.Entry<Integer, Integer> entrySet : occurances.entrySet()) {
			int value = entrySet.getValue();
			if (value > maxOcurrences) {
				maxOcurrences = value;
			}
		}
		// Get a list of wind speeds with max occurrences (there may be more
		// than one)
		List<Integer> maxWindSpeeds = new ArrayList<Integer>();
		for (Map.Entry<Integer, Integer> entrySet : occurances.entrySet()) {
			int windSpeed = entrySet.getKey();
			int occurences = entrySet.getValue();
			if (occurences == maxOcurrences) {
				maxWindSpeeds.add(windSpeed);
			}
		}
		// Now we need the average value of the wind speeds with max occurrence
		// Casting to int will round down... that's what we want
		int dominantWindSpeed = (int) maxWindSpeeds.stream().mapToInt(i -> i).average().orElse(0);
		return dominantWindSpeed;
	}

}
