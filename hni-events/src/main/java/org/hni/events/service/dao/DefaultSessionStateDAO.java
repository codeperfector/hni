package org.hni.events.service.dao;

import org.hni.common.dao.AbstractDAO;
import org.hni.events.service.om.SessionState;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;

/**
 * Created by seoklee on 11/17/16.
 */
@Component
public class DefaultSessionStateDAO extends AbstractDAO<SessionState> implements SessionStateDAO {

    public DefaultSessionStateDAO() {
        super(SessionState.class);
    }
//
//    public SessionState deleteById(String sessionId) {
//        SessionState s = null;
//
//        try {
//            s = this.get(sessionId);
//		} catch (NoResultException e) {
//			return null;
//		}
//
//		return this.delete(s);
//    }
}
