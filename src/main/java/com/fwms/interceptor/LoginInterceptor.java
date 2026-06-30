package com.fwms.interceptor;

import com.fwms.entity.User;
import com.fwms.pattern.factory.UserFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            String uri = request.getRequestURI();
            String module = getModuleFromUri(uri);
            if (module != null && !UserFactory.hasPermission(user.getRole(), module)) {
                if (uri.startsWith("/api/")) {
                    response.setStatus(403);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":403,\"message\":\"无权限访问\"}");
                    return false;
                }
                response.sendRedirect("/dashboard?error=no_permission");
                return false;
            }
            return true;
        }
        if (uriStartsWithApi(request.getRequestURI())) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"请先登录\"}");
            return false;
        }
        response.sendRedirect("/login");
        return false;
    }

    private boolean uriStartsWithApi(String uri) {
        return uri.startsWith("/api/");
    }

    private String getModuleFromUri(String uri) {
        if (uri.contains("/supplier")) return "supplier";
        if (uri.contains("/purchase")) return "purchase";
        if (uri.contains("/material") && !uri.contains("/material-stock")) return "material";
        if (uri.contains("/inbound")) return "inbound";
        if (uri.contains("/outbound") || uri.contains("/requisition")) return "requisition";
        if (uri.contains("/qc") || uri.contains("/quality")) return "qc";
        if (uri.contains("/production")) return "production";
        if (uri.contains("/product") && !uri.contains("/product-stock")) return "production";
        if (uri.contains("/customer")) return "customer";
        if (uri.contains("/sales")) return "sales";
        if (uri.contains("/shipment")) return "shipment";
        if (uri.contains("/location")) return "location";
        if (uri.contains("/user")) return "admin";
        return null;
    }
}
