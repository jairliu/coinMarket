package market;

import javax.swing.*;

public class PerformanceWatcherForm {
    private JPanel root;

    public static volatile String coinName = "EOS";
    public static volatile int refreshTime = 10;
    public static volatile int pricePrecision = 3;

    private JTextField coinNameField;
    private JTextField refreshTimeField;
    private JTextField pricePrecisionField;

    public PerformanceWatcherForm() {
        coinName = coinNameField.getText();
        refreshTime = Integer.valueOf(refreshTimeField.getText());
        pricePrecision = Integer.valueOf(pricePrecisionField.getText());
    }

    public JPanel getRoot() {
        return root;
    }

    public boolean isModified() {
        try {
            return !coinName.equals(coinNameField.getText())
                    || refreshTime != Integer.valueOf(refreshTimeField.getText())
                    || pricePrecision != Integer.valueOf(pricePrecisionField.getText());
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void apply() {
        try {
            if (Integer.parseInt(refreshTimeField.getText()) < 1 || Integer.parseInt(pricePrecisionField.getText()) < 0) {
                throw new RuntimeException("Invalid values");
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid values");
        }
        coinName = coinNameField.getText();
        refreshTime = Integer.valueOf(refreshTimeField.getText());
        pricePrecision = Integer.valueOf(pricePrecisionField.getText());
        CoinMarketPanel.getInstance().setToolTipText(coinName);
    }
}
