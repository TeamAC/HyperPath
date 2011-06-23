/*
 * $HeadURL$
 *
 * (c)2010 Stepan Rutz, Licensed under LGPL License
 */

package com.roots.swtmap;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.roots.swtmap.MapWidget.PointD;
import com.roots.swtmap.SearchPage.SearchResult;

/**
 * @author stepan.rutz@gmx.de
 * @version $Revision$
 */
public class ResultsPage extends AbstractPage implements Page {
    
    private static final Logger log = Logger.getLogger(ResultsPage.class.getName());
    
    private final MapBrowser mapBrowser;
    private Table table;
    
    private String search = "";
    private SearchResult[] results = new SearchResult[0];

    private ScrolledComposite sc;

    private Link descriptionText;

    
    public ResultsPage(MapBrowser mapBrowser) {
        this.mapBrowser = mapBrowser;
    }
    
    public String getSearch() {
        return search;
    }
    
    public void setSearch(String search) {
        this.search = search;
    }
    
    public SearchResult[] getResults() {
        return results.clone();
    }

    public void setResults(SearchResult[] results) {
        this.results = results.clone();
        table.removeAll();
        for (SearchResult result : results) {
            
            String shortName = result.getName();
            shortName = shortName.replaceAll("\\s(.*)$", "");
            String linkBody = shortName + " [" + result.getCategory() + "]";

            String description = result.getDescription();
            description = description.replaceAll("\\[.*?\\]", "");
            
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, linkBody);
        }
    }
    
    protected void initContent(final PageContainer container, Composite composite) {
        addHeaderRow(container, composite, "Actions");
        addActionLink(container, composite, "<a>Back to main menu</a>", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                container.showPage(0);
            }
        });
        
        addHeaderRow(container, composite, "Results");
        addInfoText(container, composite, "The following search results were retrieved from openstreetmap.org. " +
                "Double-click to open a location.");
        
        table = new Table(composite, SWT.FULL_SELECTION  | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2 , 1));
        
        table.setHeaderVisible(!true);
        table.setLinesVisible(true);
        TableColumn column1 = new TableColumn(table, SWT.NONE);
        column1.setText("Place");
        column1.setWidth(260);
        
        addHeaderRow(container, composite, "Description");
        descriptionText = addInfoText(container, composite, "");
        GridData layoutData = (GridData) descriptionText.getLayoutData();
        layoutData.minimumHeight = 100;
        layoutData.grabExcessVerticalSpace = false;
        layoutData.heightHint = 160;
        
        table.addListener (SWT.Selection, new Listener () {
            public void handleEvent (Event event) {
                if (event.detail == SWT.CHECK)
                    return;
                TableItem item = (TableItem) event.item;
                int index = table.indexOf(item);
                if (index >= 0 && index < results.length) {
                    SearchResult result = results[index];
                    String description = result.getDescription();
                    description = description.replaceAll("\\[.*?\\]", "");
                    description = description.replaceAll("<.*?>", "");
                    descriptionText.setText(description);
                    descriptionText.getParent().layout();
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseDoubleClick(MouseEvent e) {
                Point point = new Point(e.x, e.y);
                TableItem item = table.getItem(point);
                if (item == null)
                    return;
                int index = table.indexOf(item);
                if (index >= 0 && index < results.length) {
                    SearchResult result = results[index];
                    MapWidget mapWidget = mapBrowser.getMapWidget();
                    mapWidget.setZoom(result.getZoom() < 1 || result.getZoom() > mapWidget.getTileServer().getMaxZoom() ? 8 : result.getZoom());
                    Point position = mapWidget.computePosition(new PointD(result.getLon(), result.getLat())); 
                    mapWidget.setCenterPosition(position);
                    mapWidget.redraw();
                }
            }
        });
    }

    protected void widgetDisposed(DisposeEvent e) {
    }
    

}


