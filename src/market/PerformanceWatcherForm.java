package market;

import javax.swing.*;

public class PerformanceWatcherForm {
    private JPanel root;

    public static volatile String coinName = "EOS";
    public static volatile int refreshTime = 10;

    private JTextField coinNameField;
    private JTextField refreshTimeField;

    public PerformanceWatcherForm() {
        coinName = coinNameField.getText();
        refreshTime = Integer.valueOf(refreshTimeField.getText());
    }

    public JPanel getRoot() {
        return root;
    }

    public boolean isModified() {
        try {
            return !coinName.equals(coinNameField.getText()) || refreshTime != Integer.valueOf(refreshTimeField.getText());
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void apply() {
        try {
            if (Integer.parseInt(refreshTimeField.getText()) < 1) {
                throw new RuntimeException("Invalid values");
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid values");
        }
        coinName = coinNameField.getText();
        refreshTime = Integer.valueOf(refreshTimeField.getText());
        CoinMarketPanel.getInstance().setToolTipText(coinName);
    }
}
