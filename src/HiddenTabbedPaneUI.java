import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

public class HiddenTabbedPaneUI extends BasicTabbedPaneUI {
    @Override
    protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
        return 0;
    }

    @Override
    protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
        // Do not paint any tabs.
    }
    @Override
    protected Insets getContentBorderInsets(int tabPlacement) {
        return new Insets(0, 0, 0, 0);
    }

}
