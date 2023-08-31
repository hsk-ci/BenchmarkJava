import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mockrunner.servlet.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.stubbing.*;
import org.owasp.benchmark.testcode.BenchmarkTest00001;

public class ServletTest {

    private BenchmarkTest00001 servlet;

    @Captor ArgumentCaptor<Cookie> cookieCaptor;

    @BeforeEach
    public void setUp() {
        this.servlet = new BenchmarkTest00001();
        this.cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
    }

    @Test
    public void benchmark00001_get() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);

        when(request.getRequestURL())
                .thenReturn(new StringBuffer("http://localhost/pathtraver-00/BenchmarkTest00001"));
        when(request.getRequestDispatcher("/pathtraver-00/BenchmarkTest00001.html")).thenReturn(rd);

        this.servlet.doGet(request, response);
        verify(response).addCookie(this.cookieCaptor.capture());

        assertTrue("BenchmarkTest00001".equals(this.cookieCaptor.getValue().getName()));
        assertTrue(
                "FileName"
                        .equals(
                                java.net.URLDecoder.decode(
                                        this.cookieCaptor.getValue().getValue(), "UTF-8")));
    }
}
