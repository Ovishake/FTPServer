import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.*;



public class server_java_tcp
{  
    public static int port;
    public static boolean isNumeric(String s) throws Exception
    {
        /** Ref: Inspired by Apache Commons Number UTILS*/
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
    } 
    
    public static int msgparser(String text, String regex)
    {
        int count = 0;
        Pattern patt = Pattern.compile(regex);
        Matcher matt = patt.matcher(text);
        while(matt.find())
        {
            count++;
        }
        return count;
    }

    public static String commandexecuter(String execmd) throws Exception
    {
        Process p = Runtime.getRuntime().exec(execmd);
        BufferedReader stdout =
            new BufferedReader(new InputStreamReader(p.getInputStream()));
        /**Output for cd doesn't contain anything */
        /**to avoid an error */
        String output = stdout.readLine();
        
        return output;
    }

    public static void send_file(Socket s, File file) throws Exception
    {
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        FileInputStream fis = new FileInputStream(file);
        byte[] contents = new byte[512];
        int reading;
        while((reading = fis.read(contents, 0, contents.length))!= -1)
        {
            dos.write(contents, 0, reading);
            
        }
        // byte[] contents = fis.readAllBytes();
        // int fileLength = contents.length;
        // dos.write(fileLength);
        // int current =0;
        // int size = 512;
        // while(current!=fileLength)
        // {           
        //     if(fileLength - current < size)
        //     {
        //         size = fileLength -current;
        //         byte[] content = new byte[size];
        //         for(int j = 0; j < size; j++)
        //         {
        //             content[j] = contents[current+ j];
        //         }
        //         current = fileLength;
        //         dos.write(content);
        //     }
        //     else
        //     {
        //         byte[] content = new byte[size];
        //         current = current + size;
        //         for(int j = 0; j < size; j++)
        //         {
        //             content[j] = contents[current+ j];
        //         }
        //         dos.write(current);
        //     }
        //     dos.flush();
        //     dos.close();
        //     fis.close();
        }

    public static void rcv_file(Socket s, File file) throws Exception
    {
        DataInputStream dis = new DataInputStream(s.getInputStream());
        FileOutputStream fos = new FileOutputStream(file);
        // int fileLength = dis.readInt();
        byte[] contents = new byte[512];
        int reading;
        while((reading = dis.read(contents, 0, contents.length))!= -1)
        {
            fos.write(contents, 0, reading);
        }
        
        // int current = 0;
        // int size = 512;
        // while(current!=fileLength)
        // {
        //     if(fileLength - current < size)
        //     {
        //         size = fileLength - current;
        //         byte[] content = new byte[size];
        //         content = dis.readAllBytes();
        //         fos.write(content);
        //         current = fileLength;
        //     }
        //     else
        //     {
        //         byte[] content = new byte[size];
        //         current = current + size;
        //         content = dis.readAllBytes();
        //         fos.write(content);
        //     }
        // }
        // dis.close();
        // fos.flush();
        // fos.close();

    }

    public static void send_msg(Socket s, String msg) throws Exception, IOException
    {
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        dos.writeChars(msg);
    }

    public static String rcv_msg(Socket s) throws Exception, IOException
    {
        DataInputStream dis = new DataInputStream(s.getInputStream());
        String str = dis.readUTF();
        
        return str; 
    }

    public static void commandparser(String cmd, Socket s, int port) throws Exception
    {
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        String str = null;
        if(cmd.charAt(0) == 'c')
        {
            if(cmd.contains(".."))
            {
                str = commandexecuter(cmd);
            }
            else
            {
                //@Override to emulate the effect of cd
                //if you enter cd you get the effect of pwd
                cmd = "pwd";
                str = commandexecuter(cmd);
            }
            str = commandexecuter(cmd);
            System.out.println("The output I am sending" + str);
            dos.writeChars(str);
        }
        else if(cmd.charAt(0) == 'l')
        {
            cmd = "which " +  cmd;
            str = commandexecuter(cmd);
            System.out.println("The output I am sending" + str);
            dos.writeChars(str);
        }
        else if(cmd.charAt(0) == 'g')
        {
            String filename = cmd.substring(4);
            File file = new File(filename);
            send_file(s, file);
        }
        else if(cmd.charAt(0)=='p')
        {
            String filename = cmd.substring(4).trim();
            File file = new File(filename);
            rcv_file(s, file);
        }
    }

    //check if the argument is valid
    //VOID
    //@inParam: args
    public static void check_args(String a[]) throws Exception
    {
        int chck = Integer.parseInt(a[0]);
        if(a.length < 1 || chck > 65535 || chck < 1)
        {
            System.out.println("Invalid port number. Terminating");
            System.exit(0);
        }
        else if(isNumeric(a[0]))
        {
            System.out.println("Invalid port number. Terminating");
            System.exit(0);
        }
    }
    public static void main(String[] args) throws Exception
    {
        int sndport = 0;
        try
        {
            sndport = Integer.parseInt(args[0]);
        }
        catch (Exception e)
        {
            System.out.println("Invalid port Number, Terminating");
            System.exit(0);
        }

        if(sndport < 0 || sndport > 65535)
        {
            System.out.println("Invalid port Number, Terminating");
            System.exit(0);
        }
        int port = Integer.parseInt(args[0]);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ServerSocket socket = new ServerSocket(port);
        Socket tcpsock = socket.accept();
        String msg = rcv_msg(tcpsock);
        commandparser(msg, tcpsock, port);
    }         
}
        
        
        
        
        

