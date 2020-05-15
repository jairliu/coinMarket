package market;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.impl.status.MemoryUsagePanel;

public class CoinMarketComponent implements ProjectComponent {

    private final Project project;
    private CoinMarketPanel statusBarWidget;
    private IdeFrame ideFrame;
    private StatusBar statusBar;

    public CoinMarketComponent(Project project) {
        this.project = project;
    }

    @Override
    public String getComponentName() {
        return "CoinMarketComponent";
    }

    @Override
    public void projectOpened() {
        ideFrame = WindowManager.getInstance().getIdeFrame(this.project);
        statusBar = ideFrame.getStatusBar();
        statusBarWidget = CoinMarketPanel.getInstance();
        statusBar.addWidget(statusBarWidget, "before " + MemoryUsagePanel.WIDGET_ID);
    }

    @Override
    public void projectClosed() {
        if (statusBarWidget != null && statusBar != null) {
            statusBar.removeWidget(CoinMarketPanel.WIDGET_ID);
            Disposer.dispose(statusBarWidget);
        }
    }
}
