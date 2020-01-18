package market;

import com.intellij.ide.DataManager;
import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.update.Activatable;
import com.intellij.util.ui.update.UiNotifyConnector;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CoinMarketPanel extends JButton implements CustomStatusBarWidget {

    public static final String WIDGET_ID = "market.CoinMarketPanel";
    private static final String SAMPLE_STRING = "123456";

    private static final CoinMarketPanel instance = new CoinMarketPanel();

    private CoinMarketPanel() {
        super.setOpaque(false);
        super.setFocusable(false);
        super.setToolTipText(PerformanceWatcherForm.coinName);

        super.updateUI();
        super.setFont(getWidgetFont());
        super.setBorder(BorderFactory.createEmptyBorder());

        new UiNotifyConnector(this, new Activatable() {
            @Override
            public void showNotify() {
                CoinMarketManager.register(CoinMarketPanel.this);
            }

            @Override
            public void hideNotify() {
                CoinMarketManager.unregister(CoinMarketPanel.this);
            }
        });
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                DataContext context = DataManager.getInstance().getDataContext(CoinMarketPanel.this);
                ActionManager.getInstance().getAction("LogAction").actionPerformed(new AnActionEvent(e, context, ActionPlaces.UNKNOWN, new Presentation(""), ActionManager.getInstance(), 0));
            }
        };
        addMouseListener(mouseAdapter);
    }

    public static CoinMarketPanel getInstance() {
        return instance;
    }

    @Override
    public void setToolTipText(String text) {
        super.setToolTipText(text);
    }

    @Override
    public void dispose() {
        CoinMarketManager.unregister(CoinMarketPanel.this);
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
    }

    @Override
    public String ID() {
        return WIDGET_ID;
    }

    private static Font getWidgetFont() {
        return JBUI.Fonts.label(11);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void paintComponent(Graphics g) {
        String info = CoinMarketManager.price;

        Dimension size = getSize();
        Insets insets = getInsets();

        Image bufferedImage = UIUtil.createImage(g, size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics().create();

        int totalBarLength = size.width - insets.left - insets.right - 3;
        int barHeight = Math.max(size.height, getFont().getSize() + 2);
        int yOffset = (size.height - barHeight) / 2;
        int xOffset = insets.left;

        g2.setColor(UIUtil.getPanelBackground());
        g2.fillRect(0, 0, size.width, size.height);
        g2.fillRect(xOffset + 1, yOffset, info.length() + 1, barHeight);
        g2.fillRect(xOffset + info.length() + 1, yOffset, info.length() + 1, barHeight);
        g2.setFont(getFont());

        FontMetrics fontMetrics = g.getFontMetrics();
        int infoWidth = fontMetrics.charsWidth(info.toCharArray(), 0, info.length());
        int infoHeight = fontMetrics.getAscent();
        UISettings.setupAntialiasing(g2);

        g2.setColor(getModel().isPressed() ? UIUtil.getLabelDisabledForeground() : JBColor.foreground());
        g2.drawString(info, xOffset + (totalBarLength - infoWidth) / 2, yOffset + infoHeight + (barHeight - infoHeight) / 2 - 1);

        g2.setStroke(new BasicStroke(1));
        g2.setColor(JBColor.GRAY);
        g2.drawRect(0, 0, size.width - 2, size.height - 1);
        g2.dispose();

        UIUtil.drawImage(g, bufferedImage, 0, 0, null);
    }

    @Override
    public Dimension getPreferredSize() {
        Insets insets = getInsets();
        int width = getFontMetrics(getWidgetFont()).stringWidth(SAMPLE_STRING) + insets.left + insets.right + JBUI.scale(2);
        int height = getFontMetrics(getWidgetFont()).getHeight() + insets.top + insets.bottom + JBUI.scale(2);
        return new Dimension(width, height);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    public boolean update() {
        Graphics graphics = getGraphics();
        if (graphics != null) {
            paintComponent(graphics);
            return true;
        }
        return false;
    }
}
