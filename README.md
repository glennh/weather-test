This application demonstrates use of java, maven, cucumber and selenium for testing the 5 day weather forecast 
found here: https://github.com/buildit/acceptance-testing

### Features

* Cucumber tests
 * in src/test/resources/weather.feature
* Selenium used to drive Firefox browser
* Outputs produced in reports/test-report/

The tests currently run against the public instance of the weather app at https://weather-acceptance.herokuapp.com/.  
A locally hosted instance of the weather app can be run by following the 'Running the app locally' instructions at 
https://github.com/buildit/acceptance-testing and updating the value of targetUrl in StepDefinitions.java.

### Prerequisites
* git
* maven
* firefox
* gecko driver
  * https://github.com/mozilla/geckodriver/releases
  * saved as c:\projects\gecko\geckodrver.exe for Windows
  * or ~/projects/gecko/geckodriver for Mac
  
### Running the tests
* Clone the repo to a local folder
* Cd to the local folder
* Run mvn install
* Results are displayed on screen and in reports/test-report/index.html