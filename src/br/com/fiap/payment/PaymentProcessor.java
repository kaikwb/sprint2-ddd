package br.com.fiap.payment;

public class PaymentProcessor {
    private static final String merchant_name = "LucreMais Ltda.";
    private static final String mcc = "6012";
    private static final String merchant_city = "Sao Paulo";
    private static final String postal_code = "01311000";

    public static Pix createPix(String pix_key, double value) {
        return new Pix(pix_key, merchant_name, mcc, merchant_city, postal_code, value);
    }

    public static boolean processCardPayment(double value, Card card) {
        return (Integer.parseInt(card.getCvv()) % 2) != 0;
    }
}
