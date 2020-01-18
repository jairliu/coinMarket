package market;

import com.intellij.ide.scratch.ScratchFileService;
import com.intellij.ide.scratch.ScratchRootType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PathUtil;

import java.time.LocalDateTime;

public class LogAction extends DumbAwareAction {

    private static volatile StringBuilder sb = new StringBuilder();

    public static void addLog(String log) {
        if (sb.length() > 10000) {
            sb.delete(0, sb.length());
        }
        sb.append("Time:").append(LocalDateTime.now()).append("\n");
        sb.append(log).append("\n");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project eventProject = getEventProject(e);
        VirtualFile f = ScratchRootType.getInstance().createScratchFile(eventProject, PathUtil.makeFileName("coinMarketLog", "txt"), PlainTextLanguage.INSTANCE, sb.toString(), ScratchFileService.Option.create_if_missing);
        if (f != null) {
            FileEditorManager.getInstance(eventProject).openFile(f, true);
        }
    }
}
