import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server2 {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(1235);
            Socket socket = server.accept();
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            //Recebe os dados do webservice
            String data = in.readUTF();
            String[] dados = data.split(" ");
            //Executa a operação
            double resultado = 0;
            switch (dados[0]) {
                case "%":
                    resultado = Double.parseDouble(dados[1]) * (Double.parseDouble(dados[2]) / 100);
                    break;
                case "√":
                    resultado = Math.sqrt(Double.parseDouble(dados[1]));
                    break;
                case "^":
                    resultado = Math.pow(Double.parseDouble(dados[1]), Double.parseDouble(dados[2]));
                    break;
            }
            //Envia o resultado para o webservice
            out.writeUTF(String.valueOf(resultado));
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}