
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor2 {
    public static void main(String[] args) {
        try (
                ServerSocket serverSocket = new ServerSocket(8889);
                Socket clientSocket = serverSocket.accept();
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        ) {
            while (true) {
                String op = in.readUTF();
                double a = in.readDouble();
                double b = in.readDouble();
                double result = 0;

                switch (op) {
                    case "%":
                        result = a % b;
                        break;
                    case "sqrt":
                        result = Math.sqrt(a);
                        break;
                    case "^":
                        result = Math.pow(a, b);
                        break;
                }
                out.writeDouble(result);
            }
        } catch (IOException e) {
            System.out.println("Exceção capturada ao tentar escutar na porta 8889");
            System.out.println(e.getMessage());
        }
    }
}