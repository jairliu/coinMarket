package market;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.impl.status.MemoryUsagePanel;

public class CoinMarketComponent implements ProjectComponent {
    private Project project;
    private CoinMarketPanel statusBarWidget;
    private IdeFrame ideFrame;

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
        StatusBar statusBar = ideFrame.getStatusBar();
        statusBarWidget = new CoinMarketPanel(project);
        statusBar.addWidget(statusBarWidget, "before " + MemoryUsagePanel.WIDGET_ID);
    }

    @Override
    public void projectClosed() {
        if (statusBarWidget != null) {
            final StatusBar statusBar = ideFrame.getStatusBar();
            if (statusBar != null) {
                statusBar.removeWidget(CoinMarketPanel.WIDGET_ID);
            }
            Disposer.dispose(statusBarWidget);
        }
    }
}
