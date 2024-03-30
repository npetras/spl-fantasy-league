# Design

* Database added to record history data, and will make it easy to edit/add missing data that was not able to be scraped by the Web Scraper.
    * Possibly through a friendly GUI that even non-technical users (fantasy league admins) could probably use


Three microservices:
* Statistics
* Score
* Draft

Statistics collection and related interactions in the Score backend are designed for robustness. The current statistics collection involves scraping from the Smite Pro website, which may often contain missing or incorrect data for our model. Thus the scrapping has been designed to scrape all the possibly data that is available, setting defaults for unavailable data, which can then be corrected manually by an adminstrator users. Collecting at least some data will save the admistrator time -- they will not have to insert all the data from scratch. 
