package br.com.fiap.data_sources;

import br.com.fiap.models.AssetPrice;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface DataSource {
    abstract List<AssetPrice> sendRequest(String ticker, Date start_date, Date end_date);

    public default List<AssetPrice> getAssetPrice(String ticker, Date start_date, Date end_date) {
        return sendRequest(ticker, start_date, end_date);
    }
}
