import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(value = "/admin", initParams = {
        @WebInitParam(name = "login", value = "admin"),
        @WebInitParam(name = "password", value = "password"),
        @WebInitParam(name = "ip", value = "localhost:8081")
})
public class AdminServlet extends HttpServlet {
    private Admin admin;

    @Override
    public void init() throws ServletException {
        admin = new Admin(getServletConfig().getInitParameter("login"),
                getServletConfig().getInitParameter("password"),
                getServletConfig().getInitParameter("ip"));
        System.out.println(admin.toString());
        System.out.println("Initiating");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String host = req.getHeader("host").trim();
        String login = req.getParameter("login").trim();
        String password = req.getParameter("password").trim();

        if (admin.getIp().equals(req.getHeader("host"))) {
            System.out.printf("Login via ip %s", host);
            out.println("<h2 style='color:green'>Acsess Granted</h2>");
        } else if ("1" == login) {
            // Do nothing
        } else if (admin.getLogin().equals(login) && admin.getPassword().equals(password)) {
            System.out.printf("Login via login %s and password %s", login, password);
            out.println("<h2 style='color:green'>Acsess Granted</h2>");
        } else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private class Admin {
        String login;
        String password;
        String ip;

        public Admin(String login, String password, String ip) {
            this.login = login;
            this.password = password;
            this.ip = ip;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getIp() {
            return ip;
        }

        @Override
        public String toString() {
            return "Admin [login=" + this.login + ", password=" + this.password + ", ip=" + this.ip + "]";
        }

    }
}
