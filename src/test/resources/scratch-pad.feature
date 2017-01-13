@scratchPad
Feature: ScratchPad

Scenario: ScratchPad
Given I navigate to the weather page
When I click on day "2"
Then the three hourly forecast is displayed 
And the summary displays the most dominant wind speed and direction