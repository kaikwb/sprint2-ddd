package br.com.fiap.models;

import br.com.fiap.data_sources.YahooFinanceDS;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

public class Asset {
    private final YahooFinanceDS data_source;
    private int asset_code;
    private final String name;
    private final String ticker;
    private final String sector;
    private final Date ipo_date;
    private AssetPrice[] price_history;

    public Asset(String name, String ticker, String sector, Date ipo_date) {
        this.name = name;
        this.ticker = ticker;
        this.sector = sector;
        this.ipo_date = ipo_date;

        this.data_source = new YahooFinanceDS();
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
