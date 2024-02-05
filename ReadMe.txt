Assignment 1:

Create a UI Testing Suite ( You can use any framework you are familiar with, preferably a javascript tool set)

In the test suite, Make sure to test the following page: drata.com

1. Check for each page on this website

2. Assert a page element exists on each page
3. Assert that there are no browser console errors on page load

4. Add a Report when the test is completed to show the number of Passing/Failing cases. 
 

Acceptance Criteria

The code is delivered via GitHub
There’s documentation on how to run the testing suite
The testing suite can be run locally
All tests pass with Report
______________________________________________________
 
Java TestNG and Selenium based web page test.

Given the main web page, it does following:
 - First collect all the sub-page links
 - For each page, checks if Sign In link is available
 - For each page, check if logo is visible and no console errors are present.
 - Checks sign in action using invalid email and valid email

Not all the pages have Drata logo and Sign In option, so few tests are failing. 
Also, I added constraint to limit no of pages to 50.

______________________________________________________

How to Run:

Run as TestNG test from IDE (IntelliJ or Eclipse)

Dependent library jars are attached as lib.zip.

I tested and generated report using IntelliJ.
  - In Edit Run Configuration, from 'Configuration' --> 'Listeners', select 'Use default reports' to generate report in HTML format.

Test NG run report is attached as test-output.zip.
