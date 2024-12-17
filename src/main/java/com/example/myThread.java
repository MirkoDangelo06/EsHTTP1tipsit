package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class myThread extends Thread {

    Socket s;

    myThread(Socket s) {
        this.s = s;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            String[] stringaTmp;
            String stringa;

            String firstline = in.readLine();
            String[] request = firstline.split(" ");

            String method = request[0];
            String resource = request[1];
            String version = request[2];

            do {
                stringa = in.readLine();

            } while (!stringa.isEmpty());

            if (resource.equals("/"))
                resource = "/index.html";

            if (resource.endsWith("/"))
                resource = resource +"/index.html";


            File file = new File("httdocs" + resource);
            if (file.exists()) {
                

                out.writeBytes("HTTP/1.1 200 Ok\n");
                out.writeBytes("Content-Type" + getContentType(file) + "\n");
                out.writeBytes("Content-Length: " + file.length() + "\n");
                out.writeBytes("\n");
                InputStream input = new FileInputStream(file);
                byte[] buf = new byte[8192];
                int n;
                while((n = input.read(buf)) != -1 ){
                    out.write(buf,0,n);
                }
                input.close();


                //out.writebytes("responsebody")


            } else {
                String responseBody = "no smash";
                out.writeBytes("HTTP/1.1 404 Not Found \n");
                out.writeBytes("Content-Type: text/plain \n");
                out.writeBytes("Content-Length: " + responseBody.length() + "\n");
                out.writeBytes("\n");
                out.writeBytes(responseBody);
                s.close();
            }
           


            s.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
   
    private static String getContentType(File f) {

        String[] s = f.getName().split("\\.");
        String ext = s[s.length - 1];
        switch (ext) {
            case "html":

            case "htm":
                return "text/html";
            case "png":
                return "image/png";
            case "css":
                return "text/css";
            
            case "jpg":
                return "image/jpg";

            case "webp":
                return "image/webp";
            default:
                return "";
        }

    }



}
