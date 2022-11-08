package br.com.fiap.main;

import br.com.fiap.data_sources.YahooFinanceDS;
import br.com.fiap.models.Asset;
import br.com.fiap.models.AssetPrice;
import br.com.fiap.models.DatabaseAssistent;
import br.com.fiap.models.User;
import br.com.fiap.payment.Pix;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, ParseException {
//        String db_file = "db.sqlite3";
//        DatabaseAssistent dba;
//        User user;
//
//        dba = new DatabaseAssistent(db_file);
//        user = User.createUser("Kaik", "Bassanelli", "kaikwb@hotmail.com", "123456", dba);
//        List<User> users = User.loadAllUsers(dba);
//        user.setName("Teste");
//        user.saveUser();
//        YahooFinanceDS yf = new YahooFinanceDS();

//        Calendar from = Calendar.getInstance();
//        Calendar to = Calendar.getInstance();
//
//        from.set(2020, Calendar.JANUARY, 0);
//
////        List<AssetPrice> list_prices = yf.getAssetPrice("GOOG", from.getTime(), to.getTime());
//        Asset google = new Asset("Google Inc.", "GOOG", "IT", new Date());
//        Asset apple = new Asset("Apple Inc.", "AAPL", "IT", new Date());
//
//        google.fetchPrices(from.getTime(), to.getTime());
//        apple.fetchPrices(from.getTime(), to.getTime());
        Pix pix = new Pix("pix-key", "MC Name", "0195", "Sao Paulo", "09270080", 35.59);
        System.out.println(pix.getBrCode());
    }
}
