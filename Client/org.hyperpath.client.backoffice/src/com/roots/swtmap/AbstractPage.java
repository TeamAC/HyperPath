/*
 * $HeadURL$
 *
 * (c)2010 Stepan Rutz, Licensed under LGPL License
 */

package com.roots.swtmap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;

/**
 * @author stepan.rutz@gmx.de
 * @version $Revision$
 */
public abstract class AbstractPage implements Page {

    private PageContainer container;
    private Composite composite;
    private Image actionImage;
    
    
    protected PageContainer getContainer() {
        return container;
    }
    
    protected Composite getComposite() {
        return composite;
    }
    
    public Control getControl(PageContainer container, Composite parent) {
        if (composite == null) {
            this.container = container;
            composite = new Composite(parent, SWT.NONE);
            composite.addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent e) {
                   AbstractPage.this.widgetDisposed(e);
                }
            });
            actionImage = new Image(composite.getDisplay(), getClass().getResourceAsStream("resources/action.gif"));
            
            composite.setLayout(new GridLayout(2, false));
            container.adapt(composite);
            
            
            initContent(container, composite);
        }
        return composite;
    }
    
    protected void addHeaderRow(PageContainer container, Composite parent, String text) {
        HeaderControl header = new HeaderControl(parent);
        container.adapt(header);
        header.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false, 2 , 1));
        header.setText(text);
    }

    protected Link addInfoText(PageContainer container, Composite parent, String text) {
        
//        StyledText titleText = new StyledText(parent, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
//        container.adapt(titleText);
        Link link = new Link(parent, SWT.WRAP | SWT.MULTI);
        container.adapt(link);
        GridData layoutData = new GridData(GridData.FILL, GridData.BEGINNING, true, false, 2 , 1);
        link.setLayoutData(layoutData);
        link.setText(text);
        link.addListener (SWT.Selection, new Listener () {
            public void handleEvent(Event event) {
                try {
                    if (event.text != null && event.text.length() > 0)
                        Program.launch(event.text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return link;
    }
    
    
    protected void addActionLink(PageContainer container, Composite parent, String text, SelectionAdapter selectionAdapter) {
        Composite wrap = new Composite(parent, SWT.NONE);
        wrap.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        container.adapt(wrap);
        
        GridLayout layout = new GridLayout(2, false);
        layout.marginLeft = 8;
        layout.marginHeight = 0;
        wrap.setLayout(layout);
        
        Label titleImage = new Label(wrap, SWT.WRAP);
        container.adapt(titleImage);
        titleImage.setImage(actionImage);
        Link link = new Link(wrap, SWT.NONE);
        container.adapt(link);
        link.setText(text);
        link.addSelectionListener(selectionAdapter);
    }
    
    protected abstract void widgetDisposed(DisposeEvent e);
    
    protected abstract void initContent(PageContainer container, Composite composite);



}


