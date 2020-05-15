package market;

import com.intellij.concurrency.JobScheduler;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class CoinMarketManager {

    public static volatile String price = "-1";
    private static final Set<CoinMarketPanel> coinMarketPanels = new CopyOnWriteArraySet<>();

    static {
        JobScheduler.getScheduler().execute(CoinMarketManager::update);
    }

    static synchronized void update() {
        while (true) {
            try {
                price = Utils.getPrice();
                price = new BigDecimal(price).setScale(PerformanceWatcherForm.pricePrecision, BigDecimal.ROUND_HALF_UP).toString();
            } catch (Exception e) {
                price = "-1";
                LogAction.addLog(e.getMessage());
            } finally {
                coinMarketPanels.forEach(CoinMarketPanel::update);
                Toolkit.getDefaultToolkit().sync();
                try {
                    Thread.sleep(PerformanceWatcherForm.refreshTime * 1000);
                } catch (InterruptedException e) {
                    LogAction.addLog(e.getMessage());
                }
            }
        }
    }

    public static void unregister(CoinMarketPanel activatable) {
        coinMarketPanels.remove(activatable);
    }

    public static void register(CoinMarketPanel activatable) {
        coinMarketPanels.add(activatable);
    }
}
