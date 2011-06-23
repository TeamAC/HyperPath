package test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Color;

import com.roots.swtmap.MapBrowser;
import com.roots.swtmap.MapWidget;
import com.roots.swtmap.MapWidget.PointD;

public class View extends ViewPart {
	PointD mark_position;
	public static final String ID = "test.view";

	public View(){
		mark_position = new PointD(10.16901, 36.80843);
	}

	public void createPartControl(Composite parent) {
		MapBrowser map = new MapBrowser(parent, SWT.NONE);
		MapWidget mapWidget = map.getMapWidget();
        mapWidget.setZoom(15);
        Point position = new Point(4430841, 3270254);
        mapWidget.setCenterPosition(position);
	}

	public void setFocus() {
	}
}
