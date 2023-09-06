import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.Hashtable;
import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.servlet.*;
import javax.servlet.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.stubbing.*;
import org.owasp.benchmark.helpers.DatabaseHelper;
import org.owasp.benchmark.testcode.BenchmarkTest00008;

public class ServletSQLTest {

    @Mock InitialContextFactory ctx;

    private BenchmarkTest00008 servlet;

    public static class MyContextFactory implements InitialContextFactory {
        @Override
        public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
            ConnectionFactory mockConnFact = mock(ConnectionFactory.class);

            org.springframework.context.ApplicationContext ac =
                    new org.springframework.context.support.ClassPathXmlApplicationContext(
                            "/context.xml", DatabaseHelper.class);
            javax.sql.DataSource data = (javax.sql.DataSource) ac.getBean("dataSource");
            System.out.println("DS: " + data);

            InitialContext mockCtx = mock(InitialContext.class);
            when(mockCtx.lookup("java:comp/env/jdbc/BenchmarkDB")).thenReturn(data);
            return mockCtx;
        }
    }

    @BeforeEach
    public void setUp() {

        // configor initial context factory
        MockitoAnnotations.initMocks(this);
        System.setProperty(
                "java.naming.factory.initial",
                this.getClass().getCanonicalName() + "$MyContextFactory");

        this.servlet = new BenchmarkTest00008();
    }

    @Test
    public void benchmark00008() throws Exception {
        // get and post are the same for this benchmark
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // set parameter like example
        String param = "verifyUserPassword('foo','bar')";
        when(request.getHeader("BenchmarkTest00008")).thenReturn(param);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // call servlet
        this.servlet.doPost(request, response);

        writer.flush(); // it may not have been flushed yet...
        assertTrue(stringWriter.toString().contains("our results are"));
    }
}
