INSERT INTO investing_com SELECT 'OIL', date_format(str_to_date(source.date_str,'%M %d, %Y'), '%Y%m%d'), source.price  from investing_com_staging_oil_wti source;
INSERT INTO investing_com SELECT 'NG', date_format(str_to_date(source.date_str,'%M %d, %Y'), '%Y%m%d'), source.price  from investing_com_staging_ng source;
INSERT INTO investing_com SELECT 'USD_INDEX', date_format(str_to_date(source.date_str,'%M %d, %Y'), '%Y%m%d'), source.price  from investing_com_staging_usd_index source;
INSERT INTO investing_com SELECT 'USD_CAD', date_format(str_to_date(source.date_str,'%M %d, %Y'), '%Y%m%d'), source.price  from investing_com_staging_usd_cad source;

