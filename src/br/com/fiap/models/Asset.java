package br.com.fiap.models;

import br.com.fiap.data_sources.YahooFinanceDS;
import br.com.fiap.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Asset {
    private static final String table_name = "Assets";
    private final YahooFinanceDS data_source;
    private final int asset_code;
    private final String name;
    private final String ticker;
    private final String sector;
    private final Date ipo_date;

    private final DatabaseAssistent dba;
    private AssetPrice[] price_history;

    public Asset(int asset_code, String name, String ticker, String sector, Date ipo_date, DatabaseAssistent dba) {
        this.asset_code = asset_code;
        this.name = name;
        this.ticker = ticker;
        this.sector = sector;
        this.ipo_date = ipo_date;
        this.dba = dba;

        this.data_source = new YahooFinanceDS();
    }

    private static void createTable(DatabaseAssistent dba) {
        String sql_create = String.format(
                "CREATE TABLE %s (" +
                        "asset_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name text NOT NULL," +
                        "ticker text NOT NULL," +
                        "sector text NOT NULL," +
                        "ipo_date TEXT" +
                        ");",
                table_name
        );

        dba.executeSQL(sql_create);
    }

    public static Asset createAsset(String name, String ticker, String sector, Date ipo_date, DatabaseAssistent dba) {
        if (!dba.checkTableExists(table_name))
            createTable(dba);

        String sql_insert = String.format(
                "INSERT INTO %s (name, ticker, sector, ipo_date) " +
                        "VALUES ('%s', '%s', '%s', '%s');", table_name, name, ticker, sector, Utils.dateToDbDate(ipo_date)
        );

        int asset_code = dba.executeInset(sql_insert);

        return new Asset(asset_code, name, ticker, sector, ipo_date, dba);
    }

    private static Asset createFromResultSet(DatabaseAssistent dba, ResultSet rs) throws SQLException, ParseException {
        return new Asset(
                rs.getInt(1), rs.getString(2), rs.getString(3),
                rs.getString(4), Utils.dateFromDbFormat(rs.getString(5)), dba);
    }

    public static Asset[] loadAllAsset(DatabaseAssistent dba) throws SQLException, ParseException {
        if (!dba.checkTableExists(table_name))
            createTable(dba);

        ResultSet rs = dba.loadAll(table_name);

        if (rs != null) {
            List<Asset> assets = new ArrayList<Asset>();

            while (rs.next()) {
                assets.add(createFromResultSet(dba, rs));
            }

            return assets.toArray(Asset[]::new);
        }

        return null;
    }

    public void saveAsset() {
        if (!dba.checkTableExists(table_name))
            createTable(dba);

        String sql_update = String.format(
                "UPDATE %s " +
                        "SET name='%s', ticker='%s', sector='%s', ipo_date='%s' " +
                        "WHERE asset_id=%d;",
                table_name,
                name, ticker, sector, Utils.dateToDbDate(ipo_date),
                asset_code
        );

        dba.executeSQL(sql_update);
    }

    public void fetchPrices(Date start_Date, Date end_date) {
        List<AssetPrice> history = data_source.getAssetPrice(ticker, start_Date, end_date);
        price_history = new AssetPrice[history.size()];
        price_history = history.toArray(price_history);
    }

    public String getName() {
        return name;
    }

    public String getTicker() {
        return ticker;
    }

    public String getSector() {
        return sector;
    }

    public Date getIpo_date() {
        return ipo_date;
    }

    public AssetPrice[] getPrice_history() {
        return price_history;
    }
}
