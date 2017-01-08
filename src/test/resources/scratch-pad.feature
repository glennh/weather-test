@scratchPad
Feature: Scenario scratch-pad

Scenario: Expand day when another day is already expanded
Given I navigate to the weather page
And I have clicked on a day
When I click on a different day
Then the first day is closed
And the three hourly forecast is displayed for the most recently clicked day