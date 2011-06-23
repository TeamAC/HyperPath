package com.roots.swtmap;
import java.text.NumberFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class InfoPage extends AbstractPage implements Page {

    private abstract class Spec {
        final String key;
        final MapWidget mapWidget;
        abstract String computeValue();
        Spec(String key) { this.key = key; this.mapWidget = mapBrowser.getMapWidget(); }
    }


    private final MapBrowser mapBrowser;
    private Table table;
    private Table Stable;
    private Spec[] specs;
    private Spec[] sspecs;

    public InfoPage(MapBrowser mapBrowser) {
        this.mapBrowser = mapBrowser;
        specs = new Spec[] {
                new Spec("Zoom") { public String computeValue() { return Integer.toString(mapWidget.getZoom()); }},
                new Spec("Map Size") { public String computeValue() { Point size = mapWidget.getSize(); return size.x + ", " + size.y; }},
                new Spec("Map Position") { public String computeValue() { Point position = mapWidget.getMapPosition(); return position.x + ", " + position.y; }},
                new Spec("Center Position") { public String computeValue() { Point position = mapWidget.getCenterPosition(); return position.x + ", " + position.y; }},
                new Spec("Paint time") { public String computeValue() { mapWidget.getStats(); return mapWidget.getStats().dt + " ms"; }},
                new Spec("Imagefetchers Threads") { public String computeValue() { return Integer.toString(MapWidget.IMAGEFETCHER_THREADS); }},
                new Spec("Number painted tiles") {
                    public String computeValue() {
                        mapWidget.getStats();
                        return mapWidget.getStats().tileCount + " of " + NumberFormat.getIntegerInstance().format((long)mapWidget.getXTileCount() * mapWidget.getYTileCount());
                    }
                },
                new Spec("Tilecache") { public String computeValue() { return String.format("%3d / %3d", mapWidget.getCache().getSize(), MapWidget.CACHE_SIZE); }},
                new Spec("Longitude/Latitude") {
                    public String computeValue() {
                        Point p = mapWidget.getCursorPosition();
                        int zoom = mapWidget.getZoom();
                        return MapWidget.format(MapWidget.position2lon(p.x, zoom)) + ", " + MapWidget.format(MapWidget.position2lat(p.y, zoom));
                    }
                },

        };

        sspecs = new Spec[] {
        		new Spec("Category"){ public String computeValue(){ return "TODO";}},
                new Spec("Label") { public String computeValue() { return "TODO"; }},
                new Spec("Description") { public String computeValue() { return "TODO"; }},
                new Spec("Rating") { public String computeValue() { return "TODO"; }},
        };
    }

    public void updateInfos() {
        if (table == null)
            return;
        for (int i = 0; i < specs.length; ++i) {
            Spec spec = specs[i];
            TableItem item = table.getItem(i);
            item.setText(1, spec.computeValue());
        }
    }


    protected void initContent(final PageContainer container, Composite composite) {
        addHeaderRow(container, composite, "Actions");
        addActionLink(container, composite, "<a>Add new category</a>", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                container.showPage(0);
            }
        });
        addActionLink(container, composite, "<a>Add new service</a>", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                container.showPage(0);
            }
        });

        addHeaderRow(container, composite, "Position Infos");

        table = new Table(composite, SWT.FULL_SELECTION  | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2 , 1));

        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        TableColumn column1 = new TableColumn(table, SWT.NONE);
        column1.setText("Place");
        column1.setWidth(120);
        TableColumn column2 = new TableColumn(table, SWT.NONE);
        column2.setText("Place");
        column2.setWidth(160);

        for (Spec spec : specs) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, spec.key);
        }
        updateInfos();

        addHeaderRow(container, composite, "Service Infos");
        Stable = new Table(composite, SWT.FULL_SELECTION  | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        Stable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2 , 1));

        Stable.setHeaderVisible(true);
        Stable.setLinesVisible(true);
        TableColumn Scolumn1 = new TableColumn(Stable, SWT.NONE);
        Scolumn1.setText("Field");
        Scolumn1.setWidth(120);
        TableColumn Scolumn2 = new TableColumn(Stable, SWT.NONE);
        Scolumn2.setText("Data");
        Scolumn2.setWidth(160);

        for (Spec spec : sspecs) {
            TableItem item = new TableItem(Stable, SWT.NONE);
            item.setText(0, spec.key);
        }
        updateInfos();

    }

    protected void widgetDisposed(DisposeEvent e) {
    }


}
