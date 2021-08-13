package servlet;

import controller.PostController;
import repository.PostRepository;
import service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";

    @Override
    public void init() {
        controller = initPostLayer();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            handleRequest(req, resp);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected PostController initPostLayer() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        return new PostController(service);
    }

    protected void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        final var path = req.getRequestURI();
        final var method = req.getMethod();

        if (path.matches("/api/posts/\\d+")) {
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
            switch (method) {
                case (GET):
                    controller.getById(id, resp);
                    break;
                case (DELETE):
                    controller.removeById(id, resp);
                    break;
                default:

            }
        }

        if (path.equals("/api/posts")) {
            switch (method) {
                case (GET):
                    controller.all(resp);
                    break;
                case (POST):
                    controller.save(req.getReader(), resp);
                    break;
                default:

            }
        }
    }

}