package market;

import com.intellij.concurrency.JobScheduler;

import java.awt.*;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class CoinMarketManager {

    private static Set<CoinMarketPanel> coinMarketPanels = new CopyOnWriteArraySet<>();
    public static volatile String price = "0";

    static {
        JobScheduler.getScheduler().execute(CoinMarketManager::update);
    }

    static synchronized void update() {
        while (true) {
            try {
                price = getPrice();
                boolean painted = false;
                for (CoinMarketPanel coinMarketPanel : coinMarketPanels) {
                    painted = coinMarketPanel.update() || painted;
                }
                if (painted) {
                    Toolkit.getDefaultToolkit().sync();
                }
            } catch (Exception e) {
                LogAction.addLog(e.getMessage());
            } finally {
                try {
                    Thread.sleep(PerformanceWatcherForm.refreshTime * 1000);
                } catch (InterruptedException e) {
                    LogAction.addLog(e.getMessage());
                }
            }
        }
    }

    private static String getPrice() {
        String a = PerformanceWatcherForm.coinName;
        int b = PerformanceWatcherForm.refreshTime;
        return a + ":" + b;
    }

    public static void unregister(CoinMarketPanel activatable) {
        LogAction.addLog("unregistering " + activatable);
        coinMarketPanels.remove(activatable);
    }

    public static void register(CoinMarketPanel activatable) {
        LogAction.addLog("registering " + activatable);
        coinMarketPanels.add(activatable);
    }
}
