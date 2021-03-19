use financial_products;

drop table investing_com_history;
drop table investing_com_staging_oil_wti;
drop table investing_com_staging_ng;
drop table investing_com_staging_usd_index;
drop table investing_com_staging_usd_cad;
drop table investing_com_staging_euro_fx;
drop table investing_com_staging_us10y;
drop table eia_staging_ng;
drop table eia_staging_oil;
drop table eia_history;
drop table cftc_release_history;
drop table investing_com_staging_gold;

# replace investing_com 
create table  investing_com_history (release_week_tuesday char(8) not null, instrument varchar(20) not null, week_starting char(8) not null, close_price float(11, 4) not null, primary key (instrument, release_week_tuesday))
	PARTITION BY LIST COLUMNS(instrument) (
    PARTITION OIL VALUES IN('OIL'),
    PARTITION NG VALUES IN('NG'),
    PARTITION GOLD VALUES IN('GOLD'),
    PARTITION USD_INDEX VALUES IN('USD_INDEX'),
    PARTITION USD_CAD VALUES IN('USD_CAD'),
    PARTITION EURO_FX VALUES IN('EURO_FX'),
    PARTITION US10Y VALUES IN('US10Y')
);

create table investing_com_staging_oil_wti (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table investing_com_staging_ng (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table investing_com_staging_gold (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table investing_com_staging_usd_index (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table investing_com_staging_usd_cad (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table investing_com_staging_euro_fx (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table investing_com_staging_us10y (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table update_date (vendor varchar(50) not null, update_date char(8) not null, primary key (vendor));

# add euro_fx partition
ALTER TABLE investing_com_history ADD PARTITION (PARTITION EURO_FX VALUES IN('EURO_FX'));

# add us10y partition
ALTER TABLE investing_com_history ADD PARTITION (PARTITION US10Y VALUES IN('US10Y'));

create table eia_staging_ng (week_ending varchar(8),  inventory int);
create table eia_staging_oil (week_ending varchar(8),  inventory int);

# create table eia_history_ng (release_week_tuesday char(8) not null, week_ending char(8) not null,  inventory int not null, primary key(release_week_tuesday));
# create table eia_history_oil (release_week_tuesday char(8) not null, week_ending char(8) not null,  inventory int not null, primary key(release_week_tuesday));

# replace eia_history_ng and eia_history_oil
create table eia_history (release_week_tuesday char(8) not null, instrument varchar(20) not null, week_ending char(8) not null,  inventory int not null, primary key(instrument, release_week_tuesday))
	PARTITION BY LIST COLUMNS(instrument) (
    PARTITION OIL VALUES IN('OIL'),
    PARTITION NG VALUES IN('NG')
);

create table cftc_release_history (release_date char(8) not null, primary key(release_date));

# Note: load_price_csv.sql and extract_price.sql are no longer needed.
