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

	private Map<CONDITION, Integer> conditionPriorities = new HashMap();

	public TestHelper(WebDriver driver) {
		this.driver = driver;

		// initialise the conditions priorities
		this.conditionPriorities.put(CONDITION.RAIN, 1);
		this.conditionPriorities.put(CONDITION.CLOUDS, 2);

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

	// Gets the weather conditions for the day summary
	CONDITION getSummaryConditions(Integer day) {
		// to ensure we get the condition for the day summary and not the day
		// details we search for the specific description-day label
		WebElement conditionElement = driver.findElement(By
				.cssSelector("svg[data-test='description-" + day + "']"));
		String condition = conditionElement.getAttribute("aria-label");
		return CONDITION.valueOf(condition.toUpperCase());
	}

	// Gets the weather conditions for the day expanded details
	List<CONDITION> getDetailsConditions(Integer day) {
		List<CONDITION> conditions = new ArrayList<CONDITION>();
		// a key element of the selenium selector is the final '-'. This ensures
		// we only get the conditions for the detail elements and not for the
		// summary
		List<WebElement> conditionElements = driver.findElements(By
				.cssSelector("svg[data-test^='description-" + day + "-']"));
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
	CONDITION getDetailsDominantCondition(Integer day) {
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

}
