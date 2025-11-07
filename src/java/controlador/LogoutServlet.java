package controlador;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/Logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        if (s != null) s.invalidate();
        resp.sendRedirect("login.jsp");
    }
}
