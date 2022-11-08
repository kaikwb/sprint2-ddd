package br.com.fiap.models;

import java.util.Date;

public class AssetPrice {
    private final String ticker;
    private final Date date;
    private final double closePrice;
    private double openPrice = Double.NaN;
    private double minPrice = Double.NaN;
    private double maxPrice = Double.NaN;
    private int volume = Integer.MIN_VALUE;

    public AssetPrice(String ticker, Date date, double closePrice) {
        this.ticker = ticker;
        this.date = date;
        this.closePrice = closePrice;
    }

    public AssetPrice(String ticker, Date date, double openPrice, double closePrice, double minPrice, double maxPrice) {
        this.ticker = ticker;
        this.date = date;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public AssetPrice(String ticker, Date date, double openPrice, double closePrice, double minPrice, double maxPrice, int volume) {
        this.ticker = ticker;
        this.date = date;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.volume = volume;
    }

    public String getTicker() {
        return this.ticker;
    }

    public Date getDate() {
        return this.date;
    }

    public double getOpenPrice() {
        if (Double.isNaN(openPrice))
            throw new IllegalArgumentException(String.format("Open price is not set for %s on %s", getTicker(), getDate().toString()));
        else
            return this.openPrice;
    }

    public double getClosePrice() {
        if (Double.isNaN(closePrice))
            throw new IllegalArgumentException(String.format("Close price is not set for %s on %s", getTicker(), getDate().toString()));
        else
            return this.closePrice;
    }

    public double getMinPrice() {
        if (Double.isNaN(this.minPrice))
            throw new IllegalArgumentException(String.format("Min price is not set for %s on %s", getTicker(), getDate().toString()));
        else
            return this.minPrice;
    }

    public double getMaxPrice() {
        if (Double.isNaN(maxPrice))
            throw new IllegalArgumentException(String.format("Max price is not set for %s on %s", getTicker(), getDate().toString()));
        else
            return this.maxPrice;
    }

    public int getVolume() {
        if (this.volume == Integer.MIN_VALUE)
            throw new IllegalArgumentException(String.format("Volume is not set for %s on %s", getTicker(), getDate().toString()));
        else
            return this.volume;
    }
}
