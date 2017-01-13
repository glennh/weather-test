@weatherTest
Feature: to test the weather forecast page
I want to navigate to the page and check what is displayed

Scenario: Initial page

Given I navigate to the weather page
When I do nothing else
Then the forecast for "Glasgow" is displayed
And the forecast for "5" days is displayed
And none of the details sections are displayed

Scenario: Display and Hide the 3 hourly forecast
Given I navigate to the weather page
When I click on a day at random
Then the three hourly forecast is displayed 
When I click on the day again
Then the three hourly forecast is hidden

Scenario: Check the summary dominant weather conditons
Given I navigate to the weather page
When I click on day "2"
Then the summary displays the most dominant condition

Scenario: Check the summary dominant wind speed
Given I navigate to the weather page
When I click on day "2"
Then the summary displays the most dominant wind speed

Scenario: Check the summary dominant wind direction
Given I navigate to the weather page
When I click on day "2"
Then the summary displays the most dominant wind direction

Scenario: Check the summary aggregate rainfall
Given I navigate to the weather page
When I click on day "2"
Then the summary displays the aggregate rainfall

Scenario: Check the summary min and max temperatures
Given I navigate to the weather page
When I click on day "2"
Then the summary displays the min and max temperatures