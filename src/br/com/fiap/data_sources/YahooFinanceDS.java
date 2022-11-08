package br.com.fiap.data_sources;

import br.com.fiap.models.AssetPrice;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class YahooFinanceDS implements DataSource {

    @Override
    public List<AssetPrice> sendRequest(String ticker, Date start_date, Date end_date) {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();

        from.setTime(start_date);
        to.setTime(end_date);

        try {
            Stock stock = YahooFinance.get(ticker, from, to, Interval.DAILY);
            List<HistoricalQuote> history = stock.getHistory();
            List<AssetPrice> result = new ArrayList<AssetPrice>();

            for (HistoricalQuote quote : history) {
                AssetPrice asset_price = new AssetPrice(
                        ticker, quote.getDate().getTime(), quote.getOpen().doubleValue(),
                        quote.getClose().doubleValue(), quote.getLow().doubleValue(), quote.getHigh().doubleValue(),
                        quote.getVolume().intValue()
                );
                result.add(asset_price);
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
