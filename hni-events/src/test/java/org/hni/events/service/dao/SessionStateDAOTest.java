package org.hni.events.service.dao;

import org.hni.events.service.om.EventName;
import org.hni.events.service.om.SessionState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-applicationContext.xml"} )
@Transactional
public class SessionStateDAOTest {

    @Inject
    private SessionStateDAO sessionStateDAO;

    private static final String SESSION_ID = "1";
    private static final String PHONE_NUMBER = "8188461238";
    private static final String REUTRN_MESSAGE = "returnmessage";

    SessionState state;

    @Before
    public void setUp() {
        state = new SessionState(EventName.REGISTER, SESSION_ID, PHONE_NUMBER);
    }

    @Test
    public void testInsertGetDao() {
        SessionState returnedSession = sessionStateDAO.save(state);
    }

}
