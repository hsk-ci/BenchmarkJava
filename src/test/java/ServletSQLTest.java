import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.stubbing.*;
import org.owasp.benchmark.helpers.DatabaseHelper;
import org.owasp.benchmark.testcode.BenchmarkTest00008;

public class ServletSQLTest {

    private BenchmarkTest00008 servlet;

    @BeforeEach
    public void setUp() {
        this.servlet = new BenchmarkTest00008();
    }

    @Test
    public void benchmark00008() throws Exception {
        // get and post are the same for this benchmark
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String param = "count (*);"; // SQL Query

        when(request.getHeader("BenchmarkTest00008")).thenReturn(param);

        this.servlet.doGet(request, response);
    }
}
