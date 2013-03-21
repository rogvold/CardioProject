package ru.cardio.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.cardio.core.jpa.entity.User;
import ru.cardio.core.managers.UserManagerLocal;
import ru.cardio.core.utils.EJBUtils;
import ru.cardio.core.utils.JsonUtils;

/**
 *
 * @author rogvold
 */
public class MobileAuthentificationServlet extends HttpServlet {

    UserManagerLocal userMan;
    private static final String CHECK_USER_EXISTENCE_PURPOSE_VALUE = "CheckUserExistence";
    private static final String REGISTER_PURPOSE_VALUE = "Register";
    private static final String CHECK_USER_AUTHORISATION_DATA_PURPOSE_VALUE = "CheckAuthorisationData";
    private static final String INPUT_PARAMETER_FIELD_NAME = "json";
    private static final String SECRET = "h7a7RaRtvAVwnMGq5BV6";

    private static final String YES = "1";
    private static final String NO = "0";
    private static final String  ERROR = "-1";
    
    private static final String PURPOSE_PARAM_NAME = "purpose";
    private static final String SECRET_PARAM_NAME = "secret";
    private static final String EMAIL_PARAM_NAME = "email";
    private static final String PASSWORD_PARAM_NAME = "password";
    
    private static final String RESPONSE_PARAM_NAME = "response";
    private static final String MESSAGE_PARAM_NAME = "message";
    
    @PostConstruct
    private void initServlet() {
        userMan = EJBUtils.resolve("java:global/BaseProjectEE/BaseProjectCore/UserManager!ru.cardio.core.managers.UserManagerLocal", UserManagerLocal.class);
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();


        String message = "\"\"";
        String resp = "";
        try {
            String jsonString = request.getParameter(INPUT_PARAMETER_FIELD_NAME);

            try {

                if (jsonString == null) {
                    throw new Exception("no data passed");
                }

                String secret = JsonUtils.getStringFromJson(jsonString, SECRET_PARAM_NAME);
                if (secret == null) {
                    throw new Exception("secret is not specified");
                }
                if (!secret.equals(SECRET)) {
                    throw new Exception("incorrect secret");
                }

                String purpose = JsonUtils.getStringFromJson(jsonString, PURPOSE_PARAM_NAME);
                if (purpose == null) {
                    throw new Exception("purpose parameter is not found");
                }
                if (purpose.equals(CHECK_USER_EXISTENCE_PURPOSE_VALUE)) {
                    Boolean b = userMan.userExistsByEmail(JsonUtils.getStringFromJson(jsonString, EMAIL_PARAM_NAME));
                    resp = b ? YES : NO;
                }
                if (purpose.equals(REGISTER_PURPOSE_VALUE)) {
                    String email = JsonUtils.getStringFromJson(jsonString, EMAIL_PARAM_NAME);
                    String password = JsonUtils.getStringFromJson(jsonString, PASSWORD_PARAM_NAME);
                    User u = userMan.registerNewUser(email, password, "", "", User.USER);
                    if (u != null) {
                        resp = YES;
                    } else {
                        throw new Exception("can not register new user");
                    }
                }

                if (purpose.equals(CHECK_USER_AUTHORISATION_DATA_PURPOSE_VALUE)) {
                    String email = JsonUtils.getStringFromJson(jsonString, EMAIL_PARAM_NAME);
                    String password = JsonUtils.getStringFromJson(jsonString, PASSWORD_PARAM_NAME);
                    resp = userMan.checkEmailAndPassword(email, password) ? YES : NO;
                }
                if ("".equals(resp)) {
                    throw new Exception("purpose value is not proper");
                }

            } catch (Exception e) {
                resp = ERROR;
                message = "\"" + e.getMessage() + "\"";
            }
            out.print("{\""+RESPONSE_PARAM_NAME+"\": "+resp+", \""+MESSAGE_PARAM_NAME+"\": " + message + "}");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
