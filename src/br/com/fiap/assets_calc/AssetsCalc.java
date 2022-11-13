package br.com.fiap.assets_calc;

import br.com.fiap.models.Asset;
import br.com.fiap.models.AssetPrice;

public class AssetsCalc {
    public static double calcCorrelation(Asset assetRef, Asset assetCompare) {
        double sx = 0.0;
        double sy = 0.0;
        double sxx = 0.0;
        double syy = 0.0;
        double sxy = 0.0;

        AssetPrice[] xs = assetRef.getPrice_history();
        AssetPrice[] ys = assetCompare.getPrice_history();

        int n = xs.length;

        for (int i = 0; i < n; ++i) {
            double x = xs[i].getClosePrice();
            double y = ys[i].getClosePrice();

            sx += x;
            sy += y;
            sxx += x * x;
            syy += y * y;
            sxy += x * y;
        }

        double cov = sxy / n - sx * sy / n / n;
        double sigmax = Math.sqrt(sxx / n - sx * sx / n / n);
        double sigmay = Math.sqrt(syy / n - sy * sy / n / n);

        return cov / sigmax / sigmay;
    }
}
