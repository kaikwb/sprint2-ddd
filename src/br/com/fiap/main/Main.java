package br.com.fiap.main;

import br.com.fiap.assets_calc.AssetsCalc;
import br.com.fiap.models.Asset;
import br.com.fiap.models.DatabaseAssistent;
import br.com.fiap.models.User;
import br.com.fiap.payment.Card;
import br.com.fiap.payment.PaymentProcessor;
import br.com.fiap.payment.Pix;
import de.vandermeer.asciitable.AsciiTable;

import javax.swing.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

enum OPTIONS {
    EXIT_SYSTEM, CREATE_USER, LIST_USERS, CREATE_ASSET, LIST_ASSETS, CALC_CORRELATION, CREATE_PIX_PAYMENT, PAY_CARD_BILL;

    public static OPTIONS fromInt(int code) {
        return values()[code];
    }
}

public class Main {
    private static final String DB_FILE = "db.sqlite3";
    private static DatabaseAssistent dba;

    private static void initSystem() throws SQLException {
        dba = new DatabaseAssistent(DB_FILE);
    }

    private static void printMsg(String msg) {
        System.out.println(msg);
    }

    private static Date inputDate(String msg) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(JOptionPane.showInputDialog(msg));
    }

    private static String formatTableUsers(User[] users) {
        AsciiTable at = new AsciiTable();

        at.addRule();
        at.addRow("Nome", "Sobrenome", "Email", "Senha");
        at.addRule();

        assert users != null;
        for (User user : users) {
            at.addRow(user.getName(), user.getLast_name(), user.getMail(), user.getPassword());
            at.addRule();
        }

        return at.render();
    }

    private static String formatTableAssets(Asset[] assets) {
        AsciiTable at = new AsciiTable();

        at.addRule();
        at.addRow("Nome", "Ticker", "Setor", "Data IPO");
        at.addRule();

        assert assets != null;
        for (Asset asset : assets) {
            at.addRow(asset.getName(), asset.getTicker(), asset.getSector(), asset.getIpo_date());
            at.addRule();
        }

        return at.render();
    }

    private static void createUser() {
        String name = JOptionPane.showInputDialog("Digite o nome do usuario: ");
        String last_name = JOptionPane.showInputDialog("Digite o sobrenome do usuario: ");
        String mail = JOptionPane.showInputDialog("Digite o email do usuario: ");
        String password = JOptionPane.showInputDialog("Digite a senha do usuario: ");

        User.createUser(name, last_name, mail, password, dba);
        printMsg("Usuario criado com sucesso.");
    }

    private static void listUsers() throws SQLException, ParseException {
        printMsg("Listando usuarios");
        User[] users = User.loadAllUsers(dba);

        assert users != null;
        printMsg(formatTableUsers(users));
    }

    private static void createAsset() throws ParseException {
        String name = JOptionPane.showInputDialog("Digite o nome do ativo: ");
        String ticker = JOptionPane.showInputDialog("Digite o ticker do ativo: ");
        String sector = JOptionPane.showInputDialog("Digite o setor do ativo: ");
        Date ipo_date = inputDate("Digite a data de IPO (dd/mm/aaaa): ");
        Asset.createAsset(name, ticker, sector, ipo_date, dba);
        printMsg("Ativo criado com sucesso.");
    }

    private static void listAssets() throws SQLException, ParseException {
        printMsg("Listando ativos");
        Asset[] assets = Asset.loadAllAsset(dba);

        assert assets != null;
        printMsg(formatTableAssets(assets));
    }

    private static void calcCorrelation() throws SQLException, ParseException {
        Asset[] assets = Asset.loadAllAsset(dba);

        StringBuilder msg = new StringBuilder();

        for (int i = 0; i < assets.length; i++) {
            msg.append(String.format("%2d: %s - %s\r\n", i, assets[i].getTicker(), assets[i].getName()));
        }

        Asset asset_1 = assets[Integer.parseInt(JOptionPane.showInputDialog("Selecione um ativo na lista:\r\n" + msg))];
        Asset asset_2 = assets[Integer.parseInt(JOptionPane.showInputDialog("Selecione outro ativo na lista:\r\n" + msg))];

        Date date_start = inputDate("Selecione a data de inicio do calculo (dd/mm/aaaa): ");
        Date date_end = inputDate("Selecione a data final do calculo (dd/mm/aaaa): ");

        asset_1.fetchPrices(date_start, date_end);
        asset_2.fetchPrices(date_start, date_end);

        double correlation = AssetsCalc.calcCorrelation(asset_1, asset_2);

        printMsg(String.format("A correlacao entre %s e %s e: %f", asset_1.getName(), asset_2.getName(), correlation));
    }

    private static void createPixPayment() {
        String pix_key = JOptionPane.showInputDialog("Digite a chave Pix: ");
        double value = Double.parseDouble(JOptionPane.showInputDialog("Digite o valor da cobranca: "));
        Pix pix = PaymentProcessor.createPix(pix_key, value);

        printMsg("Pix criado com sucesso.\r\nExibindo codigo da cobranca\r\n" + pix.getBrCode());
    }

    private static void payCardBill() throws ParseException {
        double value = Double.parseDouble(JOptionPane.showInputDialog("Digite o valor da cobracao: "));
        String card_holder = JOptionPane.showInputDialog("Digite o nome do portador do cartao: ");
        String holder_doc = JOptionPane.showInputDialog("Digite o CPF do portador do cartao: ");
        String card_number = JOptionPane.showInputDialog("Digite o numero do cartao: ");
        String cvv = JOptionPane.showInputDialog("Digite o codigo de verificacao do cartao (CVV): ");
        Date valid_date = new SimpleDateFormat("MM/yy").parse(JOptionPane.showInputDialog("Digite a validade do cartao (mm/aa)"));

        Card card = new Card(card_holder, holder_doc, card_number, cvv, valid_date);
        if (PaymentProcessor.processCardPayment(value, card)) {
            printMsg("Pagamento realizado com sucesso.");
        } else {
            printMsg("Pagamento nao realizado, verifique as informacoes digitadas.");
        }
    }

    public static void main(String[] args) throws SQLException, ParseException {
        initSystem();

        while (true) {
            try {
                OPTIONS op = OPTIONS.fromInt(Integer.parseInt(JOptionPane.showInputDialog("""
                                Digite uma opcao:
                                0: Sair do sistema
                                1: Criar usuario
                                2: Listar usuarios
                                3: Criar ativo
                                4: Listar ativos
                                5: Calcular correlacao
                                6: Criar pagamento Pix
                                7: Pagar cobraca com cartao
                        """)));

                switch (op) {
                    case EXIT_SYSTEM -> System.exit(0);
                    case CREATE_USER -> createUser();
                    case LIST_USERS -> listUsers();
                    case CREATE_ASSET -> createAsset();
                    case LIST_ASSETS -> listAssets();
                    case CALC_CORRELATION -> calcCorrelation();
                    case CREATE_PIX_PAYMENT -> createPixPayment();
                    case PAY_CARD_BILL -> payCardBill();
                }
            } catch (NumberFormatException e) {
                continue;
            }
        }
    }
}
