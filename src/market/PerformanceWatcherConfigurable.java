package market;

import com.intellij.openapi.options.Configurable;

import javax.swing.*;

public class PerformanceWatcherConfigurable implements Configurable {

    private static final PerformanceWatcherForm performanceWatcherForm = new PerformanceWatcherForm();

    @Override
    public String getDisplayName() {
        return "CoinMarketSettings";
    }

    @Override
    public JComponent createComponent() {
        return performanceWatcherForm.getRoot();
    }

    @Override
    public boolean isModified() {
        return performanceWatcherForm.isModified();
    }

    @Override
    public void apply() {
        performanceWatcherForm.apply();
    }
}
