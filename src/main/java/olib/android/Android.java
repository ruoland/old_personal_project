package olib.android;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Android {
    private DatagramSocket datagramSocket;
    private Socket socket;
    private String androidIP;
    public boolean isLogin = androidIP != null;

    public Android(String password) {
        if (!isLogin) login(password);
    }

    public Android() {
    }

    private void login(String password) {
        try {
            InetAddress serverAddress = InetAddress.getByAddress(new byte[]{-1, -1, -1, -1});
            datagramSocket = new DatagramSocket();
            DatagramPacket loginPacket, ipPacket;
            String inPassword = password;
            byte[] msg = new byte[10];
            byte[] ip = new byte[100];
            DatagramPacket passwordPacket = new DatagramPacket(inPassword.getBytes(), inPassword.getBytes().length, serverAddress, 50008);
            loginPacket = new DatagramPacket(msg, msg.length);
            ipPacket = new DatagramPacket(ip, ip.length);
            datagramSocket.send(passwordPacket);
            datagramSocket.receive(loginPacket);
            datagramSocket.receive(ipPacket);
            if (!isLogin) {
                String inMessage = new String(loginPacket.getData()).trim();
                if (inMessage.equalsIgnoreCase("1")) {
                    isLogin = true;
                    androidIP = new String(ipPacket.getData()).trim();
                } else {
                    System.out.println("클라+로그인 실패했습니다. " + inMessage);
                }
            }
            System.out.println("로그인" + androidIP);
            if (isLogin) {
                tcp(androidIP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tcp(String ip) {
        try {
            InetAddress local = InetAddress.getByName(ip);
            socket = new Socket(local, 12345);
            InputStream in = socket.getInputStream();
            final DataInputStream dis = new DataInputStream(in);
            new Thread(() -> {

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
            InetAddress local = InetAddress.getByName(androidIP);
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
