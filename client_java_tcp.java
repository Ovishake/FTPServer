import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class client_java_tcp
{  
    
    /**Check the args for an Input*/
    public static int checkargs(String a[])
    {
        int port;
        try {
            port = Integer.parseInt(a[1]);
            if(port <= 0)
            {
                return 80000;
            }
            return port;
        } 
        catch (NumberFormatException | NullPointerException e) {
            //TODO: handle exception
            return 80000;
        }
    }
    public static void send_tcp_msg(Socket s, String msg) throws Exception
    {
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        dos.writeUTF(msg);
    }

    public static String read_tcp_msg(Socket s) throws Exception
    {
        DataInputStream dis = new DataInputStream(s.getInputStream());
        String str = dis.readLine();
        return str;
    }

    public static void send_file(Socket s, File file) throws Exception
    {
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        BufferedReader brip = new BufferedReader(new FileReader(file));
        byte[] contents = (brip.readLine()).getBytes();
        int fileLength = contents.length;
        dos.write(fileLength);
        int current =0;
        int size = 512;
        while(current!=fileLength)
        {           
            if(fileLength - current < size)
            {
                size = fileLength -current;
                byte[] content = new byte[size];
                for(int j = 0; j < size; j++)
                {
                    content[j] = contents[current+ j];
                }
                current = fileLength;
                dos.write(content);
            }
            else
            {
                byte[] content = new byte[size];
                current = current + size;
                for(int j = 0; j < size; j++)
                {
                    content[j] = contents[current+ j];
                }
                dos.write(current);
            }
            dos.flush();
            dos.close();
            brip.close();
        }
    }

    public static void rcv_file(Socket s, File file) throws Exception, IOException
    {
        DataInputStream dis = new DataInputStream(s.getInputStream());
        FileOutputStream fos = new FileOutputStream(file);
        
        byte[] contents = new byte[1024];
        // int current = 0;
        // int size = 512;
        int reading;
        while((reading = dis.read(contents, 0, contents.length))!= -1)
        {
            fos.write(contents, 0, reading);
        }
    }
    public static void commandreceiver(String cmd, Socket s) throws Exception
    {
        DataInputStream dis = new DataInputStream(s.getInputStream());
        if(cmd.charAt(0) == 'c')
        {
            String str = dis.readLine();
            System.out.println(str);
        }
        else if(cmd.charAt(0) == 'l')
        {
            String str = dis.readLine();
            System.out.println(str);
        }
        else if(cmd.charAt(0) == 'g')
        {
            String filename = cmd.substring(4);
            File file = new File(filename);
            rcv_file(s, file);
        }
        else if(cmd.charAt(0)=='p')
        {
            String filename = cmd.substring(4).trim();
            File file = new File(filename);
            send_file(s, file);
        }
    }

    public static void main(String[] args) throws Exception
    {
        /**the input stream from STDIN */
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the hostname:");
        String hostname = br.readLine();
        InetAddress addr = InetAddress.getByName(hostname);
        System.out.println("Enter port:");
        int port = Integer.parseInt(br.readLine());
        // int port = Integer.parseInt(br.readLine());
        System.out.print("Enter the command:");
        String cmd = br.readLine().trim();
        Socket controlclientsocket = new Socket(addr, port);
        send_tcp_msg(controlclientsocket, cmd);
        commandreceiver(cmd, controlclientsocket);
        
    }
}
