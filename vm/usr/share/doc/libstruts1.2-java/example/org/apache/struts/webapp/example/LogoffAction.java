/*
 * $Id: LogoffAction.java 54929 2004-10-16 16:38:42Z germuska $ 
 *
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.struts.webapp.example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Implementation of <strong>Action</strong> that processes a
 * user logoff.
 *
 * @version $Rev: 54929 $ $Date: 2004-10-16 17:38:42 +0100 (Sat, 16 Oct 2004) $
 */
public final class LogoffAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    private Log log = LogFactory.getLog("org.apache.struts.webapp.Example");

    // --------------------------------------------------------- Public Methods

        // See superclass for Javadoc
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {

        // Extract attributes we will need
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.USER_KEY);

        // Process this user logoff
        if (user != null) {
            if (log.isDebugEnabled()) {
                log.debug(
                    "LogoffAction: User '"
                        + user.getUsername()
                        + "' logged off in session "
                        + session.getId());
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug(
                    "LogoffActon: User logged off in session " + session.getId());
            }
        }
        session.removeAttribute(Constants.SUBSCRIPTION_KEY);
        session.removeAttribute(Constants.USER_KEY);
        session.invalidate();

        // Forward control to the specified success URI
        return (mapping.findForward("success"));

    }

}
