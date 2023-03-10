import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(value = "/admin", initParams = {
        @WebInitParam(name = "login", value = "admin"),
        @WebInitParam(name = "password", value = "password"),
        @WebInitParam(name = "ip", value = "localhost:8081"),
        @WebInitParam(name = "ACCESS_KEY", value = "ACCESSKEY")
})
public class AdminServlet extends HttpServlet {
    private Admin admin;

    @Override
    public void init() throws ServletException {
        admin = new Admin(getServletConfig().getInitParameter("login"),
                getServletConfig().getInitParameter("password"),
                getServletConfig().getInitParameter("ip"),
                getServletConfig().getInitParameter("ACCESS_KEY"));
        System.out.println(admin.toString());
        System.out.println("Initiating");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String host = req.getHeader("host").trim();
        String httpHeader = req.getHeader("access_key");
        String login = req.getParameter("login").trim();
        String password = req.getParameter("password").trim();

        try {
            out.println(validator(host, httpHeader, login, password));
        } catch (IllegalStateException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("<h2 style='color:red'>FAILED</h2>");
            out.println(e.getMessage());
        }
    }

    public String validator(String host, String httpHeader, String login, String password)
            throws IllegalStateException {
        StringBuilder errors = new StringBuilder();
        if (admin.getIp().equals(host)) {
            System.out.printf("Login via ip %s", host);
            return String.format("<h2 style='color:green'>Acsess Granted. Login via ip %s</h2>", host);
        } else {
            errors.append(String.format("Invalid ip %s\n", host));
        }
        if (admin.getHeader().equals(httpHeader)) {
            System.out.printf("Login via accessKey %s", httpHeader);
            return String.format("<h2 style='color:green'>Acsess Granted. Login via accessKey %s.</h2>", httpHeader);
        } else {
            errors.append(String.format("Invalid accessKey %s\n", httpHeader));
        }
        if (admin.getLogin().equals(login) && admin.getPassword().equals(password)) {
            System.out.printf("Login via login %s and password %s", login, password);
            return String
                    .format("<h2 style='color:green'>Acsess Granted. Login via login %s and password %s.</h2>", login,
                            password);
        } else {
            errors.append(String.format("Invalid login and(or) password: %s|%s\n", login, password));
        }

        throw new IllegalStateException(errors.toString());
    }

    private class Admin {
        String login;
        String password;
        String ip;
        String header;

        public Admin(String login, String password, String ip, String header) {
            this.login = login;
            this.password = password;
            this.ip = ip;
            this.header = header;
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

        public String getHeader() {
            return header;
        }

    }
}
