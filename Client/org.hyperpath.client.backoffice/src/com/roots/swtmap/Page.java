/*
 * (c) 2008, stepan rutz, stepan.rutz@gmx.de 
 */
package com.roots.swtmap;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author stepan.rutz
 * @version $Revision$
 */
public interface Page {
    Control getControl(PageContainer container, Composite parent);
}
