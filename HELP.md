# Usage
To build the application:

from parent folder, run mvn package

1. Create two symbolic links to "C:/Program Files/LibreOffice/program/classes"
	mklink /J "C:\research\github\investinfo\jloputility\lib" "C:/Program Files/LibreOffice/program/classes"
	mklink /J "C:\research\github\investinfo\investinfo\lib" "C:/Program Files/LibreOffice/program/classes"
2. from jloputility, 
mvn clean install
3. from investinfo,
mvn clean package

To run the application:

1. Start MySql database

cd C:\software\mysql-8.0.12-winx64\bin

mysqld --local_infile=1

2. Run any of the commands as listed in README.md

cd C:\research\github\investinfo\investinfo

run

(or other commands below)

# Use cases (CFTC commands)

0. Print Usage: run help

1. Add Documents

	1.1 Add a document for the current year (run -instrument gold adddoc)
	
	1.2 Add a document for all years (2011 - now) (run -allyears -instrument gold adddoc)
	
	1.3 Add documents for a category for the current year (run -category metal adddoc)
	
	1.4 Add documents for a category for all years (run -allyears -category metal adddoc)
	
	1.5 Add documents for all instruments for the current year (run adddoc)
	
	1.6 Add documents for all instruments for all years (run -allyears adddoc)
	
2. Add sheet for a year to the existing document

	2.1 Add a sheet for a instrument for the previous year (run -year 2017 -instrument gold addsheet)
	
	2.2 Add sheets for a category for the previous year (run -year 2017 -category metal addsheet)
	
	2.3 Add sheets for all instruments for the previous year (run -year 2017 addsheet)
	
	2.4 Add sheets for all instruments for the new year (run -year 2021 addsheet)
	
3. Delete sheets

	6.1 Delete sheets for a specific year for an instruments (run -instrument gold -year 2017 rs)
	
	6.2 Delete sheets for a specific year for a category (run -category energy -year 2017 rs)
	
	6.3 Delete sheets for a specific year for all instruments (run -year 2017 rs)
	
4. Update energy inventory

	4.1 Update database inventory history with latest EIA weekly report (run -history -update i)
	
	4.2 Load the complete EIA invenroty history into database (run -history -load i)
	
	4.3 Update the latest inventory to current year NG/Oil sheets (run i)
	
	4.4 Update inventory for a specific year fo all energy sheets ((run -year 2018 i)
	
	4.5 Adjust CFTC release date (run -year 2013 -adjust i)
	
	4.6 Update the last week inventory to current year NG/Oil sheets (run -date 20190702 i) where date is the eia release date)
	
5. Update Price/Index

	5.1 Update database price/index history with latest week Investing.com close price/index (run -history -update p)
	
	5.2 Load the complete Investing.com price/index history into database (run -history -load p)
	
	5.3 Update the latest price/index to current year all instrument sheets (run p)
	
	5.4 Update price/index for a specific year for all instrument sheets (run -year 2018 p)
	
	5.5 Adjust CFTC release date (run -year 2013 -adjust p)
	
	5.6 Update the last week price/index to current year all instrument sheets (run -date 20190702 p) where date is the eia release date)
	
	5.7 TODO Load investing.com price/index history for one instrument (run -instrument euro_fx -load  -history p)
	
	5.8 TODO - download investing.com price/index history for each instrument
	
6. Update all documents with the latest CFTC, EIA, Investing.com data for the week 

	3.1 Manual (run) - (after 6:00 PM Friday and before 2:00 PM Sunday)
	
	3.1 Manual (run -f -date 20190702) - (after 2:00 PM Sunday and before 6:00 PM Friday)
	
	3.3 Scheduler -TODO
	
7. Delete row for a specific date of the current year for all instruments (run d 181211)

8. Update CFTC release history

	8.1 Update CFTC release history for a spacific year (run -year 2018 cftc)
	
	8.2 Update CFTC release history for all years (run cftc)
	
9. Force download option

	9.1 -f or -forcedownload
	
10. Vendor option

	10.1 -v investing_com

11. Charts
		11.1 export chart for an instrument (run -instrument gold -year 2018 chart)
		
12. Market charts
	12.1 Create Marcket charts from scratch (run mc)

13. Update technical analysis

	13.1 Add technical analysis data for a spacific year (run -year 2018 ta)
	
	13.2 Add technical analysis data for all years (run ta) 
	
	13.3 Create technical analysis charts (run tac)
	
Usage:

run help

run -instrument gold adddoc

run -category forex adddoc

run adddoc

run -allyears -instrument ng adddoc

run -allyears -category energy adddoc

run -allyears adddoc


run -instrument gold -year 2017 as

run -category energy -year 2017 as

run -year 2017 as

run -instrument gold -year 2017 rs

run -category energy -year 2017 rs

run -year 2017 rs


run -history -update i

run -history -load i

run i

run -year 2018 i

run -year 2013 -adjust i

run -date 20190702 i


run -history -update p

run -history -load p

run p

run -year 2018 p

run -year 2013 -adjust p

run -date 20190702 p


run

run -date 20190702

run d 181218


run -year 2018 cftc

run cftc

run -instrument gold -year 2018 chart

run -category forex -year 2018 chart

run -year 2018 chart

run -instrument gold chart

run -category forex chart

run chart

Options:

-f, -forcedownload

-v investing_com

-vendor investing_com

-help
