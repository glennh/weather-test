import java.util.ArrayList;
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
		// this constructor is only used when unit testing the non selenium
		// functions in this class.
	}

	public TestHelper(WebDriver driver) {
		this.driver = driver;

		// initialise the conditions priorities
		this.conditionPriorities.put(CONDITION.RAIN, 1);
		this.conditionPriorities.put(CONDITION.CLOUDS, 2);

	}

	// Counts the number of visible details sections based on the height of
	// the 'details' div
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

	// Gets the weather conditions for the day summary
	public CONDITION getSummaryConditions(Integer day) {
		// to ensure we get the condition for the day summary and not the day
		// details we search for the specific description-day label
		WebElement conditionElement = driver.findElement(By.cssSelector("svg[data-test='description-" + day + "']"));
		String condition = conditionElement.getAttribute("aria-label");
		return CONDITION.valueOf(condition.toUpperCase());
	}

	// Gets the weather conditions for the day expanded details
	public List<CONDITION> getDetailsConditions(Integer day) {
		List<CONDITION> conditions = new ArrayList<CONDITION>();
		// a key element of the selenium selector is the final '-'. This ensures
		// we only get the conditions for the detail elements and not for the
		// summary
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
	public CONDITION getDetailsDominantCondition(Integer day) {
		CONDITION dominantCondition;
		List<CONDITION> conditions = getDetailsConditions(day);

		// initialise the dominant condition to the first one in the list.
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

	// Gets the wind speed for the day summary
	public String getSummaryWindSpeed(Integer day) {
		// to ensure we get the wind speed for the day summary and not the day
		// details we search for the specific description-day label
		WebElement windSpeedElement = driver.findElement(By.cssSelector("span[data-test='speed-" + day + "']"));
		String windSpeed = windSpeedElement.getText().replaceAll("kph", "");
		return windSpeed;
	}

	// Gets the wind speeds for the day expanded details
	public List<Integer> getWindSpeeds(Integer day) {
		List<Integer> windSpeeds = new ArrayList<Integer>();
		// a key element of the selenium selector is the final '-'. This ensures
		// we only get the wind speed for the detail elements and not for the
		// summary
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
	public Integer getDominantWindSpeed(Integer day) {
		List<Integer> windSpeeds = getWindSpeeds(day);
		return getDominantValue(windSpeeds);
	}

	// Gets the dominant value of a list of integers
	public Integer getDominantValue(List<Integer> values) {
		// Find the most common element
		// Create a hash of value <> number of times the value occurs
		Map<Integer, Integer> occurances = new HashMap<Integer, Integer>();
		for (Integer windSpeed : values) {
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
			Integer windSpeed = entrySet.getKey();
			int occurences = entrySet.getValue();
			if (occurences == maxOcurrences) {
				maxWindSpeeds.add(windSpeed);
			}
		}
		// Now we need the average value of the wind speeds with max occurrence
		// Casting to int will round down... that's what we want
		Integer dominantWindSpeed = (int) maxWindSpeeds.stream().mapToInt(i -> i).average().orElse(0);
		return dominantWindSpeed;
	}
}
