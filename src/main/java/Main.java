
import spark.ModelAndView;
import spark.Request;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        port(8080);

        // API
        post("/calculadora", (req, res) -> {
            String hostName = "localhost";
            int portNumber = 8888;
            String op = req.queryParams("op");
            double a = Double.parseDouble(req.queryParams("a"));
            double b = Double.parseDouble(req.queryParams("b"));
            double result = 0;

            try (
                    Socket serverSocket = new Socket(hostName, portNumber);
                    DataOutputStream out = new DataOutputStream(serverSocket.getOutputStream());
                    DataInputStream in = new DataInputStream(serverSocket.getInputStream());
            ) {
                out.writeUTF(op);
                out.writeDouble(a);
                out.writeDouble(b);
                result = in.readDouble();

            } catch (UnknownHostException e) {
                System.err.println("Erro no host " + hostName);
            } catch (IOException e) {
                System.err.println("Não foi possível obter E/S para a conexão com " + hostName);
            }

            Map<String, Object> model = new HashMap<>();
            model.put("result", result);
            return new ModelAndView(model, "calculadora.vm");
        }, new VelocityTemplateEngine());
    }
}
