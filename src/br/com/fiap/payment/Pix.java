package br.com.fiap.payment;

import br.com.fiap.utils.RandomString;
import com.emv.qrcode.core.model.mpm.TagLengthString;
import com.emv.qrcode.model.mpm.*;


public class Pix {

    private final String pix_key;
    private final String txid;
    private final String merchant_name;
    private final String mcc;
    private final String merchant_city;
    private final String postal_code;
    private final int currency;
    private final double transaction_amount;
    private final String br_code;

    public Pix(String pix_key, String merchant_name, String mcc, String merchant_city, String postal_code, double transaction_amount) {
        this.pix_key = pix_key;
        this.merchant_name = merchant_name;
        this.mcc = mcc;
        this.merchant_city = merchant_city;
        this.postal_code = postal_code;
        this.transaction_amount = transaction_amount;

        // Fix payment in BRL
        this.currency = 986;

        int TXID_SIZE = 35;
        this.txid = new RandomString(TXID_SIZE).nextString();

        MerchantPresentedMode emv_code = new MerchantPresentedMode();
        String BRAZIL_COUNTRY_CODE = "BR";
        emv_code.setCountryCode(BRAZIL_COUNTRY_CODE);
        emv_code.setMerchantCategoryCode(mcc);
        emv_code.setMerchantCity(merchant_city);
        emv_code.setMerchantName(merchant_name);
        emv_code.setPostalCode(postal_code);
        emv_code.setTransactionAmount(String.format("%.2f", transaction_amount));
        emv_code.setTransactionCurrency(String.format("%03d", this.currency));
        emv_code.addMerchantAccountInformation(getMerchantAccountInformation());
        emv_code.setAdditionalDataField(getAdditionalDataField());
        emv_code.setPayloadFormatIndicator("01");
        this.br_code = emv_code.toString();
    }

    private MerchantAccountInformationTemplate getMerchantAccountInformation() {
        final TagLengthString paymentNetworkSpecific = new TagLengthString();
        String PIX_KEY_TAG = "01";
        paymentNetworkSpecific.setTag(PIX_KEY_TAG);
        paymentNetworkSpecific.setValue(pix_key);

        final MerchantAccountInformationReservedAdditional merchantAccountInformationValue = new MerchantAccountInformationReservedAdditional();
        String BCB_GUI = "BR.GOV.BCB.PIX";
        merchantAccountInformationValue.setGloballyUniqueIdentifier(BCB_GUI);
        merchantAccountInformationValue.addPaymentNetworkSpecific(paymentNetworkSpecific);

        String MAI_TAG = "26";
        return new MerchantAccountInformationTemplate(MAI_TAG, merchantAccountInformationValue);
    }

    private AdditionalDataFieldTemplate getAdditionalDataField() {
        final AdditionalDataField additionalDataFieldValue = new AdditionalDataField();
        additionalDataFieldValue.setReferenceLabel(txid);

        final AdditionalDataFieldTemplate additionalDataField = new AdditionalDataFieldTemplate();
        additionalDataField.setValue(additionalDataFieldValue);

        return additionalDataField;
    }

    public String getTxid() {
        return txid;
    }

    public String getPixKey() {
        return pix_key;
    }

    public String getMerchantName() {
        return merchant_name;
    }

    public String getMcc() {
        return mcc;
    }

    public String getMerchantCity() {
        return merchant_city;
    }

    public String getPostalCode() {
        return postal_code;
    }

    public int getCurrency() {
        return currency;
    }

    public double getTransactionAmount() {
        return transaction_amount;
    }

    public String getBrCode() {
        return br_code;
    }

    public boolean verifyPayment() {
        return true;
    }
}
