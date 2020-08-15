LOAD DATA LOCAL INFILE  
'C:\\Users\\danny.hui\\research\\bitbucket\\cftc\\cftc-v1\\cftc\\marketmakers\\investing_com\\Crude Oil WTI Futures Historical Data.csv'
INTO TABLE investing_com_staging_oil_wti
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS
(date_str, price, open, high, low, vol, change_percentage);

LOAD DATA LOCAL INFILE  
'C:\\Users\\danny.hui\\research\\bitbucket\\cftc\\cftc-v1\\cftc\\marketmakers\\investing_com\\Natural Gas Futures Historical Data.csv'
INTO TABLE investing_com_staging_ng 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS
(date_str, price, open, high, low, vol, change_percentage);

LOAD DATA LOCAL INFILE  
'C:\\Users\\danny.hui\\research\\bitbucket\\cftc\\cftc-v1\\cftc\\marketmakers\\investing_com\\US Dollar Index Futures Historical Data.csv'
INTO TABLE investing_com_staging_usd_index 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS
(date_str, price, open, high, low, change_percentage);

LOAD DATA LOCAL INFILE  
'C:\\Users\\danny.hui\\research\\bitbucket\\cftc\\cftc-v1\\cftc\\marketmakers\\investing_com\\USD_CAD Historical Data.csv'
INTO TABLE investing_com_staging_usd_cad 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS
(date_str, price, open, high, low, change_percentage);