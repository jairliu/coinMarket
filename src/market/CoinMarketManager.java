package market;

import com.intellij.concurrency.JobScheduler;
import com.intellij.openapi.diagnostic.Logger;

import java.awt.*;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

public class CoinMarketManager {
    private static final Logger log = Logger.getInstance(CoinMarketManager.class);

    volatile static String price = "0";
    static boolean broken = false;

    private static Set<CoinMarketPanel> coinMarketPanels = new CopyOnWriteArraySet<>();

    static {
        JobScheduler.getScheduler().scheduleWithFixedDelay(CoinMarketManager::update, 1, 1, TimeUnit.SECONDS);
    }

    static synchronized void update() {
        try {

            price = getPrice();

            boolean painted = false;
            for (CoinMarketPanel coinMarketPanel : coinMarketPanels) {
                painted = coinMarketPanel.update() || painted;
            }
            if (painted) {
                Toolkit.getDefaultToolkit().sync();
            }
            broken = false;
        } catch (Exception e) {
            if (broken) {
                log.error(e);
                throw e;
            } else {
                log.info(e);
                broken = true;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
            }
        } catch (Throwable e) {
            log.error(e);
            throw e;
        }
    }

    private static String getPrice() {
        return String.valueOf(new Random().nextInt(100));
    }

    public static void unregister(CoinMarketPanel activatable) {
        log.info("unregistering " + activatable);
        coinMarketPanels.remove(activatable);
    }

    public static void register(CoinMarketPanel activatable) {
        log.info("registering " + activatable);
        coinMarketPanels.add(activatable);
    }
}
