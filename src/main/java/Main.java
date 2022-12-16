
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
        //Rota para o webservice
        get("/calculator", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //Recebe os valores dos campos digitados na tela
            String operacao = request.queryParams("operacao");
            String operando1 = request.queryParams("operando1");
            String operando2 = request.queryParams("operando2");
            //Verifica se a operação é válida
            if (operacao == null || operacao.equals("")) {
                model.put("operacao", "Operação inválida!");
            }
            //Verifica se os operandos são válidos
            if (operando1 == null || operando1.equals("")) {
                model.put("operando1", "Operando inválido!");
            }
            if (operando2 == null || operando2.equals("")) {
                model.put("operando2", "Operando inválido!");
            }
            //Verifica se os operandos são números
            if (!operando1.matches("[0-9]+") || !operando2.matches("[0-9]+")) {
                model.put("operando", "Operando inválido!");
            }
            //Realiza a conexão com os servidores
            try {
                Socket socket1 = new Socket("localhost", 1234);
                Socket socket2 = new Socket("localhost", 1235);
                DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
                DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
                //Envia a operação e os operandos para cada servidor
                out1.writeUTF(operacao + " " + operando1 + " " + operando2);
                out2.writeUTF(operacao + " " + operando1 + " " + operando2);
                //Recebe a resposta dos servidores
                DataInputStream in1 = new DataInputStream(socket1.getInputStream());
                DataInputStream in2 = new DataInputStream(socket2.getInputStream());
                model.put("resultado", in1.readUTF() + in2.readUTF());
                //Fecha as conexões
                socket1.close();
                socket2.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Retorna o resultado da operação
            return new ModelAndView(model, "templates/calculator.vm");
        }, new VelocityTemplateEngine());
    }
}