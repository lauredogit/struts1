/*
 * $Header: /home/cvs/jakarta-struts/src/share/org/apache/struts/taglib/nested/html/NestedLinkTag.java,v 1.2 2002/01/22 03:30:50 arron Exp $
 * $Revision: 1.2 $
 * $Date: 2002/01/22 03:30:50 $
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Struts", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.struts.taglib.nested.html;

import org.apache.struts.taglib.nested.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.apache.struts.taglib.html.LinkTag;

/**
 * NestedLinkTag.
 * @author Arron Bates
 * @since Struts 1.1
 * @version $Revision: 1.2 $ $Date: 2002/01/22 03:30:50 $
 */
public class NestedLinkTag extends LinkTag implements NestedNameSupport {
  
  /**
   * Overriding method of the heart of the matter. Gets the relative property
   * and leaves the rest up to the original tag implementation. Sweet.
   * @return int JSP continuation directive.
   *             This is in the hands of the super class.
   */
  public int doStartTag() throws JspException {
    
    /* if no paramName is supplied, we'll default to the current */
    if (getParamName() == null || "".equals(getParamName().trim())) {
      setParamName(getName());
    }
    
    /* singleton tag implementations will need the original property to be
       set before running */
    super.setProperty(originalProperty);
    super.setParamProperty(originalParamProperty);
    
    /* let the NestedHelper set the properties it can */
    isNesting = true;
    Tag pTag = NestedPropertyHelper.getNestingParentTag(this);
    setProperty(NestedPropertyHelper.getNestedProperty(getProperty(), pTag));
    setParamProperty(NestedPropertyHelper.getNestedProperty(getParamProperty(),
                                                            pTag));
    isNesting = false;
    
    /* do the tag */
    return super.doStartTag();
  }
  
  /** this is overridden so that properties being set by the JSP page aren't
   * written over by those needed by the extension. If the tag instance is
   * re-used by the JSP, the tag can set the property back to that set by the
   * JSP page.
   *
   * @param newProperty new property value
   */
  public void setProperty(String newProperty) {
    /* let the real tag do its thang */
    super.setProperty(newProperty);
    /* if it's the JSP setting it, remember the value */
    if (!isNesting) {
      originalProperty = newProperty;
    }
  }
  
  /** For the same reasons as the above method, we have to remember this
   * property to keep things correct here also.
   *
   * @param newProperty new property value
   */
  public void setParamProperty(String newParamProperty) {
    /* let the real tag do its thang */
    super.setParamProperty(newParamProperty);
    /* if it's the JSP setting it, remember the value */
    if (!isNesting) {
      originalParamProperty = newParamProperty;
    }
  }
  
  /* hold original property */
  private String originalProperty = null;
  private String originalParamProperty = null;
  private boolean isNesting = false;
}