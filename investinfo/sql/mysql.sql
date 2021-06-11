use financial_products;

drop table investing_com_history;
drop table investing_com_staging_oil_wti;
drop table investing_com_staging_ng;
drop table investing_com_staging_usd_index;
drop table investing_com_staging_usd_cad;
drop table investing_com_staging_euro_fx;
drop table investing_com_staging_spx500;
drop table investing_com_staging_nasdaq;
drop table investing_com_staging_dow30;
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
    PARTITION SPX500 VALUES IN('SPX500'),
    PARTITION NASDAQ VALUES IN('NASDAQ'),
    PARTITION DOW30 VALUES IN('DOW30'),
    PARTITION US10Y VALUES IN('US10Y')
);

create table investing_com_staging_oil_wti (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table investing_com_staging_ng (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table investing_com_staging_gold (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table investing_com_staging_usd_index (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table investing_com_staging_usd_cad (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table investing_com_staging_euro_fx (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table investing_com_staging_spx500 (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table investing_com_staging_nasdaq (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table investing_com_staging_dow30 (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table investing_com_staging_us10y (date_str varchar(12), price varchar(12), open varchar(12), high varchar(12), low varchar(12), vol varchar(12), change_percentage varchar(12));
create table update_date (vendor varchar(50) not null, update_date char(8) not null, primary key (vendor));

# add euro_fx partition
ALTER TABLE investing_com_history ADD PARTITION (PARTITION EURO_FX VALUES IN('EURO_FX'));

# add spx500 partition
ALTER TABLE investing_com_history ADD PARTITION (PARTITION SPX500 VALUES IN('SPX500'));

# add nasdaq partition
ALTER TABLE investing_com_history ADD PARTITION (PARTITION NASDAQ VALUES IN('NASDAQ'));

# add dow30 partition
ALTER TABLE investing_com_history ADD PARTITION (PARTITION DOW30 VALUES IN('DOW30'));

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

create table cftc_release_financial_history (release_date char(8) not null, instrument varchar(20) not null, dealer_long int, dealer_short int, asset_mgr_long int, asset_mgr_short int, lev_money_long int, lev_money_short int, other_long int, other_short int, primary key(release_date, instrument));

CREATE OR REPLACE VIEW cftc_forex_view
AS
SELECT  a.release_date,
        a.instrument,
        b.close_price,
		a.dealer_long - a.dealer_short dealer_net_long,
		a.asset_mgr_long - a.asset_mgr_short asset_mgr_net_long,
		a.lev_money_long - a.lev_money_short lev_money_net_long,
		a.other_long - a.other_short other_net_long
FROM    cftc_release_financial_history a
        LEFT JOIN investing_com_history b
            ON a.release_date = b.release_week_tuesday and a.instrument = b.instrument
		ORDER BY a.instrument, a.release_date;

