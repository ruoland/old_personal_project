package ruo.minigame.android;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

public class Android {
    private DatagramSocket datagramSocket;
    private Socket socket;
    private String ip;
    public boolean login = ip != null;

    public Android() {
        if (!login) login();
    }

    public void login() {
        try {
            datagramSocket = new DatagramSocket(50008);

            String password = "" + new Random().nextInt(10);

            InetAddress local = InetAddress.getByAddress(new byte[]{-1, -1, -1, -1});
            DatagramPacket loginPacket = new DatagramPacket(("login:" + password).getBytes(),
                    ("login:" + password).length(), local, 50008);
            datagramSocket.send(loginPacket);

            System.out.println(password);

            byte[] message = new byte[1500];
            DatagramPacket ipPacket = new DatagramPacket(message, message.length);
            datagramSocket.receive(ipPacket);
            System.out.println(new String(message, 0, ipPacket.getLength()));

            if (new String(message, 0, ipPacket.getLength()).startsWith("login:"))
                datagramSocket.receive(ipPacket);
            ip = new String(message, 0, ipPacket.getLength()).replace("IP:", "");
            System.out.println(new String(message, 0, ipPacket.getLength()));
            tcp(ip);
            datagramSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tcp(String ip) {
        try {
            InetAddress local = InetAddress.getByName(ip);
            socket = new Socket(local, 25790);
            InputStream in = socket.getInputStream();
            final DataInputStream dis = new DataInputStream(in);
            new Thread(new Runnable() {

                @Override
                public void run() {

                    while (true) {
                        try {
                            String message = dis.readUTF();
                            if (message.equals("call")) {
                                connectCall = true;
                                call.call();
                            }
                            if (message.equals("end")) {
                                connectCall = false;
                                call.end();
                            }
                            if (message.equals("dial")) {
                                call.dial();
                            }
                        } catch (IOException e) {

                            e.printStackTrace();
                            break;
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Call call;
    private boolean connectCall = false;

    public void sendCall(Call call2, String number) {
        call = call2;
        send("call", "callNumber" + number);
    }

    public void sendCallText(Call call, String number, String text) {
        this.call = call;
        send("call", "callNumber" + number, "Text:" + text);
    }

    public void sendNotification(int id, String ticker, String title, String text) {
        send("notification", "notiID" + id, "notiTicker" + ticker, "notiTitle" + title, "notiText" + text);
    }

    private void send(String... message) {
        try {
            InetAddress local = InetAddress.getByName(ip);
            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(message.length);
            for (String s : message) {
                dos.writeUTF(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
