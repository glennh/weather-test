@weatherTest
Feature: to test the weather forecast page
I want to navigate to the page and check what is displayed

# This testing determines the expected summary values by processing the detail values of the 
# specified day.  An alternative approach, requiring less coding effort would be to use fixed
# input data and hard-coded expected values.

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

# How do we calculate dominant wind direction?  
# I found some data here: http://math.stackexchange.com/questions/44621/calculate-average-wind-direction.
# The app seems to be displaying the first wind value from the day details as the summary wind direction.
# I need some conversation with the product owner or SME before implementing dominant wind direction checking.
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