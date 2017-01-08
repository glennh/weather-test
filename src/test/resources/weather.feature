@weatherTest

Feature: to test the weather forecast page
I want to navigate to the page and check what is displayed

Scenario: Initial page

Given I navigate to the weather page
When I do nothing else
Then the forecast for "Glasgowx" is displayed
And the forecast for "5" days is displayed
And none of the details sections are displayed


Scenario: Display and Hide the 3 hourly forecast

Given I navigate to the weather page
When I click on a day
Then the three hourly forecast is displayed 
When I click on the day again
Then the three hourly forecast is hidden
